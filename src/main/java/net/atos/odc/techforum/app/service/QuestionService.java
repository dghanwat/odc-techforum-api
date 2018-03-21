package net.atos.odc.techforum.app.service;

import java.util.List;

import net.atos.odc.techforum.app.model.QuestionDto;

public interface QuestionService {
	List<QuestionDto> fetchQuestionsBySession(final long sessionId);
	
	void postUserResponse(long questionId , long optionId,String dasId);
}
