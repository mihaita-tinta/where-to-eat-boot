package com.wheretoeat.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wheretoeat.domain.DomainUser;
import com.wheretoeat.repository.DomainUserRepository;
import com.wheretoeat.security.SecurityUser;

@RequestMapping("/users")
@RestController
public class UserController {

	private final DomainUserRepository userRepo;
	
	@Autowired
	public UserController(DomainUserRepository userRepo) {
		this.userRepo = userRepo;		
	}
	
	@RequestMapping("/account")
	public DomainUser getCurrentUser(Authentication authentication) {
		DomainUser user = ((SecurityUser) authentication.getPrincipal()).getUser();		
		return user;
	}

	@RequestMapping
	public List<DomainUser> get(Authentication authentication) {		
		return userRepo.findAll();
	}
}
