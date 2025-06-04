package com.ssafy.mvc.model.dto;

import java.util.List;

public class Equipment {
	private int equipmentId;
	private String equipmentName;
	private String equipmentImg;
	private int gymId;
	private List<String> tags;
	
	public Equipment() {
	}

	public Equipment(int equipmentId, String equipmentName, String equipmentImg, int gymId, List<String> tags) {
		super();
		this.equipmentId = equipmentId;
		this.equipmentName = equipmentName;
		this.equipmentImg = equipmentImg;
		this.gymId = gymId;
		this.tags = tags;
	}

	public int getEquipmentId() {
		return equipmentId;
	}

	public void setEquipmentId(int equipmentId) {
		this.equipmentId = equipmentId;
	}

	public String getEquipmentName() {
		return equipmentName;
	}

	public void setEquipmentName(String equipmentName) {
		this.equipmentName = equipmentName;
	}

	public String getEquipmentImg() {
		return equipmentImg;
	}

	public void setEquipmentImg(String equipmentImg) {
		this.equipmentImg = equipmentImg;
	}

	public int getGymId() {
		return gymId;
	}

	public void setGymId(int gymId) {
		this.gymId = gymId;
	}

	public List<String> getTags() {
		return tags;
	}

	public void setTags(List<String> tags) {
		this.tags = tags;
	}

	
}
