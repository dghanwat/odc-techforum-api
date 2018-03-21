package net.atos.odc.techforum.app.service;

public interface UserFeedbackService {

	void postUserResponse(final long questionId, final long optionId,
			final String dasId);

}
