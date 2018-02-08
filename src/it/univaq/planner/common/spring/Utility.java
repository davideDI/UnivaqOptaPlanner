package it.univaq.planner.common.spring;

import it.univaq.planner.business.model.User;

import java.util.Calendar;
import java.util.Date;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class Utility {

	public static User getUtente() {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if ( authentication != null) {
			if ( authentication.getPrincipal() instanceof UserDetailsImpl){
				UserDetailsImpl userDetailsImpl = (UserDetailsImpl) authentication.getPrincipal();
				return userDetailsImpl.getUser();
			}
		}
		return null;
	}
	
	public static Date getUtilDateFromSqlDate(java.sql.Date sqlDate) {
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(sqlDate);
		
		java.util.Date dateUtil = new Date(sqlDate.getTime());
		return dateUtil;
		
	}

}