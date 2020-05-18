package com.sifhic.absr.ui;

import android.os.Bundle;
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
import androidx.work.WorkInfo;
import com.sifhic.absr.R;
import com.sifhic.absr.databinding.ListFragmentBinding;
import com.sifhic.absr.db.entity.GroupEntity;
import com.sifhic.absr.model.Group;
import com.sifhic.absr.viewmodel.ProductListViewModel;

import java.util.List;

public class ProductListFragment extends Fragment {

    public static final String TAG = "ProductListFragment";

    private GroupAdapter mGroupAdapter;
    private ProductListViewModel viewModel;
    private ListFragmentBinding mBinding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {


        mBinding = DataBindingUtil.inflate(inflater, R.layout.list_fragment, container, false);
        mGroupAdapter = new GroupAdapter(mGroupClickCallback);
        mBinding.productsList.setAdapter(mGroupAdapter);
        mBinding.setCallback(mAddProductsClickCallback);

        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this).get(ProductListViewModel.class);


        mBinding.productsRefreshBtn.setOnClickListener(v -> {
            viewModel.refreshAll();
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

        subscribeUi(viewModel.getGroups());
    }

    private void showWorkInProgress(){
        mBinding.productsRefreshBtn.setEnabled(false);
        mBinding.productsRefreshBtn.setText(R.string.refreshing);
    }

    private void showWorkFinished(){
        mBinding.productsRefreshBtn.setEnabled(true);
        mBinding.productsRefreshBtn.setText(R.string.refresh_all);
    }

    private void subscribeUi(LiveData<List<GroupEntity>> liveData) {
        // Update the list when the data changes
        liveData.observe(getViewLifecycleOwner(), myProducts -> {
            if (myProducts != null) {
                mBinding.setIsLoading(false);
                mGroupAdapter.setGroupList(myProducts);
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
        mGroupAdapter = null;
        super.onDestroyView();
    }



    private final GroupClickCallback mGroupClickCallback = new GroupClickCallback() {
        @Override
        public void onClick(Group group) {
            if (getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.STARTED)) {
                ((MainActivity) requireActivity()).show(group);
            }
        }

        @Override
        public void onRefreshClick(Group group) {
            viewModel.refreshGroup(group);
        }

        @Override
        public void onEditClick(Group group) {
            if (getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.STARTED)) {
                ((MainActivity) requireActivity()).showEditProduct(group);
            }
        }

        @Override
        public void onDeleteClick(Group group) {
            viewModel.deleteGroup(group);
        }
    };


    private final AddProductsClickCallback mAddProductsClickCallback = () -> {
        if (getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.STARTED)) {
            ((MainActivity) requireActivity()).showAddProduct();
        }
    };
}
