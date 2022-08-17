package com.textonphoto.texttophoto.quotecreator.pro.callback;

public interface ItemTouchHelperAdapter {

    void onItemMove(int fromPosition, int toPosition);

    void onItemDismiss(int position);
}