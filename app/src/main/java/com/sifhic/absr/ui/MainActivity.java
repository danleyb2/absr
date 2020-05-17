package com.sifhic.absr.ui;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.sifhic.absr.R;
import com.sifhic.absr.model.Product;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        // Add product list fragment if this is first creation
        if (savedInstanceState == null) {
            ProductListFragment fragment = new ProductListFragment();

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, fragment, ProductListFragment.TAG).commit();
        }
    }

    /** Shows the product detail fragment */
    public void show(Product product) {

        ProductFragment productFragment = ProductFragment.forProduct(product.getId());

        getSupportFragmentManager()
                .beginTransaction()
                .addToBackStack("product")
                .replace(R.id.fragment_container,
                        productFragment, null).commit();
    }

    /** Shows the add product fragment */
    public void showAddProduct() {

        ProductsCreateFragment productsCreateFragment = ProductsCreateFragment.newInstance();

        getSupportFragmentManager()
                .beginTransaction()
                .addToBackStack("productsCreate")
                .replace(R.id.fragment_container,
                        productsCreateFragment, null).commit();
    }


}
