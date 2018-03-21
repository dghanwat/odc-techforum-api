/* Copyright (C) Atos Worldline
 $Id$
 $Log$*/
package net.atos.odc.techforum.app.model;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class SessionDto { 

	private long id;

	private List<String> presenters;

	private List<String> users;
	

	private String name;

	private String imageUrl;

	private String description;

	private String roomNumber;

	private String timeSlot;
	
	private List<PresenterDto> presenterDtos;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public List<String> getPresenters() {
		return presenters;
	}

	public void setPresenters(List<String> presenters) {
		this.presenters = presenters;
	}

	public List<String> getUsers() {
		return users;
	}

	public void setUsers(List<String> users) {
		this.users = users;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getRoomNumber() {
		return roomNumber;
	}

	public void setRoomNumber(String roomNumber) {
		this.roomNumber = roomNumber;
	}

	public String getTimeSlot() {
		return timeSlot;
	}

	public void setTimeSlot(String timeSlot) {
		this.timeSlot = timeSlot;
	}

	public List<PresenterDto> getPresenterDtos() {
		return presenterDtos;
	}

	public void setPresenterDtos(List<PresenterDto> presenterDtos) {
		this.presenterDtos = presenterDtos;
	}

}
