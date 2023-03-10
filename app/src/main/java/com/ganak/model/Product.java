package com.ganak.model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Product {

    @SerializedName("num_pcs")
    @Expose
    private String numPcs;
    @SerializedName("num_weight")
    @Expose
    private String numWeight;
    @SerializedName("num_length")
    @Expose
    private String numLength;
    @SerializedName("shape_name")
    @Expose
    private String shapeName;
    @SerializedName("location_name")
    @Expose
    private String locationName;
    @SerializedName("grade_name")
    @Expose
    private String gradeName;
    @SerializedName("pcs")
    @Expose
    private String pcs;
    @SerializedName("length")
    @Expose
    private String length;
    @SerializedName("weight")
    @Expose
    private String weight;
    @SerializedName("size_name")
    @Expose
    private String sizeName;

    public String getShapeName() {
        return shapeName;
    }

    public void setShapeName(String shapeName) {
        this.shapeName = shapeName;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getGradeName() {
        return gradeName;
    }

    public void setGradeName(String gradeName) {
        this.gradeName = gradeName;
    }

    public String getPcs() {
        return pcs;
    }

    public void setPcs(String pcs) {
        this.pcs = pcs;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getSizeName() {
        return sizeName;
    }

    public void setSizeName(String sizeName) {
        this.sizeName = sizeName;
    }

    public String getNumPcs() {
        return numPcs;
    }

    public void setNumPcs(String numPcs) {
        this.numPcs = numPcs;
    }

    public String getNumWeight() {
        return numWeight;
    }

    public void setNumWeight(String numWeight) {
        this.numWeight = numWeight;
    }

    public String getNumLength() {
        return numLength;
    }

    public void setNumLength(String numLength) {
        this.numLength = numLength;
    }
}
