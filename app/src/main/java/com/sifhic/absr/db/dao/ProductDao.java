package com.sifhic.absr.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.*;
import com.sifhic.absr.db.entity.ProductEntity;
import com.sifhic.absr.model.Product;

import java.util.List;

@Dao
public interface ProductDao {
    @Query("SELECT * FROM products")
    LiveData<List<ProductEntity>> loadAllProducts();

    @Query("SELECT * FROM products")
    List<ProductEntity> loadAllProductsSync();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<ProductEntity> products);

    @Query("select * from products where id = :productId")
    LiveData<ProductEntity> loadProduct(long productId);

    @Query("select * from products where id = :productId")
    ProductEntity loadProductSync(long productId);

    /**
     * Updating only rank
     */
    @Query("UPDATE products SET rank=:rank WHERE id = :productId")
    void update(long productId, String rank);

    /**
     * Updating only updated
     */
    @Query("UPDATE products SET updated=:updated WHERE id = :productId")
    void update(long productId, boolean updated);

    // allowing the insert of the same word multiple times by passing a
    // conflict resolution strategy
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(ProductEntity product);


    @Query("SELECT * FROM products where groupId = :groupId")
    LiveData<List<ProductEntity>> loadGroupProducts(long groupId);


    @Query("SELECT * FROM products where groupId = :groupId")
    List<ProductEntity> loadGroupProductsSync(long groupId);


}
