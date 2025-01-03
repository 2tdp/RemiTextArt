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
import com.textonphoto.texttophoto.quotecreator.pro.adapter.home.MyAppAdapter;
import com.textonphoto.texttophoto.quotecreator.pro.callback.ICheckTouch;
import com.textonphoto.texttophoto.quotecreator.pro.data.DataPic;
import com.textonphoto.texttophoto.quotecreator.pro.model.TemplateModel;
import com.textonphoto.texttophoto.quotecreator.pro.sharepref.DataLocalManager;
import com.textonphoto.texttophoto.quotecreator.pro.utils.Utils;

import java.util.ArrayList;

public class MyAppFragment extends Fragment {

    private RecyclerView rcvPicMyApp;

    private ICheckTouch clickTouch;
    private boolean isBackground;

    public MyAppFragment(){}

    public MyAppFragment(ICheckTouch clickTouch) {
        this.clickTouch = clickTouch;
    }

    public static MyAppFragment newInstance(boolean isBG, ICheckTouch checkTouch) {
        MyAppFragment fragment = new MyAppFragment(checkTouch);
        Bundle args = new Bundle();
        args.putBoolean("isBG", isBG);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_app, container, false);

        init(view);

        return view;
    }

    private void init(View view) {
        rcvPicMyApp = view.findViewById(R.id.rcvPicMyApp);
        if (getArguments() != null) isBackground = getArguments().getBoolean("isBG");

        setUpLayout();
        evenClick();
    }

    private void evenClick() {

    }

    private void setUpLayout() {
        ArrayList<String> lstPic = new ArrayList<>(DataPic.getPicAssets(requireContext(), "offline_myapp"));

        MyAppAdapter myAppAdapter = new MyAppAdapter(requireContext(), (Object o, int pos) -> {
            String picAsset = (String) o;
            DataLocalManager.setOption(picAsset, "bitmap_myapp");
            DataLocalManager.setOption("", "bitmap");
            DataLocalManager.setColor(null, "color");
            DataLocalManager.setTemp(new TemplateModel(), "temp");
            if (!isBackground)
                Utils.setIntent(requireActivity(), EditActivity.class.getName());
            else clickTouch.checkTouch(true);
        });

        myAppAdapter.setData(lstPic);
        GridLayoutManager manager = new GridLayoutManager(requireContext(), 3);
        rcvPicMyApp.setLayoutManager(manager);
        rcvPicMyApp.setAdapter(myAppAdapter);
    }
}