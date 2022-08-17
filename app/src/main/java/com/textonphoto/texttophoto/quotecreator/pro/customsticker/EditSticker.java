package com.textonphoto.texttophoto.quotecreator.pro.customsticker;

import android.content.Context;

import com.textonphoto.texttophoto.quotecreator.pro.customview.stickerview.Sticker;

import org.jetbrains.annotations.NotNull;

public abstract class EditSticker {

    public abstract Sticker duplicate(Context context, @NotNull int id);

    public abstract Sticker shadow(Context context, @NotNull Sticker sticker);

    public abstract Sticker opacity(Context context, @NotNull Sticker sticker);

    public abstract Sticker flip(Context context, @NotNull Sticker sticker);

}
