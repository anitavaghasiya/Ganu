package com.ganak.common;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Common {
    public static final String PREF_NAME = "Ganak";
    public static final String FOLDER_NAME = "Ganak";
    public static final String SD_PATH = Environment.getExternalStorageDirectory() + File.separator + "/";
    private static final boolean IS_LOG = true;
    private final static Pattern emailPattern = Pattern.compile("([\\w\\-]([\\.\\w])+[\\w]+@([\\w\\-]+\\.)+[A-Za-z]{2,4})");
    private final static Pattern passwordpattern = Pattern.compile("^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\\\S+$).{0,8}$\"");
    private static Toast toast;
    private static ProgressDialog progressDialog;
    private static String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};

    public static void showToast(Context mContext, String msg) {
        if (toast != null) {
            toast.cancel();
        }
        toast = Toast.makeText(mContext, msg, Toast.LENGTH_SHORT);
        toast.show();
    }

    public static boolean checkPermissions(Context context) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    public static void requestPermission(Context context) {
        ActivityCompat.requestPermissions((Activity) context, permissions, 1);
    }

    public static ProgressDialog getProgressDialog(Context mContext) {
        ProgressDialog progressDialog = new ProgressDialog(mContext);
        progressDialog.setMessage("Loading...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
        return progressDialog;
    }

    public static boolean validateEmail(String email) {
        email = email.trim();
        // If email is empty
        if (TextUtils.isEmpty(email)) {
            return false;
        }
        Matcher mMatcher = emailPattern.matcher(email);
        return mMatcher.matches();
    }

    public static boolean validatePassword(String password) {
        password = password.trim();
        // If email is empty
        if (TextUtils.isEmpty(password)) {
            return false;
        }
        Matcher mMatcher = passwordpattern.matcher(password);
        return mMatcher.matches();
    }


    public static boolean isNotNullEditTextBox(EditText editText) {
        if (editText == null)
            return false;
        if (editText.getText().toString().trim().isEmpty())
            return false;
        if (editText.getText().length() < 0)
            return false;

        return true;
    }

    public static boolean isConnectingToInternet(Context mContext) {
        ConnectivityManager connectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        assert connectivityManager != null;
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected() && networkInfo.isAvailable();
    }

    public static void errorLog(String tag, String msg) {
        if (IS_LOG)
            Log.e(tag, msg);
    }

    public static void showProgressDialog(Context mContext) {
        dismissDialog();
        progressDialog = new ProgressDialog(mContext);
        progressDialog.setMessage("loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    public static void dismissDialog() {
        if (progressDialog != null && progressDialog.isShowing())
            progressDialog.dismiss();
    }

    public static boolean isValidName(String email) {
        String NAME_PATTERN = "^[A-Za-z]{3,10}$";
        Pattern pattern = Pattern.compile(NAME_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}

