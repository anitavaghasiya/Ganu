package com.ganak.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RegistrationResponse {

    @SerializedName("error")
    @Expose
    private Integer error;
    @SerializedName("error_code")
    @Expose
    private String errorCode;
    @SerializedName("reg_id")
    @Expose
    private Integer regId;
    @SerializedName("reg_token")
    @Expose
    private String regToken;

    public Integer getError() {
        return error;
    }

    public void setError(Integer error) {
        this.error = error;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public Integer getRegId() {
        return regId;
    }

    public void setRegId(Integer regId) {
        this.regId = regId;
    }

    public String getRegToken() {
        return regToken;
    }

    public void setRegToken(String regToken) {
        this.regToken = regToken;
    }
}
