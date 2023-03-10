package com.ganak.rest;


import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class API {

    private static <T> T builder(Class<T> endpoint) {
        return new Retrofit.Builder()
                .baseUrl("https://ganak.webadmin.rocks/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(endpoint);
    }

    public static User user() {
        return builder(User.class);
    }

    public static Shape ShapeData() {
        return builder(Shape.class);
    }

    public static Location LocationData() {
        return builder(Location.class);
    }

    public static InwardData inwardData() {
        return builder(InwardData.class);
    }

    public static Grade GradeData() {
        return builder(Grade.class);
    }

    public static Size SizeData() {
        return builder(Size.class);
    }

    public static Product ProductData() {
        return builder(Product.class);
    }

    public static Master MasterData() {
        return builder(Master.class);
    }

}
