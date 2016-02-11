package com.wheretoeat.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wheretoeat.domain.DomainUser;
import com.wheretoeat.domain.Vote;

public interface VoteRepository extends JpaRepository<Vote, Integer>{
	List<Vote> findByDate(LocalDate date);
	Vote findOneByUserAndDate(DomainUser user, LocalDate date);
}
