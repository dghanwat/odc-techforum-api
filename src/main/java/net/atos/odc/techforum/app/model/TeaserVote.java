package net.atos.odc.techforum.app.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "teaser_vote")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@NamedQueries({ @NamedQuery(name = "net.atos.odc.techforum.app.model.TeaserVote.fetchByDasId", query = "SELECT t FROM TeaserVote t where t.dasId = :dasId") })
public class TeaserVote {

	@Id
	private String dasId;

	private long sessionId;

	public String getDasId() {
		return dasId;
	}

	public void setDasId(String dasId) {
		this.dasId = dasId;
	}

	public long getSessionId() {
		return sessionId;
	}

	public void setSessionId(long sessionId) {
		this.sessionId = sessionId;
	}

}
