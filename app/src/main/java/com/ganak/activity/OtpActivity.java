package com.ganak.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.ganak.R;
import com.ganak.common.Common;
import com.ganak.common.PrefManager;
import com.ganak.model.ForgotPasswordResponse;
import com.ganak.rest.API;

import retrofit2.Call;
import retrofit2.Response;

public class OtpActivity extends AppCompatActivity {

    private Context mContext;
    private Button send_otp;
    private android.support.v7.app.ActionBar actionBar;
    private EditText etEmail;
    private PrefManager prefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);

        prefManager = new PrefManager(mContext);

        mContext = this;

        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Forgot Password");

        // send_otp = findViewById(R.id.btn_update_and_login);
        etEmail = findViewById(R.id.etEmail);

        etEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
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
        });

        send_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkValidation()) {
                    if (Common.isConnectingToInternet(mContext)) {
                        SendOtp();
                    }
                }
            }
        });

    }

    private void SendOtp() {
        Common.showProgressDialog(mContext);
        API.user().getNewPassword(etEmail.getText().toString().trim()).enqueue(new retrofit2.Callback<ForgotPasswordResponse>() {
            @Override
            public void onResponse(Call<ForgotPasswordResponse> call, Response<ForgotPasswordResponse> response) {
                Common.errorLog("RegistrationResponse", response.toString() + "--");
                ForgotPasswordResponse forgotPasswordResponse = response.body();
                if (response.body().getErrorCode() == 200) {
                    if (etEmail.getText().toString().trim().equals(prefManager.getUserEmail())) {
                        Intent intent = new Intent(mContext, ForgotPasswordActivity.class);
                        startActivity(intent);
                        finish();
                        finishAffinity();
                    } else {
                        Common.showToast(mContext, "Please Enter Correct Email Address");
                    }
                } else {
                    if (forgotPasswordResponse.getError() != null && !forgotPasswordResponse.getError().equals("")) {
                        Common.showToast(mContext, forgotPasswordResponse.getError() + "");
                    }
                }
                Common.dismissDialog();
            }

            @Override
            public void onFailure(Call<ForgotPasswordResponse> call, Throwable t) {
                Common.dismissDialog();
                Common.showToast(mContext, "Please try again later!!");
            }
        });

    }

    private boolean checkValidation() {
        if (!Common.isNotNullEditTextBox(etEmail)) {
            etEmail.requestFocus();
            Common.showToast(mContext, "Please Enter Email");
            return false;
        }
        return true;
    }
}
