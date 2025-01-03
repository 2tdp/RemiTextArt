package com.textonphoto.texttophoto.quotecreator.pro.fragment.create;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.textonphoto.texttophoto.quotecreator.pro.R;
import com.textonphoto.texttophoto.quotecreator.pro.activity.edit.EditActivity;
import com.textonphoto.texttophoto.quotecreator.pro.adapter.home.ColorAdapter;
import com.textonphoto.texttophoto.quotecreator.pro.callback.ICheckTouch;
import com.textonphoto.texttophoto.quotecreator.pro.data.DataColor;
import com.textonphoto.texttophoto.quotecreator.pro.model.ColorModel;
import com.textonphoto.texttophoto.quotecreator.pro.model.TemplateModel;
import com.textonphoto.texttophoto.quotecreator.pro.sharepref.DataLocalManager;
import com.textonphoto.texttophoto.quotecreator.pro.utils.Utils;

import java.util.ArrayList;

public class ColorFragment extends Fragment {

    private RecyclerView rcvColor;

    private ICheckTouch clickTouch;
    private boolean isBackground;

    public ColorFragment(){}

    public ColorFragment(ICheckTouch clickTouch) {
        this.clickTouch = clickTouch;
    }

    public static ColorFragment newInstance(boolean isBG, ICheckTouch checkTouch) {
        ColorFragment fragment = new ColorFragment(checkTouch);
        Bundle args = new Bundle();
        args.putBoolean("isBG", isBG);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_color, container, false);

        init(view);

        return view;
    }

    private void init(View view) {
        rcvColor = view.findViewById(R.id.rcvColor);

        if (getArguments() != null) isBackground = getArguments().getBoolean("isBG");

        setUpLayout();
        evenClick();
    }

    private void evenClick() {

    }

    private void setUpLayout() {
        ArrayList<ColorModel> lstColor = new ArrayList<>(DataColor.getListColor(requireContext()));

        ColorAdapter colorAdapter = new ColorAdapter(requireContext(), R.layout.item_color, (o, pos) -> {
            if (pos == 0) DataColor.pickColor(requireContext(), this::sendColor);
            else {
                ColorModel color = (ColorModel) o;
                if (color.getColorStart() == color.getColorEnd()) sendColor(color);
                else DataColor.pickDirection(requireContext(), color, this::sendColor);
            }
        });
        colorAdapter.setData(lstColor);

        GridLayoutManager manager = new GridLayoutManager(requireContext(), 4);
        rcvColor.setLayoutManager(manager);
        rcvColor.setAdapter(colorAdapter);
    }

    private void sendColor(ColorModel color) {
        DataLocalManager.setColor(color, "color");
        DataLocalManager.setOption("", "bitmap");
        DataLocalManager.setOption("", "bitmap_myapp");
        DataLocalManager.setTemp(new TemplateModel(), "temp");
        if (!isBackground)
            Utils.setIntent(requireActivity(), EditActivity.class.getName());
        else clickTouch.checkTouch(true);
    }
}