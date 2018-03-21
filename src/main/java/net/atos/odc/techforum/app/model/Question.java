package net.atos.odc.techforum.app.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;


@Entity
@Table(name = "question")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@NamedQueries({
	@NamedQuery(name = "net.atos.odc.techforum.app.model.Question.fetchQuestionBySession", query = "SELECT q FROM Session s JOIN s.questions q where s.id = :id"),
	@NamedQuery(name = "net.atos.odc.techforum.app.model.Question.fetchQuestionById", query = "SELECT q FROM Question q where q.id = :id") })
public class Question {

	private long id;

	private boolean feedback;

	private Session session;
	
	private String description;

	private List<Option> options = new ArrayList<Option>();

	private long correctOption;

	@Id
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public boolean isFeedback() {
		return feedback;
	}

	public void setFeedback(boolean feedback) {
		this.feedback = feedback;
	}

	@ManyToOne
	public Session getSession() {
		return session;
	}

	@ManyToOne
	public void setSession(Session session) {
		this.session = session;
	}

	@OneToMany(mappedBy = "question", cascade = { CascadeType.ALL }, fetch = FetchType.LAZY)
	public List<Option> getOptions() {
		return options;
	}

	public void setOptions(List<Option> options) {
		this.options = options;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public long getCorrectOption() {
		return correctOption;
	}

	public void setCorrectOption(long correctOption) {
		this.correctOption = correctOption;
	}

}
