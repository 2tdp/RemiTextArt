package com.datnt.remitextart.customsticker;

import android.content.Context;

import com.datnt.remitextart.customview.stickerview.Sticker;

import org.jetbrains.annotations.NotNull;

public abstract class EditSticker {

    public abstract Sticker duplicate(Context context, @NotNull int id);

    public abstract Sticker shadow(Context context, @NotNull Sticker sticker);

    public abstract Sticker opacity(Context context, @NotNull Sticker sticker);

    public abstract Sticker flip(Context context, @NotNull Sticker sticker);

}
