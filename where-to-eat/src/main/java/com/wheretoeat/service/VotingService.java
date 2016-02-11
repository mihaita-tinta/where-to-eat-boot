package com.wheretoeat.service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wheretoeat.domain.Location;
import com.wheretoeat.domain.Vote;
import com.wheretoeat.exception.EmptyLocationException;
import com.wheretoeat.repository.VoteRepository;

@Service
public class VotingService {
	private VoteRepository voteRepository;
	
	@Autowired
	public VotingService(VoteRepository voteRepository) {
		this.voteRepository = voteRepository;
	}
	
	/**
	 * If the there is another vote made today for the given user.
	 * If the user already voted today, it will update the location
	 * of the existent vote, otherwise it will add a new vote in 
	 * the database.
	 * 
	 * @param inputVote - the vote that needs to be parsed.
	 * @throws EmptyLocationException if a location is not provided
	 */
	public Vote parseVote(Vote inputVote){
		if (inputVote.getLocation() == null) {
			throw new EmptyLocationException("You must provide a location with any vote.");
		}
		Vote existentVote = voteRepository.findOneByUserAndDate(inputVote.getUser(), LocalDate.now());
		
		if (existentVote != null) {
			existentVote.setLocation(inputVote.getLocation());
			inputVote = existentVote;			
		} else {
			inputVote.setDate(LocalDate.now());
		}
		
		return inputVote;
	}
	
	public Map<Location, Double> getTodaysPollResults() {
		List<Vote> todaysVotes = voteRepository.findByDate(LocalDate.now());
			
		Map<Location, Long> pollResultsCount = todaysVotes.stream()
				.collect(Collectors.groupingBy(Vote::getLocation, Collectors.counting()));
		
		final int totalVotesToday = todaysVotes.size();
		
		Map<Location, Double> pollResults = new HashMap<>();
		pollResultsCount.forEach((k, v) -> pollResults.put(k, (double) v *100 / totalVotesToday)); 
		
		return pollResults;
	}
}
