package com.textonphoto.texttophoto.quotecreator.pro.customview.stickerview;

/**
 * @author wupanjie
 */

public class FlipHorizontallyEvent extends AbstractFlipEvent {

    @Override
    @StickerView.Flip
    protected int getFlipDirection() {
        return StickerView.FLIP_HORIZONTALLY;
    }
}
