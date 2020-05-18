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
import com.sifhic.absr.databinding.GroupCreateFragmentBinding;
import com.sifhic.absr.viewmodel.GroupCreateViewModel;

public class GroupCreateFragment extends Fragment {
    private static final String KEY_GROUP_ID = "group_id";

    private GroupCreateFragmentBinding mBinding;
    private ProductCreateAdapter productCreateAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        // Inflate this data binding layout
        mBinding = DataBindingUtil.inflate(inflater, R.layout.group_create_fragment, container, false);

        productCreateAdapter = new ProductCreateAdapter();
        mBinding.productCreateList.setAdapter(productCreateAdapter);

        return mBinding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view,savedInstanceState);
        final GroupCreateViewModel viewModel =
                new ViewModelProvider(this).get(GroupCreateViewModel.class);

        long groupId = requireArguments().getLong(KEY_GROUP_ID,0);
        viewModel.setGroupId(groupId);

        // mBinding.setLifecycleOwner(getViewLifecycleOwner());
        mBinding.setProductsCreateViewModel(viewModel);

        mBinding.productsSaveBtn.setOnClickListener(v ->{
           viewModel.save();
            getActivity().onBackPressed();
        });
        subscribeToModel(viewModel);
    }


    private void subscribeToModel(final GroupCreateViewModel model) {
        // Observe Group
        model.getGroup().observe(getViewLifecycleOwner(), groupEntity -> {
            if (groupEntity != null) {
                mBinding.setGroup(groupEntity);
            } else {
                // mBinding.setIsLoading(true);
            }
        });

        // Observe products
        model.getProducts().observe(getViewLifecycleOwner(), commentEntities -> {
            if (commentEntities != null) {
                // mBinding.setIsLoading(false);
                productCreateAdapter.submitList(commentEntities);
            } else {
                // mBinding.setIsLoading(true);
            }
        });
    }


    @Override
    public void onDestroyView() {
        mBinding = null;
        super.onDestroyView();
    }

    /** Creates product fragment for specific product ID */
    public static GroupCreateFragment newInstance() {
        GroupCreateFragment fragment = new GroupCreateFragment();
        Bundle args = new Bundle();
        // args.putInt(KEY_PRODUCT_ID, productId);
        fragment.setArguments(args);
        return fragment;
    }

    /** Creates product fragment for specific product ID */
    public static GroupCreateFragment forGroup(long groupId) {
        GroupCreateFragment fragment = new GroupCreateFragment();
        Bundle args = new Bundle();
        args.putLong(KEY_GROUP_ID, groupId);
        fragment.setArguments(args);
        return fragment;
    }


}
