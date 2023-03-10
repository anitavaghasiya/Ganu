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
import com.ganak.model.Grade;
import com.ganak.model.GradesResponse;
import com.ganak.model.MasterDataResponse;
import com.ganak.rest.API;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class MasterGradeActivity extends AppCompatActivity {

    private Context mContext;
    private android.support.v7.app.ActionBar actionBar;
    // private Spinner sp_type;
    private EditText et_name;
    private PrefManager prefManager;
    private Button btn_submit;
    private String name, grade_name, id, type = "grade";
    private boolean newFlag = false;

    private Spinner grade_Spinner;
    private List<Grade> gradeList;
    // private List<Grade> items = new ArrayList<>();
    private TextView tv_no_data_found;
    private int index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_master);
        mContext = this;

        prefManager = new PrefManager(mContext);

        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        tv_no_data_found = findViewById(R.id.tv_no_data_found);
        grade_Spinner = findViewById(R.id.sp_type);

        et_name = findViewById(R.id.et_name);
        btn_submit = findViewById(R.id.btn_submit);

        grade_name = et_name.getText().toString().trim();

        if (Common.isConnectingToInternet(mContext)) {
            getGradesData();
        } else {
            tv_no_data_found.setVisibility(View.VISIBLE);
            tv_no_data_found.setText(mContext.getResources().getString(R.string.no_internet_available));
        }

        grade_Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                int index = grade_Spinner.getSelectedItemPosition() - 1;

                name = grade_Spinner.getItemAtPosition(index).toString();

                if (gradeList != null && index >= 0) {
                    if (!name.equals("Add New Grade")) {
                        id = gradeList.get(index).getId();
                    }
                }

                if (name.equals("Add New Grade")) {
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
                        if (!name.equals("Select Grade")) {
                            if (et_name.isEnabled())
                                name = et_name.getText().toString();
                            if (name != null) {
                                UploadMasterData();
                            } else {
                                Common.showToast(mContext, "Please Enter Grade");
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

   /* public void spinnerData(String name) {
        if (name.equals("Select Grade"))
            Common.showToast(mContext, "Please Select Data");
    }

    private void displayUserData(Grade grade) {
        id = grade.getId();
        Grade_name = grade.getName();
        Log.e("ID", "displayUserData: " + id);

    }*/


    private void UploadMasterData() {
        Common.showProgressDialog(mContext);
        Common.errorLog("PRAMAS", type + " -- " + name + "--");
        API.MasterData().getMasterData(type, name, prefManager.getToken(), prefManager.getRegId()).enqueue(new retrofit2.Callback<MasterDataResponse>() {
            @Override
            public void onResponse(Call<MasterDataResponse> call, Response<MasterDataResponse> response) {

                Common.errorLog("masterDataResponse", response.toString() + "---");
                MasterDataResponse masterDataResponse = response.body();
                if (masterDataResponse != null) {
                    String res_grade_id = masterDataResponse.getId();
                    Log.e("res_is", "onResponse: " + masterDataResponse.getId());
                    Intent intent = new Intent(mContext, MasterShapeActivity.class);
                    //intent.putExtra("grade_id", id);
                    intent.putExtra("grade_name", name);
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

    private void getGradesData() {
        Common.showProgressDialog(mContext);
        API.GradeData().getGradesResponse(prefManager.getToken(), prefManager.getRegId()).enqueue(new retrofit2.Callback<GradesResponse>() {
            @Override
            public void onResponse(Call<GradesResponse> call, Response<GradesResponse> response) {
                Common.errorLog("GradesResponse", response.toString() + "---");
                GradesResponse gradesResponse = response.body();
                Log.e("GradesResponse", gradesResponse.toString());
                if (gradesResponse.getErrorCode() == 200) {
                    prefManager.addToken(gradesResponse.getUser().getToken() + "");
                    prefManager.setUserData(gradesResponse.getUser().getName(), gradesResponse.getUser().getEmail(), gradesResponse.getUser().getMobile(),
                            gradesResponse.getUser().getId(), gradesResponse.getUser().getOrganization(), gradesResponse.getUser().getDob());
                    Log.e("username", String.valueOf(gradesResponse.getGrades()));
                    gradeList = gradesResponse.getGrades();
                    GradeDataSpinner();
                } else {
                    if (gradesResponse.getError() != null && !gradesResponse.getError().equals("")) {
                        Common.showToast(mContext, gradesResponse.getError() + "");
                        Intent Intent = new Intent(mContext, LoginActivity.class);
                        startActivity(Intent);
                        finish();
                        prefManager.userLogout();
                    }
                }
                Common.dismissDialog();
            }

            @Override
            public void onFailure(Call<GradesResponse> call, Throwable t) {
                Common.dismissDialog();
                Common.showToast(mContext, "Please try again later!!");
            }
        });

    }

 /*   private void GradeDataSpinner() {
//        List<Grade> items = new ArrayList<>();
        for (int i = 0; i < gradeList.size(); i++) {
            //Storing names to string array
//            items.get(i) = gradeList.get(i).getName();
            items.add(new Grade(gradeList.get(i).getId(),gradeList.get(i).getName()));
        }
        // items[gradeList.size()]="Add New Grade";
        ArrayAdapter<Grade> adapter;
        items.add(new Grade(gradeList.size()+1+"","Add New Grade"));
        adapter = new ArrayAdapter<Grade>(mContext,R.layout.spinner_item, items);
        grade_Spinner.setAdapter(adapter);
    }*/

    private void GradeDataSpinner() {
        List<String> items = new ArrayList<>();
        for (int i = 0; i < gradeList.size(); i++) {
            //Storing names to string array
//            items.get(i) = gradeList.get(i).getName();
            items.add(gradeList.get(i).getName());
        }
        // items[gradeList.size()]="Add New Grade";
        ArrayAdapter<String> adapter;
        items.add("Add New Grade");
        adapter = new ArrayAdapter<String>(mContext, R.layout.spinner_item, items);
        grade_Spinner.setAdapter(adapter);
    }

}
