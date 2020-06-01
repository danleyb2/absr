package com.sifhic.absr;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import com.sifhic.absr.db.AppDatabase;
import com.sifhic.absr.db.entity.GroupEntity;
import com.sifhic.absr.db.entity.ProductEntity;
import com.sifhic.absr.model.Product;

import java.util.List;

/**
 * Repository handling the work with products and comments.
 */
public class DataRepository {

    private static DataRepository sInstance;

    private final AppDatabase mDatabase;
    private MediatorLiveData<List<GroupEntity>> mObservableProducts;

    private DataRepository(final AppDatabase database) {
        mDatabase = database;
        mObservableProducts = new MediatorLiveData<>();

        mObservableProducts.addSource(mDatabase.groupDao().loadAllGroups(),
                groupEntities -> {
                    if (mDatabase.getDatabaseCreated().getValue() != null) {
                        mObservableProducts.postValue(groupEntities);
                    }
                });
    }

    public static DataRepository getInstance(final AppDatabase database) {
        if (sInstance == null) {
            synchronized (DataRepository.class) {
                if (sInstance == null) {
                    sInstance = new DataRepository(database);
                }
            }
        }
        return sInstance;
    }

    // You must call this on a non-UI thread or your app will throw an exception. Room ensures
    // that you're not doing any long running operations on the main thread, blocking the UI.
    public void insertProduct(GroupEntity groupEntity, List<ProductEntity> productEntityList){
        mDatabase.getQueryExecutor().execute(() -> {

            mDatabase.groupDao().createGroupAndProducts(groupEntity,productEntityList) ;

        });
    }


    /**
     * Get the list of products from the database and get notified when the data changes.
     */
    public LiveData<List<GroupEntity>> getGroups() {
        return mObservableProducts;
    }

    public LiveData<ProductEntity> loadProduct(final long productId) {
        return mDatabase.productDao().loadProduct(productId);
    }

    public LiveData<GroupEntity> loadGroupLive(final long groupId) {
        return mDatabase.groupDao().loadGroup(groupId);
    }

    public ProductEntity loadProductSync(final long productId) {
        return mDatabase.productDao().loadProductSync(productId);
    }

    public List<ProductEntity> loadProducts() {
        return mDatabase.productDao().loadAllProductsSync();
    }

    public void updateProductSync(final long productId,String rank) {
        mDatabase.productDao().update(productId,rank);
    }

    public void updateProductSync(final long productId, boolean updated) {
        mDatabase.productDao().update(productId, updated);
    }

    public GroupEntity loadGroup(final long groupId) {
        return mDatabase.groupDao().loadGroupSync(groupId);
    }

    public LiveData<List<ProductEntity>> loadProducts(final long groupId) {
        return mDatabase.productDao().loadGroupProducts(groupId);
    }

    public List<ProductEntity> loadGroupProductsSync(final long groupId) {
        return mDatabase.productDao().loadGroupProductsSync(groupId);
    }

    public void deleteGroup(final long groupId) {
        mDatabase.groupDao().deleteGroup(groupId);
    }

}
