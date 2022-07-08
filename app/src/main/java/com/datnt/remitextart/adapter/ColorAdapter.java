package com.datnt.remitextart.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.datnt.remitextart.R;
import com.datnt.remitextart.callback.ICallBackItem;
import com.datnt.remitextart.model.ColorModel;
import com.datnt.remitextart.utils.Utils;

import java.util.ArrayList;

public class ColorAdapter extends RecyclerView.Adapter<ColorAdapter.ColorHolder> {

    private Context context;
    private ArrayList<ColorModel> lstColor;
    private ICallBackItem callBack;
    private int resourceLayout;

    private GradientDrawable gradient;

    public ColorAdapter(Context context, int resourceLayout, ICallBackItem callBack) {
        this.context = context;
        this.resourceLayout = resourceLayout;
        this.callBack = callBack;
    }

    public void setData(ArrayList<ColorModel> lstColor) {
        this.lstColor = new ArrayList<>(lstColor);
        notifyChange();
    }

    @NonNull
    @Override
    public ColorHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ColorHolder(LayoutInflater.from(parent.getContext()).inflate(resourceLayout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ColorHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    public int getItemCount() {
        if (!lstColor.isEmpty()) return lstColor.size();
        return 0;
    }

    class ColorHolder extends RecyclerView.ViewHolder {

        ImageView ivPick;

        public ColorHolder(@NonNull View itemView) {
            super(itemView);

            ivPick = itemView.findViewById(R.id.ivPick);
        }

        public void onBind(int position) {
            ColorModel color = lstColor.get(position);
            if (color == null) return;

            itemView.setOnClickListener(v -> callBack.callBackItem(color, position));

            if (color.getColorStart() == 0) {
                if (resourceLayout == R.layout.item_color)
                    ivPick.setImageResource(R.drawable.ic_pick_color);
                else ivPick.setImageResource(R.drawable.ic_pick_color_cir);
                ivPick.setBackground(null);
            } else {
                if (color.getColorStart() != color.getColorEnd())
                    gradient = new GradientDrawable(Utils.setDirection(color.getDirec()), new int[]{color.getColorStart(), color.getColorEnd()});
                else
                    gradient = new GradientDrawable(Utils.setDirection(0), new int[]{color.getColorStart(), color.getColorEnd()});
                gradient.setGradientType(GradientDrawable.LINEAR_GRADIENT);
                if (resourceLayout == R.layout.item_color_edit) gradient.setShape(GradientDrawable.OVAL);
                else gradient.setCornerRadius(34f);
                ivPick.setImageResource(0);
                ivPick.setBackground(gradient);
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    public void notifyChange() {
        notifyDataSetChanged();
    }
}
