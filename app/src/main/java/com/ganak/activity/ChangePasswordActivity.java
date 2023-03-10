package com.ganak.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.ganak.R;
import com.ganak.common.PrefManager;

public class ChangePasswordActivity extends AppCompatActivity {
    private Context mContext;
    private Button btn_update;
    private android.support.v7.app.ActionBar actionBar;
    private EditText et_new_password, et_old_password, et_confirm_new_password;
    private ProgressDialog loading;
    private AlertDialog dialog;
    private PrefManager prefManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        mContext = this;

        prefManager = new PrefManager(mContext);

        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Change Password");

        btn_update = findViewById(R.id.btn_update);
        et_new_password = findViewById(R.id.et_new_password);
        et_old_password = findViewById(R.id.et_old_password);
        et_confirm_new_password = findViewById(R.id.et_confirm_new_password);


        et_new_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String pass = et_new_password.getText().toString();
                if (TextUtils.isEmpty(pass) || pass.length() < 8) {
                    et_new_password.setError("You must have 8 characters in your password");
                    return;
                }
            }
        });

        et_new_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String pass = et_new_password.getText().toString();
                if (TextUtils.isEmpty(pass) || pass.length() < 8) {
                    et_new_password.setError("You must have 8 characters in your password");
                    return;
                }
            }
        });

        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    private void updatePassword() {
        // showing a progress dialog loading
        loading.setMessage("Updating user profile...");
        loading.show();
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
