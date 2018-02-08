package it.univaq.planner.business.model;

import java.io.Serializable;

public class Acl implements Serializable {

	private static final long serialVersionUID = -6782800714311531589L;
	
	public Acl(Long id, Long idGroup, Long idUser, Boolean enableAccess, Boolean enableCrud) {
		super();
		this.id = id;
		this.idGroup = idGroup;
		this.idUser = idUser;
		this.enableAccess = enableAccess;
		this.enableCrud = enableCrud;
	}
	public Acl() {
		super();
	}
	
	private Long id;
	private Long idGroup;
	private Long idUser;
	private Boolean enableAccess;
	private Boolean enableCrud;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getIdGroup() {
		return idGroup;
	}
	public void setIdGroup(Long idGroup) {
		this.idGroup = idGroup;
	}
	public Long getIdUser() {
		return idUser;
	}
	public void setIdUser(Long idUser) {
		this.idUser = idUser;
	}
	public Boolean getEnableAccess() {
		return enableAccess;
	}
	public void setEnableAccess(Boolean enableAccess) {
		this.enableAccess = enableAccess;
	}
	public Boolean getEnableCrud() {
		return enableCrud;
	}
	public void setEnableCrud(Boolean enableCrud) {
		this.enableCrud = enableCrud;
	}
	
	@Override
	public String toString() {
		return "Acl [id=" + id + ", idGroup=" + idGroup + ", idUser=" + idUser + ", enableAccess=" + enableAccess
				+ ", enableCrud=" + enableCrud + "]";
	}
	
}
