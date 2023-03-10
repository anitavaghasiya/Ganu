package com.ganak.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.ganak.R;
import com.ganak.common.PrefManager;

public class SplashActivity extends AppCompatActivity {
    private Context mContext;
    private int SPLASH_TIME_OUT = 2000;
    private PrefManager prefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        mContext = this;

        prefManager = new PrefManager(mContext);

        Log.e("pin", "onCreate: " + prefManager.getPIN());

        if (prefManager.getToken() != null && !prefManager.getToken().equals("")) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(mContext, MainActivity.class);
                    startActivity(intent);
                    finish();
                   /* if (prefManager.getPIN().equals("")) {
                        String Pin=prefManager.getPIN();
                        Log.e("pin",Pin);
                        Intent intent = new Intent(mContext,PinActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Log.e("pin", "run: ");
                        Intent intent = new Intent(mContext,EPinActivity.class);
                        startActivity(intent);
                        finish();
                    }*/
                }
            }, SPLASH_TIME_OUT);
        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(mContext, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }, SPLASH_TIME_OUT);
        }
    }
}
