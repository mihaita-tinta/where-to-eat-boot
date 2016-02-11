package com.wheretoeat.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.wheretoeat.domain.DomainUser;
import com.wheretoeat.repository.DomainUserRepository;

@Controller
public class HomeController {

	@Autowired
	private DomainUserRepository userRepo;

	@Autowired
	private BCryptPasswordEncoder encoder;
		
    @RequestMapping("/")
	public String getAllLocations(Model model) {		
		return "redirect:/index.html";
	}
    
    @RequestMapping(value="/register", method = RequestMethod.POST)
    public ResponseEntity<?> register(@RequestBody DomainUser user) {
    	
    	DomainUser existingUser = userRepo.findByEmail(user.getEmail());
    	
    	if (existingUser != null) {

			return new ResponseEntity<String>("{\"success\":false,\"message\":\"Email already exists\"}", HttpStatus.BAD_REQUEST);
    	}

    	user.setPassword(encoder.encode(user.getPassword()));
    	user.setLocked(false);
    	userRepo.save(user);
    	// TODO maybe send an email to activate the account later
		return new ResponseEntity<DomainUser>(user, HttpStatus.OK);
    }
}
