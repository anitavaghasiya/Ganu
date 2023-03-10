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
import com.ganak.model.Shape;
import com.ganak.model.ShapeResponse;
import com.ganak.rest.API;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class MasterShapeActivity extends AppCompatActivity {

    private Context mContext;
    private android.support.v7.app.ActionBar actionBar;
    // private Spinner sp_type;
    private EditText et_name;
    private PrefManager prefManager;
    private Button btn_submit;
    private String name, id, grade_name, shape_id, shape_name, type = "shape", res_grade_id, res_shape_id;

    private Spinner shape_spinner;
    private List<Shape> shapeList;

    private TextView txt_grade_name;
    private boolean newFlag = false;
    private TextView tv_no_data_found;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_master_shape);

        mContext = this;
        prefManager = new PrefManager(mContext);

        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        tv_no_data_found = findViewById(R.id.tv_no_data_found);
        txt_grade_name = findViewById(R.id.txt_grade_name);

        Intent intent = getIntent();
        if (intent != null) {
            // id = intent.getStringExtra("grade_id");
            grade_name = intent.getStringExtra("grade_name");
            res_grade_id = intent.getStringExtra("res_grade_id");
            Log.e("res_grad_id", "onCreate: " + res_grade_id);
        }

        txt_grade_name.setText(grade_name);

        shape_spinner = findViewById(R.id.sp_type);
        et_name = findViewById(R.id.et_name);
        btn_submit = findViewById(R.id.btn_submit);

        if (Common.isConnectingToInternet(mContext)) {
            getShapeData();
        } else {
            tv_no_data_found.setVisibility(View.VISIBLE);
            tv_no_data_found.setText(mContext.getResources().getString(R.string.no_internet_available));
        }

        shape_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                int index = shape_spinner.getSelectedItemPosition() - 1;

                shape_name = shape_spinner.getItemAtPosition(index).toString();

                if (shapeList != null && index >= 0) {
                    if (!shape_name.equals("Add New Shape")) {
                        // shape_id = shapeList.get(index).getId();
                        // Log.e("SHAPE", "onItemSelected: " + shape_id);
                    }
                }

                if (shape_name.equals("Add New Shape")) {
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

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkValidations()) {
                    if (Common.isConnectingToInternet(mContext)) {
                        if (!shape_name.equals("Select Shape")) {

                            if (et_name.isEnabled())
                                shape_name = et_name.getText().toString();
                            if (shape_name != null) {
                                UploadMasterData();
                            } else {
                                Common.showToast(mContext, "Please Enter Shape");
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

    private void getShapeData() {
        Common.showProgressDialog(mContext);
        API.ShapeData().getShapeResponse(prefManager.getToken(), prefManager.getRegId(), res_grade_id).enqueue(new retrofit2.Callback<ShapeResponse>() {
            @Override
            public void onResponse(Call<ShapeResponse> call, Response<ShapeResponse> response) {
                ShapeResponse shapeResponse = response.body();
                Log.e("GradesResponse", shapeResponse.toString());
                if (shapeResponse.getErrorCode() == 200) {
                    prefManager.addToken(shapeResponse.getUser().getToken() + "");
                    prefManager.setUserData(shapeResponse.getUser().getName(), shapeResponse.getUser().getEmail(), shapeResponse.getUser().getMobile(),
                            shapeResponse.getUser().getId(), shapeResponse.getUser().getOrganization(), shapeResponse.getUser().getDob());
                    shapeList = shapeResponse.getShapes();
                    ShapeDataSpinner();
                } else {
                    if (shapeResponse.getError() != null && !shapeResponse.getError().equals("")) {
                        Common.showToast(mContext, shapeResponse.getError() + "");
                        Intent Intent = new Intent(mContext, LoginActivity.class);
                        startActivity(Intent);
                        finish();
                        prefManager.userLogout();
                    }
                }
                Common.dismissDialog();
            }

            @Override
            public void onFailure(Call<ShapeResponse> call, Throwable t) {
                Common.dismissDialog();
                Common.showToast(mContext, "Please try again later!!");
            }
        });

    }

    private void UploadMasterData() {
        Common.showProgressDialog(mContext);
        Common.errorLog("PRAMAS", type + " -- " + shape_name + "--" + res_grade_id);
        API.MasterData().getShapeMasterData(type, res_grade_id, shape_name, prefManager.getToken(), prefManager.getRegId()).enqueue(new retrofit2.Callback<MasterDataResponse>() {
            @Override
            public void onResponse(Call<MasterDataResponse> call, Response<MasterDataResponse> response) {
                Common.errorLog("masterDataResponse", response.toString() + "---");
                MasterDataResponse masterDataResponse = response.body();
                if (masterDataResponse != null) {
                    res_shape_id = masterDataResponse.getId();
                    Intent intent = new Intent(mContext, MaterLocationActivity.class);
                    /*intent.putExtra("grade_id", id);
                    intent.putExtra("grade_name", name);*/
                    // intent.putExtra("grade_id", id);
                    intent.putExtra("grade_name", grade_name);
                    // intent.putExtra("shape_id", shape_id);
                    intent.putExtra("shape_name", shape_name);

                    intent.putExtra("res_shape_id", res_shape_id);
                    intent.putExtra("res_grade_id", res_grade_id);
                    startActivity(intent);

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

    private void ShapeDataSpinner() {
        List<String> items = new ArrayList<>();
        for (int i = 0; i < shapeList.size(); i++) {
            //Storing names to string array
//            items.get(i) = gradeList.get(i).getName();
            items.add(shapeList.get(i).getName());
        }
        // items[gradeList.size()]="Add New Grade";
        ArrayAdapter<String> adapter;
        items.add("Add New Shape");
        adapter = new ArrayAdapter<String>(mContext, R.layout.spinner_item, items);
        shape_spinner.setAdapter(adapter);
    }
}
