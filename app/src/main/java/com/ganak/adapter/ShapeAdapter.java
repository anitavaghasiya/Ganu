package com.ganak.adapter;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.ganak.R;
import com.ganak.common.Common;
import com.ganak.fragment.AddInwardProductFragment;
import com.ganak.model.Shape;

import java.util.ArrayList;
import java.util.List;

public class ShapeAdapter extends RecyclerView.Adapter<ShapeAdapter.ProductViewHolder> implements Filterable {

    private Context mCtx;
    private List<Shape> productList;
    private List<Shape> shapeListFiltered;
    private String name_shape, name_shape_id;

    public ShapeAdapter(Context mCtx, List<Shape> productList, String name_shape) {
        this.mCtx = mCtx;
        this.productList = productList;
        this.shapeListFiltered = productList;
        this.name_shape = name_shape;
    }

    public ShapeAdapter(Context mCtx, List<Shape> productList, String name_shape, String name_shape_id) {
        this.mCtx = mCtx;
        this.productList = productList;
        this.shapeListFiltered = productList;
        this.name_shape = name_shape;
        this.name_shape_id = name_shape_id;
    }


    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.layout_recycler_view_shape, null);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, final int position) {
        final Shape product = shapeListFiltered.get(position);
        if (product.getName() != null && !product.getName().equals("")) {
            holder.textViewTitle.setText(product.getName());
        }
        holder.textViewTitle.setText(product.getName());
        holder.textViewTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                Fragment myFragment = new AddInwardProductFragment();
                Bundle bundle = new Bundle();
                bundle.putString("name", product.getName());
                bundle.putString("name_id", product.getId());
                bundle.putString("name_one", name_shape);
                bundle.putString("name_one_id", name_shape_id);
                bundle.putInt("postion", position);
                myFragment.setArguments(bundle);
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.frameContainer, myFragment).addToBackStack(null).commit();

            }
        });
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    shapeListFiltered = productList;
                } else {
                    List<Shape> filteredList = new ArrayList<>();
                    for (Shape row : productList) {
                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getName().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        } else {
                            Common.showToast(mCtx, "No Result Found");
                        }
                    }
                    shapeListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = shapeListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                shapeListFiltered = (ArrayList<Shape>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    @Override
    public int getItemCount() {
        return shapeListFiltered.size();
    }

    class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView textViewTitle;

        public ProductViewHolder(View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.tv_shape);
        }
    }
}
