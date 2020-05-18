package com.sifhic.absr.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.sifhic.absr.R;
import com.sifhic.absr.databinding.GroupFragmentBinding;
import com.sifhic.absr.viewmodel.GroupViewModel;

public class GroupFragment extends Fragment {

    private static final String KEY_GROUP_ID = "product_id";

    private GroupFragmentBinding mBinding;

    private ProductAdapter mProductAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        // Inflate this data binding layout
        mBinding = DataBindingUtil.inflate(inflater, R.layout.group_fragment, container, false);

        // Create and set the adapter for the RecyclerView.
        mProductAdapter = new ProductAdapter(mProductClickCallback);
        mBinding.commentList.setAdapter(mProductAdapter);
        return mBinding.getRoot();
    }

    private final ProductClickCallback mProductClickCallback = comment -> {
        // no-op
    };

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        GroupViewModel.Factory factory = new GroupViewModel.Factory(
                requireActivity().getApplication(), requireArguments().getLong(KEY_GROUP_ID));

        final GroupViewModel model = new ViewModelProvider(this, factory)
                .get(GroupViewModel.class);

        mBinding.setLifecycleOwner(getViewLifecycleOwner());
        mBinding.setProductViewModel(model);

        subscribeToModel(model);
    }

    private void subscribeToModel(final GroupViewModel model) {
        // Observe comments
        model.getComments().observe(getViewLifecycleOwner(), commentEntities -> {
            if (commentEntities != null) {
                mBinding.setIsLoading(false);
                mProductAdapter.submitList(commentEntities);
            } else {
                mBinding.setIsLoading(true);
            }
        });
    }

    @Override
    public void onDestroyView() {
        mBinding = null;
        mProductAdapter = null;
        super.onDestroyView();
    }

    /** Creates product fragment for specific product ID */
    public static GroupFragment forProduct(long productId) {
        GroupFragment fragment = new GroupFragment();
        Bundle args = new Bundle();
        args.putLong(KEY_GROUP_ID, productId);
        fragment.setArguments(args);
        return fragment;
    }
}
