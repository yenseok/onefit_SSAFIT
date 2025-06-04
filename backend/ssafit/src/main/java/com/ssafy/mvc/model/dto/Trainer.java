package com.ssafy.mvc.model.dto;

public class Trainer {
	private int trainerId;
	private String trainerName;
	private int gymId;
	private String trainerImg;

	public Trainer() {
	}

	public Trainer(int trainerId, String trainerName, int gymId) {
		super();
		this.trainerId = trainerId;
		this.trainerName = trainerName;
		this.gymId = gymId;
	}

	public int getTrainerId() {
		return trainerId;
	}

	public void setTrainerId(int trainerId) {
		this.trainerId = trainerId;
	}

	public String getTrainerName() {
		return trainerName;
	}

	public void setTrainerName(String trainerName) {
		this.trainerName = trainerName;
	}

	public int getGymId() {
		return gymId;
	}

	public void setGymId(int gymId) {
		this.gymId = gymId;
	}

	public String getTrainerImg() {
		return trainerImg;
	}

	public void setTrainerImg(String trainerImg) {
		this.trainerImg = trainerImg;
	}

	@Override
	public String toString() {
		return "Trainer [trainerId=" + trainerId + ", trainerName=" + trainerName + ", gymId=" + gymId + "]";
	}

}
