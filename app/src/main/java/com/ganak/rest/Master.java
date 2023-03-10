package com.ganak.rest;

import com.ganak.model.MasterDataResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface Master {
    @FormUrlEncoded
    @POST("/api/v2/insert_master.php")
    Call<MasterDataResponse> getMasterData(@Field("type") String type, @Field("name") String name, @Field("token") String token,
                                           @Field("id") String id);

    @FormUrlEncoded
    @POST("/api/v2/insert_master.php")
    Call<MasterDataResponse> getShapeMasterData(@Field("type") String type, @Field("grade_id") String grade_id, @Field("name") String name, @Field("token") String token,
                                                @Field("id") String id);

    @FormUrlEncoded
    @POST("/api/v2/insert_master.php")
    Call<MasterDataResponse> getLocationMasterData(@Field("type") String type, @Field("grade_id") String grade_id, @Field("shape_id") String shape_id, @Field("name") String name, @Field("token") String token,
                                                   @Field("id") String id);

    @FormUrlEncoded
    @POST("/api/v2/insert_master.php")
    Call<MasterDataResponse> getSizeMasterData(@Field("type") String type, @Field("grade_id") String grade_id, @Field("shape_id") String shape_id, @Field("name") String name, @Field("token") String token,
                                               @Field("id") String id);
}
