package com.datnt.remitextart.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.datnt.remitextart.R;
import com.datnt.remitextart.adapter.home.RecentAdapter;
import com.datnt.remitextart.callback.ICallBackItem;
import com.datnt.remitextart.data.DataPic;
import com.datnt.remitextart.model.picture.BucketPicModel;
import com.datnt.remitextart.model.picture.PicModel;
import com.datnt.remitextart.sharepref.DataLocalManager;
import com.datnt.remitextart.utils.Utils;

import java.util.ArrayList;

public class ImageFragment extends Fragment {

    private ImageView ivBack, ivLoading;
    private RecyclerView rcvPickImage;

    private ArrayList<BucketPicModel> lstBucket;
    private RecentAdapter recentAdapter;

    private ICallBackItem callBack;

    public ImageFragment() {
    }

    public ImageFragment(ICallBackItem callBack) {
        this.callBack = callBack;
    }

    public static ImageFragment newInstance(ICallBackItem callBack) {
        return new ImageFragment(callBack);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (DataLocalManager.getListBucket("bucket").isEmpty()) {
            new Thread(() -> {
                DataPic.getBucketPictureList(requireContext());
                handler.sendEmptyMessage(0);
            }).start();
        } else handler.sendEmptyMessage(1);
    }

    Handler handler = new Handler(Looper.getMainLooper(), new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            if (ivLoading.getVisibility() == View.VISIBLE) ivLoading.setVisibility(View.GONE);
            if (msg.what == 0) {
                lstBucket = DataLocalManager.getListBucket("bucket");

                recentAdapter.setData(lstBucket.get(0).getLstPic());
            } else {
                ArrayList<PicModel> lstPic = DataLocalManager.getListBucket("bucket").get(0).getLstPic();
                if (!lstPic.isEmpty()) recentAdapter.setData(lstPic);
            }
            return true;
        }
    });

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_image, container, false);

        init(v);
        evenClick();

        return v;
    }

    private void evenClick() {
        ivBack.setOnClickListener(v -> Utils.clearBackStack(requireActivity().getSupportFragmentManager()));

        if (ivLoading.getVisibility() == View.GONE) ivLoading.setVisibility(View.VISIBLE);

        recentAdapter = new RecentAdapter(requireContext(), (o, pos) -> {
            callBack.callBackItem(o, pos);
            Utils.clearBackStack(requireActivity().getSupportFragmentManager());
        });

        GridLayoutManager manager = new GridLayoutManager(requireContext(), 3);

        rcvPickImage.setLayoutManager(manager);
        rcvPickImage.setAdapter(recentAdapter);
    }

    private void init(View view) {
        ivBack = view.findViewById(R.id.ivBack);
        ivLoading = view.findViewById(R.id.ivLoading);
        rcvPickImage = view.findViewById(R.id.rcvPickImage);
    }

    public boolean getBack() {
        return rcvPickImage.getVisibility() == View.VISIBLE;
    }
}