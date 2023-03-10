package com.ganak.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.ganak.R;
import com.ganak.activity.LoginActivity;
import com.ganak.activity.MainActivity;
import com.ganak.common.Common;
import com.ganak.common.PrefFavoriteProduct;
import com.ganak.common.PrefManager;
import com.ganak.model.AddOutwardProductResponse;
import com.ganak.model.FavoriteProduct;
import com.ganak.model.Location;
import com.ganak.model.LocationResponse;
import com.ganak.model.Product;
import com.ganak.model.ProductResponse;
import com.ganak.model.Size;
import com.ganak.model.SizeResponse;
import com.ganak.rest.API;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class AddOutwardProductFragment extends Fragment {

    private Context mContext;
    private TextView tv_grade_name, tv_shape_name, txt_length, txt_pieces, txt_weight;
    private EditText et_length, et_pieces, et_weight;
    private PrefManager prefManager;
    private String name_shape, name_grade, num_length, num_pieces, num_weight, location, size, length, pieces, weight;
    private String shape_id = "", size_id = "", grade_id = "", location_id = "";

    private Spinner location_spinner, size_spinner;
    private List<Location> location_List;

    private List<Size> size_list;
    private List<Product> productList;

    private Fragment fragment;

    private TextView tv_no_data_found;
    private ImageView fav_img;
    private SwipeRefreshLayout swipe_refresh;
    private LinearLayout ll_main, lnl_add_new;
    private Button btn_submit;
    private Toolbar toolbar;
    private ScrollView scrollView;

    public AddOutwardProductFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate (R.layout.fragment_add_new_product, container, false);

        mContext = getActivity ();

        toolbar = (Toolbar) getActivity ().findViewById (R.id.toolbar);
        toolbar.setTitle ("Add Product");

        prefManager = new PrefManager (mContext);
        prefManager.removeData ();

        fragment = this;

        prefManager = new PrefManager (mContext);

        Bundle bundle = this.getArguments ();
        if (bundle != null) {
            name_grade = bundle.getString ("name_one");
            grade_id = bundle.getString ("name_one_id");
            name_shape = bundle.getString ("name");
            shape_id = bundle.getString ("name_id");
            Log.e ("name_grade", grade_id + " = " + name_grade);
            Log.e ("name_shape", shape_id + " = " + name_shape);
        }

        tv_shape_name = (TextView) view.findViewById (R.id.tv_shape_name);
        tv_grade_name = (TextView) view.findViewById (R.id.tv_grade_name);
        scrollView = view.findViewById (R.id.scroll_view);

        txt_length = (TextView) view.findViewById (R.id.txt_length);
        txt_pieces = (TextView) view.findViewById (R.id.txt_pieces);
        txt_weight = (TextView) view.findViewById (R.id.txt_weight);

        et_length = (EditText) view.findViewById (R.id.et_length);
        et_pieces = (EditText) view.findViewById (R.id.et_pieces);
        et_weight = (EditText) view.findViewById (R.id.et_weight);

        location_spinner = (Spinner) view.findViewById (R.id.sn_location);
        size_spinner = (Spinner) view.findViewById (R.id.sn_size);

        swipe_refresh = view.findViewById (R.id.swipe_refresh);
        tv_no_data_found = view.findViewById (R.id.tv_no_data_found);
        ll_main = view.findViewById (R.id.ll_main);

        btn_submit = view.findViewById (R.id.btn_submit);
        fav_img = view.findViewById (R.id.fav_img);

        length = et_length.getText ().toString ();
        pieces = et_pieces.getText ().toString ();
        weight = et_weight.getText ().toString ();

        Log.e ("txt_length", length);
        Log.e ("txt_pieces", pieces);
        Log.e ("txt_weight", weight);

        tv_grade_name.setText (name_grade);
        tv_shape_name.setText (name_shape);

        if (Common.isConnectingToInternet (mContext)) {
            scrollView.setVisibility (View.VISIBLE);
            btn_submit.setVisibility (View.VISIBLE);
            tv_no_data_found.setVisibility (View.GONE);
//            LoadProductData ();
            loadLocationData ();
            loadSizeData ();
        } else {
            scrollView.setVisibility (View.GONE);
            btn_submit.setVisibility (View.GONE);
            tv_no_data_found.setVisibility (View.VISIBLE);
            ll_main.setVisibility (View.GONE);
            tv_no_data_found.setVisibility (View.VISIBLE);
            tv_no_data_found.setText (mContext.getResources ().getString (R.string.no_internet_available));
        }

        swipe_refresh.setOnRefreshListener (new SwipeRefreshLayout.OnRefreshListener () {
            @Override
            public void onRefresh() {
                if (Common.isConnectingToInternet (mContext)) {
                    scrollView.setVisibility (View.VISIBLE);
                    btn_submit.setVisibility (View.VISIBLE);
                    tv_no_data_found.setVisibility (View.GONE);
                    LoadProductData ();
                    loadLocationData ();
                    loadSizeData ();
                    swipe_refresh.setRefreshing (false);
                } else {
                    scrollView.setVisibility (View.GONE);
                    btn_submit.setVisibility (View.GONE);
                    tv_no_data_found.setVisibility (View.VISIBLE);
                    tv_no_data_found.setText (mContext.getResources ().getString (R.string.no_internet_available));
                    swipe_refresh.setRefreshing (false);
                }
            }
        });


        location_spinner.setOnItemSelectedListener (new AdapterView.OnItemSelectedListener () {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                int pos = location_spinner.getSelectedItemPosition ();
                if (pos > 0) {
                    Location loc = location_List.get (location_spinner.getSelectedItemPosition () - 1);
                    if (loc != null) {
                        location = loc.getName ();
                        location_id = loc.getId ();
                        //  Common.showToast(mContext, location_id + " = " + loc.getName());
                        LoadProductData ();
                        favProduct ();
                    }
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // DO Nothing here
            }
        });

        size_spinner.setOnItemSelectedListener (new AdapterView.OnItemSelectedListener () {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                int pos = size_spinner.getSelectedItemPosition ();
                if (pos > 0) {
                    Size sz = size_list.get (size_spinner.getSelectedItemPosition () - 1);
                    if (sz != null) {
                        size = sz.getName ();
                        size_id = sz.getId ();
                        LoadProductData ();
                        favProduct ();
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // DO Nothing here
            }
        });

        btn_submit.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                if (Common.isConnectingToInternet (mContext)) {
                    /*num_length = et_length.getText().toString();
                    num_pieces = et_pieces.getText().toString();
                    num_weight = et_weight.getText().toString();
                    pieces = txt_pieces.getText().toString();
                    prefManager.setProductData(name_grade, name_shape, location, size, length, pieces, weight, "0", num_pieces, num_weight);
                    Common.errorLog("PRODUCT-XD", prefManager.getProductData());*/

                    length = et_length.getText ().toString ();
                    weight = et_weight.getText ().toString ();
                    pieces = et_pieces.getText ().toString ();

                    prefManager.setProductData (grade_id, shape_id, location_id, size_id, length, pieces, weight, "0", num_pieces, num_weight);
                    Common.errorLog ("PRODUCT-XD", prefManager.getProductData ());

                    if (!length.equals ("") && !weight.equals ("") && !pieces.equals ("") && !grade_id.equals ("") && !shape_id.equals ("") && !size_id.equals ("") && !location_id.equals ("")) {
                        outwardProduct ();
                    } else {
                        Common.showToast (mContext, "Please required data");
                    }
                } else {
                    Common.showToast (mContext, mContext.getResources ().getString (R.string.no_internet_available));
                }

            }
        });

        fav_img.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                if (size != null) {
                    if (location != null) {
                        FavoriteProduct favoriteProduct = new FavoriteProduct (shape_id, size_id, grade_id, location_id, name_shape,
                                name_grade, size, location, "", "", "");
                        if (PrefFavoriteProduct.checkOutwardFavourite (mContext, favoriteProduct)) {
                            fav_img.setImageResource (R.drawable.ic_star);
                            //Common.showToast(mContext,"Already Favorite");
                            Log.e ("FAV", "onClick: " + PrefFavoriteProduct.getOutwardFavorites (mContext));
                        } else {
                            fav_img.setImageResource (R.drawable.ic_star_border_black_24dp);
                            PrefFavoriteProduct.addOutwardFavorite (mContext, favoriteProduct);

                        }

                    } else {
                        Common.showToast (mContext, "Please Select location");
                    }
                } else {
                    Common.showToast (mContext, "Please Select Size");
                }
            }
        });

       /* lnl_add_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        */

        return view;
    }

    private void loadLocationData() {
        Common.showProgressDialog (mContext);
        API.LocationData ().getLocationData (prefManager.getToken (), prefManager.getRegId (), shape_id, grade_id).enqueue (new retrofit2.Callback<LocationResponse> () {
            @Override
            public void onResponse(Call<LocationResponse> call, Response<LocationResponse> response) {
                Common.errorLog ("locationResponse", response.toString () + "---");
                LocationResponse locationResponse = response.body ();
                if (locationResponse.getErrorCode () == 200) {
                    prefManager.addToken (locationResponse.getUser ().getToken () + "");
                    prefManager.setUserData (locationResponse.getUser ().getName (), locationResponse.getUser ().getEmail (), locationResponse.getUser ().getMobile (),
                            locationResponse.getUser ().getId (), locationResponse.getUser ().getOrganization (), locationResponse.getUser ().getDob ());
                    Log.i ("username", String.valueOf (locationResponse.getLocations ()));
                    location_List = locationResponse.getLocations ();
                    showListinSpinner ();
                } else {
                    if (locationResponse.getError () != null && !locationResponse.getError ().equals ("")) {
                        Common.showToast (mContext, locationResponse.getError () + "");
                        Intent Intent = new Intent (mContext, LoginActivity.class);
                        startActivity (Intent);
                        getActivity ().finish ();
                        prefManager.clearUserDetail ();
                    }
                }
                Common.dismissDialog ();
            }

            @Override
            public void onFailure(Call<LocationResponse> call, Throwable t) {
                Common.dismissDialog ();
                Common.showToast (mContext, "Please try again later!!");
            }
        });

    }

    private void showListinSpinner() {
        String[] items = new String[location_List.size ()];
        for (int i = 0; i < location_List.size (); i++) {
            //Storing names to string array
            items[i] = location_List.get (i).getName ();
        }
        ArrayAdapter<String> adapter;
        adapter = new ArrayAdapter<String> (mContext, R.layout.spinner_item, items);
       /* adapter = new ArrayAdapter<String>(mContext, R.layout.spinner_text_color, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);*/
        location_spinner.setAdapter (adapter);
    }

    //sizeSpinner
    private void loadSizeData() {
        Common.showProgressDialog (mContext);
        API.SizeData ().getSizeData (prefManager.getToken (), prefManager.getRegId (), grade_id, shape_id).enqueue (new retrofit2.Callback<SizeResponse> () {
            @Override
            public void onResponse(Call<SizeResponse> call, Response<SizeResponse> response) {
                SizeResponse sizeResponse = response.body ();
                if (sizeResponse.getErrorCode () == 200) {
                    prefManager.addToken (sizeResponse.getUser ().getToken () + "");
                    prefManager.setUserData (sizeResponse.getUser ().getName (), sizeResponse.getUser ().getEmail (), sizeResponse.getUser ().getMobile (),
                            sizeResponse.getUser ().getId (), sizeResponse.getUser ().getOrganization (), sizeResponse.getUser ().getDob ());
                    Log.i ("username", String.valueOf (sizeResponse.getSizes ()));
                    size_list = sizeResponse.getSizes ();
                    ShowListSizeSpinner ();
                } else {
                    if (sizeResponse.getError () != null && !sizeResponse.getError ().equals ("")) {
                        Common.showToast (mContext, sizeResponse.getError () + "");
                    }
                }
                Common.dismissDialog ();
            }

            @Override
            public void onFailure(Call<SizeResponse> call, Throwable t) {
                Common.dismissDialog ();
                Common.showToast (mContext, "Please try again later!!");
            }
        });
    }

    private void ShowListSizeSpinner() {
        String[] items = new String[size_list.size ()];
        for (int i = 0; i < size_list.size (); i++) {
            //Storing names to string array
            items[i] = size_list.get (i).getName ();
        }
        ArrayAdapter<String> adapter;
        adapter = new ArrayAdapter<String> (mContext, R.layout.spinner_item, items);
        // adapter = new ArrayAdapter<String>(mContext, R.layout.spinner_text_color, items);
        // adapter = new ArrayAdapter<String>(mContext, R.layout.spinner_drop_down, items);
        //setting adapter to spinner
        size_spinner.setPrompt ("Select Size");
        size_spinner.setAdapter (adapter);
    }

    //Method for load Productdata
    private void LoadProductData() {
        Common.showProgressDialog (mContext);
        API.ProductData ().getFilterProductData (prefManager.getToken (),
                prefManager.getRegId (),
                size_id,
                location_id,
                shape_id,
                grade_id).enqueue (new retrofit2.Callback<ProductResponse> () {
            @Override
            public void onResponse(Call<ProductResponse> call, Response<ProductResponse> response) {
                ProductResponse productResponse = response.body ();
                if (productResponse.getErrorCode () == 200) {
                    prefManager.addToken (productResponse.getUser ().getToken () + "");
                    prefManager.setUserData (productResponse.getUser ().getName (), productResponse.getUser ().getEmail (), productResponse.getUser ().getMobile (),
                            productResponse.getUser ().getId (), productResponse.getUser ().getOrganization (), productResponse.getUser ().getDob ());
                    Log.i ("username", String.valueOf (productResponse.getProducts ()));

                    productList = productResponse.getProducts ();
                    Log.e ("productList", productList.toString ());
                    String[] items = new String[productList.size ()];
                    if (productList != null && productList.size () > 0) {
                        for (int i = 0; i < productList.size (); i++) {
                            //Storing names to string array
                            // num_length=productList.get(i).
                            num_pieces = productList.get (i).getNumPcs ();
                            num_weight = productList.get (i).getNumWeight ();
                            num_length = productList.get (i).getNumLength ();

                            txt_pieces.setText (num_pieces);
                            txt_weight.setText (num_weight);
                            txt_length.setText (num_length);

                            Log.e ("pieces", num_pieces);
                            Log.e ("weight", num_weight);
                            Log.e ("length", num_length);
                        }
                    } else {
                        txt_pieces.setText ("0");
                        txt_weight.setText ("0");
                        txt_length.setText ("0");

                    }
                } else {
                    if (productResponse.getError () != null && !productResponse.getError ().equals ("")) {
                        Common.showToast (mContext, productResponse.getError () + "");
                    }
                }
                Common.dismissDialog ();
            }

            @Override
            public void onFailure(Call<ProductResponse> call, Throwable t) {
                Common.dismissDialog ();
                Common.showToast (mContext, "Please try again later!!");
            }
        });

    }

    private void outwardProduct() {
        Common.showProgressDialog (mContext);
        API.inwardData ().outwardProduct (prefManager.getToken (), prefManager.getRegId (), shape_id, grade_id, location_id, size_id, length, weight, pieces).enqueue (new retrofit2.Callback<AddOutwardProductResponse> () {
            @Override
            public void onResponse(Call<AddOutwardProductResponse> call, Response<AddOutwardProductResponse> response) {
                Common.errorLog ("locationResponse", response.toString () + "---");
                AddOutwardProductResponse addInwardProductResponse = response.body ();
                if (addInwardProductResponse.getErrorCode () == 200) {
                    prefManager.addToken (addInwardProductResponse.getUser ().getToken () + "");
                    prefManager.setUserData (addInwardProductResponse.getUser ().getName (), addInwardProductResponse.getUser ().getEmail (), addInwardProductResponse.getUser ().getMobile (),
                            addInwardProductResponse.getUser ().getId (), addInwardProductResponse.getUser ().getOrganization (), addInwardProductResponse.getUser ().getDob ());
                    Common.showToast (mContext, addInwardProductResponse.getError ());
                    mContext.startActivity (new Intent (mContext, MainActivity.class));
                } else {
                    if (addInwardProductResponse.getError () != null && !addInwardProductResponse.getError ().equals ("")) {
                        Common.showToast (mContext, addInwardProductResponse.getError () + "");
                    }
                }

                Common.dismissDialog ();
            }

            @Override
            public void onFailure(Call<AddOutwardProductResponse> call, Throwable t) {
                Common.dismissDialog ();
                Common.showToast (mContext, "Please try again later!!");
            }
        });

    }

    private void favProduct() {
        Log.e ("ID_XD", "onClick: " + shape_id + "--" + size_id + "--" + grade_id + "--" + location_id + "--" + name_shape + "--" + name_grade + "--" + size + "--" + location);
        if (PrefFavoriteProduct.checkOutwardFavourite (mContext, new FavoriteProduct (shape_id, size_id, grade_id, location_id,
                name_shape, name_grade, size, location, "", "", ""))) {
            fav_img.setImageResource (R.drawable.ic_star);
            Common.showToast (mContext, "fav");
        } else {
            fav_img.setImageResource (R.drawable.ic_star_border_black_24dp);
        }
    }
}