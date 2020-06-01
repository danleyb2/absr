package com.sifhic.absr.workers;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;
import androidx.annotation.NonNull;

import androidx.work.Worker;
import androidx.work.WorkerParameters;
import com.sifhic.absr.BasicApp;
import com.sifhic.absr.DataRepository;
import com.sifhic.absr.db.entity.GroupEntity;
import com.sifhic.absr.db.entity.ProductEntity;
import com.sifhic.absr.scrapper.Amazon;

import java.util.ArrayList;

import static com.sifhic.absr.Constants.KEY_PRODUCT_ID;


public class ScrapeWorker  extends Worker {
    private static final String TAG = ScrapeWorker.class.getSimpleName();

    public ScrapeWorker(
            @NonNull Context appContext,
            @NonNull WorkerParameters workerParams) {
        super(appContext, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {

        Context applicationContext = getApplicationContext();

         DataRepository repository = ((BasicApp) applicationContext).getRepository();

        long productId = getInputData().getLong(KEY_PRODUCT_ID,0);
        // Makes a notification when the work starts and slows down the work so that it's easier to
        // see each WorkRequest start, even on emulated devices
        WorkerUtils.makeStatusNotification("Refreshing product: "+productId, applicationContext);

        ProductEntity product = repository.loadProductSync(productId);
        GroupEntity group = repository.loadGroup(product.getGroupId());

        try {
            Log.i(TAG,"Refreshing Product: "+productId);
            ArrayList<String> bsRanks = Amazon.bestSellerRanks(product.getAsin());
            if (bsRanks.isEmpty()){
                repository.updateProductSync(productId,false);
            }else {
                if (bsRanks.isEmpty()){
                    repository.updateProductSync(productId,false);
                }else {
                    product.setRank(TextUtils.join("|",bsRanks));
                    repository.updateProductSync(productId,product.getRank());
                    repository.updateProductSync(productId,true);
                }
            }

        } catch (Throwable throwable) {
            // Technically WorkManager will return Result.failure()
            // but it's best to be explicit about it.
            // Thus if there were errors, we're return FAILURE
            Log.e(TAG, "Error scrapping Amazon", throwable);
            repository.updateProductSync(productId,false);
            return Result.failure();
        }
        return Result.success();
    }

}
