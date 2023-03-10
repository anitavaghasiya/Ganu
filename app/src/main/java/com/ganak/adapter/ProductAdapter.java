package com.ganak.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.ganak.R;
import com.ganak.common.Common;
import com.ganak.model.Product;

import java.util.ArrayList;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> implements Filterable {

    private Context mCtx;
    private List<Product> productList;
    private List<Product> gradeListFiltered;

    public ProductAdapter(Context mCtx, List<Product> productList) {
        this.mCtx = mCtx;
        this.productList = productList;
        this.gradeListFiltered = productList;
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.product_item, null);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, final int position) {
        final Product product = gradeListFiltered.get(position);

        holder.tv_shape_name.setText(Html.fromHtml("<b>Shape :</b> " + product.getShapeName()));
        holder.tv_size_name.setText(Html.fromHtml("<b>Size :</b> " + product.getSizeName()));
        holder.tv_grade_name.setText(Html.fromHtml("<b>Grade :</b> " + product.getGradeName()));
        holder.tv_location_name.setText(Html.fromHtml("<b>Location :</b> " + product.getLocationName()));
        holder.txt_length.setText(product.getLength());
        holder.txt_pieces.setText(product.getPcs());
        holder.txt_weight.setText(product.getWeight());
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    gradeListFiltered = productList;
                } else {
                    List<Product> filteredList = new ArrayList<>();
                    for (Product row : productList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getShapeName().toLowerCase().contains(charString.toLowerCase()) ||
                                row.getGradeName().toLowerCase().contains(charString.toLowerCase()) ||
                                row.getSizeName().toLowerCase().contains(charString.toLowerCase()) ||
                                row.getLocationName().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        } else {
                            Common.showToast(mCtx, "No Result Found");
                        }
                    }

                    gradeListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = gradeListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                gradeListFiltered = (ArrayList<Product>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    @Override
    public int getItemCount() {
        Log.e("a", String.valueOf(productList.size()));
        return gradeListFiltered.size();
    }

    class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView tv_grade_name, tv_shape_name, tv_size_name, tv_location_name, txt_length, txt_weight, txt_pieces;

        ProductViewHolder(View itemView) {
            super(itemView);
            tv_grade_name = itemView.findViewById(R.id.tv_grade_name);
            tv_shape_name = itemView.findViewById(R.id.tv_shape_name);
            tv_size_name = itemView.findViewById(R.id.tv_size_name);
            tv_location_name = itemView.findViewById(R.id.tv_location_name);
            txt_length = itemView.findViewById(R.id.txt_length);
            txt_weight = itemView.findViewById(R.id.txt_weight);
            txt_pieces = itemView.findViewById(R.id.txt_pieces);
        }
    }

}