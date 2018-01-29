package it.univaq.planner.common.spring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import it.univaq.planner.business.LoginService;
import it.univaq.planner.business.model.User;

public class UserDetailsServiceImpl implements UserDetailsService  {

	@Autowired
	private LoginService loginService;
	
	@Override
	public UserDetails loadUserByUsername(String arg) throws UsernameNotFoundException {
		
		try {
		
			User user = loginService.authenticate(arg);
			if(user == null)
				throw new UsernameNotFoundException("");
			
			return new UserDetailsImpl(user);
			
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		
		return null;
			
	}
	
}