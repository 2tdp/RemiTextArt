package com.textonphoto.texttophoto.quotecreator.pro.data.blend.imgpro;

import android.graphics.Bitmap;

public interface FilterListener {
	public void onFilterStart();

	public void onFilterDone(Filter f, Bitmap b);
}
