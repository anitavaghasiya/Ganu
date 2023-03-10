package com.ganak.rest;

import com.ganak.model.ShapeResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface Shape {
    @FormUrlEncoded
    @POST("/api/v2/get_shapes.php")
    Call<ShapeResponse> getShapeResponse(@Field("token") String token, @Field("id") String id, @Field("grade_id") String grade_id);
}
