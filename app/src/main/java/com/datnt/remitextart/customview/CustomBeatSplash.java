package com.datnt.remitextart.customview;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public class CustomBeatSplash extends View {

    private Paint paint;
    private float count, plus, radius, radiuss, radiusss, radiussss, radiusssss;

    public CustomBeatSplash(Context context) {
        super(context);
        init(context);
    }

    public CustomBeatSplash(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CustomBeatSplash(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.parseColor("#ED7DB0"));
        paint.setAlpha((int) (9 * 255 / 100f));

    }

    boolean isCreate = true;

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (isCreate) {
            isCreate = false;
            count = getWidth() / 6f;
            plus = 1;
            float f = getWidth() / 16f;
            radius = count;
            radiuss = count + f;
            radiusss = count + 2 * f;
            radiussss = count + 3 * f;
            radiusssss = count + 4 * f;

            runBeat1();
        }

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawCircle(getWidth() / 2f, getHeight() / 2f, radius, paint);
        canvas.drawCircle(getWidth() / 2f, getHeight() / 2f, radiuss, paint);
        canvas.drawCircle(getWidth() / 2f, getHeight() / 2f, radiusss, paint);
        canvas.drawCircle(getWidth() / 2f, getHeight() / 2f, radiussss, paint);
        canvas.drawCircle(getWidth() / 2f, getHeight() / 2f, radiusssss, paint);
    }

    private void runBeat1() {
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0f, 2f);
        valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
        valueAnimator.addUpdateListener(animation -> {
            if (radius > getWidth() * 5 / 12f) radius = count;
            radius += plus;
            if (radiuss > getWidth() * 5 / 12f) radiuss = count;
            radiuss += plus;
            if (radiusss > getWidth() * 5 / 12f) radiusss = count;
            radiusss += plus;
            if (radiussss > getWidth() * 5 / 12f) radiussss = count;
            radiussss += plus;
            if (radiusssss > getWidth() * 5 / 12f) radiusssss = count;
            radiusssss += plus;

            invalidate();
        });

        valueAnimator.start();
    }
}
