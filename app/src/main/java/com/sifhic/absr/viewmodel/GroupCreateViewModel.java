package com.sifhic.absr.viewmodel;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.lifecycle.*;
import com.sifhic.absr.BasicApp;
import com.sifhic.absr.Constants;
import com.sifhic.absr.DataRepository;
import com.sifhic.absr.db.entity.GroupEntity;
import com.sifhic.absr.db.entity.ProductEntity;
import com.sifhic.absr.model.Product;
import com.sifhic.absr.ui.ProductAdapter;

import java.util.ArrayList;
import java.util.List;

public class GroupCreateViewModel extends AndroidViewModel {
    private static final String TAG = GroupCreateViewModel.class.getSimpleName();
    private final DataRepository mRepository;
    private final MutableLiveData<GroupEntity> mObservableGroup;
    private final MutableLiveData<List<ProductEntity>> mObservableProducts;


    public GroupCreateViewModel(
            @NonNull Application application,
            @NonNull SavedStateHandle savedStateHandle) {
        super(application);
        mRepository = ((BasicApp) application).getRepository();

        // Initialize defaults
        mObservableGroup = new MutableLiveData<>();
        mObservableProducts = new MutableLiveData<>();

    }

    public void setGroupId(long groupId) {
            if (groupId > 0) {
                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        GroupEntity groupEntity = mRepository.loadGroup(groupId);
                        mObservableGroup.postValue( groupEntity);

                        List<ProductEntity> productEntityList = mRepository.loadGroupProductsSync(groupId);
                        mObservableProducts.postValue(productEntityList);
                    }
                });
            } else {
                // initialize defaults
                mObservableGroup.setValue(new GroupEntity("", Constants.DEFAULT_CATEGORY));

                List<ProductEntity> productEntityList = new ArrayList<>();
                for (int i = 1; i < Constants.GROUP_PRODUCTS_COUNT+1; i++) {
                    ProductEntity productEntity5 = new ProductEntity("Product " + i, "");
                    productEntityList.add(productEntity5);
                }
                mObservableProducts.setValue(productEntityList);
            }
    }


    public LiveData<GroupEntity> getGroup() {
        return mObservableGroup;
    }

    /**
     * Expose the LiveData Comments query so the UI can observe it.
     */
    public LiveData<List<ProductEntity>> getProducts() {
        return mObservableProducts;
    }


    public void save() {
        GroupEntity groupEntity = mObservableGroup.getValue();
        Log.i(TAG,groupEntity.getTitle()+" "+groupEntity.getCategory());

        if (groupEntity.getCategory().isEmpty())return;
        List<ProductEntity> productEntityList = mObservableProducts.getValue();

        /*

        if (!productAsin1.isEmpty()){
            ProductEntity productEntity1 = new ProductEntity(productLabel1, productAsin1);
            productEntityList.add(productEntity1);

        }
        if (!productAsin2.isEmpty()) {
            ProductEntity productEntity2 = new ProductEntity(productLabel2, productAsin2);
            productEntityList.add(productEntity2);
        }
        if (!productAsin3.isEmpty()) {
            ProductEntity productEntity3 = new ProductEntity(productLabel3, productAsin3);
            productEntityList.add(productEntity3);
        }

        if (!productAsin4.isEmpty()) {
            ProductEntity productEntity4 = new ProductEntity(productLabel4, productAsin4);
            productEntityList.add(productEntity4);
        }

        if (!productAsin5.isEmpty()) {
            ProductEntity productEntity5 = new ProductEntity(productLabel5, productAsin5);
            productEntityList.add(productEntity5);
        }

        if (!productAsin6.isEmpty()) {
            ProductEntity productEntity6 = new ProductEntity(productLabel6, productAsin6);
            productEntityList.add(productEntity6);
        }
        */
        if (productEntityList.isEmpty())return;
        mRepository.insertProduct(groupEntity, productEntityList);
    }


}
