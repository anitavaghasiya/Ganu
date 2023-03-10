package com.ganak.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ganak.R;
import com.ganak.adapter.ProductAdapter;
import com.ganak.common.Common;
import com.ganak.common.PrefManager;
import com.ganak.model.Product;
import com.ganak.model.ProductReportResponse;
import com.ganak.rest.API;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class ReportActivity extends AppCompatActivity {

    private Context mContext;
    private RecyclerView rv_products;
    private PrefManager prefManager;
    private List<Product> productList;
    private ProductAdapter productAdapter;
    private TextView tv_no_data_found;
    private SwipeRefreshLayout swipe_refresh;
    private LinearLayout ll_main;
    private Button btn_download;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        mContext = this;
        prefManager = new PrefManager(mContext);
        productList = new ArrayList<>();
        rv_products = findViewById(R.id.rv_products);

        ll_main = findViewById(R.id.ll_main);
        tv_no_data_found = findViewById(R.id.tv_no_data_found);
        swipe_refresh = findViewById(R.id.swipe_refresh);
        btn_download = findViewById(R.id.btn_download);


        if (Common.isConnectingToInternet(mContext)) {
            rv_products.setVisibility(View.VISIBLE);
            tv_no_data_found.setVisibility(View.GONE);
            getProducts();
        } else {
            rv_products.setVisibility(View.GONE);
            tv_no_data_found.setVisibility(View.VISIBLE);
            tv_no_data_found.setText(mContext.getResources().getString(R.string.no_internet_available));
        }

        swipe_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (Common.isConnectingToInternet(mContext)) {
                    rv_products.setVisibility(View.VISIBLE);
                    tv_no_data_found.setVisibility(View.GONE);
                    productList = new ArrayList<>();
                    productAdapter = new ProductAdapter(mContext, productList);
                    rv_products.setAdapter(productAdapter);
                    getProducts();
                    swipe_refresh.setRefreshing(false);
                } else {
                    rv_products.setVisibility(View.GONE);
                    tv_no_data_found.setVisibility(View.VISIBLE);
                    tv_no_data_found.setText(mContext.getResources().getString(R.string.no_internet_available));
                    swipe_refresh.setRefreshing(false);
                }
            }
        });

        btn_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://ganak.webadmin.rocks/api/v2/download-product.php?id=" + prefManager.getRegId() + "&token=" + prefManager.getToken()));
                startActivity(browserIntent);
            }
        });

    }

    private void getProducts() {
        Common.showProgressDialog(mContext);
        Common.errorLog("PRAMAS", prefManager.getRegId() + " -- " + prefManager.getToken());
        API.ProductData().getProductReport(prefManager.getToken(), prefManager.getRegId()).enqueue(new retrofit2.Callback<ProductReportResponse>() {
            @Override
            public void onResponse(Call<ProductReportResponse> call, Response<ProductReportResponse> response) {
                Common.errorLog("masterDataResponse", response.toString() + "---");
                ProductReportResponse productReportResponse = response.body();

                Log.e("report", "onResponse: prod response " + productReportResponse);
                Log.e("regId", "onResponse: reg ID " + prefManager.getRegId());
                if (productReportResponse != null) {
                    productList = productReportResponse.getProducts();
                    Log.e("prod", "onResponse: product size " + productReportResponse.getProducts().size());
                    Common.errorLog("SIZEX", productList.size() + " Xd");
                    productAdapter = new ProductAdapter(mContext, productList);
                    GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 1);
                    rv_products.setLayoutManager(gridLayoutManager);
                    rv_products.setItemAnimator(new DefaultItemAnimator());
                    rv_products.setAdapter(productAdapter);
                    tv_no_data_found.setVisibility(View.GONE);
                    rv_products.setVisibility(View.VISIBLE);
                } else {
                    rv_products.setVisibility(View.GONE);
                    tv_no_data_found.setVisibility(View.VISIBLE);
                    tv_no_data_found.setText(mContext.getResources().getString(R.string.no_data_found));
                }

                Common.dismissDialog();
            }

            @Override
            public void onFailure(Call<ProductReportResponse> call, Throwable t) {
                Common.dismissDialog();
                Common.showToast(mContext, "Please try again later!!");
            }
        });
    }
}
