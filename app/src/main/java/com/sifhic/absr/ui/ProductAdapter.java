package com.sifhic.absr.ui;

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
                if (old.getRank() != comment.getRank())return false;
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
        return new ProductViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        holder.binding.setProduct(getItem(position));
        holder.binding.executePendingBindings();
    }

    static class ProductViewHolder extends RecyclerView.ViewHolder {

        final ProductItemBinding binding;

        public ProductViewHolder(ProductItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
