package com.datnt.remitextart.data;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.datnt.remitextart.R;
import com.datnt.remitextart.callback.ICallbackColor;
import com.datnt.remitextart.model.ColorModel;
import com.flask.colorpicker.ColorPickerView;

import java.util.ArrayList;

public class DataColor {

    public static ArrayList<ColorModel> getListColor(Context context) {
        ArrayList<ColorModel> lstColor = new ArrayList<>();

        int[] arrColor = context.getResources().getIntArray(R.array.lstColor);

        int direction = 0;
        for (int i = arrColor.length - 1; i >= 0; i--) {
            if (i < 70)
                lstColor.add(new ColorModel(arrColor[i], arrColor[i], 0, false));
            else {
                lstColor.add(new ColorModel(arrColor[i], arrColor[i - 1], direction, false));
                i--;
                if (direction < 5) direction++;
                else direction = 0;
            }
        }

        lstColor.add(0, new ColorModel(0, 0, 0, false));

        return lstColor;
    }

    public static void pickColor(Context context, ICallbackColor callback) {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_pick_color, null);

        ColorPickerView colorPicker = view.findViewById(R.id.pickColor);
        TextView tvNo = view.findViewById(R.id.tvNo);
        TextView tvYes = view.findViewById(R.id.tvYes);

        AlertDialog dialog = new AlertDialog.Builder(context, R.style.SheetDialog).create();
        dialog.setView(view);
        dialog.setCancelable(false);
        dialog.show();

        tvNo.setOnClickListener(vNO -> dialog.cancel());
        tvYes.setOnClickListener(vYes -> {
            ColorModel color = new ColorModel(colorPicker.getSelectedColor(), colorPicker.getSelectedColor(), 0, false);

            callback.callbackColor(color);
            dialog.cancel();
        });
    }

    public static void pickDirection(Context context, ColorModel color, ICallbackColor callback) {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_pick_direction, null);
        ImageView iv_TB = view.findViewById(R.id.iv_TB);
        ImageView ivTL_BR = view.findViewById(R.id.ivTL_BR);
        ImageView ivLR = view.findViewById(R.id.ivLR);
        ImageView ivBL_TR = view.findViewById(R.id.ivBL_TR);
        ImageView ivBT = view.findViewById(R.id.ivBT);
        ImageView ivRL = view.findViewById(R.id.ivRL);

        ImageView ivShow = view.findViewById(R.id.ivShow);

        TextView tvNo = view.findViewById(R.id.tvNo);
        TextView tvYes = view.findViewById(R.id.tvYes);

        AlertDialog dialog = new AlertDialog.Builder(context, R.style.SheetDialog).create();
        dialog.setView(view);
        dialog.setCancelable(false);
        dialog.show();

        tvNo.setOnClickListener(v -> dialog.cancel());
        tvYes.setOnClickListener(v -> {
            callback.callbackColor(color);
            dialog.cancel();
        });

        ivShow.setBackground(clickDirec(color.getDirec(), color));
        checkPickDirec(color.getDirec(), iv_TB, ivTL_BR, ivLR, ivBL_TR, ivBT, ivRL);

        iv_TB.setOnClickListener(v -> {
            color.setDirec(0);
            ivShow.setBackground(clickDirec(0, color));
            checkPickDirec(0, iv_TB, ivTL_BR, ivLR, ivBL_TR, ivBT, ivRL);
        });
        ivTL_BR.setOnClickListener(v -> {
            color.setDirec(1);
            ivShow.setBackground(clickDirec(1, color));
            checkPickDirec(1, iv_TB, ivTL_BR, ivLR, ivBL_TR, ivBT, ivRL);
        });
        ivLR.setOnClickListener(v -> {
            color.setDirec(2);
            ivShow.setBackground(clickDirec(2, color));
            checkPickDirec(2, iv_TB, ivTL_BR, ivLR, ivBL_TR, ivBT, ivRL);
        });
        ivBL_TR.setOnClickListener(v -> {
            color.setDirec(3);
            ivShow.setBackground(clickDirec(3, color));
            checkPickDirec(3, iv_TB, ivTL_BR, ivLR, ivBL_TR, ivBT, ivRL);
        });
        ivBT.setOnClickListener(v -> {
            color.setDirec(4);
            ivShow.setBackground(clickDirec(4, color));
            checkPickDirec(4, iv_TB, ivTL_BR, ivLR, ivBL_TR, ivBT, ivRL);
        });
        ivRL.setOnClickListener(v -> {
            color.setDirec(5);
            ivShow.setBackground(clickDirec(5, color));
            checkPickDirec(5, iv_TB, ivTL_BR, ivLR, ivBL_TR, ivBT, ivRL);
        });
    }

    public static GradientDrawable clickDirec(int pos, ColorModel color) {
        switch (pos) {
            case 0:
                return createGradient(GradientDrawable.Orientation.TOP_BOTTOM, color.getColorStart(), color.getColorEnd());
            case 1:
                return createGradient(GradientDrawable.Orientation.TL_BR, color.getColorStart(), color.getColorEnd());
            case 2:
                return createGradient(GradientDrawable.Orientation.LEFT_RIGHT, color.getColorStart(), color.getColorEnd());
            case 3:
                return createGradient(GradientDrawable.Orientation.BL_TR, color.getColorStart(), color.getColorEnd());
            case 4:
                return createGradient(GradientDrawable.Orientation.BOTTOM_TOP, color.getColorStart(), color.getColorEnd());
            case 5:
                return createGradient(GradientDrawable.Orientation.RIGHT_LEFT, color.getColorStart(), color.getColorEnd());
            default:
                return null;
        }
    }

    public static GradientDrawable createGradient(GradientDrawable.Orientation direc, int start, int end) {
        GradientDrawable gradientDrawable = new GradientDrawable(direc, new int[]{start, end});
        gradientDrawable.setGradientType(GradientDrawable.LINEAR_GRADIENT);
        gradientDrawable.setCornerRadius(34f);

        return gradientDrawable;
    }

    public static void checkPickDirec(int pos, ImageView iv_tb, ImageView ivTL_br, ImageView ivLR, ImageView ivBL_tr, ImageView ivBT, ImageView ivRL) {
        switch (pos) {
            case 0:
                iv_tb.setImageResource(R.drawable.ic_top_bottom_check);
                ivTL_br.setImageResource(R.drawable.ic_tl_br_uncheck);
                ivLR.setImageResource(R.drawable.ic_left_right_uncheck);
                ivBL_tr.setImageResource(R.drawable.ic_bl_tr_uncheck);
                ivBT.setImageResource(R.drawable.ic_bottom_top_uncheck);
                ivRL.setImageResource(R.drawable.ic_right_left_uncheck);
                break;
            case 1:
                iv_tb.setImageResource(R.drawable.ic_top_bottom_uncheck);
                ivTL_br.setImageResource(R.drawable.ic_tl_br_check);
                ivLR.setImageResource(R.drawable.ic_left_right_uncheck);
                ivBL_tr.setImageResource(R.drawable.ic_bl_tr_uncheck);
                ivBT.setImageResource(R.drawable.ic_bottom_top_uncheck);
                ivRL.setImageResource(R.drawable.ic_right_left_uncheck);
                break;
            case 2:
                iv_tb.setImageResource(R.drawable.ic_top_bottom_uncheck);
                ivTL_br.setImageResource(R.drawable.ic_tl_br_uncheck);
                ivLR.setImageResource(R.drawable.ic_left_right_check);
                ivBL_tr.setImageResource(R.drawable.ic_bl_tr_uncheck);
                ivBT.setImageResource(R.drawable.ic_bottom_top_uncheck);
                ivRL.setImageResource(R.drawable.ic_right_left_uncheck);
                break;
            case 3:
                iv_tb.setImageResource(R.drawable.ic_top_bottom_uncheck);
                ivTL_br.setImageResource(R.drawable.ic_tl_br_uncheck);
                ivLR.setImageResource(R.drawable.ic_left_right_uncheck);
                ivBL_tr.setImageResource(R.drawable.ic_bl_tr_check);
                ivBT.setImageResource(R.drawable.ic_bottom_top_uncheck);
                ivRL.setImageResource(R.drawable.ic_right_left_uncheck);
                break;
            case 4:
                iv_tb.setImageResource(R.drawable.ic_top_bottom_uncheck);
                ivTL_br.setImageResource(R.drawable.ic_tl_br_uncheck);
                ivLR.setImageResource(R.drawable.ic_left_right_uncheck);
                ivBL_tr.setImageResource(R.drawable.ic_bl_tr_uncheck);
                ivBT.setImageResource(R.drawable.ic_bottom_top_check);
                ivRL.setImageResource(R.drawable.ic_right_left_uncheck);
                break;
            case 5:
                iv_tb.setImageResource(R.drawable.ic_top_bottom_uncheck);
                ivTL_br.setImageResource(R.drawable.ic_tl_br_uncheck);
                ivLR.setImageResource(R.drawable.ic_left_right_uncheck);
                ivBL_tr.setImageResource(R.drawable.ic_bl_tr_uncheck);
                ivBT.setImageResource(R.drawable.ic_bottom_top_uncheck);
                ivRL.setImageResource(R.drawable.ic_right_left_check);
                break;
        }
    }

}
