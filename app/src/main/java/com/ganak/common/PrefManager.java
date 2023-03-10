package com.ganak.common;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class PrefManager {
    public static SharedPreferences pref;
    public SharedPreferences.Editor editor;

    public PrefManager(Context mContext) {
        if (pref == null)
            pref = mContext.getSharedPreferences(Common.PREF_NAME, Context.MODE_PRIVATE);
    }

    public String getUserName() {
        String var = "";
        if (pref != null) {
            var = pref.getString("name", "");
        }
        return var;
    }

    public String getUserEmail() {
        String var = "";
        if (pref != null) {
            var = pref.getString("email", "");
            Log.e("UserEmail", var);
        }
        return var;
    }

    public String getUserMobineNo() {
        String var = "";
        if (pref != null) {
            var = pref.getString("mobile", "");
            Log.e("mobile", var);
        }
        return var;
    }

    public String getRegId() {
        String var = "";
        if (pref != null) {
            var = pref.getString("regId", "");
        }
        return var;
    }

    public String getOrganization() {
        String var = "";
        if (pref != null) {
            var = pref.getString("organization", "");
        }
        return var;
    }

    public String getDOB() {
        String var = "";
        if (pref != null) {
            var = pref.getString("dob", "");
        }
        return var;
    }

    public String getProfilePic() {
        String var = "";
        if (pref != null) {
            var = pref.getString("image", "");
        }
        return var;
    }


    public String getProductData() {
        String var = "";
        if (pref != null) {
            var = pref.getString("pro_data", "");
            Log.e("pro_data", var);
        }
        return var;
    }

    public void setProductData(String grades, String shape, String location, String size, String length, String pieces, String weight, String custom_length, String custom_pieces, String custom_weight) {
        if (pref != null) {
            HashMap<String, String> data = new HashMap<String, String>();
            editor = pref.edit();
            data.put("grade", grades);
            data.put("shape", shape);
            data.put("location", location);
            data.put("size", size);
            data.put("length", length);
            data.put("pieces", pieces);
            data.put("weight", weight);
            data.put("custom_length", custom_length);
            data.put("custom_pieces", custom_pieces);
            data.put("custom_weight", custom_weight);
            JSONObject obj = new JSONObject(data);

            String old_data = pref.getString("pro_data", "");
            JSONArray jsonArray = new JSONArray();
            try {
                jsonArray = new JSONArray(old_data);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            jsonArray.put(obj);
            editor.putString("pro_data", jsonArray.toString());
            editor.apply();
        }
    }

    public void removeData() {
        if (pref != null) {
            HashMap<String, String> data = new HashMap<String, String>();
            editor = pref.edit();
            data.remove("grade");
            data.remove("shape");
            data.remove("location");
            data.remove("size");
            data.remove("length");
            data.remove("pieces");
            data.remove("weight");
            data.remove("custom_length");
            data.remove("custom_pieces");
            data.remove("custom_weight");
            editor.apply();
        }
    }

    public void setUserData(String name, String email, String mobineNo, String regId, String organization, String dob) {
        if (pref != null) {
            editor = pref.edit();
            if (!name.equals(""))
                editor.putString("name", name);
            if (!email.equals(""))
                editor.putString("email", email);
            if (!mobineNo.equals(""))
                editor.putString("mobile", mobineNo);
            if (!regId.equals(""))
                editor.putString("regId", regId);
            if (!organization.equals(""))
                editor.putString("organization", organization);
            if (!dob.equals(""))
                editor.putString("dob", dob);
            editor.apply();
        }
    }

    public String getPIN() {
        String var = "";
        if (pref != null) {
            var = pref.getString("PIN", "");
        }
        return var;
    }

    public void setPIN(String pin) {
        if (pref != null) {
            editor = pref.edit();
            if (!pin.equals(""))
                editor.putString("PIN", pin);
            editor.apply();
        }
    }

    public void clearPIN() {
        if (pref != null) {
            editor = pref.edit();
            editor.remove("PIN");
            editor.apply();
        }
    }


    public void addToken(String token) {
        if (!token.equals("")) {
            editor = pref.edit();
            editor.putString("tokenx", token);
            editor.apply();
        }
    }

    public String getToken() {
        return pref.getString("tokenx", "");
    }

    public void clearToken() {
        if (pref != null) {
            editor = pref.edit();
            editor.remove("tokenx");
            editor.apply();
        }
    }

    public void clearUserDetail() {
        if (pref != null) {
            editor = pref.edit();
            editor.remove("tokenx");
            editor.remove("name");
            editor.remove("email");
            editor.remove("mobile");
            editor.remove("regId");
            editor.remove("organization");
            editor.remove("dob");
            editor.apply();
        }
    }

    public void userLogout() {
        clearPIN();
        clearToken();
        clearUserDetail();
    }
}
