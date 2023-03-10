package com.ganak.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ganak.R;
import com.ganak.common.Common;
import com.ganak.common.PrefManager;
import com.ganak.model.Location;
import com.ganak.model.LocationResponse;
import com.ganak.rest.API;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class AddProductActivity extends AppCompatActivity {
    private Context mContext;
    private TextView tv_product_name;
    private PrefManager prefManager;

    private Spinner location_Spinner;
    private List<Location> shapeList;
    private ArrayList<String> langName, langId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        mContext = this;

        prefManager = new PrefManager(mContext);

        tv_product_name = (TextView) findViewById(R.id.tv_pro_name);
        location_Spinner = (Spinner) findViewById(R.id.sn_location);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            tv_product_name.setText(getIntent().getExtras().getString("product_name"));
        }
        langName = new ArrayList<>();
        langId = new ArrayList<>();

        loadSpinnerData();
        location_Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String language = location_Spinner.getItemAtPosition(location_Spinner.getSelectedItemPosition()).toString();
                Toast.makeText(getApplicationContext(), language, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // DO Nothing here
            }
        });

    }

    private void loadSpinnerData() {
        Common.showProgressDialog(mContext);
        API.LocationData().getLocationData(prefManager.getToken(), prefManager.getRegId(), "0", "0").enqueue(new retrofit2.Callback<LocationResponse>() {
            @Override
            public void onResponse(Call<LocationResponse> call, Response<LocationResponse> response) {
                Common.errorLog("locationResponse", response.toString() + "---");
                LocationResponse locationResponse = response.body();
                if (locationResponse.getErrorCode() == 200) {
                    prefManager.addToken(locationResponse.getUser().getToken() + "");
                    prefManager.setUserData(locationResponse.getUser().getName(), locationResponse.getUser().getEmail(), locationResponse.getUser().getMobile(),
                            locationResponse.getUser().getId(), locationResponse.getUser().getOrganization(), locationResponse.getUser().getDob());
                    Log.i("username", String.valueOf(locationResponse.getLocations()));
                    shapeList = locationResponse.getLocations();
                    LocationDataSpinner();

                } else {
                    if (locationResponse.getError() != null && !locationResponse.getError().equals("")) {
                        Common.showToast(mContext, locationResponse.getError() + "");
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

    private void LocationDataSpinner() {
        String[] items = new String[shapeList.size()];
        for (int i = 0; i < shapeList.size(); i++) {
            //Storing names to string array
            items[i] = shapeList.get(i).getName();
        }
        ArrayAdapter<String> adapter;
        adapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_dropdown_item_1line, items);
        location_Spinner.setPrompt("Select Location");
        location_Spinner.setAdapter(adapter);

    }

}

