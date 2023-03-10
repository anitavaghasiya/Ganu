package com.ganak.rest;

import com.ganak.model.AddInwardProductResponse;
import com.ganak.model.AddOutwardProductResponse;
import com.ganak.model.InwardDataResponse;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface InwardData {
    @Multipart
    @POST("/api/v2/inward_data.php")
    Call<InwardDataResponse> getUploadFile(@Part("id") String id, @Part("token") String token, @Part("document") MultipartBody.Part file);

    @FormUrlEncoded
    @POST("/api/v2/insert_inward.php")
    Call<AddInwardProductResponse> inwardProduct(@Field("token") String token,
                                                 @Field("id") String id,
                                                 @Field("shape_id") String shape_id,
                                                 @Field("grade_id") String grade_id,
                                                 @Field("location_id") String location_id,
                                                 @Field("size_id") String size_id,
                                                 @Field("length") String length,
                                                 @Field("weight") String weight,
                                                 @Field("pcs") String pcs);

    @FormUrlEncoded
    @POST("/api/v2/insert_outward.php")
    Call<AddOutwardProductResponse> outwardProduct(@Field("token") String token,
                                                   @Field("id") String id,
                                                   @Field("shape_id") String shape_id,
                                                   @Field("grade_id") String grade_id,
                                                   @Field("location_id") String location_id,
                                                   @Field("size_id") String size_id,
                                                   @Field("length") String length,
                                                   @Field("weight") String weight,
                                                   @Field("pcs") String pcs);

}