package net.atos.odc.techforum.app.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import net.atos.odc.techforum.app.model.Attendance;
import net.atos.odc.techforum.app.model.User;
import net.atos.odc.techforum.app.model.UserDto;
import net.atos.odc.techforum.app.service.AttendanceService;

@Stateless
public class AttendanceServiceImpl implements AttendanceService {

	private EntityManager entityManager;

	@PersistenceContext
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	public EntityManager getEntityManager() {
		return entityManager;
	}

	public List<UserDto> fetchUsersByRoom(String room) {

		Query query = entityManager
				.createNamedQuery("net.atos.odc.techforum.app.model.Attendance.fetchUsersByRoom");

		query.setParameter("room", room);

		List<User> userList = query.getResultList();

		List<UserDto> listToReturn = new ArrayList<UserDto>();

		for (User user : userList) {
			listToReturn.add(getUserDto(user));
		}
		return listToReturn;
	}

	private UserDto getUserDto(User user) {

		UserDto dto = new UserDto();
		dto.setDasId(user.getDasId());
		dto.setName(user.getName());

		return dto;
	}

	@Override
	public void markAttendance(String dasId, String name, String room) {
		Attendance attendance = new Attendance();
		attendance.setDasId(dasId);
		attendance.setName(name);
		attendance.setRoom(room);
		
		entityManager.persist(attendance);
		entityManager.flush();
		
	}
}
