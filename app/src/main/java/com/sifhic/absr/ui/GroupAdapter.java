package com.sifhic.absr.ui;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
//import android.databinding.DataBindingUtil;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.sifhic.absr.databinding.GroupItemBinding;
import com.sifhic.absr.model.Group;
import com.sifhic.absr.model.Product;
import com.sifhic.absr.R;

import java.util.List;

public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.GroupViewHolder> {

    List<? extends Group> mGroupList;

    @Nullable
    private final GroupClickCallback mGroupClickCallback;

    public GroupAdapter(@Nullable GroupClickCallback clickCallback) {
        mGroupClickCallback = clickCallback;
        setHasStableIds(true);
    }

    public void setGroupList(final List<? extends Group> productList) {
        if (mGroupList == null) {
            mGroupList = productList;
            notifyItemRangeInserted(0, productList.size());
        } else {
            DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                @Override
                public int getOldListSize() {
                    return mGroupList.size();
                }

                @Override
                public int getNewListSize() {
                    return productList.size();
                }

                @Override
                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                    return mGroupList.get(oldItemPosition).getId() ==
                            productList.get(newItemPosition).getId();
                }

                @Override
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                    Group newProduct = productList.get(newItemPosition);
                    Group oldProduct = mGroupList.get(oldItemPosition);
                    return newProduct.getId() == oldProduct.getId()
                            && TextUtils.equals(newProduct.getTitle(), oldProduct.getTitle())
                            && TextUtils.equals(newProduct.getCategory(), oldProduct.getCategory());
                }
            });
            mGroupList = productList;
            result.dispatchUpdatesTo(this);
        }
    }

    @Override
    @NonNull
    public GroupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        GroupItemBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()), R.layout.group_item,
                        parent, false);
        binding.setCallback(mGroupClickCallback);
        return new GroupViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull GroupViewHolder holder, int position) {
        holder.binding.setGroup(mGroupList.get(position));
        holder.binding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return mGroupList == null ? 0 : mGroupList.size();
    }

    @Override
    public long getItemId(int position) {
        return mGroupList.get(position).getId();
    }

    static class GroupViewHolder extends RecyclerView.ViewHolder {

        final GroupItemBinding binding;

        public GroupViewHolder(GroupItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
