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
import com.ganak.fragment.ShapeInwardFragment;
import com.ganak.model.Grade;

import java.util.ArrayList;
import java.util.List;

public class GradesAdapter extends RecyclerView.Adapter<GradesAdapter.ProductViewHolder> implements Filterable {

    private Context mCtx;
    private List<Grade> productList;
    private List<Grade> gradeListFiltered;

    public GradesAdapter(Context mCtx, List<Grade> productList) {
        this.mCtx = mCtx;
        this.productList = productList;
        this.gradeListFiltered = productList;
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.layout_recycler_view_shape, null);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, final int position) {
        final Grade product = gradeListFiltered.get(position);
        if (product.getName() != null && !product.getName().equals("")) {
            holder.textViewTitle.setText(product.getName());
        }
        holder.textViewTitle.setText(product.getName());
        holder.textViewTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                Fragment myFragment = new ShapeInwardFragment();
                Bundle bundle = new Bundle();
                bundle.putString("name", product.getName());
                bundle.putString("name_id", product.getId());
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
                    gradeListFiltered = productList;
                } else {
                    List<Grade> filteredList = new ArrayList<>();
                    for (Grade row : productList) {
                        if (row.getName().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
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
                gradeListFiltered = (ArrayList<Grade>) filterResults.values;
                notifyDataSetChanged();
            }


        };
    }

    @Override
    public int getItemCount() {
        return gradeListFiltered.size();
    }

    class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView textViewTitle;

        public ProductViewHolder(View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.tv_shape);
        }
    }

}

