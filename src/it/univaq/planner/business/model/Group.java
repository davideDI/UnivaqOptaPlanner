package it.univaq.planner.business.model;

import java.io.Serializable;
import java.util.List;

public class Group implements Serializable {

	private static final long serialVersionUID = -5552923438303840629L;
	
	public Group() {
		super();
	}
	
	public Group(Long id, String name, String description, Short idTipGroup) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
		this.idTipGroup = idTipGroup;
	}
	
	private Long id;
	private String name;
	private String description;
	private Short idTipGroup;
	private List<Resource> resourceList;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Short getIdTipGroup() {
		return idTipGroup;
	}
	public void setIdTipGroup(Short idTipGroup) {
		this.idTipGroup = idTipGroup;
	}
	public List<Resource> getResourceList() {
		return resourceList;
	}
	public void setResourceList(List<Resource> resourceList) {
		this.resourceList = resourceList;
	}

	@Override
	public String toString() {
		return "Group [id=" + id + ", name=" + name + ", description=" + description + ", idTipGroup=" + idTipGroup
				+ ", resourceList=" + resourceList + "]";
	}
	
}
