package net.atos.odc.techforum.app.service;

import java.util.List;

import net.atos.odc.techforum.app.model.UserDto;

public interface AttendanceService {

	List<UserDto> fetchUsersByRoom(final String room);
	
	void markAttendance(String dasId, String name, String room);

}
