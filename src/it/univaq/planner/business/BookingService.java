package it.univaq.planner.business;

import java.util.List;

import it.univaq.planner.business.model.Booking;
import it.univaq.planner.business.model.TipEvent;

public interface BookingService {

	List<Booking> getAllBookings() throws Exception; 
	List<String> getDifferentTeacherId() throws Exception;
	List<String> getDifferentTeacherIdByIdGroupAndTipEvent(Long idGroup, List<TipEvent> tipEventList) throws Exception;
	List<Booking> getAllBookingsByIdResource(Long idResource) throws Exception; 
	List<Booking> getAllBookingsByIdGroup(Long idGroup, List<TipEvent> tipEventList) throws Exception; 
	
}
