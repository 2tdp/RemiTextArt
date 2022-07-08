package com.datnt.remitextart.customview.stickerview;

import android.view.MotionEvent;

public abstract class AbstractFlipEvent implements StickerIconEvent {

  @Override public void onActionDown(StickerView stickerView, MotionEvent event) {

  }

  @Override public void onActionMove(StickerView stickerView, MotionEvent event) {

  }

  @Override public void onActionUp(StickerView stickerView, MotionEvent event) {
    stickerView.flipCurrentSticker(0);
  }

  @StickerView.Flip protected abstract int getFlipDirection();
}
