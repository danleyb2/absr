package com.sifhic.absr.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import com.sifhic.absr.BasicApp;
import com.sifhic.absr.DataRepository;
import com.sifhic.absr.db.entity.GroupEntity;
import com.sifhic.absr.db.entity.ProductEntity;

import java.util.List;

public class GroupViewModel extends AndroidViewModel {

    // private final LiveData<ProductEntity> mObservableProduct;
    private final LiveData<GroupEntity> mObservableGroup;
//    private WorkManager mWorkManager;
//    private LiveData<List<WorkInfo>> mSavedWorkInfo;

    private final long mProductId;

    private final LiveData<List<ProductEntity>> mObservableComments;

    public GroupViewModel(@NonNull Application application, DataRepository repository,
                          final long productId) {
        super(application);
        mProductId = productId;

//        mWorkManager = WorkManager.getInstance(application);

        // This transformation makes sure that whenever the current work Id changes the WorkInfo
        // the UI is listening to changes
//        mSavedWorkInfo = mWorkManager.getWorkInfosByTagLiveData(TAG_OUTPUT);


        mObservableComments = repository.loadProducts(mProductId);
        // mObservableProduct = repository.loadProduct(mProductId);
        mObservableGroup = repository.loadGroupLive(mProductId);
    }

    /**
     * Getter method for mSavedWorkInfo
     */
//    public LiveData<List<WorkInfo>> getOutputWorkInfo() {
//        return mSavedWorkInfo;
//    }


    /**
     * Expose the LiveData Comments query so the UI can observe it.
     */
    public LiveData<List<ProductEntity>> getComments() {
        return mObservableComments;
    }

    public LiveData<GroupEntity> getGroup() {
        return mObservableGroup;
    }




    /**
     * A creator is used to inject the product ID into the ViewModel
     * <p>
     * This creator is to showcase how to inject dependencies into ViewModels. It's not
     * actually necessary in this case, as the product ID can be passed in a public method.
     */
    public static class Factory extends ViewModelProvider.NewInstanceFactory {

        @NonNull
        private final Application mApplication;

        private final long mProductId;

        private final DataRepository mRepository;

        public Factory(@NonNull Application application, long productId) {
            mApplication = application;
            mProductId = productId;
            mRepository = ((BasicApp) application).getRepository();
        }

        @SuppressWarnings("unchecked")
        @Override
        @NonNull
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            return (T) new GroupViewModel(mApplication, mRepository, mProductId);
        }
    }
}
