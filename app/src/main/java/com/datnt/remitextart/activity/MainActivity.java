package com.datnt.remitextart.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.app.ActivityOptionsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.datnt.remitextart.R;
import com.datnt.remitextart.activity.base.BaseActivity;
import com.datnt.remitextart.activity.edit.EditActivity;
import com.datnt.remitextart.activity.project.CreateProjectActivity;
import com.datnt.remitextart.activity.project.ProjectsActivity;
import com.datnt.remitextart.activity.template.TemplateActivity;
import com.datnt.remitextart.adapter.ProjectAdapter;
import com.datnt.remitextart.fragment.vip.VipOneFragment;
import com.datnt.remitextart.model.Project;
import com.datnt.remitextart.model.TemplateModel;
import com.datnt.remitextart.sharepref.DataLocalManager;
import com.datnt.remitextart.utils.Utils;

import org.wysaid.common.Common;
import org.wysaid.nativePort.CGENativeLibrary;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class MainActivity extends BaseActivity {

    private ProjectAdapter projectAdapter;

    private RelativeLayout rlTop;
    private ImageView ivVip;
    private TextView tvCrePro, tvTemp, tvPro, tvProjects, tvShowAll;

    private RecyclerView rcvProject;

    private final CGENativeLibrary.LoadImageCallback mLoadImageCallback = new CGENativeLibrary.LoadImageCallback() {

        //Notice: the 'name' passed in is just what you write in the rule, e.g: 1.jpg
        @Override
        public Bitmap loadImage(String name, Object arg) {

            Log.d("2tdp", "Loading file: " + name);
            AssetManager am = getAssets();
            InputStream is;
            try {
                is = am.open("filter_blend/" + name);
            } catch (IOException e) {
//                Log.d("2tdp", "Loading file: can't load file");
                Log.d("2tdp", "Loading file: " + name);
                return BitmapFactory.decodeFile(name);
            }

            return BitmapFactory.decodeStream(is);
        }

        @Override
        public void loadImageOK(Bitmap bmp, Object arg) {
            Log.i(Common.LOG_TAG, "Loading bitmap over, you can choose to recycle or cache");

            //The bitmap is which you returned at 'loadImage'.
            //You can call recycle when this function is called, or just keep it for further usage.
            bmp.recycle();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        CGENativeLibrary.setLoadImageCallback(mLoadImageCallback, null);
    }


    private void init() {
        setUpLayout();
        evenClick();

        setData();
    }

    private void setData() {
        ArrayList<Project> lstProject = DataLocalManager.getListProject(this, "lstProject");

        projectAdapter = new ProjectAdapter(this, R.layout.item_project_home, (o, pos) -> {
            DataLocalManager.setProject((Project) o, Utils.PROJECT);
            DataLocalManager.setInt(pos, "indexProject");
            DataLocalManager.setColor(null, "color");
            DataLocalManager.setOption("", "bitmap");
            DataLocalManager.setOption("", "bitmap_myapp");
            DataLocalManager.setTemp(new TemplateModel(), "temp");

            Intent intent = new Intent(MainActivity.this, EditActivity.class);
            startActivity(intent, ActivityOptionsCompat.makeCustomAnimation(this, R.anim.slide_in_right, R.anim.slide_out_left).toBundle());
        });

        if (!lstProject.isEmpty()) {
            projectAdapter.setData(lstProject);
            rlTop.setVisibility(View.VISIBLE);
            tvProjects.setVisibility(View.GONE);
        } else {
            rlTop.setVisibility(View.GONE);
            tvProjects.setVisibility(View.VISIBLE);
        }

        GridLayoutManager manager = new GridLayoutManager(this, 2);
        rcvProject.setLayoutManager(manager);
        rcvProject.setAdapter(projectAdapter);
    }

    private void evenClick() {
        tvCrePro.setOnClickListener(v -> {
            View view = LayoutInflater.from(this).inflate(R.layout.dialog_click_create, null);

            ImageView ivUseTemp = view.findViewById(R.id.ivUseTemp);
            ImageView ivCreate = view.findViewById(R.id.ivCreate);
            TextView tvCancel = view.findViewById(R.id.tvCancel);

            AlertDialog dialog = new AlertDialog.Builder(this, R.style.SheetDialog).create();
            dialog.setView(view);
            dialog.setCancelable(false);
            dialog.show();

            tvCancel.setOnClickListener(v1 -> dialog.cancel());
            ivUseTemp.setOnClickListener(v2 -> {
                Utils.setIntent(this, TemplateActivity.class.getName());
                dialog.cancel();
            });

            ivCreate.setOnClickListener(v3 -> {
                Utils.setIntent(this, CreateProjectActivity.class.getName());
                dialog.cancel();
            });
        });

        tvPro.setOnClickListener(v -> {
            Utils.setIntent(this, ProjectsActivity.class.getName());
        });

        tvTemp.setOnClickListener(v -> {
            Utils.setIntent(this, TemplateActivity.class.getName());
        });

        ivVip.setOnClickListener(v -> clickVip());

        tvShowAll.setOnClickListener(v -> Utils.setIntent(this, ProjectsActivity.class.getName()));
    }

    private void clickVip() {
        VipOneFragment vipOneFragment = VipOneFragment.newInstance();
        Utils.replaceFragment(getSupportFragmentManager(), vipOneFragment, false, true);
    }

    private void setUpLayout() {
        ivVip = findViewById(R.id.ivVip);
        tvCrePro = findViewById(R.id.tvCreate);
        tvTemp = findViewById(R.id.tvTemp);
        tvPro = findViewById(R.id.tvPro);

        rlTop = findViewById(R.id.rlTop);
        tvProjects = findViewById(R.id.tvProjects);
        rcvProject = findViewById(R.id.rcvProject);
        tvShowAll = findViewById(R.id.tvShowAll);
    }

    @Override
    protected void onResume() {
        super.onResume();

        ArrayList<Project> lstProject = DataLocalManager.getListProject(this, Utils.LIST_PROJECT);
        projectAdapter.setData(lstProject);
        if (!lstProject.isEmpty()) {
            rlTop.setVisibility(View.VISIBLE);
            tvProjects.setVisibility(View.GONE);
        } else {
            rlTop.setVisibility(View.GONE);
            tvProjects.setVisibility(View.VISIBLE);
        }
    }
}