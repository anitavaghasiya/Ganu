package com.ganak.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.ganak.R;
import com.ganak.activity.LoginActivity;
import com.ganak.adapter.ShapeOutwardAdapter;
import com.ganak.common.Common;
import com.ganak.common.PrefManager;
import com.ganak.model.Shape;
import com.ganak.model.ShapeResponse;
import com.ganak.rest.API;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class ShapeOutwardFragment extends Fragment {

    private Context mContext;
    private RecyclerView recyclerview_shape;
    private SwipeRefreshLayout swipe_refresh;
    private TextView tv_no_data_found;
    private Shape status;
    private List<Shape> shapeList;
    private ShapeOutwardAdapter shapeOutwardAdapter;
    private LinearLayoutManager linearLayoutManager;
    private PrefManager prefManager;
    private String id, name_grade, token, name_grade_id;
    private TextView textView;
    private GridLayoutManager gridLayoutManager;
    private Fragment fragment;
    private EditText etSearch;


    public ShapeOutwardFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_shape_inward, container, false);

        mContext = getActivity();

        prefManager = new PrefManager(mContext);

        fragment = this;

        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle("Shapes");

        swipe_refresh = view.findViewById(R.id.swipe_refresh);
        tv_no_data_found = view.findViewById(R.id.tv_no_data_found);
        recyclerview_shape = view.findViewById(R.id.recyclerview_shape);
        etSearch = view.findViewById(R.id.etSearch);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            name_grade = bundle.getString("name");
            name_grade_id = bundle.getString("name_id");
            Log.e("name_grade", name_grade);
            Log.e("name_grade_od", name_grade_id);
        }

        shapeList = new ArrayList<>();

        if (Common.isConnectingToInternet(mContext)) {
            recyclerview_shape.setVisibility(View.VISIBLE);
            tv_no_data_found.setVisibility(View.GONE);
            getshapeData();
        } else {
            recyclerview_shape.setVisibility(View.GONE);
            tv_no_data_found.setVisibility(View.VISIBLE);
            tv_no_data_found.setText(mContext.getResources().getString(R.string.no_internet_available));
        }

        swipe_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (Common.isConnectingToInternet(mContext)) {
                    recyclerview_shape.setVisibility(View.VISIBLE);
                    tv_no_data_found.setVisibility(View.GONE);
                    shapeList = new ArrayList<>();
                    shapeOutwardAdapter = new ShapeOutwardAdapter(mContext, shapeList, name_grade, name_grade_id);
                    recyclerview_shape.setAdapter(shapeOutwardAdapter);
                    getshapeData();
                    swipe_refresh.setRefreshing(false);
                } else {
                    recyclerview_shape.setVisibility(View.GONE);
                    tv_no_data_found.setVisibility(View.VISIBLE);
                    tv_no_data_found.setText(mContext.getResources().getString(R.string.no_internet_available));
                    swipe_refresh.setRefreshing(false);
                }
            }
        });

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (shapeOutwardAdapter == null)
                    return;
                shapeOutwardAdapter.getFilter().filter(etSearch.getText().toString().trim());
                if (shapeOutwardAdapter.getItemCount() < 1) {
                    recyclerview_shape.setVisibility(View.GONE);
                    tv_no_data_found.setVisibility(View.VISIBLE);
                } else {
                    recyclerview_shape.setVisibility(View.VISIBLE);
                    tv_no_data_found.setText(mContext.getResources().getText(R.string.tv_no_result_found));
                    tv_no_data_found.setVisibility(View.GONE);
                }
                // shapeOutwardAdapter.getFilter().filter(etSearch.getText().toString().trim());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        return view;

    }

    private void getshapeData() {
        Common.showProgressDialog(mContext);
        API.ShapeData().getShapeResponse(prefManager.getToken(), prefManager.getRegId(), name_grade_id).enqueue(new retrofit2.Callback<ShapeResponse>() {
            @Override
            public void onResponse(Call<ShapeResponse> call, Response<ShapeResponse> response) {
                Common.errorLog("shaperesponse", response.toString() + "---");
                ShapeResponse shapeResponse = response.body();
                Log.e("shapeResponse", shapeResponse.toString());
                if (shapeResponse.getErrorCode() == 200) {
                    prefManager.addToken(shapeResponse.getUser().getToken() + "");
                    prefManager.setUserData(shapeResponse.getUser().getName(), shapeResponse.getUser().getEmail(), shapeResponse.getUser().getMobile(),
                            shapeResponse.getUser().getId(), shapeResponse.getUser().getOrganization(), shapeResponse.getUser().getDob());
                    Log.e("mobile", shapeResponse.getUser().getMobile());

                    if (shapeResponse.getShapes() != null) {
                        List<Shape> shapeList = shapeResponse.getShapes();
                        Log.e("shapeList", String.valueOf(shapeResponse.getShapes()));

                        if (shapeList != null && shapeList.size() > 0) {
                            recyclerview_shape.setVisibility(View.VISIBLE);
                            shapeOutwardAdapter = new ShapeOutwardAdapter(mContext, shapeList, name_grade, name_grade_id);
                            gridLayoutManager = new GridLayoutManager(mContext, 1);
                            recyclerview_shape.setLayoutManager(gridLayoutManager);
                            recyclerview_shape.setItemAnimator(new DefaultItemAnimator());
                            //recyclerview_shape.setLayoutManager(new LinearLayoutManager(mContext));
                            recyclerview_shape.setAdapter(shapeOutwardAdapter);
                        } else {
                            recyclerview_shape.setVisibility(View.GONE);
                            tv_no_data_found.setVisibility(View.VISIBLE);
                            tv_no_data_found.setText(mContext.getResources().getString(R.string.no_data_found));
                        }
                    } else {
                        Common.showToast(mContext, "Data not fount");
                    }

                } else {
                    if (shapeResponse.getError() != null && !shapeResponse.getError().equals("")) {
                        Common.showToast(mContext, shapeResponse.getError() + "");
                        Intent Intent = new Intent(mContext, LoginActivity.class);
                        startActivity(Intent);
                        getActivity().finish();
                        prefManager.clearUserDetail();
                    }
                }
                Common.dismissDialog();
            }

            @Override
            public void onFailure(Call<ShapeResponse> call, Throwable t) {
                Common.dismissDialog();
                Common.showToast(mContext, "Please try again later!!");
            }
        });

    }

}
