package net.atos.odc.techforum.app.model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "userfeedback")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@NamedQueries({ @NamedQuery(name = "net.atos.odc.techforum.app.model.UserFeedback.fetchFeedbackByUserAndQuestion", query = "SELECT f FROM UserFeedback f where f.user.dasId = :id AND f.questionId = :questionId") })
public class UserFeedback {

	private long id;

	private User user;

	private long questionId;

	private long answerGiven;

	private boolean correct;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@ManyToOne(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY)
	public User getUser() {
		return user;
	}

	@ManyToOne(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY)
	public void setUser(User user) {
		this.user = user;
	}

	public long getQuestionId() {
		return questionId;
	}

	public void setQuestionId(long questionId) {
		this.questionId = questionId;
	}

	public long getAnswerGiven() {
		return answerGiven;
	}

	public void setAnswerGiven(long answerGiven) {
		this.answerGiven = answerGiven;
	}

	public boolean isCorrect() {
		return correct;
	}

	public void setCorrect(boolean correct) {
		this.correct = correct;
	}
}
