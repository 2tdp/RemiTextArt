package com.datnt.remitextart.activity.project;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.datnt.remitextart.R;
import com.datnt.remitextart.activity.Settings;
import com.datnt.remitextart.activity.base.BaseActivity;
import com.datnt.remitextart.sharepref.DataLocalManager;
import com.datnt.remitextart.utils.Utils;

public class ProjectsActivity extends BaseActivity {

    private ImageView ivBack, ivSettings;
    private TextView tvProjects;

    private RecyclerView rcvProject;
//    private ProjectAdapter projectAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_projects);

        init();
    }

    private void init() {
        setUpLayout();
        evenClick();
    }

    private void evenClick() {
        ivBack.setOnClickListener(v -> onBackPressed());
        ivSettings.setOnClickListener(v -> Utils.setIntent(this, Settings.class.getName()));
    }

    private void setUpLayout() {
        ivBack = findViewById(R.id.ivBack);
        ivSettings = findViewById(R.id.ivSettings);
        rcvProject = findViewById(R.id.rcvProject);
        tvProjects = findViewById(R.id.tvProjects);

//        setData();
    }

//    private void setData() {
//        ArrayList<Project> lstProject = DataLocalManager.getListProject("lstProject");
//
//        projectAdapter = new ProjectAdapter(this, (o, pos) -> {
//            DataLocalManager.setCheck("isProject", true);
//            DataLocalManager.setProject((Project) o, "project");
//        });
//
//        if (!lstProject.isEmpty()) {
//            projectAdapter.setData(lstProject);
//            tvProjects.setVisibility(View.GONE);
//        } else tvProjects.setVisibility(View.VISIBLE);
//
//        GridLayoutManager manager = new GridLayoutManager(this, 2);
//        rcvProject.setLayoutManager(manager);
//        rcvProject.setAdapter(projectAdapter);
//    }
}