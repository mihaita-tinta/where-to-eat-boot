package com.wheretoeat.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.wheretoeat.domain.Location;
import com.wheretoeat.repository.LocationRepository;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RequestMapping("/api/locations")
@RestController
public class LocationController {
	private LocationRepository locationRepo;
	
	@Autowired
	public LocationController(LocationRepository locationRepo) {
		this.locationRepo = locationRepo;
	}
	
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<List<Location>> getAllLocations() {
		List<Location> locationList = locationRepo.findAll();
		return new ResponseEntity<List<Location>>(locationList, HttpStatus.OK);
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<?> addLocation(@RequestBody Location location) {
		locationRepo.save(location);
		return new ResponseEntity<>(null, HttpStatus.CREATED);
	}
	
	@RequestMapping(value = "/{locationId}", method = RequestMethod.GET)
	public ResponseEntity<?> getLocationById(@PathVariable int locationId) {
		Location location = locationRepo.findOne(locationId);
		
		if(location == null) {
			return new ResponseEntity<String>("The location with id <" + locationId + "> was not found.", HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity<Location>(location, HttpStatus.OK);
	}
	
	@ApiParam(required = true, value = "The id of the location that will be deleted.")
	@ApiOperation(value = "The delete operation for locations. It is different from addLocation by using a DELETE http verb.")
	@ApiResponses(value = {
			@ApiResponse(code = 201, message = "The location with id {id} has been deleted."),
			@ApiResponse(code = 404, message = "The location with the id {id} cannot be found.")
	})
	@RequestMapping(value = "/{locationId}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteLocation(@PathVariable int locationId) {
		Location location = locationRepo.findOne(locationId);
		
		if(location == null) {
			return new ResponseEntity<String>("The location with id <" + locationId + "> was not found.", HttpStatus.NOT_FOUND);
		} else {
			return new ResponseEntity<String>("The location with id <" + locationId + "> has been deleted.", HttpStatus.ACCEPTED);
		}
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/find/{name}")
	public ResponseEntity<List<Location>> getLocationsByNameContainsIngnoreCase(
			@PathVariable String name) {
		List<Location> locationList = locationRepo.findByNameContainsIgnoreCase(name);
		return new ResponseEntity<List<Location>>(locationList, HttpStatus.OK);
	}
}
