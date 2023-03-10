package com.ganak.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.ganak.R;
import com.ganak.common.Common;
import com.ganak.common.PrefManager;
import com.ganak.model.AuthPasswordResponse;
import com.ganak.rest.API;
import com.mukesh.OtpView;

import retrofit2.Call;
import retrofit2.Response;

public class ForgotPasswordActivity extends AppCompatActivity {
    private Context mContext;
    private Button btn_update_and_login, btn_confirmOtp;
    private android.support.v7.app.ActionBar actionBar;
    private EditText et_new_password, et_confirm_new_password;
    private PrefManager prefManager;
    private String token, email;
    private Dialog dialog;
    private OtpView otpView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        prefManager = new PrefManager(mContext);

        mContext = this;

        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Confirm OTP");

        btn_confirmOtp = findViewById(R.id.btn_confirmOtp);
        otpView = findViewById(R.id.et_otp);


        /*etEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (etEmail != null && !etEmail.equals("")) {
                    if (Common.validateEmail(etEmail.getText().toString().trim())) {
                        etEmail.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_true_green, 0);
                    } else {
                        etEmail.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_true_blue, 0);
                    }
                }
            }
        });*/


        btn_confirmOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Common.isConnectingToInternet(mContext)) {
                    if (!Common.isNotNullEditTextBox(otpView)) {
                        otpView.requestFocus();
                        Common.showToast(mContext, "Please Enter OTP");
                    } else {
                        showDialog();
                    }
                } else {
                    Common.showToast(mContext, mContext.getResources().getString(R.string.no_internet_available));
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void showDialog() {
        dialog = new Dialog(mContext, R.style.full_screen_dialog);
        dialog.setContentView(R.layout.dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.BOTTOM;
        lp.windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setAttributes(lp);

        final Button btn_update = dialog.findViewById(R.id.btn_update);
        et_new_password = dialog.findViewById(R.id.et_new_password);
        et_confirm_new_password = dialog.findViewById(R.id.et_confirm_new_password);

        Intent intent = getIntent();
        token = intent.getStringExtra("token");
        email = intent.getStringExtra("email");
        Log.e("emailone", email);
        Log.e("token", "onCreate: " + token);

        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkValidations()) {
                    if (Common.isConnectingToInternet(mContext)) {
                        getPassword();
                        Common.showToast(mContext, "Password has been changed");
                    } else {
                        Common.showToast(mContext, mContext.getResources().getString(R.string.no_internet_available));
                    }
                }

            }
        });
        dialog.show();
    }

    private void getPassword() {
        Common.showProgressDialog(mContext);
        String otp = otpView.getEditableText().toString();
        Log.e("otp", "onCreate: " + otp);
        API.user().setNewPassword(email, et_new_password.getText().toString().trim(), token, otpView.getText().toString().trim()).enqueue(new retrofit2.Callback<AuthPasswordResponse>() {
            @Override
            public void onResponse(Call<AuthPasswordResponse> call, Response<AuthPasswordResponse> response) {
                Common.errorLog("RegistrationResponse", response.toString() + "--");
                AuthPasswordResponse authPasswordResponse = response.body();
                if (authPasswordResponse != null) {
                    Intent intent = new Intent(mContext, LoginActivity.class);
                    startActivity(intent);
                    finish();
                    finishAffinity();
                    Common.dismissDialog();
                } else {
                    Common.showToast(mContext, authPasswordResponse.getError() + "");
                }
            }

            @Override
            public void onFailure(Call<AuthPasswordResponse> call, Throwable t) {
                Common.dismissDialog();
                Common.showToast(mContext, "Please try again later!!");
            }
        });

    }

    private boolean checkValidations() {
        if (!Common.isNotNullEditTextBox(et_new_password)) {
            et_new_password.requestFocus();
            Common.showToast(mContext, "Please Enter Password");
            return false;
        } else {
            if (et_new_password.getText().toString().trim().length() != 8 && et_new_password.getText().toString().trim().length() < 8) {
                et_new_password.requestFocus();
                Common.showToast(mContext, "You must have 8 characters in your password");
                return false;
            }
        }
        if (!Common.isNotNullEditTextBox(et_confirm_new_password)) {
            et_confirm_new_password.requestFocus();
            Common.showToast(mContext, "Please Enter Confirm Password");
            return false;
        } else {
            if (!et_new_password.getText().toString().trim().equals(et_confirm_new_password.getText().toString().trim())) {
                et_confirm_new_password.requestFocus();
                Common.showToast(mContext, "password and confirm password must be same!!!");
                return false;
            }
        }
        return true;
    }

}
