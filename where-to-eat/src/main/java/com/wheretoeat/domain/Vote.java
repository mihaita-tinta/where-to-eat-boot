package com.wheretoeat.domain;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

@Entity
public class Vote {
	@Id
	@GeneratedValue
	private int voteId;
	
	private LocalDate date;
	
	@ManyToOne
	private Location location;
	private String comment;
	
	@OneToOne
	private DomainUser user;
	
	public Vote(LocalDate date, Location location, String comment, DomainUser user) {
		super();
		this.date = date;
		this.location = location;
		this.comment = comment;
		this.user = user;
	}
	
	public Vote() {
		super();
	}
	
	public boolean isNewVote() {
		return voteId < 1;
	}
	
	public int getVoteId() {
		return voteId;
	}
	public void setVoteId(int voteId) {
		this.voteId = voteId;
	}
	public LocalDate getDate() {
		return date;
	}
	public void setDate(LocalDate date) {
		this.date = date;
	}
	public Location getLocation() {
		return location;
	}
	public void setLocation(Location location) {
		this.location = location;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}

	
	public DomainUser getUser() {
		return user;
	}

	public void setUser(DomainUser user) {
		this.user = user;
	}

	@Override
	public String toString() {
		return "Vote [voteId=" + voteId + ", date=" + date + ", location=" + location + ", comment=" + comment
				+ ", user=" + user + "]";
	}
}
