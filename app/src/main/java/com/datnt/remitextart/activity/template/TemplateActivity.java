package com.datnt.remitextart.activity.template;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.datnt.remitextart.R;
import com.datnt.remitextart.activity.base.BaseActivity;
import com.datnt.remitextart.activity.edit.EditActivity;
import com.datnt.remitextart.adapter.TemplateAdapter;
import com.datnt.remitextart.data.DataTemplate;
import com.datnt.remitextart.model.TemplateModel;
import com.datnt.remitextart.sharepref.DataLocalManager;
import com.datnt.remitextart.utils.Utils;

public class TemplateActivity extends BaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_template);

        init();
    }

    private void init() {
        RecyclerView rcvTemplate = findViewById(R.id.rcvTemplate);
        ImageView ivBack = findViewById(R.id.ivBack);
        ivBack.setOnClickListener(v -> onBackPressed());

        TemplateAdapter templateAdapter = new TemplateAdapter(this, R.layout.item_template, (o, pos) -> {
            TemplateModel template = (TemplateModel) o;
            DataLocalManager.setTemp(template, "temp");
            DataLocalManager.setOption("", "bitmap");
            DataLocalManager.setOption("", "bitmap_myapp");
            DataLocalManager.setColor(null, "color");
            Utils.setIntent(this, EditActivity.class.getName());
        });

        templateAdapter.setData(DataTemplate.getTemplate(this, ""));

        GridLayoutManager manager = new GridLayoutManager(this, 2);
        rcvTemplate.setLayoutManager(manager);
        rcvTemplate.setAdapter(templateAdapter);
    }
}