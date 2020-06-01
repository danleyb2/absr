package com.sifhic.absr.ui;

import android.content.Context;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import androidx.recyclerview.widget.AsyncDifferConfig;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.ViewGroup;


import com.sifhic.absr.databinding.ProductItemBinding;

import com.sifhic.absr.R;
import com.sifhic.absr.db.entity.ProductEntity;
import com.sifhic.absr.model.Product;

public class ProductAdapter extends ListAdapter<ProductEntity, ProductAdapter.ProductViewHolder> {

    @Nullable
    private final ProductClickCallback mProductClickCallback;

    ProductAdapter(@Nullable ProductClickCallback productClickCallback) {
        super(new AsyncDifferConfig.Builder<>(new DiffUtil.ItemCallback<ProductEntity>() {
            @Override
            public boolean areItemsTheSame(@NonNull ProductEntity old,
                    @NonNull ProductEntity comment) {
                return old.getId() == comment.getId();
            }

            @Override
            public boolean areContentsTheSame(@NonNull ProductEntity old,
                    @NonNull ProductEntity comment) {
                if (old.getId() != comment.getId())return false;
                if (!old.getAsin().equals(comment.getAsin()))return false;
                if (!TextUtils.equals( old.getRank(),comment.getRank()))return false;
                if (!TextUtils.equals(old.getLabel(), comment.getLabel()))return false;
                if (old.getUpdatedAt() != null && comment.getUpdatedAt() == null || old.getUpdatedAt() == null && comment.getUpdatedAt() != null){
                    return false ;
                }
                return old.getUpdatedAt().equals(comment.getUpdatedAt());

            }
        }).build());
        mProductClickCallback = productClickCallback;
    }

    @Override
    @NonNull
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ProductItemBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()), R.layout.product_item,
                        parent, false);
        binding.setCallback(mProductClickCallback);
        return new ProductViewHolder(binding,parent.getContext());
    }

    public static void justifyListViewHeightBasedOnChildren (ListView listView) {

        android.widget.ListAdapter adapter = listView.getAdapter();

        if (adapter == null) {
            return;
        }
        ViewGroup vg = listView;
        int totalHeight = 0;
        for (int i = 0; i < adapter.getCount(); i++) {
            View listItem = adapter.getView(i, null, vg);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams par = listView.getLayoutParams();
        par.height = totalHeight + (listView.getDividerHeight() * (adapter.getCount() - 1));
        listView.setLayoutParams(par);
        listView.requestLayout();
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = getItem(position);
        holder.binding.setProduct(product);

        String[] items = null;
        if (product.getRank()!=null){
            items = product.getRank().split("\\|");
        }else {
            items = new String[]{};
        }
        ArrayAdapter adapter = new ArrayAdapter<>(holder.context,R.layout.activity_listview , items);

        holder.binding.listView.setAdapter(adapter);
        justifyListViewHeightBasedOnChildren(holder.binding.listView);
        holder.binding.executePendingBindings();
    }

    static class ProductViewHolder extends RecyclerView.ViewHolder {

        final ProductItemBinding binding;
        final Context context;

        public ProductViewHolder(ProductItemBinding binding, Context context) {
            super(binding.getRoot());
            this.binding = binding;
            this.context = context;
        }
    }
}
