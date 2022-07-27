package com.datnt.remitextart.fragment.vip;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.fragment.app.Fragment;

import com.datnt.remitextart.R;
import com.datnt.remitextart.utils.Utils;

public class VipTwoFragment extends Fragment {

    private ImageView ivBack, ivMonth, ivYear, ivLifetime;
    private RelativeLayout rlMonth, rlYear, rlLifeTime;

    public VipTwoFragment(){}

    public static VipTwoFragment newInstance() {
        return new VipTwoFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_vip_two, container, false);

        init(view);

        evenClick();
        return view;
    }

    private void evenClick() {
        ivBack.setOnClickListener(v -> Utils.clearBackStack(requireActivity().getSupportFragmentManager()));

        rlMonth.setOnClickListener(v -> setUpChooseOption(0));
        rlYear.setOnClickListener(v -> setUpChooseOption(1));
        rlLifeTime.setOnClickListener(v -> setUpChooseOption(2));
    }

    private void setUpChooseOption(int pos) {
        switch (pos) {
            case 0:
                ivMonth.setImageResource(R.drawable.ic_choose);

                ivYear.setImageResource(R.drawable.ic_un_choose);
                ivLifetime.setImageResource(R.drawable.ic_un_choose);
                break;
            case 1:
                ivYear.setImageResource(R.drawable.ic_choose);

                ivMonth.setImageResource(R.drawable.ic_un_choose);
                ivLifetime.setImageResource(R.drawable.ic_un_choose);
                break;
            case 2:
                ivLifetime.setImageResource(R.drawable.ic_choose);

                ivMonth.setImageResource(R.drawable.ic_un_choose);
                ivYear.setImageResource(R.drawable.ic_un_choose);
                break;
        }
    }

    private void init(View view) {
        ivBack = view.findViewById(R.id.ivBack);
        ivMonth = view.findViewById(R.id.iv1);
        ivYear = view.findViewById(R.id.iv2);
        ivLifetime = view.findViewById(R.id.iv3);
        rlMonth = view.findViewById(R.id.rlMonth);
        rlYear = view.findViewById(R.id.rlYear);
        rlLifeTime = view.findViewById(R.id.rlLifeTime);
    }
}