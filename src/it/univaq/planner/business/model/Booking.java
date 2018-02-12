package it.univaq.planner.business.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class Booking implements Serializable {

	private static final long serialVersionUID = 4370517112010665205L;
	
	public Booking() {
		super();
	}

	public Booking(Long id, String name, String description, Date bookingDate, Integer numStudents, String subjectID,
			String teacherID, String cdsID, Short idTipEvent, Long idUser, List<Repeat> repeatList, Resource resource) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
		this.bookingDate = bookingDate;
		this.numStudents = numStudents;
		this.subjectID = subjectID;
		this.teacherID = teacherID;
		this.cdsID = cdsID;
		this.idTipEvent = idTipEvent;
		this.idUser = idUser;
		this.repeatList = repeatList;
		this.resource = resource;
	}

	private Long id;
	private String name;
	private String description;
	private Date bookingDate;
	private Integer numStudents;
	private String subjectID;
	private String teacherID;
	private String cdsID;
	private Short idTipEvent;
	private Long idUser;
	private List<Repeat> repeatList;
	private Resource resource;
	
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
	public Date getBookingDate() {
		return bookingDate;
	}
	public void setBookingDate(Date bookingDate) {
		this.bookingDate = bookingDate;
	}
	public Integer getNumStudents() {
		return numStudents;
	}
	public void setNumStudents(Integer numStudents) {
		this.numStudents = numStudents;
	}
	public String getSubjectID() {
		return subjectID;
	}
	public void setSubjectID(String subjectID) {
		this.subjectID = subjectID;
	}
	public String getTeacherID() {
		return teacherID;
	}
	public void setTeacherID(String teacherID) {
		this.teacherID = teacherID;
	}
	public String getCdsID() {
		return cdsID;
	}
	public void setCdsID(String cdsID) {
		this.cdsID = cdsID;
	}
	public Short getIdTipEvent() {
		return idTipEvent;
	}
	public void setIdTipEvent(Short idTipEvent) {
		this.idTipEvent = idTipEvent;
	}
	public Long getIdUser() {
		return idUser;
	}
	public void setIdUser(Long idUser) {
		this.idUser = idUser;
	}
	public List<Repeat> getRepeatList() {
		return repeatList;
	}
	public void setRepeatList(List<Repeat> repeatList) {
		this.repeatList = repeatList;
	}
	public Resource getResource() {
		return resource;
	}
	public void setResource(Resource resource) {
		this.resource = resource;
	}
	
	@Override
	public String toString() {
		return "Booking [id=" + id + ", name=" + name + ", description=" + description + ", bookingDate=" + bookingDate
				+ ", numStudents=" + numStudents + ", subjectID=" + subjectID + ", teacherID=" + teacherID + ", cdsID="
				+ cdsID + ", idTipEvent=" + idTipEvent + ", idUser=" + idUser + ", repeatList=" + repeatList
				+ ", resource=" + resource + "]";
	}
	
}
