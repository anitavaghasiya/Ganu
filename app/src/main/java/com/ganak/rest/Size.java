package com.ganak.rest;

import com.ganak.model.SizeResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface Size {
    @FormUrlEncoded
    @POST("/api/v2/get_sizes.php")
    Call<SizeResponse> getSizeData(@Field("token") String token, @Field("id") String id, @Field("grade_id") String grade_id, @Field("shape_id") String shape_id);
}
