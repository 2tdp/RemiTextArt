package com.textonphoto.texttophoto.quotecreator.pro.customview.stickerview;

/**
 * @author wupanjie
 */

public class FlipVerticallyEvent extends AbstractFlipEvent {

    @Override
    @StickerView.Flip
    protected int getFlipDirection() {
        return StickerView.FLIP_VERTICALLY;
    }
}
