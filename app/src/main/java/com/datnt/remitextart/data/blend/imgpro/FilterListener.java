package com.datnt.remitextart.data.blend.imgpro;

import android.graphics.Bitmap;

public interface FilterListener {
	public void onFilterStart();

	public void onFilterDone(Filter f, Bitmap b);
}
