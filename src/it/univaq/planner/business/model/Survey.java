package it.univaq.planner.business.model;

import java.io.Serializable;

public class Survey implements Serializable {

	private static final long serialVersionUID = 3717175793178769167L;
	
	public Survey() {
		super();
	}

	public Survey(Long id, Integer realNumStudents, String note, Long idRepeat, Long requestedBy, Long performedBy,
			Short idTipSurveyStatus) {
		super();
		this.id = id;
		this.realNumStudents = realNumStudents;
		this.note = note;
		this.idRepeat = idRepeat;
		this.requestedBy = requestedBy;
		this.performedBy = performedBy;
		this.idTipSurveyStatus = idTipSurveyStatus;
	}

	private Long id;
	private Integer realNumStudents;
	private String note;
	private Long idRepeat;
	private Long requestedBy;
	private Long performedBy;
	private Short idTipSurveyStatus;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Integer getRealNumStudents() {
		return realNumStudents;
	}
	public void setRealNumStudents(Integer realNumStudents) {
		this.realNumStudents = realNumStudents;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public Long getIdRepeat() {
		return idRepeat;
	}
	public void setIdRepeat(Long idRepeat) {
		this.idRepeat = idRepeat;
	}
	public Long getRequestedBy() {
		return requestedBy;
	}
	public void setRequestedBy(Long requestedBy) {
		this.requestedBy = requestedBy;
	}
	public Long getPerformedBy() {
		return performedBy;
	}
	public void setPerformedBy(Long performedBy) {
		this.performedBy = performedBy;
	}
	public Short getIdTipSurveyStatus() {
		return idTipSurveyStatus;
	}
	public void setIdTipSurveyStatus(Short idTipSurveyStatus) {
		this.idTipSurveyStatus = idTipSurveyStatus;
	}
	
	@Override
	public String toString() {
		return "Survey [id=" + id + ", realNumStudents=" + realNumStudents + ", note=" + note + ", idRepeat=" + idRepeat
				+ ", requestedBy=" + requestedBy + ", performedBy=" + performedBy + ", idTipSurveyStatus="
				+ idTipSurveyStatus + "]";
	}
	
}
