package com.sifhic.absr.viewmodel;

import android.app.Application;
import android.text.TextUtils;
import androidx.annotation.NonNull;
import androidx.arch.core.util.Function;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.Transformations;
import androidx.work.*;
import com.sifhic.absr.BasicApp;
import com.sifhic.absr.DataRepository;
import com.sifhic.absr.db.entity.ProductEntity;
import com.sifhic.absr.model.Product;
import com.sifhic.absr.workers.ScrapeWorker;

import java.util.List;

import static com.sifhic.absr.Constants.*;


public class ProductListViewModel extends AndroidViewModel {
    private static final String QUERY_KEY = "QUERY";

    private final SavedStateHandle mSavedStateHandler;
    private final DataRepository mRepository;
    private WorkManager mWorkManager;
    private LiveData<List<WorkInfo>> mSavedWorkInfo;

    private final LiveData<List<ProductEntity>> mProducts;

    public ProductListViewModel(@NonNull Application application,
                                @NonNull SavedStateHandle savedStateHandle) {
        super(application);
        mSavedStateHandler = savedStateHandle;
        mRepository = ((BasicApp) application).getRepository();
        mWorkManager = WorkManager.getInstance(application);

        // This transformation makes sure that whenever the current work Id changes the WorkInfo
        // the UI is listening to changes
        mSavedWorkInfo = mWorkManager.getWorkInfosByTagLiveData(TAG_OUTPUT);

        // Use the savedStateHandle.getLiveData() as the input to switchMap,
        // allowing us to recalculate what LiveData to get from the DataRepository
        // based on what query the user has entered

        mProducts = mRepository.getProducts();
    }

    /**
     * Getter method for mSavedWorkInfo
     */
    public LiveData<List<WorkInfo>> getOutputWorkInfo() {
        return mSavedWorkInfo;
    }


    /**
     * Creates the input data bundle which includes the Uri to operate on
     *
     * @return Data which contains the Image Uri as a String
     */
    private Data createInputDataForProduct(Product product) {
        Data.Builder builder = new Data.Builder();
        builder.putLong(KEY_IMAGE_URI, product.getId());

        return builder.build();
    }

    public void refresh() {

        WorkContinuation continuation = null;
        // List<OneTimeWorkRequest> workRequests =  new ArrayList<>();

        // Add WorkRequests to blur the image the number of times requested
        for (Product product : mRepository.getProducts().getValue()) {
            OneTimeWorkRequest.Builder blurBuilder = new OneTimeWorkRequest.Builder(ScrapeWorker.class);
            blurBuilder.setInputData(createInputDataForProduct(product));
            blurBuilder.addTag(TAG_OUTPUT); // This adds the tag

            if (continuation == null) {
                continuation = mWorkManager.beginUniqueWork(
                        IMAGE_MANIPULATION_WORK_NAME,
                        ExistingWorkPolicy.REPLACE,
                        blurBuilder.build()
                );
            } else {
                continuation = continuation.then(blurBuilder.build());
            }

            // workRequests.add(blurBuilder.build());

        }

        // Create charging constraint
        Constraints constraints = new Constraints.Builder()
                .setRequiresCharging(true)
                .build();


        // Start the work
        if (continuation != null) continuation.enqueue();

    }

    /**
     * Cancel work using the work's unique name
     */
    void cancelWork() {
        mWorkManager.cancelUniqueWork(IMAGE_MANIPULATION_WORK_NAME);
    }

    /**
     * Expose the LiveData Products query so the UI can observe it.
     */
    public LiveData<List<ProductEntity>> getProducts() {
        return mProducts;
    }
}
