package com.ganak.rest;

import com.ganak.model.ProductReportResponse;
import com.ganak.model.ProductResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface Product {
    @FormUrlEncoded
    @POST("/api/v2/get_products.php")
    Call<ProductResponse> getProductData(@Field("token") String token, @Field("id") String id);

    @FormUrlEncoded
    @POST("/api/v2/get_products.php")
    Call<ProductResponse> getFilterProductData(@Field("token") String token,
                                               @Field("id") String id,
                                               @Field("size") String size,
                                               @Field("location") String location,
                                               @Field("shape") String shape,
                                               @Field("grade") String grade);


    @FormUrlEncoded
    @POST("/api/v2/product_data.php")
    Call<ProductReportResponse> getProductReport(@Field("token") String token, @Field("id") String id);
}
