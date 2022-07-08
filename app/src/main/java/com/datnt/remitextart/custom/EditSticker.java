package com.datnt.remitextart.custom;

import android.content.Context;

import com.datnt.remitextart.customview.stickerview.Sticker;
import com.datnt.remitextart.model.ColorModel;

import org.jetbrains.annotations.NotNull;

public abstract class EditSticker {

    public abstract Sticker replace(Context context, @NotNull Sticker sticker);

    public abstract Sticker duplicate(Context context, @NotNull int id);

    public abstract Sticker shadow(Context context, @NotNull Sticker sticker);

    public abstract Sticker opacity(Context context, @NotNull Sticker sticker);

    public abstract Sticker flip(Context context, @NotNull Sticker sticker);
}
