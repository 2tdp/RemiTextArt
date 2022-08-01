package com.datnt.remitextart.activity.edit;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.datnt.remitextart.R;
import com.datnt.remitextart.activity.base.BaseActivity;
import com.datnt.remitextart.adapter.ViewPagerAddFragmentsAdapter;
import com.datnt.remitextart.adapter.emoji.TitleEmojiAdapter;
import com.datnt.remitextart.adapter.textadapter.FontsAdapter;
import com.datnt.remitextart.adapter.textadapter.QuotesAdapter;
import com.datnt.remitextart.adapter.textadapter.TitleQuotesAdapter;
import com.datnt.remitextart.adapter.textadapter.TypeFontAdapter;
import com.datnt.remitextart.data.DataEmoji;
import com.datnt.remitextart.data.text.DataFont;
import com.datnt.remitextart.data.text.DataQuotes;
import com.datnt.remitextart.fragment.EmojiFragment;
import com.datnt.remitextart.fragment.QuotesFragment;
import com.datnt.remitextart.model.ColorModel;
import com.datnt.remitextart.model.EmojiModel;
import com.datnt.remitextart.model.text.FontModel;
import com.datnt.remitextart.model.text.QuoteModel;
import com.datnt.remitextart.model.text.TextModel;
import com.datnt.remitextart.model.text.TypeFontModel;
import com.datnt.remitextart.sharepref.DataLocalManager;
import com.datnt.remitextart.utils.Utils;

import java.util.ArrayList;

public class AddTextActivity extends BaseActivity {

    private ImageView ivBack, ivTick, ivLeft, ivCenter, ivRight;
    private TextView tvQuotes, tvFonts, tvStyle, tvFavorite, tvFont, tvClear;
    private EditText etText;
    private ViewPager2 vpQuotes;
    private RecyclerView rcvTitleQuotes, rcvFonts, rcvStyleFont;
    private RelativeLayout rlFonts, rlQuotes, rlText;
    private LinearLayout llStyleFont;
    private Animation animation;
    private FontModel font;
    private TextModel textModel;
    private TypeFontAdapter typeFontAdapter;
    private FontsAdapter fontsAdapter;
    private TitleQuotesAdapter titleQuotesAdapter;
    private ArrayList<FontModel> lstFont;
    private int positionStyleFont, positionFont, posGravity;
    private boolean check, isEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_text);

        init();
    }

    private void init() {
        setUpLayout();
        evenClick();
    }

    private void evenClick() {
        ivBack.setOnClickListener(v -> onBackPressed());
        tvClear.setOnClickListener(v -> etText.setText(""));

        rlText.setOnClickListener(v -> Utils.hideKeyboard(this, etText));

        ivLeft.setOnClickListener(v -> changeStateAlignText(0));
        ivCenter.setOnClickListener(v -> changeStateAlignText(1));
        ivRight.setOnClickListener(v -> changeStateAlignText(2));

        tvQuotes.setOnClickListener(v -> changeStateText(0));
        tvFonts.setOnClickListener(v -> changeStateText(1));
        tvStyle.setOnClickListener(v -> changeStateText(2));

        tvFavorite.setOnClickListener(v -> {
            changePickFont(0);
            ArrayList<FontModel> lstFavorite = DataLocalManager.getListFont("lstFavoriteFont");
            if (fontsAdapter != null && !lstFavorite.isEmpty())
                fontsAdapter.setData(lstFavorite);
            else if (fontsAdapter != null) fontsAdapter.setData(new ArrayList<>());
        });

        tvFont.setOnClickListener(v -> {
            changePickFont(1);
            if (fontsAdapter != null) fontsAdapter.setData(lstFont);
        });

        ivTick.setOnClickListener(v -> clickTick());
    }

    private void clickTick() {
        String text = etText.getText().toString();
        if (text.equals("")) {
            Utils.showToast(this, getResources().getString(R.string.pls_enter_text));
        } else {
            Intent returnIntent = new Intent();
            if (!isEditText) {
                textModel = new TextModel(text, font, null, null, null,
                        posGravity, false, false, 255, null);
                returnIntent.putExtra("isAdd", true);
            } else {
                textModel.setContent(text);
                textModel.setFontModel(font);
                textModel.setTypeAlign(posGravity);
                returnIntent.putExtra("isAdd", false);
            }
            returnIntent.putExtra("text", textModel);
            setResult(Activity.RESULT_OK, returnIntent);
            Utils.hideKeyboard(this, ivTick);
            onBackPressed();
        }
    }

    private void setUpQuotes() {

        titleQuotesAdapter = new TitleQuotesAdapter(this, (o, pos) -> {
            titleQuotesAdapter.setCurrent(pos);
            vpQuotes.setCurrentItem(pos, true);
        });

        titleQuotesAdapter.setData(DataQuotes.getListTitleQuote(this));
        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rcvTitleQuotes.setLayoutManager(manager);
        rcvTitleQuotes.setAdapter(titleQuotesAdapter);

        ViewPagerAddFragmentsAdapter addFragmentsAdapter = new ViewPagerAddFragmentsAdapter(getSupportFragmentManager(), getLifecycle());
        String[] titleQuotes = new String[]{"celebrate", "death", "knowledge", "love", "morning", "motivation", "sad"};

        for (String s : titleQuotes) {
            QuotesFragment quotesFragment = QuotesFragment.newInstance(s, (o, pos) -> {
                String oldStr = etText.getText().toString();
                etText.setText(oldStr.concat(o.toString()));
            });
            addFragmentsAdapter.addFrag(quotesFragment);
        }

        vpQuotes.setAdapter(addFragmentsAdapter);
        vpQuotes.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                titleQuotesAdapter.setCurrent(position);
                rcvTitleQuotes.smoothScrollToPosition(position);
            }
        });
    }

    private void setUpFonts() {
        fontsAdapter = new FontsAdapter(this, (o, pos) -> {
            font = (FontModel) o;
            etText.setTypeface(Utils.getTypeFace(font.getNameFont(), font.getLstType().get(0).getName(), this));
            font.getLstType().get(0).setSelected(true);
            if (typeFontAdapter != null) typeFontAdapter.setCurrent(0);
            positionStyleFont = 0;
            check = true;
        });
        fontsAdapter.setData(lstFont);
        if (!check) getCurrentStyleFont(lstFont);
        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rcvFonts.setLayoutManager(manager);
        rcvFonts.setAdapter(fontsAdapter);
        rcvFonts.scrollToPosition(positionFont);
        changePickFont(1);
    }

    private void setUpStyle() {
        typeFontAdapter = new TypeFontAdapter(this, (o, pos) -> {
            TypeFontModel styleFont = (TypeFontModel) o;
            etText.setTypeface(Utils.getTypeFace(styleFont.getFont(), styleFont.getName(), this));
            positionStyleFont = pos + 2;
            check = true;
        });
        if (font != null) typeFontAdapter.setData(font.getLstType());
        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rcvStyleFont.setLayoutManager(manager);
        rcvStyleFont.setAdapter(typeFontAdapter);
        rcvStyleFont.scrollToPosition(positionStyleFont);
    }

    private void getCurrentStyleFont(ArrayList<FontModel> lstFont) {
        for (int j = 0; j < lstFont.size(); j++) {
            FontModel f = lstFont.get(j);
            if (f.getNameFont().equals("poppins")) {
                font = f;
                font.setSelected(true);
                positionFont = j + 2;
                for (int i = 0; i < font.getLstType().size(); i++) {
                    TypeFontModel style = font.getLstType().get(i);
                    if (style.getName().trim().equals("Regular")) {
                        style.setSelected(true);
                        positionStyleFont = i + 2;
                    }
                }
            }
        }
    }

    private void changePickFont(int pos) {
        switch (pos) {
            case 0:
                tvFavorite.setBackgroundResource(R.drawable.border_click_font);
                tvFavorite.setTextColor(Color.WHITE);

                tvFont.setBackgroundResource(R.drawable.border_unclick_font);
                tvFont.setTextColor(Color.BLACK);
                break;
            case 1:
                tvFavorite.setBackgroundResource(R.drawable.border_unclick_font);
                tvFavorite.setTextColor(Color.BLACK);

                tvFont.setBackgroundResource(R.drawable.border_click_font);
                tvFont.setTextColor(Color.WHITE);
                break;
        }
    }

    private void changeLayout(int pos) {
        Utils.hideKeyboard(this, etText);
        switch (pos) {
            case 0:
                animation = AnimationUtils.loadAnimation(this, R.anim.slide_up_in);
                if (rlQuotes.getVisibility() == View.GONE) {
                    rlQuotes.startAnimation(animation);
                    rlQuotes.setVisibility(View.VISIBLE);
                    setUpQuotes();
                }

                animation = AnimationUtils.loadAnimation(this, R.anim.slide_up);
                if (rlFonts.getVisibility() == View.VISIBLE) {
                    rlFonts.startAnimation(animation);
                    rlFonts.setVisibility(View.GONE);
                }

                if (llStyleFont.getVisibility() == View.VISIBLE) {
                    llStyleFont.startAnimation(animation);
                    llStyleFont.setVisibility(View.GONE);
                }
                break;
            case 1:
                animation = AnimationUtils.loadAnimation(this, R.anim.slide_up_in);
                if (rlFonts.getVisibility() == View.GONE) {
                    rlFonts.startAnimation(animation);
                    rlFonts.setVisibility(View.VISIBLE);
                    setUpFonts();
                }

                animation = AnimationUtils.loadAnimation(this, R.anim.slide_up);
                if (rlQuotes.getVisibility() == View.VISIBLE) {
                    rlQuotes.startAnimation(animation);
                    rlQuotes.setVisibility(View.GONE);
                }

                if (llStyleFont.getVisibility() == View.VISIBLE) {
                    llStyleFont.startAnimation(animation);
                    llStyleFont.setVisibility(View.GONE);
                }
                break;
            case 2:
                animation = AnimationUtils.loadAnimation(this, R.anim.slide_up_in);
                if (llStyleFont.getVisibility() == View.GONE) {
                    llStyleFont.startAnimation(animation);
                    llStyleFont.setVisibility(View.VISIBLE);
                    setUpStyle();
                }

                animation = AnimationUtils.loadAnimation(this, R.anim.slide_up);
                if (rlQuotes.getVisibility() == View.VISIBLE) {
                    rlQuotes.startAnimation(animation);
                    rlQuotes.setVisibility(View.GONE);
                }

                if (rlFonts.getVisibility() == View.VISIBLE) {
                    rlFonts.startAnimation(animation);
                    rlFonts.setVisibility(View.GONE);
                }
                break;
        }

        if (animation != null)
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    rlFonts.clearAnimation();
                    rlQuotes.clearAnimation();
                    llStyleFont.clearAnimation();
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
    }

    private void changeStateText(int pos) {
        switch (pos) {
            case 0:
                changeLayout(0);
                tvQuotes.setTextColor(getResources().getColor(R.color.white));
                tvQuotes.setBackgroundResource(R.drawable.boder_text_check);
                tvFonts.setBackgroundResource(R.drawable.boder_text);
                tvFonts.setTextColor(getResources().getColor(R.color.black));
                tvStyle.setBackgroundResource(R.drawable.boder_text);
                tvStyle.setTextColor(getResources().getColor(R.color.black));
                break;
            case 1:
                changeLayout(1);
                tvQuotes.setTextColor(getResources().getColor(R.color.black));
                tvQuotes.setBackgroundResource(R.drawable.boder_text);
                tvFonts.setBackgroundResource(R.drawable.boder_text_check);
                tvFonts.setTextColor(getResources().getColor(R.color.white));
                tvStyle.setBackgroundResource(R.drawable.boder_text);
                tvStyle.setTextColor(getResources().getColor(R.color.black));
                break;
            case 2:
                changeLayout(2);
                tvQuotes.setTextColor(getResources().getColor(R.color.black));
                tvQuotes.setBackgroundResource(R.drawable.boder_text);
                tvFonts.setBackgroundResource(R.drawable.boder_text);
                tvFonts.setTextColor(getResources().getColor(R.color.black));
                tvStyle.setBackgroundResource(R.drawable.boder_text_check);
                tvStyle.setTextColor(getResources().getColor(R.color.white));
                break;
        }
    }

    private void changeStateAlignText(int pos) {
        switch (pos) {
            case 0:
                posGravity = 0;
                etText.setGravity(Gravity.START);
                ivLeft.setImageResource(R.drawable.ic_text_align_left_select);
                ivCenter.setImageResource(R.drawable.ic_text_align_center);
                ivRight.setImageResource(R.drawable.ic_text_align_right);
                break;
            case 1:
                posGravity = 1;
                etText.setGravity(Gravity.CENTER);
                ivLeft.setImageResource(R.drawable.ic_text_align_left);
                ivCenter.setImageResource(R.drawable.ic_text_align_center_select);
                ivRight.setImageResource(R.drawable.ic_text_align_right);
                break;
            case 2:
                posGravity = 2;
                etText.setGravity(Gravity.END);
                ivLeft.setImageResource(R.drawable.ic_text_align_left);
                ivCenter.setImageResource(R.drawable.ic_text_align_center);
                ivRight.setImageResource(R.drawable.ic_text_align_right_select);
                break;
        }
    }

    private void setUpLayout() {
        ivBack = findViewById(R.id.ivBack);
        ivTick = findViewById(R.id.ivTick);
        etText = findViewById(R.id.etText);
        ivLeft = findViewById(R.id.ivLeft);
        ivCenter = findViewById(R.id.ivCenter);
        ivRight = findViewById(R.id.ivRight);
        tvQuotes = findViewById(R.id.tvQuotes);
        vpQuotes = findViewById(R.id.vpQuotes);
        rcvTitleQuotes = findViewById(R.id.rcvTitleQuotes);
        tvFonts = findViewById(R.id.tvFonts);
        tvStyle = findViewById(R.id.tvStyle);
        tvFavorite = findViewById(R.id.tvFavorite);
        tvFont = findViewById(R.id.tvFont);
        rcvFonts = findViewById(R.id.rcvFonts);
        rcvStyleFont = findViewById(R.id.rcvStyleFont);
        rlFonts = findViewById(R.id.rlFonts);
        rlQuotes = findViewById(R.id.rlQuotes);
        rlText = findViewById(R.id.rlText);
        llStyleFont = findViewById(R.id.llStyleFont);
        tvClear = findViewById(R.id.tvClear);

        setUpQuotes();

        textModel = (TextModel) getIntent().getSerializableExtra("text");
        if (textModel != null) {
            setUpText();
            isEditText = true;
        } else {
            lstFont = DataFont.getDataFont(this);
            font = new FontModel("poppins", DataFont.getDataTypeFont(this, "poppins"), true, false);
            for (TypeFontModel f : font.getLstType()) {
                if (f.getName().equals("Regular")) f.setSelected(true);
            }
            changeStateText(0);
            changeStateAlignText(0);
            Utils.showSoftKeyboard(this, etText);
        }
    }

    private void setUpText() {
        etText.setText(textModel.getContent());

        switch (textModel.getTypeAlign()) {
            case 0:
                changeStateAlignText(0);
                etText.setGravity(Gravity.START);
                break;
            case 1:
                changeStateAlignText(1);
                etText.setGravity(Gravity.CENTER);
                break;
            case 2:
                changeStateAlignText(2);
                etText.setGravity(Gravity.END);
                break;
        }

        for (int i = 0; i < textModel.getFontModel().getLstType().size(); i++) {
            TypeFontModel f = textModel.getFontModel().getLstType().get(i);
            if (f.isSelected()) {
                positionStyleFont = i + 2;
                etText.setTypeface(Utils.getTypeFace(textModel.getFontModel().getNameFont(), f.getName(), this));
                break;
            }
        }
        lstFont = DataFont.getDataFont(this);
        for (int i = 0; i < lstFont.size(); i++) {
            FontModel f = lstFont.get(i);
            if (f.getNameFont().equals(textModel.getFontModel().getNameFont())) {
                positionFont = i + 2;
                f.setSelected(true);
            }
        }
        font = textModel.getFontModel();
        check = true;
        setUpFonts();
        setUpStyle();
        changeStateText(0);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Utils.setAnimExit(this);
        finish();
    }
}