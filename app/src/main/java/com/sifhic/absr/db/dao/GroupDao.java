package com.sifhic.absr.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.*;
import com.sifhic.absr.db.entity.GroupEntity;
import com.sifhic.absr.db.entity.ProductEntity;
import com.sifhic.absr.model.Group;

import java.util.List;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public abstract class GroupDao {
    @Query("SELECT * FROM groups")
  public abstract  LiveData<List<GroupEntity>> loadAllGroups();

    @Insert(onConflict = REPLACE)
    public abstract  void insertAll(List<GroupEntity> groups);

    @Query("select * from groups where id = :groupId")
    public abstract  LiveData<GroupEntity> loadGroup(long groupId);

    // If the @Insert method receives only 1 parameter, it can return a long,
    // which is the new rowId for the inserted item.
    // https://developer.android.com/training/data-storage/room/accessing-data
    @Insert(onConflict = REPLACE)
    public abstract long insert(GroupEntity groupEntity);

    @Query("select * from groups where id = :groupId")
    public abstract GroupEntity loadGroupSync(long groupId);

    @Insert
    public abstract void insert(ProductEntity productEntity);

    @Transaction
    public long createGroupAndProducts(GroupEntity groupEntity, List<ProductEntity> productEntityList){
     final long groupId = insert(groupEntity);
        // Set companyId for all related employeeEntities
        for (ProductEntity productEntity : productEntityList) {
            productEntity.setGroupId(groupId);
            insert(productEntity);
        }
     return groupId;
    }

}
