package net.atos.odc.techforum.app.service.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.ejb.Singleton;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import net.atos.odc.techforum.app.model.Option;
import net.atos.odc.techforum.app.model.OptionDto;
import net.atos.odc.techforum.app.model.Question;
import net.atos.odc.techforum.app.model.QuestionDto;
import net.atos.odc.techforum.app.service.QuestionService;

@Stateless
public class QuestionServiceImpl implements QuestionService {

	private EntityManager entityManager;

	@PersistenceContext
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	public EntityManager getEntityManager() {
		return entityManager;
	}

	public List<QuestionDto> fetchQuestionsBySession(long sessionId) {

		Query query = entityManager
				.createNamedQuery("net.atos.odc.techforum.app.model.Question.fetchQuestionBySession");

		query.setParameter("id", sessionId);

		List<Question> questionList = query.getResultList();

		List<QuestionDto> listToReturn = new ArrayList<QuestionDto>();

		for (Question question : questionList) {
			listToReturn.add(getQuestionDto(question));
		}
		return listToReturn;
	}

	private QuestionDto getQuestionDto(Question question) {

		QuestionDto dto = new QuestionDto();
		dto.setId(question.getId());
		dto.setCorrectOption(question.getCorrectOption());
		dto.setFeedback(question.isFeedback());
		// dto.setSession(question.getSession());
		dto.setDescription(question.getDescription());
		dto.setSessionId(question.getSession().getId());
		dto.setSessionName(question.getSession().getName());

		List<String> opts = new ArrayList<String>();
		List<OptionDto> options = new ArrayList<OptionDto>();

		Iterator<Option> iterator = question.getOptions().iterator();

		while (iterator.hasNext()) {
			Option op = iterator.next();
			OptionDto opDto = new OptionDto();
			opDto.setOptionId(op.getId());
			opDto.setValue(op.getValue());
			options.add(opDto);
		}

		// dto.setOptions(opts);
		dto.setQuestionOptions(options);

		return dto;
	}

	@Override
	public void postUserResponse(long questionId, long optionId, String dasId) {
		
		
	}
}
