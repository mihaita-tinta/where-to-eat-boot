package com.wheretoeat.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.wheretoeat.domain.DomainUser;
import com.wheretoeat.domain.Location;
import com.wheretoeat.domain.Vote;
import com.wheretoeat.repository.VoteRepository;
import com.wheretoeat.security.SecurityUser;
import com.wheretoeat.service.VotingService;

@RequestMapping("/voting")
@RestController
public class VoteController {
	private final VoteRepository voteRepository;
	private final VotingService votingService;
	
	@Autowired
	public VoteController(VoteRepository voteRepository,
			VotingService votingService) {
		this.voteRepository = voteRepository;
		this.votingService = votingService;
	}
	
	@ResponseStatus(HttpStatus.OK)
	@RequestMapping(method = RequestMethod.GET, value = "/todayList")
	public List<Vote> showTodayVotes () {
		return voteRepository.findByDate(LocalDate.now());
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<?> addVote(@RequestBody Vote inputVote, Authentication authentication) {
		DomainUser user = ((SecurityUser) authentication.getPrincipal()).getUser();
		
		inputVote.setUser(user);
		inputVote.setDate(LocalDate.now());
		Vote validatedVote = votingService.parseVote(inputVote);
		ResponseEntity<?> response = computeResponseStatus(validatedVote);
		
		if(response.getStatusCode().is2xxSuccessful()) {
			voteRepository.save(validatedVote);
		}		
		
		return response;
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/todayPoll")
	public List<VoteLine> showTodaysPollResult() {
		 Map<Location, Double> results = votingService.getTodaysPollResults();
		 
		 return results.entrySet().stream().map( e -> {
			 VoteLine vote = new VoteLine();
			 vote.locationId = e.getKey().getLocationId();
			 vote.percent = e.getValue();
			 return vote;
		 })
		.sorted( (e1, e2) -> new Double(e1.percent).compareTo(e2.percent))
		.collect(Collectors.toList());
	}

	private ResponseEntity<?> computeResponseStatus(Vote validatedVote) {
		ResponseEntity<?> response = new ResponseEntity<> (HttpStatus.ACCEPTED);
		if (StringUtils.isEmpty(validatedVote.getUser())) {
			response = new ResponseEntity<> ("The username cannot be null!", HttpStatus.NOT_ACCEPTABLE);
		} else if (validatedVote.isNewVote()) {
			response = new ResponseEntity<> (HttpStatus.CREATED);
		}
		return response;
	}
	
	static class VoteLine {
		int locationId;
		double percent;
		public int getLocationId() {
			return locationId;
		}
		public void setLocationId(int locationId) {
			this.locationId = locationId;
		}
		public double getPercent() {
			return percent;
		}
		public void setPercent(double percent) {
			this.percent = percent;
		}
		
		
	}
	
}
