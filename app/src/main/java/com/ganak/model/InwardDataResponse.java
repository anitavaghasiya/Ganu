package com.ganak.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class InwardDataResponse {

    @SerializedName("user")
    @Expose
    private User user;
    @SerializedName("error")
    @Expose
    private String error;
    @SerializedName("error_code")
    @Expose
    private Integer errorCode;
    @SerializedName("msg")
    @Expose
    private String msg;

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

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

}
