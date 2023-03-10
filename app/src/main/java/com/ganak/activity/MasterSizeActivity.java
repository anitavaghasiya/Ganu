package com.ganak.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.ganak.R;
import com.ganak.common.Common;
import com.ganak.common.PrefManager;
import com.ganak.model.MasterDataResponse;
import com.ganak.model.Size;
import com.ganak.model.SizeResponse;
import com.ganak.rest.API;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class MasterSizeActivity extends AppCompatActivity {

    private Context mContext;
    private android.support.v7.app.ActionBar actionBar;
    // private Spinner sp_type;
    private EditText et_name;
    private PrefManager prefManager;
    private Button btn_submit;
    private String name, size_name, grade_id, grade_name, shape_id, shape_name, location_id, location_name, type = "size", res_grade_id, res_shape_id;

    private Spinner size_spinner;
    private List<Size> sizeList;

    private TextView txt_grade_name, txt_shape_name, txt_location_name, tv_no_data_found;
    private boolean newFlag = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_master_size);

        mContext = this;
        prefManager = new PrefManager(mContext);

        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        tv_no_data_found = findViewById(R.id.tv_no_data_found);
        txt_grade_name = findViewById(R.id.txt_grade_name);
        txt_shape_name = findViewById(R.id.txt_shape_name);
        txt_location_name = findViewById(R.id.txt_size_name);

        size_spinner = findViewById(R.id.sp_type);
        et_name = findViewById(R.id.et_name);
        btn_submit = findViewById(R.id.btn_submit);

        Intent intent = getIntent();
        if (intent != null) {
            //grade_id = intent.getStringExtra("grade_id");
            grade_name = intent.getStringExtra("grade_name");
            // shape_id = intent.getStringExtra("shape_id");
            shape_name = intent.getStringExtra("shape_name");
            // location_id = intent.getStringExtra("location_id");
            location_name = intent.getStringExtra("location_name");

            res_grade_id = intent.getStringExtra("res_grade_id");
            res_shape_id = intent.getStringExtra("res_shape_id");
        }

        txt_grade_name.setText(grade_name);
        txt_shape_name.setText(shape_name);
        txt_location_name.setText(location_name);

        if (Common.isConnectingToInternet(mContext)) {
            loadSizeData();
        } else {
            tv_no_data_found.setVisibility(View.VISIBLE);
            tv_no_data_found.setText(mContext.getResources().getString(R.string.no_internet_available));
        }

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkValidations()) {
                    if (Common.isConnectingToInternet(mContext)) {
                        if (!size_name.equals("Select size")) {
                            if (et_name.isEnabled())
                                size_name = et_name.getText().toString();
                            if (size_name != null) {
                                UploadMasterData();
                            } else {
                                Common.showToast(mContext, "Please Enter location");
                            }
                        } else {
                            Common.showToast(mContext, "Please Select Data From List");
                        }

                    } else {
                        Common.showToast(mContext, mContext.getResources().getString(R.string.no_internet_available));
                    }
                }
            }
        });


        size_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                int index = size_spinner.getSelectedItemPosition() - 1;

                size_name = size_spinner.getItemAtPosition(index).toString();

                if (sizeList != null && index >= 0) {
                    if (!size_name.equals("Add New Size")) {
                        // grade_id = sizeList.get(index).getId();
                    }
                }

                if (size_name.equals("Add New Size")) {
                    newFlag = true;
                    et_name.setEnabled(true);
                } else {
                    newFlag = false;
                    et_name.setEnabled(false);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // DO Nothing here
            }
        });

    }

    private void loadSizeData() {
        Common.showProgressDialog(mContext);
        API.SizeData().getSizeData(prefManager.getToken(), prefManager.getRegId(), res_grade_id, res_shape_id).enqueue(new retrofit2.Callback<SizeResponse>() {
            @Override
            public void onResponse(Call<SizeResponse> call, Response<SizeResponse> response) {
                SizeResponse sizeResponse = response.body();
                if (sizeResponse.getErrorCode() == 200) {
                    prefManager.addToken(sizeResponse.getUser().getToken() + "");
                    prefManager.setUserData(sizeResponse.getUser().getName(), sizeResponse.getUser().getEmail(), sizeResponse.getUser().getMobile(),
                            sizeResponse.getUser().getId(), sizeResponse.getUser().getOrganization(), sizeResponse.getUser().getDob());
                    Log.i("username", String.valueOf(sizeResponse.getSizes()));
                    sizeList = sizeResponse.getSizes();
                    sizeDataSpinner();
                } else {
                    if (sizeResponse.getError() != null && !sizeResponse.getError().equals("")) {
                        Common.showToast(mContext, sizeResponse.getError() + "");
                        Intent Intent = new Intent(mContext, LoginActivity.class);
                        startActivity(Intent);
                        finish();
                        prefManager.userLogout();
                    }
                }
                Common.dismissDialog();
            }

            @Override
            public void onFailure(Call<SizeResponse> call, Throwable t) {
                Common.dismissDialog();
                Common.showToast(mContext, "Please try again later!!");
            }
        });
    }

    private void UploadMasterData() {
        Common.showProgressDialog(mContext);
        Common.errorLog("PRAMAS", type + " -- " + size_name + "--" + res_grade_id + "--" + res_shape_id);
        API.MasterData().getSizeMasterData(type, res_grade_id, res_shape_id, size_name, prefManager.getToken(), prefManager.getRegId()).enqueue(new retrofit2.Callback<MasterDataResponse>() {
            @Override
            public void onResponse(Call<MasterDataResponse> call, Response<MasterDataResponse> response) {
                Common.errorLog("masterDataResponse", response.toString() + "---");
                MasterDataResponse masterDataResponse = response.body();
                Log.e("prodMaster", "onResponse: " + response.body());
                if (masterDataResponse != null) {

                    Log.e("product", "onResponse: " + type);
                    Log.e("product", "onResponse: " + res_grade_id);
                    Log.e("product", "onResponse: " + res_shape_id);
                    Log.e("product", "onResponse: " + size_name);
                    Log.e("product", "onResponse: " + prefManager.getToken());
                    Log.e("product", "onResponse: " + prefManager.getRegId());

                    Intent intent = new Intent(mContext, MainActivity.class);
                    startActivity(intent);
                    finishAffinity();
                } else {
                    if (masterDataResponse.getError() != null && !masterDataResponse.getError().equals("")) {
                        Common.showToast(mContext, masterDataResponse.getError() + "");
                    }
                }
                Common.dismissDialog();
            }

            @Override
            public void onFailure(Call<MasterDataResponse> call, Throwable t) {
                Common.dismissDialog();
                Common.showToast(mContext, "Please try again later!!");
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

    private boolean checkValidations() {
        if (newFlag && !Common.isNotNullEditTextBox(et_name)) {
            et_name.requestFocus();
            Common.showToast(mContext, "Please Enter  Name");
            return false;
        }
        return true;
    }

    private void sizeDataSpinner() {
        List<String> items = new ArrayList<>();
        for (int i = 0; i < sizeList.size(); i++) {
            //Storing names to string array
//            items.get(i) = gradeList.get(i).getName();
            items.add(sizeList.get(i).getName());
        }
        // items[gradeList.size()]="Add New Grade";
        ArrayAdapter<String> adapter;
        items.add("Add New Size");
        adapter = new ArrayAdapter<String>(mContext, R.layout.spinner_item, items);
        size_spinner.setAdapter(adapter);
    }
}
