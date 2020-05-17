package com.sifhic.absr.model;

public interface Product {
    long getId();
    long getGroupId();
    String getLabel();
    String getAsin();
    int getRank();
    boolean isUpdated();

}
