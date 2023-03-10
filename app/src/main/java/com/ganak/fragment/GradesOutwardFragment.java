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
import com.ganak.adapter.GradesOutwardAdapter;
import com.ganak.common.Common;
import com.ganak.common.PrefManager;
import com.ganak.model.Grade;
import com.ganak.model.GradesResponse;
import com.ganak.rest.API;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;


public class GradesOutwardFragment extends Fragment {

    private Context mContext;
    private RecyclerView recyclerview_grades;
    private SwipeRefreshLayout swipe_refresh;
    private TextView tv_no_data_found, textView;
    private Grade grade;
    private List<Grade> gradeList;
    private GradesOutwardAdapter gradesOutwardAdapter;
    private LinearLayoutManager linearLayoutManager;
    private PrefManager prefManager;
    private String id, token;
    private GridLayoutManager gridLayoutManager;
    private Fragment fragment;
    private EditText etSearch;
    private Toolbar toolbar;

    public GradesOutwardFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_outward_grades, container, false);

        mContext = getActivity();

        prefManager = new PrefManager(mContext);

        toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle("Grades");

        swipe_refresh = view.findViewById(R.id.swipe_refresh);
        tv_no_data_found = view.findViewById(R.id.tv_no_data_found);
        recyclerview_grades = view.findViewById(R.id.recyclerview_grades);

        etSearch = view.findViewById(R.id.etSearch);

        gradeList = new ArrayList<>();

        if (Common.isConnectingToInternet(mContext)) {
            recyclerview_grades.setVisibility(View.VISIBLE);
            tv_no_data_found.setVisibility(View.GONE);
            getGradesData();
        } else {
            recyclerview_grades.setVisibility(View.GONE);
            tv_no_data_found.setVisibility(View.VISIBLE);
            tv_no_data_found.setText(mContext.getResources().getString(R.string.no_internet_available));
        }

        swipe_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (Common.isConnectingToInternet(mContext)) {
                    recyclerview_grades.setVisibility(View.VISIBLE);
                    tv_no_data_found.setVisibility(View.GONE);
                    gradeList = new ArrayList<>();
                    gradesOutwardAdapter = new GradesOutwardAdapter(mContext, gradeList);
                    recyclerview_grades.setAdapter(gradesOutwardAdapter);
                    getGradesData();
                    swipe_refresh.setRefreshing(false);
                } else {
                    recyclerview_grades.setVisibility(View.GONE);
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
                if (gradesOutwardAdapter == null)
                    return;
                gradesOutwardAdapter.getFilter().filter(etSearch.getText().toString().trim());
                if (gradesOutwardAdapter.getItemCount() < 1) {
                    recyclerview_grades.setVisibility(View.GONE);
                    tv_no_data_found.setVisibility(View.VISIBLE);
                } else {
                    recyclerview_grades.setVisibility(View.VISIBLE);
                    tv_no_data_found.setText(mContext.getResources().getText(R.string.tv_no_result_found));
                    tv_no_data_found.setVisibility(View.GONE);
                }
                //  gradesOutwardAdapter.getFilter().filter(etSearch.getText().toString().trim());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        return view;
    }

    private void getGradesData() {
        Common.showProgressDialog(mContext);
        API.GradeData().getGradesResponse(prefManager.getToken(), prefManager.getRegId()).enqueue(new retrofit2.Callback<GradesResponse>() {
            @Override
            public void onResponse(Call<GradesResponse> call, Response<GradesResponse> response) {
                Common.errorLog("GradesResponse", response.toString() + "---");
                GradesResponse gradesResponse = response.body();
                Log.e("GradesResponse", gradesResponse.toString());
                if (gradesResponse.getErrorCode() == 200) {
                    prefManager.addToken(gradesResponse.getUser().getToken() + "");
                    prefManager.setUserData(gradesResponse.getUser().getName(), gradesResponse.getUser().getEmail(), gradesResponse.getUser().getMobile(),
                            gradesResponse.getUser().getId(), gradesResponse.getUser().getOrganization(), gradesResponse.getUser().getDob());
                    Log.e("username", String.valueOf(gradesResponse.getGrades()));

                    if (gradesResponse.getGrades() != null) {
                        List<Grade> gradeList = gradesResponse.getGrades();
                        Log.e("shapeList", String.valueOf(gradesResponse.getGrades()));

                        if (gradeList != null && gradeList.size() > 0) {
                            recyclerview_grades.setVisibility(View.VISIBLE);
                            gradesOutwardAdapter = new GradesOutwardAdapter(mContext, gradeList);
                            gridLayoutManager = new GridLayoutManager(mContext, 1);
                            recyclerview_grades.setLayoutManager(gridLayoutManager);
                            recyclerview_grades.setItemAnimator(new DefaultItemAnimator());
                            //recyclerview_shape.setLayoutManager(new LinearLayoutManager(mContext));
                            recyclerview_grades.setAdapter(gradesOutwardAdapter);
                        } else {
                            recyclerview_grades.setVisibility(View.GONE);
                            tv_no_data_found.setVisibility(View.VISIBLE);
                            tv_no_data_found.setText(mContext.getResources().getString(R.string.no_data_found));
                        }
                    } else {
                        Common.showToast(mContext, "Data not found");
                    }

                } else {
                    if (gradesResponse.getError() != null && !gradesResponse.getError().equals("")) {
                        Common.showToast(mContext, gradesResponse.getError() + "");
                        Intent Intent = new Intent(mContext, LoginActivity.class);
                        startActivity(Intent);
                        getActivity().finish();
                        prefManager.clearUserDetail();
                    }
                }
                Common.dismissDialog();
            }

            @Override
            public void onFailure(Call<GradesResponse> call, Throwable t) {
                Common.dismissDialog();
                Common.showToast(mContext, "Please try again later!!");
            }
        });

    }
}
