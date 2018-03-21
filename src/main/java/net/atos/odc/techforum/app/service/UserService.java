package net.atos.odc.techforum.app.service;

import java.util.List;

import net.atos.odc.techforum.app.model.UserDto;

public interface UserService {

	List<UserDto> fetchUsersBySession(final long sessionId);

}
