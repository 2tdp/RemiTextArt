package com.datnt.remitextart.custom;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;

import androidx.annotation.NonNull;

import com.datnt.remitextart.customview.stickerview.Sticker;
import com.datnt.remitextart.model.EmojiModel;
import com.datnt.remitextart.model.ImageModel;
import com.datnt.remitextart.utils.Utils;
import com.datnt.remitextart.utils.UtilsAdjust;

public class DrawableStickerCustom extends Sticker {

    private Context context;
    private Drawable drawable;
    private RectF realBounds;
    private int id;
    private String typeSticker;

    private EmojiModel emojiModel;
    private ImageModel imageModel;

    public DrawableStickerCustom(Context context, Object o, int id, String type) {
        this.context = context;
        this.typeSticker = type;
        this.id = id;
        switch (type) {
            case Utils.EMOJI:
                this.emojiModel = (EmojiModel) o;
                initEmoji();
                break;
            case Utils.IMAGE:
                this.imageModel = (ImageModel) o;
                initImage();
                break;
        }
    }

    private void initEmoji() {
        this.drawable = new BitmapDrawable(context.getResources(),
                Utils.getBitmapFromAsset(context, emojiModel.getFolder(),
                        emojiModel.getNameEmoji(), true, false));

        realBounds = new RectF(0, 0, getWidth(), getHeight());
    }

    private void initImage() {
        this.drawable = new BitmapDrawable(context.getResources(),
                BitmapFactory.decodeFile(imageModel.getUri()));

        realBounds = new RectF(0, 0, getWidth(), getHeight());
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        canvas.save();
        canvas.concat(getMatrix());

        drawable.setBounds((int) realBounds.left, (int) realBounds.top, (int) realBounds.right, (int) realBounds.bottom);
        drawable.draw(canvas);
        canvas.restore();
    }

    @Override
    public int getWidth() {
        return drawable.getIntrinsicWidth();
    }

    @Override
    public int getHeight() {
        return drawable.getIntrinsicHeight();
    }

    @Override
    public DrawableStickerCustom setDrawable(@NonNull Drawable drawable) {
        this.drawable = drawable;
        realBounds = new RectF(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        return this;
    }

    @NonNull
    @Override
    public Drawable getDrawable() {
        return drawable;
    }

    @NonNull
    @Override
    public DrawableStickerCustom setAlpha(int alpha) {
        drawable.setAlpha(alpha);
        return this;
    }

    public void replaceImage(){
        initImage();
    }

    public ImageModel getImageModel() {
        return imageModel;
    }

    public void setImageModel(ImageModel imageModel) {
        this.imageModel = imageModel;
    }

    public EmojiModel getEmojiModel() {
        return emojiModel;
    }

    public void setEmojiModel(EmojiModel emojiModel) {
        this.emojiModel = emojiModel;
        initEmoji();
    }

    public String getTypeSticker() {
        return typeSticker;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
