package com.textonphoto.texttophoto.quotecreator.pro.activity.project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.app.ActivityOptionsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.textonphoto.texttophoto.quotecreator.pro.R;
import com.textonphoto.texttophoto.quotecreator.pro.activity.base.BaseActivity;
import com.textonphoto.texttophoto.quotecreator.pro.activity.edit.EditActivity;
import com.textonphoto.texttophoto.quotecreator.pro.activity.setting.Settings;
import com.textonphoto.texttophoto.quotecreator.pro.adapter.ProjectAdapter;
import com.textonphoto.texttophoto.quotecreator.pro.model.Project;
import com.textonphoto.texttophoto.quotecreator.pro.model.TemplateModel;
import com.textonphoto.texttophoto.quotecreator.pro.sharepref.DataLocalManager;
import com.textonphoto.texttophoto.quotecreator.pro.utils.Utils;

import java.util.ArrayList;

public class ProjectsActivity extends BaseActivity {

    private ProjectAdapter projectAdapter;

    private ImageView ivBack, ivSettings;
    private TextView tvProjects;

    private RecyclerView rcvProject;

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

        setData();
    }

    private void setData() {
        ArrayList<Project> lstProject = DataLocalManager.getListProject(this, Utils.LIST_PROJECT);

        projectAdapter = new ProjectAdapter(this, R.layout.item_template, (o, pos) -> {
            DataLocalManager.setProject((Project) o, Utils.PROJECT);
            DataLocalManager.setInt(pos, "indexProject");
            DataLocalManager.setColor(null, "color");
            DataLocalManager.setOption("", "bitmap");
            DataLocalManager.setOption("", "bitmap_myapp");
            DataLocalManager.setTemp(new TemplateModel(), "temp");

            Intent intent = new Intent(ProjectsActivity.this, EditActivity.class);
            startActivity(intent, ActivityOptionsCompat.makeCustomAnimation(this, R.anim.slide_in_right, R.anim.slide_out_left).toBundle());
        });


        if (!lstProject.isEmpty()) {
            projectAdapter.setData(lstProject);
            tvProjects.setVisibility(View.GONE);
        } else tvProjects.setVisibility(View.VISIBLE);

        GridLayoutManager manager = new GridLayoutManager(this, 2);
        rcvProject.setLayoutManager(manager);
        rcvProject.setAdapter(projectAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();

        ArrayList<Project> lstProject = DataLocalManager.getListProject(this, Utils.LIST_PROJECT);
        if (!lstProject.isEmpty()) {
            projectAdapter.setData(lstProject);
            tvProjects.setVisibility(View.GONE);
        } else tvProjects.setVisibility(View.VISIBLE);
    }
}