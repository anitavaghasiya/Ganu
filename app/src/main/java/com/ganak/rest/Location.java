package com.ganak.rest;

import com.ganak.model.LocationResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface Location {
    @FormUrlEncoded
    @POST("/api/v2/get_locations.php")
    Call<LocationResponse> getLocationData(@Field("token") String token, @Field("id") String id, @Field("shape_id") String shape_id, @Field("grade_id") String grade_id);
}
