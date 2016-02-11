package com.wheretoeat.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wheretoeat.domain.Location;

public interface LocationRepository extends JpaRepository<Location, Integer>{
	public List<Location> findByNameContainsIgnoreCase(String name);
}
