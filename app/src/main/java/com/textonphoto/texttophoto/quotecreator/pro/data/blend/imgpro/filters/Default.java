package com.textonphoto.texttophoto.quotecreator.pro.data.blend.imgpro.filters;

import android.graphics.Bitmap;

import com.textonphoto.texttophoto.quotecreator.pro.data.blend.imgpro.Filter;

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
