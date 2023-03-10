package com.ganak.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.ganak.R;
import com.ganak.common.PrefManager;
import com.mukesh.OnOtpCompletionListener;
import com.mukesh.OtpView;

public class PinActivity extends AppCompatActivity {

    private Context mContext;
    private PrefManager prefManager;
    private OtpView et_pin, et_confirm_pin;
    private String pin, confirm_pin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin);

        mContext = this;

        prefManager = new PrefManager(mContext);

        et_pin = findViewById(R.id.et_pin);

        et_confirm_pin = findViewById(R.id.et_confirm_pin);

        et_confirm_pin.setOtpCompletionListener(new OnOtpCompletionListener() {
            @Override
            public void onOtpCompleted(String otp) {
                pin = et_pin.getEditableText().toString();
                confirm_pin = et_confirm_pin.getEditableText().toString();
                prefManager.setPIN(pin);

                if (pin.equals(confirm_pin)) {
                    Intent i = new Intent(mContext, MasterGradeActivity.class);
                    startActivity(i);
                    finish();
                } else {
                    Toast.makeText(mContext, "PIN and Confirm PIN must be same!!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
