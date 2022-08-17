package com.textonphoto.texttophoto.quotecreator.pro.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import com.makeramen.roundedimageview.RoundedImageView;
import com.textonphoto.texttophoto.quotecreator.pro.R;
import com.textonphoto.texttophoto.quotecreator.pro.callback.ICallBackItem;
import com.textonphoto.texttophoto.quotecreator.pro.customsticker.DrawableStickerCustom;
import com.textonphoto.texttophoto.quotecreator.pro.customsticker.TextStickerCustom;
import com.textonphoto.texttophoto.quotecreator.pro.customview.CustomViewPathData;
import com.textonphoto.texttophoto.quotecreator.pro.model.LayerModel;
import com.textonphoto.texttophoto.quotecreator.pro.utils.Utils;
import com.textonphoto.texttophoto.quotecreator.pro.utils.UtilsAdjust;
import com.textonphoto.texttophoto.quotecreator.pro.utils.UtilsBitmap;

import java.util.ArrayList;

public class LayerAdapter extends RecyclerView.Adapter<LayerAdapter.LayerHolder> {

    private Context context;
    private ArrayList<LayerModel> lstLayer;
    private final ICallBackItem callBack;

    private Bitmap bitmap = null;
    private final Paint paintText;
    private final Rect rectText;

    public LayerAdapter(Context context, ICallBackItem callBack) {
        this.context = context;
        this.callBack = callBack;
        lstLayer = new ArrayList<>();

        paintText = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintText.setStrokeJoin(Paint.Join.ROUND);
        paintText.setStrokeCap(Paint.Cap.ROUND);
        rectText = new Rect();
    }

    public void setData(ArrayList<LayerModel> lstLayer) {
        this.lstLayer = lstLayer;
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

    public class LayerHolder extends RecyclerView.ViewHolder {

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

        @SuppressLint("ClickableViewAccessibility")
        public void onBind(int position) {
            LayerModel layer = lstLayer.get(position);
            if (layer == null) return;

            if (layer.isSelected())
                itemView.setBackgroundResource(R.drawable.border_item_layer_selected);
            else itemView.setBackgroundResource(R.drawable.border_item_layer_unselected);

            if (layer.getSticker().isLock() && layer.getSticker().isLook()) {
                ivLock.setVisibility(View.GONE);
                ivLook.setVisibility(View.VISIBLE);
                ivLook.setImageResource(R.drawable.ic_lock1);
            } else if (layer.getSticker().isLock()) {
                ivLock.setVisibility(View.VISIBLE);
                ivLock.setImageResource(R.drawable.ic_lock1);
                ivLook.setVisibility(View.VISIBLE);
                ivLook.setImageResource(R.drawable.ic_look1);
            } else if (layer.getSticker().isLook()) ivLook.setVisibility(View.GONE);
            else {
                ivLock.setVisibility(View.GONE);
                ivLook.setVisibility(View.VISIBLE);
                ivLook.setImageResource(R.drawable.ic_look1);
            }

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
                    case Utils.IMAGE:
                        ivLayer.setVisibility(View.VISIBLE);
                        ivPath.setVisibility(View.GONE);
                        ivLayer.setImageBitmap(BitmapFactory.decodeFile(drawableSticker.getImageModel().getUri()));
                        break;
                    default:
                        ivLayer.setVisibility(View.VISIBLE);
                        ivPath.setVisibility(View.GONE);
                        ivLayer.setImageBitmap(((BitmapDrawable) drawableSticker.getDrawable()).getBitmap());
                        break;
                }
            } else {
                TextStickerCustom textSticker = (TextStickerCustom) layer.getSticker();

                if (bitmap == null)
                    bitmap = UtilsBitmap.loadBitmapFromView(context, ivLayer, false);

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
