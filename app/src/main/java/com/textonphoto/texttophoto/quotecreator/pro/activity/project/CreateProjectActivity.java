package com.textonphoto.texttophoto.quotecreator.pro.activity.project;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.textonphoto.texttophoto.quotecreator.pro.R;
import com.textonphoto.texttophoto.quotecreator.pro.activity.base.BaseActivity;
import com.textonphoto.texttophoto.quotecreator.pro.adapter.ViewPagerAddFragmentsAdapter;
import com.textonphoto.texttophoto.quotecreator.pro.fragment.create.ColorFragment;
import com.textonphoto.texttophoto.quotecreator.pro.fragment.create.MyAppFragment;
import com.textonphoto.texttophoto.quotecreator.pro.fragment.create.RecentFragment;

import java.util.Objects;

public class CreateProjectActivity extends BaseActivity {

    private boolean isBackground;

    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private TextView tvRecent, tvMyApp, tvColor;
    private ImageView ivRecent, ivBack;
    private View vRecent, vMyApp, vColor;

    private RecentFragment recentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_project);

        init();
    }

    private void init() {
        ivBack = findViewById(R.id.ivBack);
        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPage);

        isBackground = getIntent().getBooleanExtra("pickBG", false);
        setUp();
    }

    private void setUp() {
        setUpLayout();
        evenClick();
    }

    private void evenClick() {
        ivBack.setOnClickListener(v -> onBackPressed());
    }

    private void setUpLayout() {
        setUpViewPager();

        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            switch (position) {
                case 0:
                    tab.setCustomView(R.layout.custom_tab_recent);
                    break;
                case 1:
                    tab.setCustomView(R.layout.custom_tab_myapp);
                    break;
                case 2:
                    tab.setCustomView(R.layout.custom_tab_color);
                    break;
            }
        }).attach();

        tabLayout.selectTab(tabLayout.getTabAt(1));
        viewPager.setCurrentItem(1, true);

        setUpListenerTabLayout();
    }

    private void setUpListenerTabLayout() {
        vRecent = Objects.requireNonNull(Objects.requireNonNull(tabLayout.getTabAt(0)).getCustomView());
        vMyApp = Objects.requireNonNull(Objects.requireNonNull(tabLayout.getTabAt(1)).getCustomView());
        vColor = Objects.requireNonNull(Objects.requireNonNull(tabLayout.getTabAt(2)).getCustomView());

        tvRecent = vRecent.findViewById(R.id.tvRecent);
        tvMyApp = vMyApp.findViewById(R.id.tvMyApp);
        tvColor = vColor.findViewById(R.id.tvColor);
        ivRecent = vRecent.findViewById(R.id.ivRecent);

        vRecent.setOnClickListener(v -> {
            if (viewPager.getCurrentItem() == 0) recentFragment.setExpand(ivRecent, "");
            else {
                viewPager.setCurrentItem(0, true);
                recentFragment.setExpand(ivRecent, "click");
            }
        });

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                checkTab(true, tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                checkTab(false, tab.getPosition());
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void setUpViewPager() {
        ViewPagerAddFragmentsAdapter fragmentsAdapter = new ViewPagerAddFragmentsAdapter(getSupportFragmentManager(), getLifecycle());

        recentFragment = RecentFragment.newInstance(isBackground, folder -> tvRecent.setText(folder), isBG -> finish());
        MyAppFragment myAppFragment = MyAppFragment.newInstance(isBackground, isBG -> finish());
        ColorFragment colorFragment = ColorFragment.newInstance(isBackground, isBG -> finish());

        fragmentsAdapter.addFrag(recentFragment);
        fragmentsAdapter.addFrag(myAppFragment);
        fragmentsAdapter.addFrag(colorFragment);

        viewPager.setAdapter(fragmentsAdapter);
    }

    private void checkTab(boolean isSelect, int position) {
        if (isSelect) {
            switch (position) {
                case 0:
                    vRecent.setBackgroundResource(R.drawable.tab_indicator_check);
                    tvRecent.setTextColor(getResources().getColor(R.color.pink));
                    ivRecent.setImageResource(R.drawable.ic_bottom_pink);
                    break;
                case 1:
                    vMyApp.setBackgroundResource(R.drawable.tab_indicator_check);
                    tvMyApp.setTextColor(getResources().getColor(R.color.pink));
                    break;
                case 2:
                    vColor.setBackgroundResource(R.drawable.tab_indicator_check);
                    tvColor.setTextColor(getResources().getColor(R.color.pink));
                    break;
            }
        } else {
            switch (position) {
                case 0:
                    vRecent.setBackgroundResource(R.drawable.tab_indicator_uncheck);
                    tvRecent.setTextColor(getResources().getColor(R.color.black));
                    ivRecent.setImageResource(R.drawable.ic_bottom_black);
                    recentFragment.setExpand(null, "");
                    break;
                case 1:
                    vMyApp.setBackgroundResource(R.drawable.tab_indicator_uncheck);
                    tvMyApp.setTextColor(getResources().getColor(R.color.black));
                    break;
                case 2:
                    vColor.setBackgroundResource(R.drawable.tab_indicator_uncheck);
                    tvColor.setTextColor(getResources().getColor(R.color.black));
                    break;
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkTab(true, tabLayout.getSelectedTabPosition());
    }
}