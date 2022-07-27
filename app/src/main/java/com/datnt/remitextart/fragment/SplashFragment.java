package com.datnt.remitextart.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.datnt.remitextart.R;

public class SplashFragment extends Fragment {

    private static final String PIC = "pic";
    private static final String TEXT = "text";

    private int pic;
    private String text;

    public SplashFragment() {
    }

    public static SplashFragment newInstance(int pic, String text) {
        SplashFragment fragment = new SplashFragment();
        Bundle args = new Bundle();
        args.putInt(PIC, pic);
        args.putString(TEXT, text);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            pic = getArguments().getInt(PIC);
            text = getArguments().getString(TEXT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_splash, container, false);
        ImageView ivPic = v.findViewById(R.id.ivPic);
        TextView tvDes = v.findViewById(R.id.tvDes);

        Glide.with(requireContext())
                .load(pic)
                .into(ivPic);
        tvDes.setText(text);

        return v;
    }
}