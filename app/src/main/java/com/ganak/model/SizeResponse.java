package com.ganak.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SizeResponse {
    @SerializedName("user")
    @Expose
    private User user;
    @SerializedName("error")
    @Expose
    private String error;
    @SerializedName("error_code")
    @Expose
    private Integer errorCode;
    @SerializedName("sizes")
    @Expose
    private List<Size> sizes = null;

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

    public List<Size> getSizes() {
        return sizes;
    }

    public void setSizes(List<Size> sizes) {
        this.sizes = sizes;
    }

}
