package com.sifhic.absr.model;

import java.util.Date;

public interface Product {
    long getId();
    long getGroupId();
    String getLabel();
    String getAsin();
    int getRank();
    boolean isUpdated();
    Date getUpdatedAt();
    String getStatus();

}
