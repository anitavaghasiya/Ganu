package com.ganak.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import com.ganak.BuildConfig;
import com.ganak.R;

public class AboutUsActivity extends AppCompatActivity {
    private Context mContext;
    private android.support.v7.app.ActionBar actionBar;
    private TextView tv_app_name, tv_app_version, tv_app_description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        mContext = this;

        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("About Us");

        tv_app_name = findViewById(R.id.tv_app_name);
        tv_app_version = findViewById(R.id.tv_app_version);
        tv_app_description = findViewById(R.id.tv_app_description);

        tv_app_name.setText(mContext.getResources().getString(R.string.app_name));
        tv_app_version.setText(BuildConfig.VERSION_NAME);
        tv_app_description.setText("Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book.");

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
