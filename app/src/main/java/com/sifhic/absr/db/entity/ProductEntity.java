package com.sifhic.absr.db.entity;

import androidx.annotation.Nullable;
import androidx.room.*;
import com.sifhic.absr.model.Product;

import java.util.Date;

@Entity(tableName = "products",
        foreignKeys = {
                @ForeignKey(entity = GroupEntity.class,
                        parentColumns = "id",
                        childColumns = "groupId",
                        onDelete = ForeignKey.CASCADE)},
        indices = {@Index(value = "groupId")}
)
public class ProductEntity implements Product {
    @PrimaryKey(autoGenerate = true)
    private long id;

    private String label;
    private String asin;
    private long groupId;

    private boolean updated;
    @Nullable
    private Date updatedAt;
    @Nullable
    private String rank;

    @Override
    public boolean isUpdated() {
        return updated;
    }

    public void setUpdated(boolean updated) {
        this.updated = updated;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    @Override
    public String getAsin() {
        return asin;
    }

    public void setAsin(String asin) {
        this.asin = asin;
    }

    @Override
    public long getGroupId() {
        return groupId;
    }
    public void setGroupId(long groupId) {
        this.groupId = groupId;
    }



    @Override
    public String getRank() {
        return rank;
    }
    public void setRank(String rank) {
        this.rank = rank;
    }
    public ProductEntity() {
    }


    public Date getUpdatedAt() {
        return updatedAt;
    }

    @Override
    public String getStatus() {
        return this.isUpdated()?"Updated":"Not Updated";
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }



    @Ignore
    public ProductEntity(long id, long groupId, String label, String asin, String rank) {
        this.id = id;
        this.groupId = groupId;
        this.label = label;
        this.asin = asin;
        this.rank = rank;
    }

    @Ignore
    public ProductEntity(String label, String asin) {
        this.label = label;
        this.asin = asin;
        this.updated = false;
    }


    public ProductEntity(Product product) {
        this.groupId = product.getGroupId();
        this.label = product.getLabel();
        this.asin = product.getAsin();
        this.rank = product.getRank();
    }
}
