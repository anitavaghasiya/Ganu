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
import com.ganak.model.Location;
import com.ganak.model.LocationResponse;
import com.ganak.model.MasterDataResponse;
import com.ganak.rest.API;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class MaterLocationActivity extends AppCompatActivity {

    private Context mContext;
    private android.support.v7.app.ActionBar actionBar;
    // private Spinner sp_type;
    private EditText et_name;
    private PrefManager prefManager;
    private Button btn_submit;
    private String location_name, grade_id, grade_name, shape_id, shape_name, location_id, type = "location", res_grade_id, res_shape_id;
    private Spinner location_spinner;
    private List<Location> location_List;
    private TextView txt_grade_name, txt_shape_name, tv_no_data_found;
    private boolean newFlag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mater_location);

        mContext = this;
        prefManager = new PrefManager(mContext);

        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        tv_no_data_found = findViewById(R.id.tv_no_data_found);
        txt_grade_name = findViewById(R.id.txt_grade_name);
        txt_shape_name = findViewById(R.id.txt_shape_name);

        location_spinner = findViewById(R.id.sp_type);
        et_name = findViewById(R.id.et_name);
        btn_submit = findViewById(R.id.btn_submit);

        Intent intent = getIntent();
        if (intent != null) {
            // grade_id = intent.getStringExtra("grade_id");
            grade_name = intent.getStringExtra("grade_name");
            //  shape_id = intent.getStringExtra("shape_id");
            shape_name = intent.getStringExtra("shape_name");

            res_grade_id = intent.getStringExtra("res_grade_id");
            res_shape_id = intent.getStringExtra("res_shape_id");
        }

        txt_grade_name.setText(grade_name);
        txt_shape_name.setText(shape_name);

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkValidations()) {
                    if (Common.isConnectingToInternet(mContext)) {
                        if (!location_name.equals("Select Location")) {
                            if (et_name.isEnabled())
                                location_name = et_name.getText().toString();
                            if (location_name != null) {
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

        if (Common.isConnectingToInternet(mContext)) {
            loadLocationData();
        } else {
            tv_no_data_found.setVisibility(View.VISIBLE);
            tv_no_data_found.setText(mContext.getResources().getString(R.string.no_internet_available));
        }

        location_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                int index = location_spinner.getSelectedItemPosition() - 1;

                location_name = location_spinner.getItemAtPosition(index).toString();

                if (location_List != null && index >= 0) {
                    if (!location_name.equals("Add New Location")) {
                        // grade_id = locationList.get(index).getId();
                    }
                }

                if (location_name.equals("Add New Location")) {
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

    private void loadLocationData() {
        Common.showProgressDialog (mContext);
        API.LocationData ().getLocationData (prefManager.getToken (), prefManager.getRegId (), shape_id, grade_id).enqueue (new retrofit2.Callback<LocationResponse> () {
            @Override
            public void onResponse(Call<LocationResponse> call, Response<LocationResponse> response) {
                LocationResponse locationResponse = response.body ();
                if (locationResponse.getErrorCode () == 200) {
                    prefManager.addToken (locationResponse.getUser ().getToken () + "");
                    prefManager.setUserData (locationResponse.getUser ().getName (), locationResponse.getUser ().getEmail (), locationResponse.getUser ().getMobile (),
                            locationResponse.getUser ().getId (), locationResponse.getUser ().getOrganization (), locationResponse.getUser ().getDob ());
                    location_List = locationResponse.getLocations ();
                    locationDataSpinner();
                } else {
                    if (locationResponse.getError () != null && !locationResponse.getError ().equals ("")) {
                        Common.showToast (mContext, locationResponse.getError () + "");
                        Intent Intent = new Intent (mContext, LoginActivity.class);
                        startActivity (Intent);
                        finish ();
                        prefManager.userLogout();
                    }
                }
                Common.dismissDialog ();
            }

            @Override
            public void onFailure(Call<LocationResponse> call, Throwable t) {
                Common.dismissDialog ();
                Common.showToast (mContext, "Please try again later!!");
            }
        });

    }

   /* private void loadLocationData() {
        Common.showProgressDialog(mContext);
        Log.e("IdofTwo", "loadLocationData: " +res_grade_id+"--"+res_shape_id);
        API.LocationData().getLocationData(prefManager.getToken(), prefManager.getRegId(), res_grade_id, res_shape_id).enqueue(new retrofit2.Callback<LocationResponse>() {
            @Override
            public void onResponse(Call<LocationResponse> call, Response<LocationResponse> response) {
                Log.e("TOken", "onResponse: " + prefManager.getToken()+"--"+prefManager.getRegId());

                LocationResponse locationResponse = response.body();
                locationList = locationResponse.getLocations();
                Log.e("location", "onResponse: " + locationList);
                if (locationResponse.getErrorCode() == 200) {
                    prefManager.addToken(locationResponse.getUser().getToken() + "");
                    prefManager.setUserData(locationResponse.getUser().getName(), locationResponse.getUser().getEmail(), locationResponse.getUser().getMobile(),
                            locationResponse.getUser().getId(), locationResponse.getUser().getOrganization(), locationResponse.getUser().getDob());


                    locationDataSpinner();
                } else {
                    if (locationResponse.getError() != null && !locationResponse.getError().equals("")) {
                        Common.showToast(mContext, locationResponse.getError() + "");
                        Intent Intent = new Intent(mContext, LoginActivity.class);
                        startActivity(Intent);
                        finish();
                        prefManager.userLogout();
                    }
                }
                Common.dismissDialog();
            }

            @Override
            public void onFailure(Call<LocationResponse> call, Throwable t) {
                Common.dismissDialog();
                Common.showToast(mContext, "Please try again later!!");
            }
        });

    }
*/
    private void UploadMasterData() {
        Common.showProgressDialog(mContext);
        Common.errorLog("PRAMAS", type + " -- " + location_name + "--" + res_grade_id + "--" + res_shape_id);
        API.MasterData().getLocationMasterData(type, res_grade_id, res_shape_id, location_name, prefManager.getToken(), prefManager.getRegId()).enqueue(new retrofit2.Callback<MasterDataResponse>() {
            @Override
            public void onResponse(Call<MasterDataResponse> call, Response<MasterDataResponse> response) {
                Common.errorLog("masterDataResponse", response.toString() + "---");
                MasterDataResponse masterDataResponse = response.body();
                if (masterDataResponse != null) {

                    String res_location_id = masterDataResponse.getId();
                    Intent intent = new Intent(mContext, MasterSizeActivity.class);
                    // intent.putExtra("grade_id", grade_id);
                    intent.putExtra("grade_name", grade_name);
                    //intent.putExtra("shape_id", shape_id);
                    intent.putExtra("shape_name", shape_name);
                    intent.putExtra("location_id", location_id);
                    intent.putExtra("location_name", location_name);

                    intent.putExtra("res_grade_id", res_grade_id);
                    intent.putExtra("res_shape_id", res_shape_id);

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

    private void locationDataSpinner() {
        List<String> items = new ArrayList<>();
        for (int i = 0; i < location_List.size(); i++) {
            //Storing names to string array
//            items.get(i) = gradeList.get(i).getName();
            items.add(location_List.get(i).getName());
        }
        // items[gradeList.size()]="Add New Grade";
        ArrayAdapter<String> adapter;
        items.add("Add New Location");
        adapter = new ArrayAdapter<String>(mContext, R.layout.spinner_item, items);
        location_spinner.setAdapter(adapter);
    }
}
