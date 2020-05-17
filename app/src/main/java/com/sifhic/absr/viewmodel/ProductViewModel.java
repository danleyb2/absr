package com.sifhic.absr.viewmodel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.annotation.NonNull;

import com.sifhic.absr.BasicApp;
import com.sifhic.absr.DataRepository;
import com.sifhic.absr.db.entity.CommentEntity;
import com.sifhic.absr.db.entity.ProductEntity;

import java.util.List;

public class ProductViewModel extends AndroidViewModel {

    private final LiveData<ProductEntity> mObservableProduct;

    private final long mProductId;

    private final LiveData<List<CommentEntity>> mObservableComments;

    public ProductViewModel(@NonNull Application application, DataRepository repository,
            final long productId) {
        super(application);
        mProductId = productId;

        mObservableComments = repository.loadComments(mProductId);
        mObservableProduct = repository.loadProduct(mProductId);
    }

    /**
     * Expose the LiveData Comments query so the UI can observe it.
     */
    public LiveData<List<CommentEntity>> getComments() {
        return mObservableComments;
    }

    public LiveData<ProductEntity> getProduct() {
        return mObservableProduct;
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
            return (T) new ProductViewModel(mApplication, mRepository, mProductId);
        }
    }
}
