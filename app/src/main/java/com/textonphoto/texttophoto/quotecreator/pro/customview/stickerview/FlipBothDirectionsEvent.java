package com.textonphoto.texttophoto.quotecreator.pro.customview.stickerview;

/**
 * @author wupanjie
 */

public class FlipBothDirectionsEvent extends AbstractFlipEvent {

    @Override
    @StickerView.Flip
    protected int getFlipDirection() {
        return StickerView.FLIP_VERTICALLY | StickerView.FLIP_HORIZONTALLY;
    }
}
