package com.ganak.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ganak.R;
import com.ganak.adapter.FavProductAdapter;
import com.ganak.common.Common;
import com.ganak.common.PrefFavoriteProduct;
import com.ganak.common.PrefManager;
import com.ganak.model.AddInwardProductResponse;
import com.ganak.model.FavoriteProduct;
import com.ganak.model.Product;
import com.ganak.model.ProductResponse;
import com.ganak.rest.API;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class FavoriteInwardProductActivity extends AppCompatActivity {
    private Context mContext;
    private PrefManager prefManager;git init
    private android.support.v7.app.ActionBar actionBar;
    private List<FavoriteProduct> favoriteProductList;
    private RecyclerView rv_fav_product;
    private FavProductAdapter favProductAdapter;
    private SwipeRefreshLayout swipe_refresh;
    private TextView tv_no_data_found;
    private LinearLayout ll_main;
    private GridLayoutManager gridLayoutManager;
    private int isFrom = 1, i = 0;
    private String grade_id, shape_id, location_id, size_id;
    private List<FavoriteProduct> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_favorite_inward_product);

        mContext = this;
        prefManager = new PrefManager (mContext);
        data = new ArrayList<> ();

        actionBar = getSupportActionBar ();
        actionBar.setDisplayHomeAsUpEnabled (true);

        rv_fav_product = findViewById (R.id.rv_products);

        ll_main = findViewById (R.id.ll_main);
        tv_no_data_found = findViewById (R.id.tv_no_data_found);
        swipe_refresh = findViewById (R.id.swipe_refresh);

        favoriteProductList = new ArrayList<> ();
        if (Common.isConnectingToInternet (mContext)) {
            favoriteProductList = PrefFavoriteProduct.getFavorites (mContext);
            if (favoriteProductList != null && favoriteProductList.size () > 0) {
                Log.e ("data", "onCreate: " + favoriteProductList.size ());
                Common.showProgressDialog (mContext);
                getproductdata (i);
            } else {
                Common.dismissDialog ();
                rv_fav_product.setVisibility (View.GONE);
                tv_no_data_found.setVisibility (View.VISIBLE);
            }
        }
        swipe_refresh.setOnRefreshListener (new SwipeRefreshLayout.OnRefreshListener () {
            @Override
            public void onRefresh() {
                if (Common.isConnectingToInternet (mContext)) {
                    getFavoriteInwardProduct ();
                    swipe_refresh.setRefreshing (false);
                } else {
                    rv_fav_product.setVisibility (View.GONE);
                    tv_no_data_found.setVisibility (View.VISIBLE);
                    tv_no_data_found.setText (mContext.getResources ().getString (R.string.no_internet_available));
                    swipe_refresh.setRefreshing (false);
                }
            }
        });


    }

    public void getproductdata(int index) {
        final FavoriteProduct product = favoriteProductList.get (index);

        grade_id = product.getGrade_id ();
        size_id = product.getSize_id ();
        location_id = product.getLocation_id ();
        shape_id = product.getShape_id ();

        if (prefManager.getToken () != null && prefManager.getRegId () != null && grade_id != null && size_id != null && location_id != null && shape_id != null) {
            API.ProductData ().getFilterProductData (prefManager.getToken (), prefManager.getRegId (), size_id, location_id, shape_id, grade_id).enqueue (new retrofit2.Callback<ProductResponse> () {
                @Override
                public void onResponse(Call<ProductResponse> call, Response<ProductResponse> response) {
                    ProductResponse productResponse = response.body ();
                    if (productResponse.getErrorCode () == 200) {
                        prefManager.addToken (productResponse.getUser ().getToken () + "");
                        prefManager.setUserData (productResponse.getUser ().getName (), productResponse.getUser ().getEmail (), productResponse.getUser ().getMobile (),
                                productResponse.getUser ().getId (), productResponse.getUser ().getOrganization (), productResponse.getUser ().getDob ());
                        /*Log.i ("username", String.valueOf (productResponse.getProducts ()));
                        Log.e ("token", "token: " + prefManager.getToken ());
                        Log.e ("Reg_id", "reg_id: " + prefManager.getRegId ());
                        Log.e ("grade_id", "grade_id: " + grade_id + "----" + product.getName_grade ());
                        Log.e ("size_id", "size_id: " + size_id + "----" + product.getName_size ());
                        Log.e ("loction_id", "loction_id: " + location_id + "----" + product.getName_location ());
                        Log.e ("shape_id", "shape_id: " + shape_id + "----" + product.getName_shape ());*/

                        Common.errorLog ("DATAXDX -- " + i, grade_id + " -- " + shape_id + " -- " + location_id + " -- " + size_id);

                        List<Product> productList = productResponse.getProducts ();
                        if (productList != null && productList.size () > 0) {
                            for (int j = 0; j < productList.size (); j++) {
                                product.setLength (productList.get (j).getNumLength ());
                                product.setWeight (productList.get (j).getNumWeight ());
                                product.setPieces (productList.get (j).getNumPcs ());

                                /*Log.e ("et_length", "onResponse: " + productList.get (j).getNumLength ());
                                Log.e ("et_weight", "onResponse: " + productList.get (j).getNumWeight ());
                                Log.e ("et_pieces", "onResponse: " + productList.get (j).getNumPcs ());*/
                                Common.errorLog ("ITEMXDX -- " + i, product.getLength () + " -- " + product.getWeight () + " -- " + product.getPieces ());
                                data.add (product);
                            }
                        } else {
                            product.setLength ("0");
                            product.setWeight ("0");
                            product.setPieces ("0");
                            data.add (product);
                        }
                        if (favoriteProductList.size () > i) {
                            i++;
                            if (favoriteProductList.size () >= i + 1) {
                                getproductdata (i);
                            } else {
                                i = 0;
                                getFavoriteInwardProduct ();
                            }
                        }

                    } else {
                        if (productResponse.getError () != null && !productResponse.getError ().equals ("")) {
                            Common.showToast (mContext, productResponse.getError () + "");
                        }
                    }
                }

                @Override
                public void onFailure(Call<ProductResponse> call, Throwable t) {
                    Common.dismissDialog ();
                    Common.showToast (mContext, "Please try again later!!");
                    Log.e ("error", "onFailure: " + t.getMessage ());
                }
            });
        }
    }

    public void getFavoriteInwardProduct() {
        if (data != null && data.size () > 0) {
            Common.dismissDialog ();
            rv_fav_product.setVisibility (View.VISIBLE);
            tv_no_data_found.setVisibility (View.GONE);
            favProductAdapter = new FavProductAdapter (mContext, data, "inward");
            gridLayoutManager = new GridLayoutManager (mContext, 1);
            rv_fav_product.setLayoutManager (gridLayoutManager);
            rv_fav_product.setItemAnimator (new DefaultItemAnimator ());
            rv_fav_product.setAdapter (favProductAdapter);

        } else {
            Common.dismissDialog ();
            rv_fav_product.setVisibility (View.GONE);
            tv_no_data_found.setVisibility (View.VISIBLE);
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId ()) {
            case android.R.id.home:
                finish ();
                return true;
            default:
                return super.onOptionsItemSelected (item);
        }
    }

    public void updateInwardProduct(String shape_id, String grade_id, String location_id, String size_id, String length, String weight, String pieces) {
        Common.showProgressDialog (mContext);
        API.inwardData ().inwardProduct (prefManager.getToken (), prefManager.getRegId (), shape_id, grade_id, location_id, size_id, length, weight, pieces).enqueue (new retrofit2.Callback<AddInwardProductResponse> () {
            @Override
            public void onResponse(Call<AddInwardProductResponse> call, Response<AddInwardProductResponse> response) {
                Common.errorLog ("locationResponse", response.toString () + "---");
                AddInwardProductResponse addInwardProductResponse = response.body ();
                if (addInwardProductResponse.getErrorCode () == 200) {
                    prefManager.addToken (addInwardProductResponse.getUser ().getToken () + "");
                    prefManager.setUserData (addInwardProductResponse.getUser ().getName (), addInwardProductResponse.getUser ().getEmail (), addInwardProductResponse.getUser ().getMobile (),
                            addInwardProductResponse.getUser ().getId (), addInwardProductResponse.getUser ().getOrganization (), addInwardProductResponse.getUser ().getDob ());
                    Common.showToast (mContext, addInwardProductResponse.getError ());
                    data.clear ();
                    Common.showProgressDialog (mContext);
                    getproductdata (i);
                } else {
                    if (addInwardProductResponse.getError () != null && !addInwardProductResponse.getError ().equals ("")) {
                        Common.showToast (mContext, addInwardProductResponse.getError () + "");
                    }
                }

                Common.dismissDialog ();
            }

            @Override
            public void onFailure(Call<AddInwardProductResponse> call, Throwable t) {
                Common.dismissDialog ();
                Common.showToast (mContext, "Please try again later!!");
            }
        });

    }
}
