package com.wheretoeat;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.wheretoeat.domain.DomainUser;
import com.wheretoeat.domain.Location;
import com.wheretoeat.domain.Vote;
import com.wheretoeat.repository.DomainUserRepository;
import com.wheretoeat.repository.LocationRepository;
import com.wheretoeat.repository.VoteRepository;

@Component
public class DatabasePopulator implements CommandLineRunner{
	@Autowired
	private LocationRepository locationRepo;
	
	@Autowired
	private VoteRepository voteRepo;

	@Autowired
	private DomainUserRepository userRepo;

	@Autowired
	private BCryptPasswordEncoder encoder;
	
	@Override
	public void run(String... args) throws Exception {
		locationRepo.save(Arrays.asList(new Location("Cora", 10),
				new Location("Plaza", 15),
				new Location("Plaza - Pizza Hut", 25),
				new Location("Afi", 20)
			));
		
		userRepo.save(Arrays.asList("adrian.cosma,a", "denisa.cirstescu,a", "bogdan.popescu,a",
				"oana.zamfir,a", "bogdan.ravdan,a", "mihaita.tinta,a"				
				).stream()
				.map(s -> {
					String username = s.split(",")[0];
					String password = s.split(",")[1];
					DomainUser user = new DomainUser();
					user.setEmail(username);
					user.setPassword(encoder.encode(password));
					user.setLocked(false);
					return user;
					})
		        .collect(Collectors.toList())
						);
		
		Location cora = locationRepo.findOne(1);
		Location plaza = locationRepo.findOne(2);
		Location pizzaHut = locationRepo.findOne(3);
		
		voteRepo.save(Arrays.asList(
				new Vote(LocalDate.now(), cora, "I wanna go there.", userRepo.findByEmail("adrian.cosma")),
				new Vote(LocalDate.now(), pizzaHut, "I wanna go there.", userRepo.findByEmail("denisa.cirstescu")),
				new Vote(LocalDate.now(), plaza, "I wanna go there.", userRepo.findByEmail("bogdan.popescu")),
				new Vote(LocalDate.now(), cora, "I wanna go there.", userRepo.findByEmail("oana.zamfir")),
				new Vote(LocalDate.now(), plaza, "I wanna go there.", userRepo.findByEmail("bogdan.ravdan")),
				new Vote(LocalDate.now(), plaza, "I wanna go there.", userRepo.findByEmail("mihaita.tinta"))
			));
	}
}
