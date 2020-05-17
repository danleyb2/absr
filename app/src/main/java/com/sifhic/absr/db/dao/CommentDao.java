package com.sifhic.absr.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import com.sifhic.absr.db.entity.CommentEntity;

import java.util.List;

@Dao
public interface CommentDao {
    @Query("SELECT * FROM comments where productId = :productId")
    LiveData<List<CommentEntity>> loadComments(long productId);

    @Query("SELECT * FROM comments where productId = :productId")
    List<CommentEntity> loadCommentsSync(int productId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<CommentEntity> comments);
}
