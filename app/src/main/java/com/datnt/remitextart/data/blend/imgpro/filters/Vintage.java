package com.datnt.remitextart.data.blend.imgpro.filters;

import android.graphics.Bitmap;

import com.datnt.remitextart.data.blend.imgpro.Filter;
import com.datnt.remitextart.data.blend.imgpro.ImgPro;
import com.datnt.remitextart.data.blend.imgpro.actions.Blend;
import com.datnt.remitextart.data.blend.imgpro.actions.Contrast;

public class Vintage extends Filter {

	public void onBitmapSet(Bitmap bitmap) {
		int size = bitmap.getWidth() * bitmap.getHeight();

		int[] blue = ImgPro.createSolidPixels(0xff002288, size);
		int[] yellow = ImgPro.createSolidPixels(0xffFFFB00, size);

		clearActions();
		addAction(new Contrast(50));
		addAction(new Blend(blue, 0.8f, Blend.Mode.LIGHTEN));
		addAction(new Blend(yellow, 0.1f, Blend.Mode.SOFT_LIGHT));
	}

	@Override
	public String getName() {
		return "Vintage";
	}
}
