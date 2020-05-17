package com.sifhic.absr.viewmodel;

import android.app.Application;
import android.text.TextUtils;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.arch.core.util.Function;
import androidx.databinding.Bindable;
import androidx.databinding.ObservableField;
import androidx.lifecycle.*;
import com.sifhic.absr.BasicApp;
import com.sifhic.absr.DataRepository;
import com.sifhic.absr.db.entity.GroupEntity;
import com.sifhic.absr.db.entity.ProductEntity;
import com.sifhic.absr.model.Product;

import java.util.ArrayList;
import java.util.List;

public class ProductsCreateViewModel extends AndroidViewModel {
    private static final String TAG = ProductsCreateViewModel.class.getSimpleName();
    private final DataRepository mRepository;

    private String groupTitle = "";
    private String groupCategory =  "";

    private String productLabel1 =  "";
    private String productAsin1 =  "";
    private String productLabel2 =  "";
    private String productAsin2 =  "";
    private String productLabel3 =  "";
    private String productAsin3 =  "";

    public ProductsCreateViewModel(
            @NonNull Application application,
            @NonNull SavedStateHandle savedStateHandle) {
        super(application);
        mRepository = ((BasicApp) application).getRepository();

    }


    public String getGroupTitle() {
        return groupTitle;
    }

    public String getGroupCategory() {
        return groupCategory;
    }

    public void setGroupCategory(String groupCategory) {
        this.groupCategory = groupCategory;
    }

    public String getProductLabel1() {
        return productLabel1;
    }

    public void setProductLabel1(String productLabel1) {
        this.productLabel1 = productLabel1;
    }

    public String getProductAsin1() {
        return productAsin1;
    }

    public void setProductAsin1(String productAsin1) {
        this.productAsin1 = productAsin1;
    }

    public String getProductLabel2() {
        return productLabel2;
    }

    public void setProductLabel2(String productLabel2) {
        this.productLabel2 = productLabel2;
    }

    public void setProductAsin2(String productAsin2) {
        this.productAsin2 = productAsin2;
    }

    public String getProductAsin2() {
        return productAsin2;
    }

    public String getProductLabel3() {
        return productLabel3;
    }

    public void setProductLabel3(String productLabel3) {
        this.productLabel3 = productLabel3;
    }

    public String getProductAsin3() {
        return productAsin3;
    }

    public void setProductAsin3(String productAsin3) {
        this.productAsin3 = productAsin3;
    }

    public void setGroupTitle(String groupTitle) {
        this.groupTitle = groupTitle;
    }

    public void save() {
        Log.i(TAG,groupTitle+" "+groupCategory);

        if (groupCategory.isEmpty())return;
        GroupEntity groupEntity = new  GroupEntity(groupTitle,groupCategory);
        List<ProductEntity> productEntityList = new ArrayList<>();

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

        if (productEntityList.isEmpty())return;
        mRepository.insertProduct(groupEntity, productEntityList);

    }


}
