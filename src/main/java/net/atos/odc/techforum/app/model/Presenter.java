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
@Table(name = "presenter")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@NamedQueries({ @NamedQuery(name = "net.atos.odc.techforum.app.model.Presenter.fetchAllPresenter", query = "SELECT p FROM Presenter p") })
public class Presenter {

	private long id;
	/**
	 * 
	 * 
	 */
	private String name;
	/**
	 * 
	 * 
	 */
	private String description;
	/**
	 * 
	 * 
	 */
	private String imageUrl;

	private List<Session> sessions = new ArrayList<Session>();

	/**
	 * Return value of attribute name
	 * 
	 * @return <code>String</code>
	 */
	public String getName() {
		return this.name;
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
	 * Return value of attribute imageUrl
	 * 
	 * @return <code>String</code>
	 */
	public String getImageUrl() {
		return this.imageUrl;
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
	 * Set value of attribute imageUrl
	 * 
	 * @param imageUrl
	 *            : (<code>String</code>) the new value of imageUrl
	 */
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE }, fetch = FetchType.EAGER, mappedBy = "presenters")
	public List<Session> getSessions() {
		return sessions;
	}

	public void setSessions(List<Session> sessions) {
		this.sessions = sessions;
	}

	/**
	 * Equals method for Presenter class
	 * 
	 */
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Presenter)) {
			return false;
		}
		Presenter __obj = (Presenter) obj;
		return (this.id == __obj.id);
	}
}
