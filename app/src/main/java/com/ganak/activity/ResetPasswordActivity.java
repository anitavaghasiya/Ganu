package com.ganak.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ganak.R;
import com.ganak.common.Common;
import com.ganak.common.PrefManager;
import com.ganak.model.ForgotPasswordResponse;
import com.ganak.rest.API;

import retrofit2.Call;
import retrofit2.Response;

public class ResetPasswordActivity extends AppCompatActivity {
    private Button btn_signIn;
    private Context mContext;
    private PrefManager prefManager;
    private EditText et_Email;
    private TextView tv_logIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        mContext = this;

        prefManager = new PrefManager(mContext);

        btn_signIn = findViewById(R.id.btn_signIn);
        et_Email = findViewById(R.id.etEmail);
        tv_logIn = findViewById(R.id.tv_logIn);

        btn_signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Common.isConnectingToInternet(mContext)) {
                    if (!Common.isNotNullEditTextBox(et_Email)) {
                        et_Email.requestFocus();
                        Common.showToast(mContext, "Please Enter Email");
                    } else {
                        if (!Common.validateEmail(et_Email.getText().toString().trim())) {
                            et_Email.requestFocus();
                            Common.showToast(mContext, "Please Enter Valid Email");
                        } else {
                            sendOtp();
                        }
                    }
                } else {
                    Common.showToast(mContext, mContext.getResources().getString(R.string.no_internet_available));

                }

            }
        });

        tv_logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent logInIntent = new Intent(mContext, LoginActivity.class);
                startActivity(logInIntent);
                finishAffinity();
            }
        });
    }

    private void sendOtp() {
        Common.showProgressDialog(mContext);
        API.user().getNewPassword(et_Email.getText().toString().trim()).enqueue(new retrofit2.Callback<ForgotPasswordResponse>() {
            @Override
            public void onResponse(Call<ForgotPasswordResponse> call, Response<ForgotPasswordResponse> response) {
                Common.errorLog("RegistrationResponse", response.toString() + "--");
                ForgotPasswordResponse forgotPasswordResponse = response.body();
                if (forgotPasswordResponse.getErrorCode() == 200) {
                    String token = forgotPasswordResponse.getToken();
                    Log.e("token", token);
                    String email = et_Email.getText().toString().trim();
                    Log.e("email", email);

                    Intent intent = new Intent(mContext, ForgotPasswordActivity.class);
                    intent.putExtra("token", token);
                    intent.putExtra("email", email);
                    startActivity(intent);
                    Common.dismissDialog();
                } else {
                    if (forgotPasswordResponse.getError() != null && !forgotPasswordResponse.getError().equals("")) {
                        Common.showToast(mContext, forgotPasswordResponse.getError() + "");
                        Common.dismissDialog();
                    }
                }
            }

            @Override
            public void onFailure(Call<ForgotPasswordResponse> call, Throwable t) {
                Common.dismissDialog();
                Common.showToast(mContext, "Please try again later!!");
            }
        });

    }
}
