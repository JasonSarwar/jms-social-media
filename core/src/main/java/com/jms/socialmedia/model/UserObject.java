package com.jms.socialmedia.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class UserObject {

	private final int userId;
	private final String username;
	private final String passwordHash;
	private final String passwordkey;
	private final String firstName;
	private final String lastName;
	private final String emailAddress;
	private final LocalDate birthdate;
	private final LocalDateTime timeOfCreation;
	private final String profilePicLink;
	
	private UserObject(Builder b) {
		userId = b.userId;
		username = b.username;
		passwordHash = b.passwordHash;
		passwordkey = b.passwordkey;
		firstName = b.firstName;
		lastName = b.lastName;
		emailAddress = b.emailAddress;
		birthdate = b.birthdate;
		timeOfCreation = b.timeOfCreation;
		profilePicLink = b.profilePicLink;
	}
	
	public final int getUserId() {
		return userId;
	}

	public final String getUsername() {
		return username;
	}

	public final String getPasswordHash() {
		return passwordHash;
	}

	public final String getPasswordkey() {
		return passwordkey;
	}

	public final String getFirstName() {
		return firstName;
	}

	public final String getLastName() {
		return lastName;
	}

	public final String getEmailAddress() {
		return emailAddress;
	}

	public final LocalDate getBirthdate() {
		return birthdate;
	}

	public final LocalDateTime getTimeOfCreation() {
		return timeOfCreation;
	}

	public final String getProfilePicLink() {
		return profilePicLink;
	}

	public static final Builder newBuilder() {
		return new Builder();
	}
	
	public static class Builder {
		
		private int userId;
		private String username;
		private String passwordHash;
		private String passwordkey;
		private String firstName;
		private String lastName;
		private String emailAddress;
		private LocalDate birthdate;
		private LocalDateTime timeOfCreation;
		private String profilePicLink;
		
		public UserObject build() {
			return new UserObject(this);
		}
		
		public Builder setUserId(int userId) {
			this.userId = userId;
			return this;
		}
		public Builder setUsername(String username) {
			this.username = username;
			return this;
		}
		public Builder setPasswordHash(String passwordHash) {
			this.passwordHash = passwordHash;
			return this;
		}
		public Builder setPasswordkey(String passwordkey) {
			this.passwordkey = passwordkey;
			return this;
		}
		public Builder setFirstName(String firstName) {
			this.firstName = firstName;
			return this;
		}
		public Builder setLastName(String lastName) {
			this.lastName = lastName;
			return this;
		}
		public Builder setEmailAddress(String emailAddress) {
			this.emailAddress = emailAddress;
			return this;
		}
		public Builder setBirthdate(String birthdate) {
			this.birthdate = LocalDate.parse(birthdate, DateTimeFormatter.ISO_DATE);
			return this;
		}
		public Builder setTimeOfCreation(String timeOfCreation) {
			this.timeOfCreation = LocalDateTime.parse(timeOfCreation);
			return this;
		}
		public Builder setTimeOfCreation(LocalDateTime timeOfCreation) {
			this.timeOfCreation = timeOfCreation;
			return this;
		}
		public Builder setProfilePicLink(String profilePicLink) {
			this.profilePicLink = profilePicLink;
			return this;
		}
		
	}
}
