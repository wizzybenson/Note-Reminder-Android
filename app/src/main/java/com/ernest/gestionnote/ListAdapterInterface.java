package com.ernest.gestionnote;

public interface ListAdapterInterface {

    boolean onItemMove(int fromPosition, int toPosition);
    void onItemDismiss(int position);
}
