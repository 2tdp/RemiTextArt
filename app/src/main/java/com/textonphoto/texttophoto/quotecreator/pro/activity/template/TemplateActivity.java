package com.textonphoto.texttophoto.quotecreator.pro.activity.template;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.textonphoto.texttophoto.quotecreator.pro.R;
import com.textonphoto.texttophoto.quotecreator.pro.activity.base.BaseActivity;
import com.textonphoto.texttophoto.quotecreator.pro.activity.edit.EditActivity;
import com.textonphoto.texttophoto.quotecreator.pro.adapter.TemplateAdapter;
import com.textonphoto.texttophoto.quotecreator.pro.data.DataTemplate;
import com.textonphoto.texttophoto.quotecreator.pro.model.TemplateModel;
import com.textonphoto.texttophoto.quotecreator.pro.sharepref.DataLocalManager;
import com.textonphoto.texttophoto.quotecreator.pro.utils.Utils;

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