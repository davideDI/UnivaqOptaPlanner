package it.univaq.planner.business.model;

import java.io.Serializable;
import java.util.Date;

public class Repeat implements Serializable {

	private static final long serialVersionUID = 3275707283958412585L;
	
	public Repeat() {
		super();
	}
	
	public Repeat(Long id, Date eventDateStart, Date eventDateEnd, Long idBooking, Short idTipBookingStatus) {
		super();
		this.id = id;
		this.eventDateStart = eventDateStart;
		this.eventDateEnd = eventDateEnd;
		this.idBooking = idBooking;
		this.idTipBookingStatus = idTipBookingStatus;
	}

	private Long id;
	private Date eventDateStart;
	private Date eventDateEnd;
	private Long idBooking;
	private Short idTipBookingStatus;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Date getEventDateStart() {
		return eventDateStart;
	}
	public void setEventDateStart(Date eventDateStart) {
		this.eventDateStart = eventDateStart;
	}
	public Date getEventDateEnd() {
		return eventDateEnd;
	}
	public void setEventDateEnd(Date eventDateEnd) {
		this.eventDateEnd = eventDateEnd;
	}
	public Long getIdBooking() {
		return idBooking;
	}
	public void setIdBooking(Long idBooking) {
		this.idBooking = idBooking;
	}
	public Short getIdTipBookingStatus() {
		return idTipBookingStatus;
	}
	public void setIdTipBookingStatus(Short idTipBookingStatus) {
		this.idTipBookingStatus = idTipBookingStatus;
	}
	
	@Override
	public String toString() {
		return "Repeat [id=" + id + ", eventDateStart=" + eventDateStart + ", eventDateEnd=" + eventDateEnd
				+ ", idBooking=" + idBooking + ", idTipBookingStatus=" + idTipBookingStatus + "]";
	}
	
}
