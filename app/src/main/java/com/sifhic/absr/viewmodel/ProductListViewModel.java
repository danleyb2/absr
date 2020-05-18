package com.sifhic.absr.viewmodel;

import android.app.Application;
import android.os.AsyncTask;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.SavedStateHandle;
import androidx.work.*;
import com.sifhic.absr.BasicApp;
import com.sifhic.absr.DataRepository;
import com.sifhic.absr.db.entity.GroupEntity;
import com.sifhic.absr.db.entity.ProductEntity;
import com.sifhic.absr.model.Group;
import com.sifhic.absr.model.Product;
import com.sifhic.absr.workers.ScrapeWorker;
import com.sifhic.absr.workers.WorkerUtils;

import java.util.List;

import static com.sifhic.absr.Constants.*;


public class ProductListViewModel extends AndroidViewModel {
    private static final String QUERY_KEY = "QUERY";

    private final SavedStateHandle mSavedStateHandler;
    private final DataRepository mRepository;
    private WorkManager mWorkManager;
    private LiveData<List<WorkInfo>> mSavedWorkInfo;

    private final LiveData<List<GroupEntity>> mGroups;

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

        mGroups = mRepository.getGroups();
    }

    /**
     * Getter method for mSavedWorkInfo
     */
    public LiveData<List<WorkInfo>> getOutputWorkInfo() {
        return mSavedWorkInfo;
    }

    public void refresh(List<ProductEntity> productEntityList){
        WorkContinuation continuation = null;

        // Add WorkRequests to blur the image the number of times requested
        for (Product product : productEntityList) {
            if (product.getAsin().isEmpty())continue;
            OneTimeWorkRequest.Builder blurBuilder = new OneTimeWorkRequest.Builder(ScrapeWorker.class);
            blurBuilder.setInputData(WorkerUtils.createInputDataForProduct(product));
            blurBuilder.addTag(TAG_OUTPUT); // This adds the tag

            if (continuation == null) {
                continuation = mWorkManager.beginUniqueWork(
                        SCRAPE_WORK_NAME,
                        ExistingWorkPolicy.REPLACE,
                        blurBuilder.build()
                );
            } else {
                continuation = continuation.then(blurBuilder.build());
            }

            // workRequests.add(blurBuilder.build());

        }
//
//                // Create charging constraint
//                Constraints constraints = new Constraints.Builder()
//                        .setRequiresCharging(true)
//                        .build();
//

        // Start the work
        if (continuation != null) continuation.enqueue();

    }

    public void refreshAll() {

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                List<ProductEntity> productEntityList = mRepository.loadProducts();
                ProductListViewModel.this.refresh(productEntityList);

            }
        });


    }


    public void refreshGroup(Group group) {

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {

                List<ProductEntity> productEntityList = mRepository.loadGroupProductsSync(group.getId());
                ProductListViewModel.this.refresh(productEntityList);

            }
        });
    }

    public void deleteGroup(Group group){
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                mRepository.deleteGroup(group.getId());
            }
        });

    }

    /**
     * Cancel work using the work's unique name
     */
    void cancelWork() {
        mWorkManager.cancelUniqueWork(SCRAPE_WORK_NAME);
    }

    /**
     * Expose the LiveData Products query so the UI can observe it.
     */
    public LiveData<List<GroupEntity>> getGroups() {
        return mGroups;
    }
}
