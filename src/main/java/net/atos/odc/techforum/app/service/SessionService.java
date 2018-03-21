package net.atos.odc.techforum.app.service;

import java.util.List;

import net.atos.odc.techforum.app.model.SessionDto;

public interface SessionService  {

	List<SessionDto> fetchAllSession();

	SessionDto fetchSessionById(final long sessionId);

	List<SessionDto> fetchSessionByRoom(final String roomNumber);

	List<SessionDto> fetchSessionBySlot(final String timeSlot);

	List<SessionDto> fetchSessionByPresenter(final long presenterId);

	void registerUserToSession(final long sessionId, final String dasId, String name, String location);
	
	
	SessionDto fetchSessionByIdAndUser(final long sessionId, final String dasId);
	
	void recordTeaserVote(final long sessionId, final String dasId);

}
