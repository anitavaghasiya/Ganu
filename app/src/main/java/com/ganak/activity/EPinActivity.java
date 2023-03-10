package com.ganak.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.ganak.R;
import com.ganak.common.PrefManager;
import com.mukesh.OnOtpCompletionListener;
import com.mukesh.OtpView;

public class EPinActivity extends AppCompatActivity {

    private Context mContext;
    private PrefManager prefManager;
    private OtpView et_pin;
    private String pin;
    private Button btn_logout;
    private android.support.v7.app.ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_epin);

        mContext = this;

        prefManager = new PrefManager(mContext);

        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        et_pin = findViewById(R.id.et_pin);
        btn_logout = findViewById(R.id.btn_logout);

        et_pin.setOtpCompletionListener(new OnOtpCompletionListener() {
            @Override
            public void onOtpCompleted(String otp) {
                pin = et_pin.getEditableText().toString();
                if (prefManager.getPIN().equals(pin)) {
                    Intent i = new Intent(mContext, MasterGradeActivity.class);
                    startActivity(i);
                    finish();
                } else {
                    Toast.makeText(mContext, " Enter Valid PIN ", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prefManager.userLogout();
                Intent homeIntent = new Intent(mContext, LoginActivity.class);
                startActivity(homeIntent);
                finishAffinity();
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

}
