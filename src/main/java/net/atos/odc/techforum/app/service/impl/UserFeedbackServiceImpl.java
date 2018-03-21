package net.atos.odc.techforum.app.service.impl;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import net.atos.odc.techforum.app.model.Question;
import net.atos.odc.techforum.app.model.User;
import net.atos.odc.techforum.app.model.UserFeedback;
import net.atos.odc.techforum.app.service.UserFeedbackService;

@Stateless
public class UserFeedbackServiceImpl implements UserFeedbackService {

	private EntityManager entityManager;

	@PersistenceContext
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	public EntityManager getEntityManager() {
		return entityManager;
	}

	public void postUserResponse(long questionId, long optionId, String dasId) {

		UserFeedback userFeedback = null;

		Query query = entityManager
				.createNamedQuery("net.atos.odc.techforum.app.model.User.fetchUserByDasId");
		query.setParameter("dasId", dasId);

		User user = (User) query.getSingleResult();

		query = entityManager
				.createNamedQuery("net.atos.odc.techforum.app.model.UserFeedback.fetchFeedbackByUserAndQuestion");

		query.setParameter("id", dasId);
		query.setParameter("questionId", questionId);

		if (query.getResultList().size() == 0) {
			userFeedback = new UserFeedback();
		} else {
			userFeedback = (UserFeedback) query.getResultList().get(0);
		}

		userFeedback.setAnswerGiven(optionId);
		userFeedback.setQuestionId(questionId);
		userFeedback.setUser(user);

		query = entityManager
				.createNamedQuery("net.atos.odc.techforum.app.model.Question.fetchQuestionById");

		query.setParameter("id", questionId);

		Question question = (Question) query.getSingleResult();

		if (question.getCorrectOption() == optionId) {
			userFeedback.setCorrect(true);
		} else {
			userFeedback.setCorrect(false);
		}

		entityManager.persist(userFeedback);
		entityManager.merge(userFeedback);

		entityManager.flush();
	}
}
