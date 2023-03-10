package com.ganak.fragment;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.ganak.R;
import com.ganak.common.PrefManager;


public class HomeFragment extends Fragment {

    private Context mContext;
    private PrefManager prefManager;
    private Button btn_inward, btn_outward;
    private Toolbar toolbar;

    public HomeFragment() {
        // Required empty public constructor
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        mContext = getActivity();

        toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle("Dashboard");


        prefManager = new PrefManager(mContext);

        btn_inward = view.findViewById(R.id.btn_inward);
        btn_outward = view.findViewById(R.id.btn_outward);

        btn_inward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.frameContainer, new GradesInwardFragment());
                ft.addToBackStack(null);
                ft.commit();
            }
        });

        btn_outward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.frameContainer, new GradesOutwardFragment());
                ft.addToBackStack (null);
                ft.commit();
            }
        });
        return view;
    }

}
