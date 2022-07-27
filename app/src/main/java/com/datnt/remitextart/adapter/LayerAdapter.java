package com.datnt.remitextart.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.datnt.remitextart.R;
import com.datnt.remitextart.callback.ICallBackItem;
import com.datnt.remitextart.customsticker.DrawableStickerCustom;
import com.datnt.remitextart.customsticker.TextStickerCustom;
import com.datnt.remitextart.customview.CustomViewPathData;
import com.datnt.remitextart.model.LayerModel;
import com.datnt.remitextart.utils.Utils;
import com.datnt.remitextart.utils.UtilsAdjust;
import com.datnt.remitextart.utils.UtilsBitmap;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;

public class LayerAdapter extends RecyclerView.Adapter<LayerAdapter.LayerHolder> {

    private Context context;
    private ArrayList<LayerModel> lstLayer;
    private final ICallBackItem callBack;

    private Bitmap bitmap = null;
    private final Paint paintText;
    private final Rect rectText;

    private Path path;
    private RectF rectF;
    private Canvas canvas;

    public LayerAdapter(Context context, ICallBackItem callBack) {
        this.context = context;
        this.callBack = callBack;
        lstLayer = new ArrayList<>();

        path = new Path();
        rectF = new RectF();
        canvas = new Canvas();

        paintText = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintText.setStrokeJoin(Paint.Join.ROUND);
        paintText.setStrokeCap(Paint.Cap.ROUND);
        rectText = new Rect();
    }

    public void setData(ArrayList<LayerModel> lstLayer) {
        this.lstLayer = new ArrayList<>(lstLayer);
        changeNotify();
    }

    @NonNull
    @Override
    public LayerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new LayerHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layer, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull LayerHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    public int getItemCount() {
        if (!lstLayer.isEmpty()) return lstLayer.size();
        return 0;
    }

    class LayerHolder extends RecyclerView.ViewHolder {

        private final RoundedImageView ivLayer;
        private final CustomViewPathData ivPath;
        private final ImageView ivLook, ivLock;

        public LayerHolder(@NonNull View itemView) {
            super(itemView);

            ivLayer = itemView.findViewById(R.id.ivLayer);
            ivPath = itemView.findViewById(R.id.ivPath);
            ivLock = itemView.findViewById(R.id.ivLock);
            ivLook = itemView.findViewById(R.id.ivLook);
        }

        public void onBind(int position) {
            LayerModel layer = lstLayer.get(position);
            if (layer == null) return;

            if (layer.isSelected())
                itemView.setBackgroundResource(R.drawable.border_item_layer_selected);
            else itemView.setBackgroundResource(R.drawable.border_item_layer_unselected);


            if (layer.getSticker().isLock())
                ivLock.setVisibility(View.VISIBLE);
            else ivLock.setVisibility(View.GONE);

            if (layer.getSticker().isLook())
                ivLook.setVisibility(View.GONE);
            else ivLook.setVisibility(View.VISIBLE);

            if (layer.getSticker() instanceof DrawableStickerCustom) {
                DrawableStickerCustom drawableSticker = (DrawableStickerCustom) layer.getSticker();
                switch (drawableSticker.getTypeSticker()) {
                    case Utils.DECOR:
                        ivLayer.setVisibility(View.GONE);
                        ivPath.setVisibility(View.VISIBLE);
                        ivPath.setDataPath(drawableSticker.getDecorModel().getLstPathData(), true, false);
                        break;
                    case Utils.TEMPLATE:
                        ivLayer.setVisibility(View.GONE);
                        ivPath.setVisibility(View.VISIBLE);
                        ivPath.setDataPath(drawableSticker.getTemplateModel().getLstPathDataText(), true, false);
                        break;
                    default:
                        ivLayer.setVisibility(View.VISIBLE);
                        ivPath.setVisibility(View.GONE);
                        ivLayer.setImageBitmap(((BitmapDrawable) drawableSticker.getDrawable()).getBitmap());
                        break;
                }
            } else {
                TextStickerCustom textSticker = (TextStickerCustom) layer.getSticker();

                if (bitmap == null) bitmap = UtilsBitmap.loadBitmapFromView(ivLayer, false);

                String str = textSticker.getText();
                paintText.setTextSize(15);

                if (textSticker.getTextModel().getColorModel() != null)
                    UtilsAdjust.setColor(textSticker.getTextModel().getColorModel(),
                            paintText, textSticker.getWidth(), textSticker.getHeight());
                else
                    paintText.setColor(Color.parseColor(UtilsBitmap.toRGBString(textSticker.getColor())));

                if (str != null) paintText.getTextBounds(str, 0, str.length(), rectText);

                Canvas canvas = new Canvas(bitmap);
                canvas.drawText(str, bitmap.getWidth() / 2f - rectText.width() / 2f,
                        bitmap.getHeight() / 2f - rectText.height(), paintText);

                ivLayer.setImageBitmap(bitmap);
            }

            itemView.setOnClickListener(v -> callBack.callBackItem(layer, position));
        }
    }

    public void setCurrent(int pos) {
        for (int i = 0; i < lstLayer.size(); i++) lstLayer.get(i).setSelected(i == pos);
        changeNotify();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void changeNotify() {
        notifyDataSetChanged();
    }
}
