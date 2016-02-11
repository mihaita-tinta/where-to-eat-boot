package com.wheretoeat.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Location {
	@Id
	@GeneratedValue
	private int locationId;
	
	private String name;
	private int minutesOnRoad;
	
	public Location(String name, int minutesOnRoad) {
		super();
		this.name = name;
		this.minutesOnRoad = minutesOnRoad;
	}

	public Location() {
		super();
	}

	public int getLocationId() {
		return locationId;
	}

	public void setLocationId(int locationId) {
		this.locationId = locationId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getMinutesOnRoad() {
		return minutesOnRoad;
	}

	public void setMinutesOnRoad(int minutesOnRoad) {
		this.minutesOnRoad = minutesOnRoad;
	}

	@Override
	public String toString() {
		return "Location [locationId=" + locationId + ", name=" + name + ", minutesOnRoad=" + minutesOnRoad + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + locationId;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Location other = (Location) obj;
		if (locationId != other.locationId)
			return false;
		return true;
	}

}
