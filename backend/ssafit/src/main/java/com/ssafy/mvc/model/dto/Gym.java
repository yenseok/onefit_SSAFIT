package com.ssafy.mvc.model.dto;

public class Gym {
	private int gymId;
	private String gymName;
	private String gymAddress;
	
	public Gym() {
	}

	public Gym(int gymId, String gymName, String gymAddress) {
		super();
		this.gymId = gymId;
		this.gymName = gymName;
		this.gymAddress = gymAddress;
	}

	public int getGymId() {
		return gymId;
	}

	public void setGymId(int gymId) {
		this.gymId = gymId;
	}

	public String getGymName() {
		return gymName;
	}

	public void setGymName(String gymName) {
		this.gymName = gymName;
	}

	public String getGymAddress() {
		return gymAddress;
	}

	public void setGymAddress(String gymAddress) {
		this.gymAddress = gymAddress;
	}

	@Override
	public String toString() {
		return "Gym [gymId=" + gymId + ", gymName=" + gymName + ", gymAddress=" + gymAddress + "]";
	}
	
	
}
