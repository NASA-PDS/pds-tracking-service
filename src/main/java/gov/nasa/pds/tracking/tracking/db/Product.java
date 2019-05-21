/**
 * Copyright 2010-2017, by the California Institute of Technology.
 * 
 * The object class represents Product table.
 *
 */
package gov.nasa.pds.tracking.tracking.db;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.xml.bind.annotation.XmlRootElement;

import org.apache.log4j.Logger;

@XmlRootElement(name = "product")

/**
 * @author danyu dan.yu@jpl.nasa.gov
 *
 */

public class Product  implements Serializable  {

	private static final long serialVersionUID = 1L;

	public static Logger logger = Logger.getLogger(Product.class);
		
	private String identifier = null;
	private String version = null;
	private String title = null;
	private String type = null;
	private String alternate = null;
	private String instRef = null;
	/**
	 * @return the instRef
	 */
	public String getInstRef() {
		return instRef;
	}

	/**
	 * @param instRef, the instRef to set
	 */
	public void setInstRef(String instRef) {
		this.instRef = instRef;
	}

	/**
	 * @return the instTitle
	 */
	public String getInstTitle() {
		return instTitle;
	}

	/**
	 * @param instTitle, the instTitle to set
	 */
	public void setInstTitle(String instTitle) {
		this.instTitle = instTitle;
	}

	/**
	 * @return the inveRef
	 */
	public String getInveRef() {
		return inveRef;
	}

	/**
	 * @param inveRef, the inveRef to set
	 */
	public void setInveRef(String inveRef) {
		this.inveRef = inveRef;
	}

	/**
	 * @return the inveTitle
	 */
	public String getInveTitle() {
		return inveTitle;
	}

	/**
	 * @param inveTitle, the inveTitle to set
	 */
	public void setInveTitle(String inveTitle) {
		this.inveTitle = inveTitle;
	}

	/**
	 * @return the nodeRef
	 */
	public String getNodeRef() {
		return nodeRef;
	}

	/**
	 * @param nodeRef, the nodeRef to set
	 */
	public void setNodeRef(String nodeRef) {
		this.nodeRef = nodeRef;
	}

	/**
	 * @return the nodeTitle
	 */
	public String getNodeTitle() {
		return nodeTitle;
	}

	/**
	 * @param nodeTitle, the nodeTitle to set
	 */
	public void setNodeTitle(String nodeTitle) {
		this.nodeTitle = nodeTitle;
	}

	private String instTitle = null;
	private String inveRef = null;
	private String inveTitle = null;
	private String nodeRef = null;
	private String nodeTitle = null;

	private String aStatus = null;

	private String cStatus = null;

	private String nssdca = null;

	/**
	 * @return
	 */
	public String getIdentifier() {
		return identifier;
	}

	/**
	 * @param identifier
	 */
	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	/**
	 * @return
	 */
	public String getVersion() {
		return version;
	}

	/**
	 * @param version
	 */
	public void setVersion(String version) {
		this.version = version;
	}

	/**
	 * @return
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return
	 */
	public String getAlternate() {
		return alternate;
	}

	/**
	 * @param alternate
	 */
	public void setAlternate(String alternate) {
		this.alternate = alternate;
	}

	public Product(String identifier, String version, String title, String type, String alternate) {
		this.identifier = identifier;
		this.version = version;
		this.title = title;
		this.type = type;
		this.alternate = alternate;
	}

	public Product() {
		// TODO Auto-generated constructor stub
	}

	public void setAStatus(String astatus) {
		aStatus = astatus;
		
	}
	public void setCStatus(String cstatus) {
		cStatus = cstatus;
		
	}
	public void setNssdca(String nss) {
		nssdca = nss;
		
	}
	public String getAStatus() {
		return aStatus;
	}
	public String getCStatus() {
		return cStatus;
	}
	public String getNssdca() {
		return nssdca;
	}
	
	private String releasesName = null;	
	private Timestamp releasesDate = null;
	private String releasesDescription = null;
	
	public void setReleasesName(String name) {
		this.releasesName = name;
	}

	public void setReleasesDate(Timestamp release_date) {
		this.releasesDate = release_date;
		
	}

	public void setReleasesDescription(String description) {
		this.releasesDescription = description;
		
	}
	/**
	 * @return the releasesName
	 */
	public String getReleasesName() {
		return releasesName;
	}

	/**
	 * @return the releasesDate
	 */
	public Timestamp getReleasesDate() {
		return releasesDate;
	}

	/**
	 * @return the releasesDescription
	 */
	public String getReleasesDescription() {
		return releasesDescription;
	}

}
