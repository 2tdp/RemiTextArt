package com.datnt.remitextart.data.blend.imgpro.filters;

import android.graphics.Bitmap;

import com.datnt.remitextart.data.blend.imgpro.Filter;

public class Default extends Filter {

	public Default(){
		isDefault = true;
	}

	public void onBitmapSet(Bitmap bitmap) {
	}

	@Override
	public String getName() {
		return "Default";
	}

}
