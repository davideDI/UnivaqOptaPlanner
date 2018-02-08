package it.univaq.planner.business;

import java.util.List;

import it.univaq.planner.business.model.Repeat;

public interface RepeatService {

	List<Repeat> getAllRepeats() throws Exception;
	List<Repeat> getRepeatsByIdBooking(Long idBooking) throws Exception;
	
}
