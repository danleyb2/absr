package com.sifhic.absr.ui;

import com.sifhic.absr.model.Group;

public interface GroupClickCallback {
    void onClick(Group group);
    void onRefreshClick(Group group);
    void onEditClick(Group group);
    void onDeleteClick(Group group);
}
