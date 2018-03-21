package net.atos.odc.techforum.app.service.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.ejb.Singleton;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import net.atos.odc.techforum.app.model.Presenter;
import net.atos.odc.techforum.app.model.PresenterDto;
import net.atos.odc.techforum.app.model.Session;
import net.atos.odc.techforum.app.model.SessionDto;
import net.atos.odc.techforum.app.model.TeaserVote;
import net.atos.odc.techforum.app.model.TimeSlot;
import net.atos.odc.techforum.app.model.User;
import net.atos.odc.techforum.app.service.SessionService;

@Stateless
public class SessionServiceImpl implements SessionService {

	private EntityManager entityManager;

	@PersistenceContext
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	public EntityManager getEntityManager() {
		return entityManager;
	}

	public List<SessionDto> fetchAllSession() {

		Query query = entityManager
				.createNamedQuery("net.atos.odc.techforum.app.model.Session.fetchAllSession");

		List<Session> sessionList = query.getResultList();

		List<SessionDto> listToReturn = new ArrayList<SessionDto>();

		for (Session session : sessionList) {
			listToReturn.add(getSessionDto(session));
		}
		return listToReturn;
	}

	public SessionDto fetchSessionById(long sessionId) {

		Query query = entityManager
				.createNamedQuery("net.atos.odc.techforum.app.model.Session.fetchSessionById");

		query.setParameter("id", sessionId);

		Session session = (Session) query.getSingleResult();

		return getSessionDto(session);
	}

	public List<SessionDto> fetchSessionByRoom(String roomNumber) {

		Query query = entityManager
				.createNamedQuery("net.atos.odc.techforum.app.model.Session.fetchSessionByRoom");

		query.setParameter("roomNumber", roomNumber);

		List<Session> sessionList = query.getResultList();

		List<SessionDto> listToReturn = new ArrayList<SessionDto>();

		for (Session session : sessionList) {
			listToReturn.add(getSessionDto(session));
		}
		return listToReturn;
	}

	public List<SessionDto> fetchSessionBySlot(String timeSlot) {
		Query query = entityManager
				.createNamedQuery("net.atos.odc.techforum.app.model.Session.fetchSessionBySlot");

		query.setParameter("timeSlot", timeSlot);

		List<Session> sessionList = query.getResultList();

		List<SessionDto> listToReturn = new ArrayList<SessionDto>();

		for (Session session : sessionList) {
			listToReturn.add(getSessionDto(session));
		}
		return listToReturn;
	}

	public List<SessionDto> fetchSessionByPresenter(long presenterId) {
		Query query = entityManager
				.createNamedQuery("net.atos.odc.techforum.app.model.Session.fetchSessionByPresenter");

		query.setParameter("id", presenterId);

		List<Session> sessionList = query.getResultList();

		List<SessionDto> listToReturn = new ArrayList<SessionDto>();

		for (Session session : sessionList) {
			listToReturn.add(getSessionDto(session));
		}
		return listToReturn;
	}

	private SessionDto getSessionDto(Session session) {

		SessionDto dto = new SessionDto();
		dto.setId(session.getId());
		dto.setName(session.getName());
		dto.setDescription(session.getDescription());
		dto.setImageUrl(session.getImageUrl());
		dto.setRoomNumber(session.getRoomNumber());
		dto.setTimeSlot(session.getTimeSlot());

		List<PresenterDto> presenterDtos = new ArrayList<PresenterDto>(1);

		Iterator<Presenter> iterator = session.getPresenters().iterator();

		while (iterator.hasNext()) {
			Presenter presenter = iterator.next();
			PresenterDto presenterDto = new PresenterDto();
			presenterDto.setId(presenter.getId());
			/*
			 * presenterDto.setName(presenter.getName().substring(0,
			 * presenter.getName().indexOf(" ")));
			 */
			presenterDto.setName(presenter.getName());
			presenterDtos.add(presenterDto);

		}

		dto.setPresenterDtos(presenterDtos);

		List<String> users = new ArrayList<String>();

		Iterator<User> iterator2 = session.getUsers().iterator();

		while (iterator2.hasNext()) {
			users.add(iterator2.next().getName());
		}

		dto.setUsers(users);

		return dto;
	}

	public void registerUserToSession(long sessionId, String dasId,
			String name, String location) {

		Query query = entityManager
				.createNamedQuery("net.atos.odc.techforum.app.model.Session.fetchSessionByIdAndUser");

		query.setParameter("id", sessionId);
		query.setParameter("dasId", dasId);
		User user = null;

		if (query.getResultList().size() == 0) {
			query = entityManager
					.createNamedQuery("net.atos.odc.techforum.app.model.Session.fetchSessionById");
			query.setParameter("id", sessionId);

			Session session = (Session) query.getSingleResult();

			query = entityManager
					.createNamedQuery("net.atos.odc.techforum.app.model.User.fetchUserByDasId");
			query.setParameter("dasId", dasId);
			try {
				user = (User) query.getSingleResult();
			} catch (Exception ex) {
				// if no user exists
				user = new User();
				user.setDasId(dasId);
				user.setName(name);
				user.setLocation(location);
				entityManager.persist(user);
			}

			session.addUser(user);

			entityManager.persist(session);
			entityManager.merge(session);

			entityManager.flush();

		} else {
			System.out.println("in else user is alredy registered");
			query = entityManager
					.createNamedQuery("net.atos.odc.techforum.app.model.Session.fetchSessionById");
			query.setParameter("id", sessionId);
			Session session = (Session) query.getSingleResult();
			for (User u : session.getUsers()) {
				if (u.getDasId().equals(dasId)) {
					// un register the user
					session.removeUser(u);
					break;
				}
			}
			entityManager.persist(session);
			entityManager.merge(session);

			entityManager.flush();

		}
	}

	public SessionDto fetchSessionByIdAndUser(long sessionId, String dasId) {

		Query query = entityManager
				.createNamedQuery("net.atos.odc.techforum.app.model.Session.fetchSessionByIdAndUser");

		query.setParameter("id", sessionId);
		query.setParameter("dasId", dasId);

		Session session = null;
		SessionDto sessionDto = null;

		try {
			session = (Session) query.getSingleResult();
			sessionDto = getSessionDto(session);
		} catch (NoResultException nre) {

		}

		return sessionDto;
	}

	@Override
	public void recordTeaserVote(long sessionId, String dasId) {
		Query query = entityManager
				.createNamedQuery("net.atos.odc.techforum.app.model.TeaserVote.fetchByDasId");

		query.setParameter("dasId", dasId);
		TeaserVote t = null;
		try {
			t = (TeaserVote) query.getSingleResult();
		} catch (NoResultException nre) {
			// no result
		}

		if (t == null) {
			t = new TeaserVote();
			t.setDasId(dasId);
			t.setSessionId(sessionId);

			entityManager.persist(t);
		} else {
			t.setSessionId(sessionId);
			entityManager.merge(t);
		}

		entityManager.flush();

	}
}
