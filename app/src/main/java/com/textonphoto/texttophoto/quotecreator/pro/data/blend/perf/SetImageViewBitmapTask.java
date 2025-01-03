package com.textonphoto.texttophoto.quotecreator.pro.data.blend.perf;

import android.graphics.Bitmap;
import android.widget.ImageView;

import java.lang.ref.WeakReference;

public class SetImageViewBitmapTask extends BitmapTask {

	private final WeakReference<ImageView> imgViewRef;

	public SetImageViewBitmapTask(ImageView weakImageView, BitmapTask nextTask) {
		super(null, nextTask);

		imgViewRef = new WeakReference<>(weakImageView);
	}

	public SetImageViewBitmapTask(ImageView weakImageView) {
		this(weakImageView, null);
	}

	@Override
	protected Bitmap doInBackground(Bitmap... args) {
		if (args.length > 0)
			return args[0];
		else
			return null;
	}

	@Override
	protected void onPostExecute(Bitmap bitmap) {
		final ImageView iv = imgViewRef.get();
		if (iv != null && bitmap != null) {
			iv.setImageBitmap(bitmap);
		}

		super.onPostExecute(bitmap);
	}
}