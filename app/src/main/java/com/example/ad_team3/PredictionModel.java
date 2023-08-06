package com.example.ad_team3;

import com.google.gson.annotations.SerializedName;


public class PredictionModel {

    @SerializedName("modelId")
    private int modelId;

    @SerializedName("createdBy")
    private String createdBy;

    @SerializedName("modelName")
    private String modelName;

    @SerializedName("createdDate")
    private String createdDate;

    @SerializedName("modelFileURL")
    private String modelFileURL;

    @SerializedName("isDefault")
    private boolean isDefault;

    public int getModelId() {
        return modelId;
    }

    public void setModelId(int modelId) {
        this.modelId = modelId;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getModelFileURL() {
        return modelFileURL;
    }

    public void setModelFileURL(String modelFileURL) {
        this.modelFileURL = modelFileURL;
    }

    public boolean getIsDefault(){
        return isDefault;
    }

    public void setIsDefault(boolean isDefault){
        this.isDefault = isDefault;
    }
}

