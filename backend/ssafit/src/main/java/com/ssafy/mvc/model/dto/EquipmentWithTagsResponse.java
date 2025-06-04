package com.ssafy.mvc.model.dto;

import java.util.List;

public class EquipmentWithTagsResponse {
    private int equipmentId;
    private String equipmentName;
    private String equipmentImg;
    private List<String> tags;

    public EquipmentWithTagsResponse() {}

    public EquipmentWithTagsResponse(int equipmentId, String equipmentName, String equipmentImg, List<String> tags) {
        this.equipmentId = equipmentId;
        this.equipmentName = equipmentName;
        this.equipmentImg = equipmentImg;
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

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }
}
