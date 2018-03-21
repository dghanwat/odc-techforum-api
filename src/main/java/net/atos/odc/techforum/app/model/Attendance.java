/* Copyright (C) Atos Worldline
 $Id$
 $Log$*/
package net.atos.odc.techforum.app.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
@Table(name = "attendance")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@NamedQueries({
		@NamedQuery(name = "net.atos.odc.techforum.app.model.Attendance.fetchUsersByRoom", query = "SELECT s FROM Attendance s where s.room = :room"),
		@NamedQuery(name = "net.atos.odc.techforum.app.model.Attendance.fetchUserByDasId", query = "SELECT u FROM Attendance u where u.dasId = :dasId") })
public class Attendance {
	
	private long id;

	private String dasId;
	private String room;
	
	private String name;

	private Date createdDate = new Date();
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	/**
	 * @return the dasId
	 */
	public String getDasId() {
		return dasId;
	}

	/**
	 * @param dasId the dasId to set
	 */
	public void setDasId(String dasId) {
		this.dasId = dasId;
	}

	/**
	 * @return the room
	 */
	public String getRoom() {
		return room;
	}

	/**
	 * @param room the room to set
	 */
	public void setRoom(String room) {
		this.room = room;
	}

	/**
	 * @return the createdDate
	 */
	public Date getCreatedDate() {
		return createdDate;
	}

	/**
	 * @param createdDate the createdDate to set
	 */
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	

}
