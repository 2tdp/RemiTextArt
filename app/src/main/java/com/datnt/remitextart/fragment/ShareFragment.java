package com.datnt.remitextart.fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.datnt.remitextart.R;
import com.datnt.remitextart.callback.ICheckTouch;
import com.datnt.remitextart.utils.Utils;

public class ShareFragment extends Fragment {

    private Bitmap bitmap;
    private final ICheckTouch checkTouch;

    private ImageView ivBack, ivHome, ivSaved;
    private LinearLayout llInsta, llFb, llTelegram, llOther;

    public ShareFragment(ICheckTouch checkTouch) {
        this.checkTouch = checkTouch;
    }

    public static ShareFragment newInstance(ICheckTouch checkTouch) {
        return new ShareFragment(checkTouch);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_share, container, false);

        init(v);

        if (bitmap != null) ivSaved.setImageBitmap(bitmap);
        return v;
    }

    private void init(View v) {
        ivBack = v.findViewById(R.id.ivBack);
        ivHome = v.findViewById(R.id.ivHome);
        ivSaved = v.findViewById(R.id.ivSaved);
        llInsta = v.findViewById(R.id.llInstagram);
        llFb = v.findViewById(R.id.llFb);
        llTelegram = v.findViewById(R.id.llTelegram);
        llOther = v.findViewById(R.id.llOther);

        evenClick();
    }

    private void evenClick() {
        ivBack.setOnClickListener(v -> Utils.clearBackStack(requireActivity().getSupportFragmentManager()));
        ivHome.setOnClickListener(v -> checkTouch.checkTouch(true));

        llInsta.setOnClickListener(view -> Utils.shareFile(requireContext(), bitmap, Utils.INSTAGRAM_PACKAGE_NAME));
        llFb.setOnClickListener(view -> Utils.shareFile(requireContext(), bitmap, Utils.FACEBOOK_PACKAGE_NAME));
        llTelegram.setOnClickListener(view -> Utils.shareFile(requireContext(), bitmap, Utils.TELEGRAM_PACKAGE_NAME));
        llOther.setOnClickListener(view -> Utils.shareFile(requireContext(), bitmap, null));
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public boolean getBack() {
        return ivSaved.getVisibility() == View.VISIBLE;
    }
}