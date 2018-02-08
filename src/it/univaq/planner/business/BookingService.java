package it.univaq.planner.business;

import java.util.List;

import it.univaq.planner.business.model.Booking;

public interface BookingService {

	List<Booking> getAllBookings() throws Exception; 
	List<String> getDifferentTeacherId() throws Exception;
	List<String> getDifferentTeacherIdByIdResource(Long idResource) throws Exception;
	List<Booking> getAllBookingsByIdResource(Long idResource) throws Exception; 
	
}
