package com.wheretoeat.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.wheretoeat.domain.DomainUser;



public class SecurityUser implements UserDetails, Authentication {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7230719699059279896L;
	
	public final static String ROLE_ADMIN = "ROLE_ADMIN";
	public final static String ROLE_USER = "ROLE_USER";
    public static final String ANONYMOUS = "ROLE_ANONYMOUS";
    
	private final DomainUser user;
	private List<GrantedAuthority> authorities;
	private boolean authenticated;
	
	public SecurityUser(DomainUser user) {
		this.user = user;		
		authorities = new ArrayList<GrantedAuthority>();
		SimpleGrantedAuthority authority = new SimpleGrantedAuthority(ROLE_USER);
		authorities.add(authority);
	}

	public DomainUser getUser() {
		return user;
	}

	@Override
	public String getName() {
		return null;
	}

	@Override
	public Object getCredentials() {
		return this;
	}

	@Override
	public Object getDetails() {
		return this;
	}

	@Override
	public Object getPrincipal() {
		return this;
	}

	@Override
	public boolean isAuthenticated() {
		return authenticated;
	}

	@Override
	public void setAuthenticated(boolean isAuthenticated)
			throws IllegalArgumentException {
		authenticated = isAuthenticated;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	@Override
	public String getPassword() {
		return user.getPassword();
	}

	@Override
	public String getUsername() {
		return user.getEmail();
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
		return !user.isLocked();
	}

}