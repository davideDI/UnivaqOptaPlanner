package it.univaq.planner.business;

import it.univaq.planner.business.model.User;

public interface LoginService {

	User authenticate(String arg) throws Exception;
	
}
