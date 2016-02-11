package com.wheretoeat.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wheretoeat.domain.DomainUser;

public interface DomainUserRepository extends JpaRepository<DomainUser, Integer>{
	public DomainUser findByEmail(String email);
}
