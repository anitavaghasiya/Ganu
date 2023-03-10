package com.ganak.common;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.ganak.model.FavoriteProduct;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PrefFavoriteProduct {

    public static final String PREFS_NAME = "PRODUCT";
    public static final String FAVORITES_INWARD = "FAV_PRODUCT";
    public static final String FAVORITES_OUTWARD = "FAV_OUTWARD_PRODUCT";


    public PrefFavoriteProduct() {
        super ();
    }

    // THIS FOUR METHODS ARE USED FOR MAINTAINING FAVORITES.
    public static void saveFavorite(Context context, List<FavoriteProduct> favItems) {

        SharedPreferences settings = context.getSharedPreferences (PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit ();

        Gson gson = new Gson ();
        String jsonFavorites = gson.toJson (favItems);

        editor.putString (FAVORITES_INWARD, jsonFavorites);
        editor.apply ();

    }

    public static void saveOutwardFavorite(Context context, List<FavoriteProduct> favItems) {

        SharedPreferences settings = context.getSharedPreferences (PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit ();

        Gson gson = new Gson ();
        String jsonFavorites = gson.toJson (favItems);

        editor.putString (FAVORITES_OUTWARD, jsonFavorites);
        editor.apply ();

    }


    public static boolean checkFavourite(Context context, FavoriteProduct favItem) {
        List<FavoriteProduct> favorites = getFavorites (context);
        if (favorites != null) {
            for (int i = 0; i < favorites.size (); i++) {
                if (favorites.get (i).getShape_id ().equals (favItem.getShape_id ())) {
                    Log.e ("ID", "checkFavourite: " + favorites.get (i).getShape_id () + "--" + favItem.getShape_id ());
                    if (favorites.get (i).getSize_id ().equals (favItem.getSize_id ()))
                        if (favorites.get (i).getGrade_id ().equals (favItem.getGrade_id ()))
                            if (favorites.get (i).getLocation_id ().equals (favItem.getLocation_id ()))

                                if (favorites.get (i).getName_shape ().equals (favItem.getName_shape ()))

                                    if (favorites.get (i).getName_grade ().equals (favItem.getName_grade ()))
                                        if (favorites.get (i).getName_size ().equals (favItem.getName_size ()))
                                            if (favorites.get (i).getName_location ().equals (favItem.getName_location ())) {
                                                return true;
                                            }
                }
            }
        }
        return false;
    }

    public static boolean checkOutwardFavourite(Context context, FavoriteProduct favItem) {
        List<FavoriteProduct> favorites = getOutwardFavorites (context);
        if (favorites != null) {
            for (int i = 0; i < favorites.size (); i++) {
                if (favorites.get (i).getShape_id ().equals (favItem.getShape_id ())) {
                    Log.e ("ID", "checkFavourite: " + favorites.get (i).getShape_id () + "--" + favItem.getShape_id ());
                    if (favorites.get (i).getSize_id ().equals (favItem.getSize_id ()))
                        if (favorites.get (i).getGrade_id ().equals (favItem.getGrade_id ()))
                            if (favorites.get (i).getLocation_id ().equals (favItem.getLocation_id ()))

                                if (favorites.get (i).getName_shape ().equals (favItem.getName_shape ()))

                                    if (favorites.get (i).getName_grade ().equals (favItem.getName_grade ()))
                                        if (favorites.get (i).getName_size ().equals (favItem.getName_size ()))
                                            if (favorites.get (i).getName_location ().equals (favItem.getName_location ())) {
                                                return true;
                                            }
                }
            }
        }
        return false;
    }


    public static void addFavorite(Context context, FavoriteProduct favItem) {
        List<FavoriteProduct> favorites = getFavorites (context);
        if (favorites == null) {
            favorites = new ArrayList<> ();
            favorites.add (favItem);
            Log.e ("Fav", "addFavorite: " + favorites.size ());
            saveFavorite (context, favorites);
        } else {
            favorites.add (favItem);
            saveFavorite (context, favorites);
        }
    }

    public static void addOutwardFavorite(Context context, FavoriteProduct favItem) {
        List<FavoriteProduct> favorites = getOutwardFavorites (context);
        if (favorites == null) {
            favorites = new ArrayList<> ();
            favorites.add (favItem);
            Log.e ("Fav", "addFavorite: " + favorites.size ());
            saveOutwardFavorite (context, favorites);
        } else {
            favorites.add (favItem);
            saveOutwardFavorite (context, favorites);
        }
    }

    public static void removeFavorite(Context context, int favItem) {
        ArrayList<FavoriteProduct> favorites = getFavorites (context);
        favorites.remove (favItem);
        saveFavorite (context, favorites);
    }

    public static void removeOutwardFavorite(Context context, int favItem) {
        ArrayList<FavoriteProduct> favorites = getOutwardFavorites (context);
        favorites.remove (favItem);
        Log.e ("Fav", "removeFavorite: " + favorites.size ());
        saveOutwardFavorite (context, favorites);
    }

    public static ArrayList<FavoriteProduct> getFavorites(Context context) {
        SharedPreferences settings;
        List<FavoriteProduct> favorites;

        settings = context.getSharedPreferences (PREFS_NAME, Context.MODE_PRIVATE);

        if (settings.contains (FAVORITES_INWARD)) {
            String jsonFavorites = settings.getString (FAVORITES_INWARD, null);
            Gson gson = new Gson ();
            FavoriteProduct[] favoriteItems = gson.fromJson (jsonFavorites, FavoriteProduct[].class);
            favorites = Arrays.asList (favoriteItems);
            favorites = new ArrayList<> (favorites);
        } else {
            return null;
        }
        return (ArrayList<FavoriteProduct>) favorites;
    }

    public static ArrayList<FavoriteProduct> getOutwardFavorites(Context context) {
        SharedPreferences settings;
        List<FavoriteProduct> favorites;

        settings = context.getSharedPreferences (PREFS_NAME, Context.MODE_PRIVATE);

        if (settings.contains (FAVORITES_OUTWARD)) {
            String jsonFavorites = settings.getString (FAVORITES_OUTWARD, null);
            Gson gson = new Gson ();
            FavoriteProduct[] favoriteItems = gson.fromJson (jsonFavorites, FavoriteProduct[].class);
            favorites = Arrays.asList (favoriteItems);
            favorites = new ArrayList<> (favorites);
        } else {
            return null;
        }
        return (ArrayList<FavoriteProduct>) favorites;
    }

}
