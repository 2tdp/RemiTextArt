package com.textonphoto.texttophoto.quotecreator.pro.customview.stickerview;

import android.view.MotionEvent;

/**
 * @author wupanjie
 */

public interface StickerIconEvent {
    void onActionDown(StickerView stickerView, MotionEvent event);

    void onActionMove(StickerView stickerView, MotionEvent event);

    void onActionUp(StickerView stickerView, MotionEvent event);
}
