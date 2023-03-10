package com.ganak.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.ganak.R;
import com.ganak.common.Common;
import com.ganak.common.PrefManager;
import com.ganak.common.RealFilePathUtil;
import com.ganak.model.InwardDataResponse;
import com.ganak.rest.API;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION;
import static android.content.Intent.FLAG_GRANT_WRITE_URI_PERMISSION;

public class OutwardDataActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE = 100;
    private String filePath;
    private Context mContext;
    private PrefManager prefManager;
    private Button btn_continue;
    private TextView tv_product_name, btn_select_doc, btn_upload, tv_doc_name;
    private Uri selectedFile;
    private File f;
    private String fileName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_outward);

        mContext = this;

        prefManager = new PrefManager(mContext);

        tv_product_name = (TextView) findViewById(R.id.tv_pro_name);
        tv_doc_name = (TextView) findViewById(R.id.tv_doc_name);
        btn_continue = (Button) findViewById(R.id.btn_continue);

        btn_select_doc = (TextView) findViewById(R.id.btn_select_doc);
        btn_upload = (TextView) findViewById(R.id.btn_upload);

        btn_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fileName != null) {
                    Intent i = new Intent(mContext, MainActivity.class);
                    startActivity(i);
                } else {
                    Toast.makeText(mContext, "Please select your document", Toast.LENGTH_LONG).show();
                }
            }
        });

        btn_select_doc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkPermission()) {
                    callIntent();
                } else {
                    requestPermission();
                }

            }
        });

        btn_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Common.isConnectingToInternet(mContext)) {
                    if (selectedFile == null) {
                        Toast.makeText(mContext, "please select the document", Toast.LENGTH_LONG).show();
                    } else {
                        getUploadData();
                    }
                } else {
                    Common.showToast(mContext, mContext.getResources().getString(R.string.no_internet_available));
                }
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 123 && resultCode == RESULT_OK) {
            if ((data != null) && (data.getData() != null)) {
                selectedFile = data.getData();
                RealFilePathUtil realFilePathUtil = new RealFilePathUtil();
                filePath = realFilePathUtil.getPath(this, selectedFile);
                fileName = new File(filePath).getName();
                tv_doc_name.setText(fileName);
            }
        }
    }

    public void callIntent() {
        Intent intent = new Intent().setType("application/*").setAction(Intent.ACTION_GET_CONTENT);
        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setFlags(FLAG_GRANT_READ_URI_PERMISSION | FLAG_GRANT_WRITE_URI_PERMISSION);
        startActivityForResult(Intent.createChooser(intent, "Select a file"), 123);
    }

    private void getUploadData() {

        Common.showProgressDialog(mContext);
        File file = new File(filePath);

        String id = prefManager.getRegId() + "";
        String token = prefManager.getToken() + "";


        //creating request body for file
        RequestBody requestFile = RequestBody.create(MediaType.parse(mContext.getContentResolver().getType(selectedFile)),
                file
        );
        final MultipartBody.Part body = MultipartBody.Part.createFormData("document", file.getName(), requestFile);

        final RequestBody id_req = RequestBody.create(MediaType.parse("text/plain"), id);
        final RequestBody token_req = RequestBody.create(MediaType.parse("text/plain"), token);

        API.user().uploadImage(body, id_req, token_req).enqueue(new Callback<InwardDataResponse>() {
            @Override
            public void onResponse(Call<InwardDataResponse> call, Response<InwardDataResponse> response) {
                Common.dismissDialog();
                InwardDataResponse inwardDataResponse = response.body();
                Log.e("server", "Part: " + body.toString());
                Log.e("server", "Token from pref: " + token_req.toString());
                Log.e("server", "Id from pref: " + id_req.toString());
                Log.e("server", "Token from response: " + inwardDataResponse.getUser().getToken());
                Log.e("server", "Message: " + inwardDataResponse.getMsg());
                Log.e("server", "Error Code: " + inwardDataResponse.getErrorCode());
                Log.e("server", "Error: " + inwardDataResponse.getError());
                Log.e("server", "User: " + inwardDataResponse.getUser().getName());
                Toast.makeText(mContext, "" + response.body().getMsg(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<InwardDataResponse> call, Throwable t) {
                Common.dismissDialog();
                Toast.makeText(mContext, "File Could not Uploaded", Toast.LENGTH_SHORT).show();
                Log.e("server", "call: " + call);
                Log.e("server", "T: " + t);
            }
        });

    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(mContext, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions((Activity) mContext,
                new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (ActivityCompat.checkSelfPermission(mContext, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                Common.errorLog("Permission", "permission Allow");

            }
        }
    }

}
