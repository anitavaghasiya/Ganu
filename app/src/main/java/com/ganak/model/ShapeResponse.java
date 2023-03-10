package com.ganak.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ShapeResponse {
    @SerializedName("user")
    @Expose
    private User user;
    @SerializedName("error")
    @Expose
    private String error;
    @SerializedName("error_code")
    @Expose
    private Integer errorCode;
    @SerializedName("shapes")
    @Expose
    private List<Shape> shapes = null;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }

    public List<Shape> getShapes() {
        return shapes;
    }

    public void setShapes(List<Shape> shapes) {
        this.shapes = shapes;
    }
}
