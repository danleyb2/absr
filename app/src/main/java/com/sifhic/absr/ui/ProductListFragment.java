package com.sifhic.absr.ui;

import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import androidx.work.Data;
import androidx.work.WorkInfo;
import com.sifhic.absr.Constants;
import com.sifhic.absr.R;
import com.sifhic.absr.databinding.ListFragmentBinding;
import com.sifhic.absr.db.entity.ProductEntity;
import com.sifhic.absr.viewmodel.ProductListViewModel;

import java.util.List;

public class ProductListFragment extends Fragment {

    public static final String TAG = "ProductListFragment";

    private ProductAdapter mProductAdapter;

    private ListFragmentBinding mBinding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {


        mBinding = DataBindingUtil.inflate(inflater, R.layout.list_fragment, container, false);
        mProductAdapter = new ProductAdapter(mProductClickCallback);
        mBinding.productsList.setAdapter(mProductAdapter);
        mBinding.setCallback(mAddProductsClickCallback);

        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final ProductListViewModel viewModel =
                new ViewModelProvider(this).get(ProductListViewModel.class);


        mBinding.productsRefreshBtn.setOnClickListener(v -> {
            viewModel.refresh();
        });

        viewModel.getOutputWorkInfo().observe(getViewLifecycleOwner(), listOfWorkInfo -> {

            Log.i(TAG, "listOfWorkInfo");
            Log.i(TAG, listOfWorkInfo.toString());

            // If there are no matching work info, do nothing
            if (listOfWorkInfo == null || listOfWorkInfo.isEmpty()) {
                return;
            }

            // We only care about the first output status.
            // Every continuation has only one worker tagged TAG_OUTPUT
            WorkInfo workInfo = listOfWorkInfo.get(0);

            boolean finished = workInfo.getState().isFinished();
            if (!finished) {
                mBinding.setIsLoading(true);
                showWorkInProgress();
            } else {
                mBinding.setIsLoading(false);
                showWorkFinished();
            }
        });

        subscribeUi(viewModel.getProducts());
    }

    private void showWorkInProgress(){
        mBinding.productsRefreshBtn.setEnabled(false);
        mBinding.productsRefreshBtn.setText("Refreshing...");
    }

    private void showWorkFinished(){
        mBinding.productsRefreshBtn.setEnabled(true);
        mBinding.productsRefreshBtn.setText("Refresh");
    }

    private void subscribeUi(LiveData<List<ProductEntity>> liveData) {
        // Update the list when the data changes
        liveData.observe(getViewLifecycleOwner(), myProducts -> {
            if (myProducts != null) {
                mBinding.setIsLoading(false);
                mProductAdapter.setProductList(myProducts);
            } else {
                mBinding.setIsLoading(true);
            }
            // espresso does not know how to wait for data binding's loop so we execute changes
            // sync.
            mBinding.executePendingBindings();
        });
    }

    @Override
    public void onDestroyView() {
        mBinding = null;
        mProductAdapter = null;
        super.onDestroyView();
    }

    private final ProductClickCallback mProductClickCallback = product -> {
        if (getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.STARTED)) {
           // TODO ((MainActivity) requireActivity()).show(product);
        }
    };
    private final AddProductsClickCallback mAddProductsClickCallback = () -> {
        if (getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.STARTED)) {
            ((MainActivity) requireActivity()).showAddProduct();
        }
    };
}
