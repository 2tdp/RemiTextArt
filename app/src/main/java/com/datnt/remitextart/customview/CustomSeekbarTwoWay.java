package com.datnt.remitextart.customview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import com.datnt.remitextart.R;

public class CustomSeekbarTwoWay extends View {

    private Paint paint, paintProgress;
    private int progress;
    private int max, value;
    private float sizeThumb, sizeBg, sizePos;
    private OnSeekbarResult onSeekbarResult;

    public void setOnSeekbarResult(OnSeekbarResult onSeekbarResult) {
        this.onSeekbarResult = onSeekbarResult;
    }

    public CustomSeekbarTwoWay(Context context) {
        super(context);
        init(context);
    }

    public CustomSeekbarTwoWay(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CustomSeekbarTwoWay(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        sizeThumb = context.getResources().getDimension(com.intuit.sdp.R.dimen._10sdp);
        sizeBg = context.getResources().getDimension(com.intuit.sdp.R.dimen._3sdp);
        sizePos = context.getResources().getDimension(com.intuit.sdp.R.dimen._5sdp);

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintProgress = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);

        paint.setStyle(Paint.Style.FILL);
        paintProgress.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        paint.clearShadowLayer();
        paint.setColor(getResources().getColor(R.color.gray_2));
        paint.setStrokeWidth(sizeBg);
        canvas.drawLine(sizeThumb / 2, getHeight() / 2f, getWidth() - sizeThumb / 2, getHeight() / 2f, paint);

        paintProgress.setColor(getResources().getColor(R.color.green));
        paintProgress.setStrokeWidth(sizePos);
        float p = (getWidth() - sizeThumb) * progress / max + sizeThumb / 2;
        canvas.drawLine(getWidth() / 2f, getHeight() / 2f, p, getHeight() / 2f, paintProgress);

        paint.setColor(getResources().getColor(R.color.green));
        paint.setShadowLayer(sizeThumb / 8, 0, 0, Color.WHITE);
        canvas.drawCircle(p, getHeight() / 2f, sizeThumb / 2, paint);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (onSeekbarResult != null) onSeekbarResult.onDown(this);
                break;
            case MotionEvent.ACTION_MOVE:
                progress = (int) ((x - sizeThumb / 2) * max / ((getWidth() - sizeThumb)));

                if (progress < 0) progress = 0;
                else if (progress > max) progress = max;
                invalidate();
                if (progress > max / 2) value = progress - max / 2;
                else value = -(max / 2 - progress);

                if (onSeekbarResult != null) onSeekbarResult.onMove(this, value);
                break;
            case MotionEvent.ACTION_UP:
                if (onSeekbarResult != null) onSeekbarResult.onUp(this, value);
                break;
        }
        return true;
    }

    public void setProgress(int progress) {
        if (progress == 0) {
            this.progress = max / 2;
        } else {
            this.progress = max / 2 + progress;
        }
        invalidate();
    }

    public int getProgress() {
        return progress - max / 2;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
        progress = max / 2;
        invalidate();
    }
}
