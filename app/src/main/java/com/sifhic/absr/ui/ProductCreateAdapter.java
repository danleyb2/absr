package com.sifhic.absr.ui;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.AsyncDifferConfig;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import com.sifhic.absr.R;
import com.sifhic.absr.databinding.ProductItemCreateBinding;
import com.sifhic.absr.db.entity.ProductEntity;

public class ProductCreateAdapter extends ListAdapter<ProductEntity, ProductCreateAdapter.ProductCreateViewHolder> {


    ProductCreateAdapter() {
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

    }

    @Override
    @NonNull
    public ProductCreateViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ProductItemCreateBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()), R.layout.product_item_create,
                        parent, false);

        return new ProductCreateViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductCreateViewHolder holder, int position) {
        holder.binding.setProduct(getItem(position));
        holder.binding.executePendingBindings();
    }

    static class ProductCreateViewHolder extends RecyclerView.ViewHolder {

        final ProductItemCreateBinding binding;

        public ProductCreateViewHolder(ProductItemCreateBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
