package com.ganak.activity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.ganak.R;
import com.ganak.common.Common;
import com.ganak.common.PrefManager;
import com.ganak.common.RealFilePathUtil;
import com.ganak.model.UpdateProfileResponse;
import com.ganak.rest.API;

import java.io.File;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Response;

public class EditProfileActivity extends AppCompatActivity {
    private static int RESULT_LOAD_IMAGE = 101;
    String profileName, profilePath;
    private Context mContext;
    private android.support.v7.app.ActionBar actionBar;
    private CircleImageView iv_photo;
    private ImageView iv_upload_photo;
    private EditText etEmail, etFName, etLName, etMobile, etOrgName, etDob;
    private Button btn_update;
    private int mYear, mMonth, mDay;
    private String currentDate = "", month, day, lastName, firstName;
    private TextView tv_title;
    private PrefManager prefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        prefManager = new PrefManager(mContext);
        mContext = this;
        //  tv_title = (TextView) findViewById(R.id.toolbar_title);

        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Edit Profile");

        initView();

        String fullName = prefManager.getUserName();
        String phone = prefManager.getUserMobineNo();

        /* String[] str_array = fullName.split(" ");
        String fname = str_array[0];
        String lname = str_array[1];
*/
        lastName = "";
        firstName = "";
        if (fullName.split("\\w+").length > 1) {
            lastName = fullName.substring(fullName.lastIndexOf(" ") + 1);
            firstName = fullName.substring(0, fullName.lastIndexOf(' '));
        }

        etFName.setText(firstName);
        etLName.setText(lastName);
        etEmail.setText(prefManager.getUserEmail());
        etDob.setText(prefManager.getDOB());
        etMobile.setText(prefManager.getUserMobineNo());
        etOrgName.setText(prefManager.getOrganization());
       /* SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        String mImageUri = preferences.getString("image", null);
        if (mImageUri != null) {
            iv_photo.setImageURI(Uri.parse(mImageUri));
        }else {
           // iv_photo.setImageDrawable(R.drawable.ganak_splash_logo);
        }*/
        onClickEvent();
    }

    private void initView() {
        iv_photo = findViewById(R.id.iv_photo);
        iv_upload_photo = findViewById(R.id.iv_upload_photo);
        etEmail = findViewById(R.id.etEmail);
        etFName = findViewById(R.id.etFName);
        etLName = findViewById(R.id.etLName);
        etMobile = findViewById(R.id.etMobile);
        etOrgName = findViewById(R.id.etOrgName);
        etDob = findViewById(R.id.etDob);
        btn_update = findViewById(R.id.btn_update);
    }

    private void onClickEvent() {
        iv_upload_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Common.checkPermissions(mContext)) {
                    Intent getImageIntent = new Intent(Intent.ACTION_GET_CONTENT);
                    getImageIntent.setType("image/*");
                    startActivityForResult(getImageIntent, RESULT_LOAD_IMAGE);
                } else {
                    Common.requestPermission(mContext);
                }
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
            @Override
            public void onClick(View view) {
                etDob.setRawInputType(InputType.TYPE_NULL);
                etDob.setFocusable(true);
                selDate();
            }
        });

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

        etMobile.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (etMobile != null && !etMobile.equals("")) {
                    if (etMobile.getText().toString().trim().length() == 10) {
                        etMobile.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_true_green, 0);
                    } else {
                        etMobile.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_true_blue, 0);
                    }
                }
            }
        });

        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkValidations()) {
                    if (Common.isConnectingToInternet(mContext)) {
                        ProfileDataUpdate();
                    } else {
                        Common.showToast(mContext, mContext.getResources().getString(R.string.no_internet_available));
                    }
                }
            }
        });
    }

    private void selDate() {
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        if (monthOfYear + 1 < 10) {
                            month = "0" + (monthOfYear + 1);
                            Log.e("MONTH", "onDateSet: " + month);
                        } else {
                            month = String.valueOf(monthOfYear + 1);
                        }
                        if (dayOfMonth < 10) {
                            day = "0" + dayOfMonth;
                            Log.e("MONTH", "onDateSet: " + day);
                        } else {
                            day = String.valueOf(dayOfMonth);
                        }
                        currentDate = year + "-" + (month) + "-" + day;
                        etDob.setText(currentDate);
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        datePickerDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri fullPhotoUri = data.getData ();
          /*  Glide
                    .with(mContext)
                    .load(fullPhotoUri)
                    .into(iv_photo);*/
            iv_photo.setImageURI (fullPhotoUri);
            RealFilePathUtil realFilePathUtil = new RealFilePathUtil ();
            profilePath = realFilePathUtil.getPath (mContext, fullPhotoUri);
            profileName = new File (profilePath).getName ();
            Log.e ("path", "path: " + profilePath);
            Log.e ("name", "name: " + profileName);

           /* Uri fullPhotoUri = data.getData();
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mContext);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("image", String.valueOf(fullPhotoUri));
            editor.apply();
            iv_photo.setImageURI(fullPhotoUri);
            RealFilePathUtil realFilePathUtil = new RealFilePathUtil();
            profilePath = realFilePathUtil.getPath(mContext, fullPhotoUri);
            profileName = new File(profilePath).getName();
*/
        }
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

    private boolean checkValidations() {
        if (!Common.isNotNullEditTextBox(etFName)) {
            etFName.requestFocus();
            Common.showToast(mContext, "Please Enter First Name");
            return false;
        } else {
            firstName = etFName.getText().toString();
            if (!isValidName(firstName)) {
                etFName.requestFocus();
                Log.e("firstName", "checkValidations: " + firstName);
                Common.showToast(mContext, "First Name not allow space");
                return false;
            }
        }

        if (!Common.isNotNullEditTextBox(etLName)) {
            etLName.requestFocus();
            Common.showToast(mContext, "Please Enter Last Name");
            return false;
        } else {
            lastName = etLName.getText().toString();
            if (!isValidName(lastName)) {
                etLName.requestFocus();
                Common.showToast(mContext, "Last Name not allow space");
                return false;
            }

        }

        if (!Common.isNotNullEditTextBox(etDob)) {
            etDob.requestFocus();
            Common.showToast(mContext, "Please Select DOB");
            return false;
        }

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

        if (!Common.isNotNullEditTextBox(etMobile)) {
            etMobile.requestFocus();
            Common.showToast(mContext, "Please Enter Mobile Number");
            return false;
        } else {
            if (etMobile.getText().toString().trim().length() != 10) {
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
        return true;
    }

    private void ProfileDataUpdate() {
        Common.showProgressDialog(mContext);

        File profilepic;
        MultipartBody.Part body = null;
        if (profilePath != null) {
            profilepic = new File(profilePath);
            RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), profilepic);
            body = MultipartBody.Part.createFormData("profilepic", profilepic.getName(), requestBody);
        }

        RequestBody fname = RequestBody.create(MediaType.parse("multipart/form-data"), etFName.getText().toString().trim());
        RequestBody lname = RequestBody.create(MediaType.parse("multipart/form-data"), etLName.getText().toString().trim());
        RequestBody dob = RequestBody.create(MediaType.parse("multipart/form-data"), etDob.getText().toString().trim());
        RequestBody email = RequestBody.create(MediaType.parse("multipart/form-data"), etEmail.getText().toString().trim());
        RequestBody mobile = RequestBody.create(MediaType.parse("multipart/form-data"), etMobile.getText().toString().trim());
        RequestBody org = RequestBody.create(MediaType.parse("multipart/form-data"), etOrgName.getText().toString().trim());
        RequestBody token = RequestBody.create(MediaType.parse("multipart/form-data"), prefManager.getToken());
        RequestBody regId = RequestBody.create(MediaType.parse("multipart/form-data"), prefManager.getRegId());

        API.user().getUpdateProfile(body, fname, lname, dob, email, mobile, org, token, regId).enqueue(new retrofit2.Callback<UpdateProfileResponse>() {
            @Override
            public void onResponse(Call<UpdateProfileResponse> call, Response<UpdateProfileResponse> response) {
                Common.errorLog("RegistrationResponse", response.toString() + "--");
                UpdateProfileResponse updateProfileResponse = response.body();

                if (updateProfileResponse != null) {
                    prefManager.setUserData(etFName.getText().toString().trim() + " " + etLName.getText().toString().trim(),
                            etEmail.getText().toString().trim(),
                            etMobile.getText().toString().trim(),
                            prefManager.getRegId(),
                            etOrgName.getText().toString().trim(),
                            etDob.getText().toString().trim());
                    Common.showToast(mContext, updateProfileResponse.getErrorCode() + "");
                } else {
                    Common.showToast(mContext, updateProfileResponse.getError() + "");
                }
                Common.dismissDialog();
            }

            @Override
            public void onFailure(Call<UpdateProfileResponse> call, Throwable t) {
                Common.dismissDialog();
                Common.showToast(mContext, "Please try again later!!");
            }
        });

    }

    private boolean isValidName(String email) {
        String NAME_PATTERN = "^[A-Za-z0-9]{0,20}$";
        Pattern pattern = Pattern.compile(NAME_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
