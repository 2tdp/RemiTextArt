package com.datnt.remitextart.custom;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.Log;

import androidx.annotation.ContentView;
import androidx.annotation.Dimension;
import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.datnt.remitextart.R;
import com.datnt.remitextart.customview.stickerview.Sticker;
import com.datnt.remitextart.customview.stickerview.TextSticker;
import com.datnt.remitextart.model.ColorModel;
import com.datnt.remitextart.model.ShadowModel;
import com.datnt.remitextart.model.text.FontModel;
import com.datnt.remitextart.model.text.QuoteModel;
import com.datnt.remitextart.model.text.ShearTextModel;
import com.datnt.remitextart.model.text.TextModel;
import com.datnt.remitextart.model.text.TypeFontModel;
import com.datnt.remitextart.utils.Utils;
import com.datnt.remitextart.utils.UtilsAdjust;

public class TextStickerCustom extends Sticker {

    private static final String mEllipsis = "\u2026";

    private Context context;
    private TextModel textModel;
    private int id;

    private Rect realBounds;
    private Rect textRect;
    private TextPaint textPaint;
    private Drawable drawable;
    private StaticLayout staticLayout;
    private Layout.Alignment alignment;

    /**
     * Upper bounds for text size.
     * This acts as a starting point for resizing.
     */
    private float maxTextSizePixels;

    /**
     * Lower bounds for text size.
     */
    private float minTextSizePixels;

    /**
     * Line spacing multiplier.
     */
    private float lineSpacingMultiplier = 1.0f;

    /**
     * Additional line spacing.
     */
    private float lineSpacingExtra = 0.0f;

    public TextStickerCustom(Context context, TextModel textModel, int id) {
        this.context = context;
        this.textModel = textModel;
        this.id = id;
        init();
        setData(textModel);
    }

    private void init() {
        if (drawable == null)
            this.drawable = ContextCompat.getDrawable(context, R.drawable.sticker_transparent_text);
        textPaint = new TextPaint(TextPaint.ANTI_ALIAS_FLAG);
        realBounds = new Rect(0, 0, getWidth(), getHeight());
        textRect = new Rect(0, 0, getWidth(), getHeight());
        minTextSizePixels = convertSpToPx(6);
        maxTextSizePixels = convertSpToPx(32);

        alignment = Layout.Alignment.ALIGN_CENTER;
        textPaint.setTextSize(maxTextSizePixels);
    }

    public void setData(TextModel textModel) {
        setText(textModel.getContent());

        for (TypeFontModel f : textModel.getFontModel().getLstType()) {
            if (f.isSelected()) {
                setTypeface(Utils.getTypeFace(textModel.getFontModel().getNameFont(), f.getName(), context));
                break;
            }
        }
        if (textModel.getColorModel() != null) setTextColor(textModel.getColorModel());

        setAlpha((int) (textModel.getOpacity() * 255 / 100f));

        if (textModel.getShadowModel() != null) {
            setShadow(textModel.getShadowModel().getBlur(), textModel.getShadowModel().getXPos(),
                    textModel.getShadowModel().getYPos(), textModel.getShadowModel().getColorBlur());
        }

        switch (textModel.getTypeAlign()) {
            case 0:
                setTextAlign(Layout.Alignment.ALIGN_NORMAL);
                break;
            case 1:
                setTextAlign(Layout.Alignment.ALIGN_CENTER);
                break;
            case 2:
                setTextAlign(Layout.Alignment.ALIGN_OPPOSITE);
                break;
        }
        resizeText();
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        if (staticLayout == null) return;

        Matrix matrix = getMatrix();
        canvas.save();
        canvas.concat(matrix);
        if (drawable != null) {
            drawable.setBounds(realBounds);
            drawable.draw(canvas);
        }
        canvas.restore();

        canvas.save();
        canvas.concat(matrix);
        if (textRect.width() == getWidth()) {
            int dy = getHeight() / 2 - staticLayout.getHeight() / 2;
            // center vertical
            canvas.translate(0, dy);
        } else {
            int dx = textRect.left;
            int dy = textRect.top + textRect.height() / 2 - staticLayout.getHeight() / 2;
            canvas.translate(dx, dy);
        }
        staticLayout.draw(canvas);
        canvas.restore();
    }

    public TextStickerCustom setText(String text) {
        this.textModel.setContent(text);
        createDrawableText();
        return this;
    }

    public void setTextModel(TextModel textModel) {
        this.textModel = textModel;
        setData(textModel);
        createDrawableText();
    }

    public TextModel getTextModel() {
        return textModel;
    }

    public String getText() {
        return textModel.getContent();
    }

    @NonNull
    public TextStickerCustom setTextAlign(@NonNull Layout.Alignment alignment) {
        this.alignment = alignment;
        return this;
    }

    @NonNull
    public TextStickerCustom setTextSize(@Dimension(unit = Dimension.SP) float size) {
        createDrawableText();
        textPaint.setTextSize(convertSpToPx(size));
        maxTextSizePixels = textPaint.getTextSize();
        return this;
    }

    @NonNull
    public float getTextSize() {
        return convertPxToSp(textPaint.getTextSize());
    }

    @NonNull
    public TextStickerCustom setTypeface(@Nullable Typeface typeface) {
        textPaint.setTypeface(typeface);
        return this;
    }

    public int getColor() {
        return textPaint.getColor();
    }

    @NonNull
    public TextStickerCustom setTextColor(@NonNull ColorModel color) {
        this.textModel.setColorModel(color);

        if (color.getColorStart() == color.getColorEnd()) {
            textPaint.setShader(null);
            textPaint.setColor(color.getColorStart());
        } else {
            if (color.getDirec() == 4) {
                int c = color.getColorStart();
                color.setColorStart(color.getColorEnd());
                color.setColorEnd(c);

                color.setDirec(0);
            } else if (color.getDirec() == 5) {
                int c = color.getColorStart();
                color.setColorStart(color.getColorEnd());
                color.setColorEnd(c);

                color.setDirec(2);
            }

            Shader shader = new LinearGradient(setDirection(color.getDirec())[0],
                    setDirection(color.getDirec())[1],
                    setDirection(color.getDirec())[2],
                    setDirection(color.getDirec())[3],
                    new int[]{Color.parseColor(UtilsAdjust.toRGBString(color.getColorStart())), Color.parseColor(UtilsAdjust.toRGBString(color.getColorEnd()))},
                    new float[]{0, 1}, Shader.TileMode.MIRROR);
            textPaint.setShader(shader);
        }
        return this;
    }

    private int[] setDirection(int direc) {
        float w = getWidth();
        float h = getHeight();
        switch (direc) {
            case 0:
                return new int[]{(int) w / 2, 0, (int) w / 2, (int) h};
            case 1:
                return new int[]{0, 0, (int) w, (int) h};
            case 2:
                return new int[]{0, (int) h / 2, (int) w, (int) h / 2};
            case 3:
                return new int[]{0, (int) h, (int) w, 0};
        }
        return new int[]{};
    }

    public TextStickerCustom setShadow(float radiusBlur, float dx, float dy, int color) {
        textModel.getShadowModel().setBlur(radiusBlur);
        textModel.getShadowModel().setXPos(dx);
        textModel.getShadowModel().setYPos(dy);
        textModel.getShadowModel().setColorBlur(color);

        if (color != 0)
            textPaint.setShadowLayer(radiusBlur, dx, dy, Color.parseColor(UtilsAdjust.toRGBString(color)));
        else textPaint.setShadowLayer(radiusBlur, dx, dy, Color.BLACK);

        return this;
    }

    public ShadowModel getShadowModel() {
        return textModel.getShadowModel();
    }

    /**
     * Resize this view's text size with respect to its width and height
     * (minus padding). You should always call this method after the initialization.
     */
    @NonNull
    public TextStickerCustom resizeText() {
        final int availableHeightPixels = textRect.height();

        final int availableWidthPixels = textRect.width();

        final CharSequence text = getText();

        // Safety check
        // (Do not resize if the view does not have dimensions or if there is no text)
        if (text == null || text.length() <= 0 || availableHeightPixels <= 0 || availableWidthPixels <= 0 || maxTextSizePixels <= 0) {
            return this;
        }

        float targetTextSizePixels = maxTextSizePixels;
        int targetTextHeightPixels = getTextHeightPixels(text, availableWidthPixels, targetTextSizePixels);

        // Until we either fit within our TextView or we have reached our minimum text size,
        // incrementally try smaller sizes
        while (targetTextHeightPixels > availableHeightPixels && targetTextSizePixels > minTextSizePixels) {
            targetTextSizePixels = Math.max(targetTextSizePixels - 2, minTextSizePixels);

            targetTextHeightPixels = getTextHeightPixels(text, availableWidthPixels, targetTextSizePixels);
        }

        // If we have reached our minimum text size and the text still doesn't fit, append an ellipsis
        // (NOTE: Auto-ellipsize doesn't work hence why we have to do it here)
        if (targetTextSizePixels == minTextSizePixels && targetTextHeightPixels > availableHeightPixels) {
            // Make a copy of the original TextPaint object for measuring
            TextPaint textPaintCopy = new TextPaint(textPaint);
            textPaintCopy.setTextSize(targetTextSizePixels);

            // Measure using a StaticLayout instance
            StaticLayout staticLayout = new StaticLayout(text, textPaintCopy, availableWidthPixels, Layout.Alignment.ALIGN_NORMAL,
                    lineSpacingMultiplier, lineSpacingExtra, false);

            // Check that we have a least one line of rendered text
            if (staticLayout.getLineCount() > 0) {
                // Since the line at the specific vertical position would be cut off,
                // we must trim up to the previous line and add an ellipsis
                int lastLine = staticLayout.getLineForVertical(availableHeightPixels) - 1;

                if (lastLine >= 0) {
                    int startOffset = staticLayout.getLineStart(lastLine);
                    int endOffset = staticLayout.getLineEnd(lastLine);
                    float lineWidthPixels = staticLayout.getLineWidth(lastLine);
                    float ellipseWidth = textPaintCopy.measureText(mEllipsis);

                    // Trim characters off until we have enough room to draw the ellipsis
                    while (availableWidthPixels < lineWidthPixels + ellipseWidth) {
                        endOffset--;
                        lineWidthPixels =
                                textPaintCopy.measureText(text.subSequence(startOffset, endOffset + 1).toString());
                    }
                    textModel.setContent(text.subSequence(0, endOffset) + mEllipsis);
                }
            }
        }
        textPaint.setTextSize(targetTextSizePixels);
        staticLayout = new StaticLayout(this.textModel.getContent(), textPaint, textRect.width(), alignment, lineSpacingMultiplier,
                lineSpacingExtra, true);
        return this;
    }

    protected int getTextHeightPixels(@NonNull CharSequence source, int availableWidthPixels, float textSizePixels) {
        textPaint.setTextSize(textSizePixels);
        // It's not efficient to create a StaticLayout instance every time when measuring,
        // we can use StaticLayout.Builder since api 23.
        StaticLayout staticLayout =
                new StaticLayout(source, textPaint, availableWidthPixels, Layout.Alignment.ALIGN_NORMAL,
                        lineSpacingMultiplier, lineSpacingExtra, true);
        return staticLayout.getHeight();
    }

    public Rect getTextRect() {
        return textRect;
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
    public TextStickerCustom setDrawable(@NonNull Drawable drawable) {
        this.drawable = drawable;
        realBounds.set(0, 0, getWidth(), getHeight());
        textRect.set(0, 0, getWidth(), getHeight());
        return this;
    }

    @NonNull
    @Override
    public Drawable getDrawable() {
        return drawable;
    }

    @NonNull
    @Override
    public Sticker setAlpha(@IntRange(from = 0, to = 255) int alpha) {
        textPaint.setAlpha(alpha);
        return this;
    }

    private float convertSpToPx(float scaledPixels) {
        return scaledPixels * context.getResources().getDisplayMetrics().scaledDensity;
    }

    private float convertPxToSp(float scaledPixels) {
        return scaledPixels / context.getResources().getDisplayMetrics().scaledDensity;
    }

    public void createDrawableText() {
        String text = getText();

        if (text != null) {
            int line = 1, max = 0;

            String[] temp = text.split("\n");
            line = temp.length;

            if (temp.length == 1) max = text.length();
            Log.d("2tdp", "createDrawableText: " + line);
            for (String s : temp) {
                if (s.length() > max) max = s.length();
            }

            textPaint.getTextBounds(text, 0, text.length(), textRect);

            GradientDrawable drawable = new GradientDrawable();
//            if (text.length() > 20 && line != 1)
//                drawable.setSize((int) (textRect.width() * 1.1f * max / text.length()), (int) (textRect.height() * 1.2f * line));
//            else if (text.length() > 20)
//                drawable.setSize((int) (textRect.width() * 1.2f),
//                        (int) (textRect.height() * Math.sqrt(textRect.width() * textRect.width() + textRect.height() * textRect.height())));
//            else
//                drawable.setSize((int) context.getResources().getDimension(com.intuit.sdp.R.dimen._134sdp), (int) (textRect.height() * 2f));

            if (text.length() < 20)
                drawable.setSize((int) (textRect.width() * 1.2f), (int) (textRect.height() * 2f));
            else
                drawable.setSize((int) context.getResources().getDimension(com.intuit.sdp.R.dimen._134sdp), (int) (textRect.height() * 2f));
            
            drawable.setColor(Color.TRANSPARENT);
            setDrawable(drawable);
            staticLayout = new StaticLayout(text, textPaint, textRect.width(), alignment, lineSpacingMultiplier,
                    lineSpacingExtra, true);
        }
    }

    @Override
    public void release() {
        super.release();
        if (drawable != null) {
            drawable = null;
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
