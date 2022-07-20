package com.datnt.remitextart.customsticker.imgpro.filters;

import android.graphics.Bitmap;

import com.datnt.remitextart.customsticker.imgpro.Filter;

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
