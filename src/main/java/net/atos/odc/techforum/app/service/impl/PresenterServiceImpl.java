package net.atos.odc.techforum.app.service.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.ejb.Singleton;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import net.atos.odc.techforum.app.model.Presenter;
import net.atos.odc.techforum.app.model.PresenterDto;
import net.atos.odc.techforum.app.model.Session;
import net.atos.odc.techforum.app.model.SessionDto;
import net.atos.odc.techforum.app.service.PresenterService;


@Stateless
public class PresenterServiceImpl implements PresenterService {

	private EntityManager entityManager;

	@PersistenceContext
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	public EntityManager getEntityManager() {
		return entityManager;
	}

	public List<PresenterDto> fetchAllPresenter() {

		Query query = entityManager
				.createNamedQuery("net.atos.odc.techforum.app.model.Presenter.fetchAllPresenter");

		List<Presenter> presenterList = query.getResultList();

		List<PresenterDto> listToReturn = new ArrayList<PresenterDto>();

		for (Presenter presenter : presenterList) {
			listToReturn.add(getPresenterDto(presenter));
		}
		return listToReturn;
	}

	private PresenterDto getPresenterDto(Presenter presenter) {

		PresenterDto dto = new PresenterDto();

		dto.setId(presenter.getId());
		dto.setDescription(presenter.getDescription());
		dto.setImageUrl(presenter.getImageUrl());
		dto.setName(presenter.getName());

		List<String> sessions = new ArrayList<String>();
		List<SessionDto> sessionsDtos = new ArrayList<SessionDto>();

		Iterator<Session> iterator = presenter.getSessions().iterator();

		while (iterator.hasNext()) {
			Session session = iterator.next();
			SessionDto sessionDto = new SessionDto();
			sessionDto.setDescription(session.getDescription());
			sessionDto.setId(session.getId());
			sessionDto.setImageUrl(session.getImageUrl());
			sessionDto.setName(session.getName());
			sessionDto.setRoomNumber(session.getRoomNumber());
			sessionDto.setTimeSlot(session.getTimeSlot());
			sessionsDtos.add(sessionDto);
			
		}

		//dto.setSessions(sessions);
		dto.setSessionDtos(sessionsDtos);

		return dto;
	}

}
