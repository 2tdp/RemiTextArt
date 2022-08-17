package com.textonphoto.texttophoto.quotecreator.pro.fragment.vip;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.textonphoto.texttophoto.quotecreator.pro.R;
import com.textonphoto.texttophoto.quotecreator.pro.utils.Utils;

public class VipOneFragment extends Fragment {

    private TextView tvContinue;
    private ImageView ivBack;

    public VipOneFragment(){}

    public static VipOneFragment newInstance() {
        return new VipOneFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_vip_one, container, false);

        init(view);

        evenClick();

        return view;
    }

    private void evenClick() {
        tvContinue.setOnClickListener(v -> {
            VipTwoFragment vipTwoFragment = VipTwoFragment.newInstance();
            Utils.replaceFragment(requireActivity().getSupportFragmentManager(), vipTwoFragment, false, true);
        });

        ivBack.setOnClickListener(v -> requireActivity().getSupportFragmentManager().popBackStack());
    }

    private void init(View view) {
        tvContinue = view.findViewById(R.id.tvContinue);
        ivBack = view.findViewById(R.id.ivBack);
    }
}