package com.sifhic.absr.db.entity;

import androidx.room.Entity;
import androidx.room.Fts4;
import androidx.room.PrimaryKey;
import com.sifhic.absr.model.Group;

@Entity(tableName = "groups")
public class GroupEntity implements Group {
  @PrimaryKey(autoGenerate = true)
  private long id;
  private String title;
  private String category;

  public GroupEntity(String title, String category) {
    this.title = title;
    this.category = category;
  }

  @Override
  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public String getCategory() {
    return category;
  }
}
