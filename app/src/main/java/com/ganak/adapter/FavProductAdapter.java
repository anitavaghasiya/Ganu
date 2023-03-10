package com.ganak.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.ganak.R;
import com.ganak.activity.FavoriteInwardProductActivity;
import com.ganak.activity.FavoriteOutwardProductActivity;
import com.ganak.common.Common;
import com.ganak.common.PrefFavoriteProduct;
import com.ganak.model.FavoriteProduct;

import java.util.List;

public class FavProductAdapter extends RecyclerView.Adapter<FavProductAdapter.ProductViewHolder> {
    private String isFrom;
    private Context mContext;
    private List<FavoriteProduct> favoriteProductList;
    private String weight, pieces, length;

    public FavProductAdapter(Context mContext, List<FavoriteProduct> favoriteProductList, String isFrom) {
        this.isFrom = isFrom;
        this.mContext = mContext;
        this.favoriteProductList = favoriteProductList;
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from (mContext);
        View view = inflater.inflate (R.layout.layout_favorite, null);
        return new ProductViewHolder (view);
    }

    @Override
    public void onBindViewHolder(final ProductViewHolder holder, final int position) {
//        Log.e("DATA", "onBindViewHolder: " + favoriteProductList.get(position));

        final FavoriteProduct product = favoriteProductList.get (position);

        if (isFrom.equals ("inward")) {

            holder.tv_shape_name.setText (Html.fromHtml ("<b>Shape :</b> " + product.getName_shape ()));
            holder.tv_size_name.setText (Html.fromHtml ("<b>Size :</b> " + product.getName_size ()));
            holder.tv_grade_name.setText (Html.fromHtml ("<b>Grade :</b> " + product.getName_grade ()));
            holder.tv_location_name.setText (Html.fromHtml ("<b>Location :</b> " + product.getName_location ()));

            holder.txt_length.setText(product.getLength());
            holder.txt_pieces.setText(product.getPieces());
            holder.txt_weight.setText(product.getWeight());

            holder.et_length.setOnFocusChangeListener (new View.OnFocusChangeListener () {
                public void onFocusChange(View v, boolean hasFocus) {
                    if (hasFocus)
                        holder.et_length.setHint ("");
                    else
                        holder.et_length.setHint ("0");
                }
            });

            holder.et_pieces.setOnFocusChangeListener (new View.OnFocusChangeListener () {
                public void onFocusChange(View v, boolean hasFocus) {
                    if (hasFocus)
                        holder.et_pieces.setHint ("");
                    else
                        holder.et_pieces.setHint ("0");
                }
            });

            holder.et_weight.setOnFocusChangeListener (new View.OnFocusChangeListener () {
                public void onFocusChange(View v, boolean hasFocus) {
                    if (hasFocus)
                        holder.et_weight.setHint ("");
                    else
                        holder.et_weight.setHint ("0");
                }
            });

            if (PrefFavoriteProduct.checkFavourite (mContext, new FavoriteProduct (product.getShape_id (),
                    product.getSize_id (), product.getGrade_id (),
                    product.getLocation_id (), product.getName_shape (),
                    product.getName_grade (), product.getName_size (),
                    product.getName_location (), "", "", ""))) {
                holder.img_fav.setImageResource (R.drawable.ic_star);
            } else {
                holder.img_fav.setImageResource (R.drawable.ic_star_border_black_24dp);
            }

            holder.img_fav.setOnClickListener (new View.OnClickListener () {
                @Override
                public void onClick(View view) {
                    if (mContext instanceof FavoriteInwardProductActivity) {
                        new AlertDialog.Builder (mContext)
                                .setTitle ("Remove from Favourites?")
                                .setMessage ("Do you want to remove from Favourites?")
                                .setPositiveButton ("Yes,Remove", new DialogInterface.OnClickListener () {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        favoriteProductList.remove (position);
                                        PrefFavoriteProduct.removeFavorite (mContext, position);
                                        notifyDataSetChanged ();
                                        ((FavoriteInwardProductActivity) mContext).getFavoriteInwardProduct ();
                                    }
                                })
                                .setNegativeButton ("No", null)
                                .show ();
                    }

                }
            });

            holder.btn_update.setOnClickListener (new View.OnClickListener () {
                @Override
                public void onClick(View v) {
                    length = holder.et_length.getText ().toString ();
                    weight = holder.et_weight.getText ().toString ();
                    pieces = holder.et_pieces.getText ().toString ();
                    if (Common.isConnectingToInternet (mContext)) {
                        ((FavoriteInwardProductActivity) mContext).updateInwardProduct (product.getShape_id (), product.getGrade_id (), product.getLocation_id (),
                                product.getSize_id (), length, weight, pieces);
                    }
                }
            });
        } else {
            holder.tv_shape_name.setText (Html.fromHtml ("<b>Shape :</b> " + product.getName_shape ()));
            holder.tv_size_name.setText (Html.fromHtml ("<b>Size :</b> " + product.getName_size ()));
            holder.tv_grade_name.setText (Html.fromHtml ("<b>Grade :</b> " + product.getName_grade ()));
            holder.tv_location_name.setText (Html.fromHtml ("<b>Location :</b> " + product.getName_location ()));

            holder.txt_length.setText (product.getLength ());
            holder.txt_pieces.setText (product.getPieces ());
            holder.txt_weight.setText (product.getWeight ());
            holder.et_length.setOnFocusChangeListener (new View.OnFocusChangeListener () {
                public void onFocusChange(View v, boolean hasFocus) {
                    if (hasFocus)
                        holder.et_length.setHint ("");
                    else
                        holder.et_length.setHint ("0");
                }
            });

            holder.et_pieces.setOnFocusChangeListener (new View.OnFocusChangeListener () {
                public void onFocusChange(View v, boolean hasFocus) {
                    if (hasFocus)
                        holder.et_pieces.setHint ("");
                    else
                        holder.et_pieces.setHint ("0");
                }
            });

            holder.et_weight.setOnFocusChangeListener (new View.OnFocusChangeListener () {
                public void onFocusChange(View v, boolean hasFocus) {
                    if (hasFocus)
                        holder.et_weight.setHint ("");
                    else
                        holder.et_weight.setHint ("0");
                }
            });
            if (PrefFavoriteProduct.checkOutwardFavourite (mContext, new FavoriteProduct (product.getShape_id (),
                    product.getSize_id (), product.getGrade_id (),
                    product.getLocation_id (), product.getName_shape (),
                    product.getName_grade (), product.getName_size (),
                    product.getName_location (), "", "", ""))) {
                holder.img_fav.setImageResource (R.drawable.ic_star);
            } else {
                holder.img_fav.setImageResource (R.drawable.ic_star_border_black_24dp);
            }

            holder.img_fav.setOnClickListener (new View.OnClickListener () {
                @Override
                public void onClick(View view) {
                    if (mContext instanceof FavoriteOutwardProductActivity) {
                        new AlertDialog.Builder (mContext)
                                .setTitle ("Remove from Favourites?")
                                .setMessage ("Do you want to remove from Favourites?")
                                .setPositiveButton ("Yes,Remove", new DialogInterface.OnClickListener () {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        PrefFavoriteProduct.removeOutwardFavorite (mContext, position);
                                        favoriteProductList.remove (position);
                                        notifyDataSetChanged ();
                                        ((FavoriteOutwardProductActivity) mContext).getFavoriteOutwardProdcut ();
                                    }
                                })
                                .setNegativeButton ("No", null)
                                .show ();
                    }

                }
            });

            holder.btn_update.setOnClickListener (new View.OnClickListener () {
                @Override
                public void onClick(View v) {
                    length = holder.et_length.getText ().toString ();
                    weight = holder.et_weight.getText ().toString ();
                    pieces = holder.et_pieces.getText ().toString ();
                    if (Common.isConnectingToInternet (mContext)) {
                        ((FavoriteOutwardProductActivity) mContext).updateOutwardProduct (product.getShape_id (), product.getGrade_id (), product.getLocation_id (),
                                product.getSize_id (), length, weight, pieces);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return favoriteProductList.size ();
    }

    class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView tv_grade_name, tv_shape_name, tv_size_name, tv_location_name, txt_length, txt_weight, txt_pieces, tv_no_data_found;
        ImageView img_fav;
        EditText et_length, et_weight, et_pieces;
        Button btn_update;

        private ProductViewHolder(View itemView) {
            super (itemView);
            tv_grade_name = itemView.findViewById (R.id.tv_grade_name);
            tv_shape_name = itemView.findViewById (R.id.tv_shape_name);
            tv_size_name = itemView.findViewById (R.id.tv_size_name);
            tv_location_name = itemView.findViewById (R.id.tv_location_name);
            txt_length = itemView.findViewById (R.id.txt_length);
            txt_weight = itemView.findViewById (R.id.txt_weight);
            txt_pieces = itemView.findViewById (R.id.txt_pieces);
            img_fav = itemView.findViewById (R.id.img_fav);
            tv_no_data_found = itemView.findViewById (R.id.tv_no_data_found);
            et_length = itemView.findViewById (R.id.et_length);
            et_weight = itemView.findViewById (R.id.et_weight);
            et_pieces = itemView.findViewById (R.id.et_pieces);
            btn_update = itemView.findViewById (R.id.btn_update);
        }
    }
}