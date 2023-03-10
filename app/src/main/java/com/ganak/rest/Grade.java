package com.ganak.rest;

import com.ganak.model.GradesResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface Grade {
    @FormUrlEncoded
    @POST("/api/v2/get_grades.php")
    Call<GradesResponse> getGradesResponse(@Field("token") String token, @Field("id") String id);
}

