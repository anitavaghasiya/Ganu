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
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ganak.R;
import com.ganak.common.Common;
import com.ganak.common.PrefManager;
import com.ganak.model.ForgotPasswordResponse;
import com.ganak.model.LoginResponse;
import com.ganak.rest.API;

import retrofit2.Call;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private Context mContext;
    private Button btnLogin;
    private EditText etEmail, etPassword;
    private TextView tv_forgot_password, tv_new_user, tvHaveAcc;
    private PrefManager prefManager;
    private Dialog dialog;
    private String email;
    private EditText et_Email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_backup);

        mContext = this;

        prefManager = new PrefManager(mContext);

        initView();

        initListner();
    }

    private void initView() {
        btnLogin = findViewById(R.id.btnLogin);

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);

        tv_forgot_password = findViewById(R.id.tv_forgot_password);
        tv_new_user = findViewById(R.id.tv_new_user);
        tvHaveAcc = findViewById(R.id.tvHaveAcc);
    }

    private void initListner() {
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkValidation()) {
                    if (Common.isConnectingToInternet(mContext)) {
                        checkLogin();
                    } else {
                        Common.showToast(mContext, mContext.getResources().getString(R.string.no_internet_available));
                    }
                }

            }
        });

        tv_forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent passwordIntent = new Intent(mContext, ResetPasswordActivity.class);
                startActivity(passwordIntent);
                // getDialog();
               /* LayoutInflater li = LayoutInflater.from(mContext);
                View promptsView = li.inflate(R.layout.custom, null);

                  *//*Intent intent = new Intent(mContext, ForgotPasswordActivity.class);
                startActivity(intent);*/
            }
        });

        tv_new_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, RegistrationActivity.class);
                startActivity(intent);
            }
        });

        tvHaveAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, RegistrationActivity.class);
                startActivity(intent);
            }
        });
    }

    private boolean checkValidation() {
        if (!Common.isNotNullEditTextBox(etEmail)) {
            etEmail.requestFocus();
            Common.showToast(mContext, "Please Enter Email");
            return false;
        } else {
            if (!Common.validateEmail(etEmail.getText().toString().trim())) {
                etEmail.requestFocus();
                Common.showToast(mContext, "Please Enter Valid Email");
                return false;
            }
        }
        if (!Common.isNotNullEditTextBox(etPassword)) {
            etPassword.requestFocus();
            Common.showToast(mContext, "Please Enter Password");
            return false;
        } else {
            if (etPassword.getText().toString().trim().length() != 8 && etPassword.getText().toString().trim().length() < 8) {
                etPassword.requestFocus();
                Common.showToast(mContext, "You must have 8 characters in your password");
                return false;
            }
        }

        return true;
    }

    private void checkLogin() {


        Common.showProgressDialog(mContext);
        API.user().getLoginResponse(etEmail.getText().toString().trim(), etPassword.getText().toString().trim()).enqueue(new retrofit2.Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                Common.errorLog("LoginResponse", response.toString() + "---");
                LoginResponse loginResponse = response.body();
                Common.errorLog("PASS NOT MATCH", loginResponse.getError());
                if (loginResponse.getErrorCode() == 200) {
                    prefManager.addToken(loginResponse.getUser().getToken() + "");
                    prefManager.setUserData(loginResponse.getUser().getName(), loginResponse.getUser().getEmail(), loginResponse.getUser().getMobile(),
                            loginResponse.getUser().getId(), loginResponse.getUser().getOrganization(), loginResponse.getUser().getDob());
                    Intent intent = new Intent(mContext, MainActivity.class);
                    startActivity(intent);
                    finishAffinity();
                } else {
                    Common.errorLog("PASS NOT MATCH", loginResponse.getError().toString());
                    if (loginResponse.getError() != null && !loginResponse.getError().equals("")) {
                        Common.showToast(mContext, loginResponse.getError() + "");

                    }
                }
                Common.dismissDialog();
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                // Common.showToast(mContext, "please try again later!!");
                Common.showToast(mContext, "Password does not match");
                Common.dismissDialog();
            }
        });
    }

    public void getDialog() {
        dialog = new Dialog(mContext, R.style.full_screen_dialog);
        dialog.setContentView(R.layout.dialog_layout_email);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        lp.windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setAttributes(lp);

        et_Email = dialog.findViewById(R.id.et_email);
        final Button btn_submit = dialog.findViewById(R.id.btn_submit);

        //  email = etEmail.getText().toString().trim();

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
        dialog.show();
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

                    dialog.dismiss();

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
