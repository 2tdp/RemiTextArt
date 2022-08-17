package com.textonphoto.texttophoto.quotecreator.pro.customview.stickerview;

import android.view.MotionEvent;

/**
 * @author wupanjie
 */

public class DeleteIconEvent implements StickerIconEvent {
    @Override
    public void onActionDown(StickerView stickerView, MotionEvent event) {

    }

    @Override
    public void onActionMove(StickerView stickerView, MotionEvent event) {

    }

    @Override
    public void onActionUp(StickerView stickerView, MotionEvent event) {
        stickerView.removeCurrentSticker();
    }
}
