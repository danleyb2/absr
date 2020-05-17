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
import com.sifhic.absr.databinding.ProductsCreateFragmentBinding;
import com.sifhic.absr.db.entity.ProductEntity;
import com.sifhic.absr.model.Product;
import com.sifhic.absr.viewmodel.ProductViewModel;
import com.sifhic.absr.viewmodel.ProductsCreateViewModel;

public class ProductsCreateFragment extends Fragment {

    private ProductsCreateFragmentBinding mBinding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        // Inflate this data binding layout
        mBinding = DataBindingUtil.inflate(inflater, R.layout.products_create_fragment, container, false);
        return mBinding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view,savedInstanceState);
        final ProductsCreateViewModel viewModel =
                new ViewModelProvider(this).get(ProductsCreateViewModel.class);
        mBinding.setProductsCreateViewModel(viewModel);
        mBinding.productsSaveBtn.setOnClickListener(v ->{
           viewModel.save();
            getActivity().onBackPressed();

        });
    }

    @Override
    public void onDestroyView() {
        mBinding = null;
        super.onDestroyView();
    }

    /** Creates product fragment for specific product ID */
    public static ProductsCreateFragment newInstance() {
        ProductsCreateFragment fragment = new ProductsCreateFragment();
        Bundle args = new Bundle();
        // args.putInt(KEY_PRODUCT_ID, productId);
        fragment.setArguments(args);
        return fragment;
    }



}
