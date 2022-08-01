package com.datnt.remitextart.fragment.create;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.datnt.remitextart.R;
import com.datnt.remitextart.activity.edit.EditActivity;
import com.datnt.remitextart.activity.project.CreateProjectActivity;
import com.datnt.remitextart.adapter.BucketAdapter;
import com.datnt.remitextart.adapter.home.RecentAdapter;
import com.datnt.remitextart.callback.ICheckTouch;
import com.datnt.remitextart.callback.IClickFolder;
import com.datnt.remitextart.data.DataPic;
import com.datnt.remitextart.model.TemplateModel;
import com.datnt.remitextart.model.picture.BucketPicModel;
import com.datnt.remitextart.model.picture.PicModel;
import com.datnt.remitextart.sharepref.DataLocalManager;
import com.datnt.remitextart.utils.Utils;

import java.util.ArrayList;

public class RecentFragment extends Fragment {

    private static final String IS_BACKGROUND = "isBackground";

    private ArrayList<BucketPicModel> lstBucket;
    private RelativeLayout rlExpand;
    private RecyclerView rcvPicRecent, rcvBucket;
    private BucketAdapter bucketAdapter;
    private RecentAdapter recentAdapter;
    private View vBg;
    private ImageView ivExpand, ivLoading;
    private Animation animation;

    private boolean isBackground;
    private IClickFolder clickFolder;
    private ICheckTouch checkTouch;

    public RecentFragment() {

    }

    public RecentFragment(IClickFolder clickFolder, ICheckTouch checkTouch) {
        this.clickFolder = clickFolder;
        this.checkTouch = checkTouch;
    }

    public static RecentFragment newInstance(boolean isBackground, IClickFolder clickFolder, ICheckTouch checkTouch) {
        RecentFragment fragment = new RecentFragment(clickFolder, checkTouch);
        Bundle args = new Bundle();
        args.putBoolean(IS_BACKGROUND, isBackground);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) isBackground = getArguments().getBoolean(IS_BACKGROUND);

        new Thread(() -> {
            DataPic.getBucketPictureList(requireContext());
            handler.sendEmptyMessage(0);
        }).start();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_recent, container, false);

        init(v);

        return v;
    }

    Handler handler = new Handler(Looper.getMainLooper(), new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            if (msg.what == 0) {
                lstBucket = DataLocalManager.getListBucket("bucket");

                if (ivLoading.getVisibility() == View.VISIBLE) ivLoading.setVisibility(View.GONE);

                bucketAdapter.setData(lstBucket);
                recentAdapter.setData(lstBucket.get(0).getLstPic());
            }
            return true;
        }
    });

    private void init(View view) {
        rcvPicRecent = view.findViewById(R.id.rcvPicRecent);
        rcvBucket = view.findViewById(R.id.rcvBucketPic);
        rlExpand = view.findViewById(R.id.rlExpand);
        vBg = view.findViewById(R.id.viewBg);
        ivLoading = view.findViewById(R.id.ivLoading);

        Glide.with(this)
                .asGif()
                .load(R.drawable.loading)
                .into(ivLoading);

        lstBucket = new ArrayList<>();

        if (ivLoading.getVisibility() == View.GONE) ivLoading.setVisibility(View.VISIBLE);

        rlExpand.getLayoutParams().height = getResources().getDisplayMetrics().heightPixels * 60 / 100;

        setUpLayout();
        evenClick();
    }

    private void evenClick() {
        vBg.setOnClickListener(v -> setExpand(ivExpand, ""));
    }

    private void setUpLayout() {
        setUpPic();
        setUpBucket();
    }

    public void setExpand(ImageView ivExpand, String click) {
        this.ivExpand = ivExpand;
        if (rlExpand != null)
            checkExpand(ivExpand, rlExpand.getVisibility() == View.VISIBLE, click);
    }

    private void checkExpand(ImageView ivExpand, boolean check, String click) {
        if (click.equals("")) {
            if (ivExpand != null) {
                if (check) {
                    animation = AnimationUtils.loadAnimation(requireContext(), R.anim.slide_up);
                    rlExpand.startAnimation(animation);
                    vBg.setVisibility(View.GONE);
                    rlExpand.setVisibility(View.GONE);
                    ivExpand.setImageResource(R.drawable.ic_bottom_pink);
                } else {
                    animation = AnimationUtils.loadAnimation(requireContext(), R.anim.slide_down);
                    rlExpand.startAnimation(animation);
                    vBg.setVisibility(View.VISIBLE);
                    rlExpand.setVisibility(View.VISIBLE);
                    ivExpand.setImageResource(R.drawable.ic_top_pink);
                }
            } else {
                if (check) {
                    animation = AnimationUtils.loadAnimation(requireContext(), R.anim.slide_up);
                    rlExpand.startAnimation(animation);
                    vBg.setVisibility(View.GONE);
                    rlExpand.setVisibility(View.GONE);
                }
            }
        } else {
            if (check) {
                animation = AnimationUtils.loadAnimation(requireContext(), R.anim.slide_up);
                rlExpand.startAnimation(animation);
                vBg.setVisibility(View.GONE);
                rlExpand.setVisibility(View.GONE);
                ivExpand.setImageResource(R.drawable.ic_bottom_pink);
            }
        }
        if (animation != null)
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    rlExpand.clearAnimation();
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
    }

    private void setUpBucket() {
        bucketAdapter = new BucketAdapter(requireContext(), (Object o, int pos) -> {
            BucketPicModel bucket = (BucketPicModel) o;
            recentAdapter.setData(bucket.getLstPic());
            recentAdapter.notifyChange();
            setExpand(ivExpand, "");
            clickFolder.clickFolder(bucket.getBucket());
        });
        if (!lstBucket.isEmpty()) bucketAdapter.setData(lstBucket);

        LinearLayoutManager manager = new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false);
        rcvBucket.setLayoutManager(manager);
        rcvBucket.setAdapter(bucketAdapter);
    }

    private void setUpPic() {
        recentAdapter = new RecentAdapter(requireContext(), (Object o, int pos) -> {
            PicModel pic = (PicModel) o;
            DataLocalManager.setOption(pic.getUri(), "bitmap");
            DataLocalManager.setOption("", "bitmap_myapp");
            DataLocalManager.setColor(null, "color");
            DataLocalManager.setTemp(new TemplateModel(), "temp");
            if (!isBackground)
                Utils.setIntent(requireActivity(), EditActivity.class.getName());
            else checkTouch.checkTouch(true);
        });
        if (!lstBucket.isEmpty()) recentAdapter.setData(lstBucket.get(0).getLstPic());

        GridLayoutManager manager = new GridLayoutManager(requireContext(), 3);
        rcvPicRecent.setLayoutManager(manager);
        rcvPicRecent.setAdapter(recentAdapter);
    }
}