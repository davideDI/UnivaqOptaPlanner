package it.univaq.planner.common.spring;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import it.univaq.planner.business.model.Role;
import it.univaq.planner.business.model.User;

public class UserDetailsImpl implements UserDetails {

	private static final long serialVersionUID = 7830243429281366210L;
	
	private User user;
	
	public UserDetailsImpl(User user) {
		super();
		this.user = user;
	}
	
	@Override
	public Collection<GrantedAuthority> getAuthorities() {
		List<GrantedAuthority> result = new ArrayList<GrantedAuthority>();
		for (Role role : user.getRoles()) {
			result.add(new GrantedAuthorityImpl(role.getName()));			
		}
		return result;
	}

	@Override
	public String getPassword() {
		return user.getPassword();
	}

	@Override
	public String getUsername() {
		return user.getCn();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
	
	public User getUser() {
		return user;
	}

}
