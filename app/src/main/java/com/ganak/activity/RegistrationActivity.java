package com.ganak.activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.ganak.R;
import com.ganak.common.Common;
import com.ganak.common.PrefManager;
import com.ganak.model.OtpResponse;
import com.ganak.model.RegistrationResponse;
import com.ganak.rest.API;

import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Response;

public class RegistrationActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener, View.OnClickListener {
    boolean doubleBackToExitPressedOnce = false;
    private Context mContext;
    private Button btn_signup;
    private EditText etDob, etEmail, etMobile, etFName, etLName, etOrgName, et_new_password, et_confirm_new_password;
    private TextView tv_login;
    private int mYear, mMonth, mDay;
    private String currentDate = "", gender = "Male", mobile, firstName = "", lastName = "";
    private android.support.v7.app.ActionBar actionBar;
    private RadioGroup sg_gender;
    private PrefManager prefManager;
    private Dialog dialog;
    private AwesomeValidation awesomeValidation;
    private DatePickerDialog datePickerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        mContext = this;

        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);

        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Register New");

        prefManager = new PrefManager(mContext);

        initView();

        initListner();

    }

    private void initView() {
        btn_signup = findViewById(R.id.btn_signUp);
        tv_login = findViewById(R.id.tv_login);
        etDob = findViewById(R.id.etDob);
        etEmail = findViewById(R.id.etEmail);
        etMobile = findViewById(R.id.etMobile);
        etFName = findViewById(R.id.etFName);
        etLName = findViewById(R.id.etLName);
        etOrgName = findViewById(R.id.etOrgName);
        et_new_password = findViewById(R.id.et_new_password);
        et_confirm_new_password = findViewById(R.id.et_confirm_new_password);
        sg_gender = (RadioGroup) findViewById(R.id.sg_gender);
    }

    private void initListner() {

       /* etFName.setOnKeyListener(new View.OnKeyListener() {

            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER))
                {
                    // Perform action on Enter key press
                    etFName.clearFocus();
                    etLName.requestFocus();
                    return true;
                }
                return false;
            }
        });*/
        sg_gender.setOnClickListener(this);

        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firstName = etFName.getText().toString().trim();
                lastName = etLName.getText().toString().trim();
                if (checkValidations()) {
                    if (Common.isConnectingToInternet(mContext)) {
                        addRegistration();
                    } else {
                        Common.showToast(mContext, mContext.getResources().getString(R.string.no_internet_available));
                    }
                }
                //retriveOTPDialog();
            }
        });

        etDob.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                etDob.setRawInputType(InputType.TYPE_NULL);
                etDob.setFocusable(true);
                if (b)
                    selDate();
            }
        });
        etDob.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
            @Override
            public void onClick(View view) {
                etDob.setRawInputType(InputType.TYPE_NULL);
                etDob.setFocusable(true);
                selDate();
            }
        });

        tv_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* Intent intent = new Intent(mContext, LoginActivity.class);
                startActivity(intent);*/
                finish();
            }
        });

        etEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
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

        etMobile.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                mobile = etMobile.getText().toString();
                if (etMobile != null && !etMobile.equals("")) {
                    if (isValidMobile(mobile)) {
                        etMobile.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_true_green, 0);
                    } else {
                        etMobile.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_true_blue, 0);

                    }
                }
                   /* if (etMobile.getText().toString().trim().length() == 10 || !isValidMobile(mobile)) {
                        etMobile.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_true_green, 0);
                    } else {
                        etMobile.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_true_blue, 0);
                    }*/

            }
        });

    }

    private boolean checkValidations() {
        if (!Common.isNotNullEditTextBox(etFName)) {
            etFName.requestFocus();
            Common.showToast(mContext, "Please Enter First Name");
            return false;
        } else if (!Common.isValidName(firstName)) {
            Common.showToast(mContext, "First Name not allow space");
            return false;
        }

        if (!Common.isNotNullEditTextBox(etLName)) {
            etLName.requestFocus();
            Common.showToast(mContext, "Please Enter Last Name");
            return false;
        } else if (!Common.isValidName(lastName)) {
            Common.showToast(mContext, "Last Name not allow space");
            return false;
        }


        if (!Common.isNotNullEditTextBox(etDob)) {
            etDob.requestFocus();
            Common.showToast(mContext, "Please Select DOB");
            return false;
        }

        if (!Common.isNotNullEditTextBox(etMobile)) {
            etMobile.requestFocus();
            Common.showToast(mContext, "Please Enter Mobile Number");
            return false;
        } else {
            if (etMobile.getText().toString().trim().length() != 10 || !isValidMobile(mobile)) {
                etMobile.requestFocus();
                Common.showToast(mContext, "Please Enter Valid Mobile Number");
                return false;
            }
        }

        if (!Common.isNotNullEditTextBox(etOrgName)) {
            etOrgName.requestFocus();
            Common.showToast(mContext, "Please Enter Organization Name");
            return false;
        }

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

    private void selDate() {
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        currentDate = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                        etDob.setText(currentDate);
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        datePickerDialog.show();
    }

    public void retriveOTPDialog() {
        dialog = new Dialog(mContext, R.style.full_screen_dialog);
        dialog.setContentView(R.layout.dialog_layout_otp);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(false);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.BOTTOM;
        lp.windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setAttributes(lp);

        final EditText et_otp = dialog.findViewById(R.id.et_otp);
        final Button btn_cancel = dialog.findViewById(R.id.btn_cancel);
        final Button btn_next = dialog.findViewById(R.id.btn_next);
        final Button btn_confirm = dialog.findViewById(R.id.btn_confirm);

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!et_otp.getText().toString().trim().equals("")) {
                    checkOTP(et_otp.getText().toString().trim());
                } else {
                    Common.showToast(mContext, "Please enter OTP");
                }
            }
        });

        dialog.show();
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

    @Override
    public void onClick(View v) {
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.btn_male:
                gender = "Male";
                Common.showToast(mContext, "Male");
                break;
            case R.id.btn_female:
                gender = "Female";
                Common.showToast(mContext, "Female");
                break;
            default:
                // Nothing to do
        }
    }

    private void addRegistration() {
        Common.showProgressDialog(mContext);
        API.user().getRegistrationResponse(etFName.getText().toString().trim(), etLName.getText().toString().trim(),
                etDob.getText().toString().trim(), etEmail.getText().toString().trim(), etMobile.getText().toString().trim(),
                etOrgName.getText().toString().trim(), et_new_password.getText().toString().trim(), gender).enqueue(new retrofit2.Callback<RegistrationResponse>() {
            @Override
            public void onResponse(Call<RegistrationResponse> call, Response<RegistrationResponse> response) {
                Common.errorLog("RegistrationResponse", response.toString() + "--");
                RegistrationResponse registrationResponse = response.body();
                if (registrationResponse.getRegToken() != null && !registrationResponse.getRegToken().isEmpty()) {
                    prefManager.addToken(registrationResponse.getRegToken());
                    prefManager.setUserData(etFName.getText().toString().trim(), etEmail.getText().toString().trim(),
                            etMobile.getText().toString().trim(), registrationResponse.getRegId().toString(),
                            etOrgName.getText().toString().trim(), etDob.getText().toString().trim());
                    retriveOTPDialog();
                } else {
                    Common.showToast(mContext, registrationResponse.getErrorCode() + "");
                }
                Common.dismissDialog();
            }

            @Override
            public void onFailure(Call<RegistrationResponse> call, Throwable t) {
                Common.dismissDialog();
                Common.showToast(mContext, "Please try again later!!");
            }
        });

    }

    private void checkOTP(final String otp) {
        Common.showProgressDialog(mContext);
        API.user().getOtpResponse(prefManager.getRegId() + "", prefManager.getToken() + "", otp).enqueue(new retrofit2.Callback<OtpResponse>() {
            @Override
            public void onResponse(Call<OtpResponse> call, Response<OtpResponse> response) {
                Common.dismissDialog();
                OtpResponse otpResponse = response.body();
                Common.showToast(mContext, otpResponse.getError() + "");
                if (otpResponse.getErrorCode() == 200) {
                    dialog.dismiss();
                    prefManager.addToken(otpResponse.getUser().getToken() + "");
                    prefManager.setUserData(otpResponse.getUser().getName(), otpResponse.getUser().getEmail(), otpResponse.getUser().getMobile(),
                            otpResponse.getUser().getId(), otpResponse.getUser().getOrganization(), otpResponse.getUser().getDob());
                    Intent intent = new Intent(mContext, InwardDataActivity.class);
                    intent.putExtra("skip", true);
                    startActivity(intent);
                    finishAffinity();

                }

            }

            @Override
            public void onFailure(Call<OtpResponse> call, Throwable t) {
                Common.dismissDialog();
                Common.showToast(mContext, "please try again later!!");
            }
        });
    }

    private boolean isValidMobile(String mobile) {
        Pattern p = Pattern.compile("(0/91)?[7-9][0-9]{9}");
        Matcher m = p.matcher(mobile);
        return (m.find() && m.group().equals(mobile));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        Common.showToast(mContext, "Please click BACK again to exit!!");
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }
}
