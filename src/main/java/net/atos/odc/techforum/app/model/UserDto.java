/* Copyright (C) Atos Worldline
 $Id$
 $Log$*/
package net.atos.odc.techforum.app.model;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 
 * 
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class UserDto {

	private String dasId;

	private String name;

	private List<Session> session;
	
	private List<SessionDto> sessionDto;

	public String getDasId() {
		return dasId;
	}

	public void setDasId(String dasId) {
		this.dasId = dasId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Session> getSession() {
		return session;
	}

	public void setSession(List<Session> session) {
		this.session = session;
	}

	public List<SessionDto> getSessionDto() {
		return sessionDto;
	}

	public void setSessionDto(List<SessionDto> sessionDto) {
		this.sessionDto = sessionDto;
	}
}
