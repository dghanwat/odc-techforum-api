/* Copyright (C) Atos Worldline
 $Id$
 $Log$*/
package net.atos.odc.techforum.app.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 
 * 
 */
@Entity
@Table(name = "user")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@NamedQueries({
		@NamedQuery(name = "net.atos.odc.techforum.app.model.User.fetchUsersBySession", query = "SELECT u FROM Session s JOIN s.users u where s.id = :id"),
		@NamedQuery(name = "net.atos.odc.techforum.app.model.User.fetchUserByDasId", query = "SELECT u FROM User u where u.dasId = :dasId") })
public class User {
	/**
	 * 
	 * 
	 */
	private String dasId;
	/**
	 * 
	 * 
	 */
	private String name;
	
	private String location;
	
	private Date createdDate = new Date();

	private List<Session> sessions = new ArrayList<Session>();

	/**
	 * Return value of attribute dasId
	 * 
	 * @return <code>String</code>
	 */
	@Id
	public String getDasId() {
		return this.dasId;
	}

	/**
	 * Return value of attribute name
	 * 
	 * @return <code>String</code>
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Set value of attribute dasId
	 * 
	 * @param dasId
	 *            : (<code>String</code>) the new value of dasId
	 */
	public void setDasId(String dasId) {
		this.dasId = dasId;
	}

	/**
	 * Set value of attribute name
	 * 
	 * @param name
	 *            : (<code>String</code>) the new value of name
	 */
	public void setName(String name) {
		this.name = name;
	}

	@ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE }, fetch = FetchType.LAZY, mappedBy = "users")
	public List<Session> getSessions() {
		return sessions;
	}

	public void setSessions(List<Session> sessions) {
		this.sessions = sessions;
	}

	/**
	 * Equals method for User class
	 * 
	 */
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof User)) {
			return false;
		}
		User __obj = (User) obj;
		return (this.dasId == __obj.dasId);
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
}
