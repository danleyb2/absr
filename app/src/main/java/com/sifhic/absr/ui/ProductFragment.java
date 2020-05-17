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
import com.sifhic.absr.databinding.ProductFragmentBinding;
import com.sifhic.absr.viewmodel.ProductViewModel;

public class ProductFragment extends Fragment {

    private static final String KEY_PRODUCT_ID = "product_id";

    private ProductFragmentBinding mBinding;

    private CommentAdapter mCommentAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        // Inflate this data binding layout
        mBinding = DataBindingUtil.inflate(inflater, R.layout.product_fragment, container, false);

        // Create and set the adapter for the RecyclerView.
        mCommentAdapter = new CommentAdapter(mCommentClickCallback);
        mBinding.commentList.setAdapter(mCommentAdapter);
        return mBinding.getRoot();
    }

    private final CommentClickCallback mCommentClickCallback = comment -> {
        // no-op
    };

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ProductViewModel.Factory factory = new ProductViewModel.Factory(
                requireActivity().getApplication(), requireArguments().getLong(KEY_PRODUCT_ID));

        final ProductViewModel model = new ViewModelProvider(this, factory)
                .get(ProductViewModel.class);

        mBinding.setLifecycleOwner(getViewLifecycleOwner());
        mBinding.setProductViewModel(model);

        subscribeToModel(model);
    }

    private void subscribeToModel(final ProductViewModel model) {
        // Observe comments
        model.getComments().observe(getViewLifecycleOwner(), commentEntities -> {
            if (commentEntities != null) {
                mBinding.setIsLoading(false);
                mCommentAdapter.submitList(commentEntities);
            } else {
                mBinding.setIsLoading(true);
            }
        });
    }

    @Override
    public void onDestroyView() {
        mBinding = null;
        mCommentAdapter = null;
        super.onDestroyView();
    }

    /** Creates product fragment for specific product ID */
    public static ProductFragment forProduct(long productId) {
        ProductFragment fragment = new ProductFragment();
        Bundle args = new Bundle();
        args.putLong(KEY_PRODUCT_ID, productId);
        fragment.setArguments(args);
        return fragment;
    }
}
