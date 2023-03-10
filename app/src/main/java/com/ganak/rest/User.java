package com.ganak.rest;

import com.ganak.model.AuthPasswordResponse;
import com.ganak.model.ForgotPasswordResponse;
import com.ganak.model.InsertMasterDataResponse;
import com.ganak.model.InwardDataResponse;
import com.ganak.model.LoginResponse;
import com.ganak.model.OtpResponse;
import com.ganak.model.RegistrationResponse;
import com.ganak.model.UpdateProfileResponse;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface User {

    @FormUrlEncoded
    @POST("/api/v2/auth-registration.php")
    Call<RegistrationResponse> getRegistrationResponse(@Field("fname") String firstName, @Field("lname") String lastName,
                                                       @Field("dob") String dob, @Field("email") String email,
                                                       @Field("phone") String phone, @Field("organization") String organization,
                                                       @Field("password") String password, @Field("gender") String gender);

    @FormUrlEncoded
    @POST("/api/v2/auth-otp.php")
    Call<OtpResponse> getOtpResponse(@Field("reg_id") String regId, @Field("token") String token, @Field("otp") String otp);

    @FormUrlEncoded
    @POST("/api/v2/auth-login.php")
    Call<LoginResponse> getLoginResponse(@Field("email") String email, @Field("password") String password);

    @Multipart
    @POST("/api/v2/inward_data.php")
    Call<InwardDataResponse> uploadImage(@Part MultipartBody.Part file, @Part("id") RequestBody id, @Part("token") RequestBody token);

    @Multipart
//    @FormUrlEncoded
    @POST("/api/v2/edit_profile.php")
    Call<UpdateProfileResponse> getUpdateProfile(@Part MultipartBody.Part profilepic,
                                                 @Part("fname") RequestBody firstName, @Part("lname") RequestBody lastName,
                                                 @Part("dob") RequestBody dob, @Part("email") RequestBody email,
                                                 @Part("phone") RequestBody phone, @Part("organization") RequestBody organization,
                                                 @Part("token") RequestBody token, @Part("id") RequestBody id);

    @FormUrlEncoded
    @POST("/api/v2/forgot-password.php")
    Call<ForgotPasswordResponse> getNewPassword(@Field("email") String email);

    @FormUrlEncoded
    @POST("/api/v2/auth-forgot-password.php")
    Call<AuthPasswordResponse> setNewPassword(@Field("email") String email,
                                              @Field("password") String password,
                                              @Field("token") String token,
                                              @Field("otp") String otp);


    @FormUrlEncoded
    @POST("/api/v2/auth-forgot-password.php")
    Call<InsertMasterDataResponse> getSelectedType(@Field("type") String type,
                                                   @Field("name") String name,
                                                   @Field("token") String token,
                                                   @Field("id") String id);



}



