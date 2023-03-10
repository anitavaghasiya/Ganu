package com.ganak.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Grade {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;

    public Grade(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public Grade() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    @Override
    public String toString() {
        return name;
    }
}
