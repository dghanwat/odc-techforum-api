/* Copyright (C) Atos Worldline
 $Id$
 $Log$*/
package net.atos.odc.techforum.app.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;


@Entity
@Table(name = "session")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
//@JsonIgnoreProperties(ignoreUnknown = true)
@NamedQueries({
		@NamedQuery(name = "net.atos.odc.techforum.app.model.Session.fetchAllSession", query = "SELECT s FROM Session s"),
		@NamedQuery(name = "net.atos.odc.techforum.app.model.Session.fetchSessionById", query = "SELECT s FROM Session s where s.id = :id"),
		@NamedQuery(name = "net.atos.odc.techforum.app.model.Session.fetchSessionByRoom", query = "SELECT s FROM Session s where s.roomNumber = :roomNumber"),
		@NamedQuery(name = "net.atos.odc.techforum.app.model.Session.fetchSessionBySlot", query = "SELECT s FROM Session s where s.timeSlot = :timeSlot"),
		@NamedQuery(name = "net.atos.odc.techforum.app.model.Session.fetchSessionByPresenter", query = "SELECT s FROM Presenter p JOIN p.sessions s where p.id = :id"),
		@NamedQuery(name = "net.atos.odc.techforum.app.model.Session.fetchSessionByIdAndUser", query = "SELECT s FROM User u JOIN u.sessions s where UPPER(u.dasId) = :dasId AND  s.id = :id") })
public class Session {
 
	private long id;

	private List<Presenter> presenters = new ArrayList<Presenter>();

	private List<User> users = new ArrayList<User>();

	private String name;

	private String imageUrl;

	private String description;

	private String roomNumber;

	private String timeSlot;
	
	private List<Question> questions = new ArrayList<Question>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Return value of attribute imageUrl
	 * 
	 * @return <code>String</code>
	 */
	public String getImageUrl() {
		return this.imageUrl;
	}

	/**
	 * Return value of attribute description
	 * 
	 * @return <code>String</code>
	 */
	public String getDescription() {
		return this.description;
	}

	/**
	 * Set value of attribute imageUrl
	 * 
	 * @param imageUrl
	 *            : (<code>String</code>) the new value of imageUrl
	 */
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	/**
	 * Set value of attribute description
	 * 
	 * @param description
	 *            : (<code>String</code>) the new value of description
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Relation presenters Set value of attribute presenter
	 * 
	 * @param presenter
	 *            : the new value of presenter
	 */
	@ManyToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY)
	@JoinTable(name = "session_presenter_table", joinColumns = { @JoinColumn(name = "SESSION_ID") }, inverseJoinColumns = { @JoinColumn(name = "PRESENTER_ID") })
	@XmlTransient
	public List<Presenter> getPresenters() {
		return presenters;
	}

	/**
	 * Relation presenters Return value of attribute presenter
	 * 
	 * @return <code>Presenter</code>
	 */
	public void setPresenters(final List<Presenter> presenters) {
		this.presenters = presenters;
	}

	/**
	 * Relation attendees Set value of attribute user
	 * 
	 * @param user
	 *            : the new value of user
	 */
	@ManyToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY)
	@JoinTable(name = "session_user_table", joinColumns = { @JoinColumn(name = "SESSION_ID") }, inverseJoinColumns = { @JoinColumn(name = "USER_ID") })
	@XmlTransient
	public List<User> getUsers() {
		return users;
	}

	/**
	 * Relation attendees Return value of attribute user
	 * 
	 * @return <code>User</code>
	 */
	public void setUsers(final List<User> users) {
		this.users = users;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	/**
	 * Relation roomNumber Set value of attribute roomNumber
	 * 
	 * @param roomNumber
	 *            : the new value of roomNumber
	 */
	public String getRoomNumber() {
		return this.roomNumber;
	}

	/**
	 * Relation timeSlot Set value of attribute timeSlot
	 * 
	 * @param timeSlot
	 *            : the new value of timeSlot
	 */
	public String getTimeSlot() {
		return this.timeSlot;
	}

	/**
	 * Relation roomNumber Return value of attribute roomNumber
	 * 
	 * @return <code>RoomNumber</code>
	 */
	public void setRoomNumber(final String roomNumber) {
		this.roomNumber = roomNumber;
	}

	/**
	 * Relation timeSlot Return value of attribute timeSlot
	 * 
	 * @return <code>TimeSlot</code>
	 */
	public void setTimeSlot(final String timeSlot) {
		this.timeSlot = timeSlot;
	}

	public Session addUser(final User user) {
		this.users.add(user);
		return this;
	}

	public Session removeUser(final User user) {
		this.users.remove(user);
		return this;
	}

	/**
	 * Equals method for Session class
	 * 
	 */
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Session)) {
			return false;
		}
		Session __obj = (Session) obj;
		return (this.id == __obj.id);
	}
	
	@OneToMany(mappedBy = "session", cascade = { CascadeType.ALL }, fetch = FetchType.LAZY)
	@XmlTransient
	public List<Question> getQuestions() {
		return questions;
	}

	public void setQuestions(List<Question> questions) {
		this.questions = questions;
	}

}

