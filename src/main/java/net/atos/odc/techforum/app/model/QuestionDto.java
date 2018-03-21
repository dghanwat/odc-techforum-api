package net.atos.odc.techforum.app.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class QuestionDto {

	private long id;

	private boolean feedback;

	private Session session;

	private List<String> options = new ArrayList<String>();

	private long correctOption;
	
	private String description;
	
	private long sessionId;
	
	private List<OptionDto> questionOptions;
	
	private String sessionName;

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

	public Session getSession() {
		return session;
	}

	public void setSession(Session session) {
		this.session = session;
	}

	public List<String> getOptions() {
		return options;
	}

	public void setOptions(List<String> options) {
		this.options = options;
	}

	public long getCorrectOption() {
		return correctOption;
	}

	public void setCorrectOption(long correctOption) {
		this.correctOption = correctOption;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public long getSessionId() {
		return sessionId;
	}

	public void setSessionId(long sessionId) {
		this.sessionId = sessionId;
	}

	public String getSessionName() {
		return sessionName;
	}

	public void setSessionName(String sessionName) {
		this.sessionName = sessionName;
	}

	public List<OptionDto> getQuestionOptions() {
		return questionOptions;
	}

	public void setQuestionOptions(List<OptionDto> questionOptions) {
		this.questionOptions = questionOptions;
	}
}
