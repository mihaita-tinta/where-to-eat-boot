package com.wheretoeat.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.wheretoeat.domain.DomainUser;
import com.wheretoeat.repository.DomainUserRepository;
import com.wheretoeat.security.SecurityUser;

@Service
public class DomainUserService implements UserDetailsService {

	private final DomainUserRepository userRepo;
	
	@Autowired
	public DomainUserService(DomainUserRepository userRepo) {
		this.userRepo = userRepo;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		DomainUser user = userRepo.findByEmail(username);
		
		if (user == null)
			throw new UsernameNotFoundException(username + " not found");
		
		return new SecurityUser(user);
	}
}
