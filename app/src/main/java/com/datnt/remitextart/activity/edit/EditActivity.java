package com.datnt.remitextart.activity.edit;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityOptionsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.datnt.remitextart.R;
import com.datnt.remitextart.activity.base.BaseActivity;
import com.datnt.remitextart.activity.edit.AddTextActivity;
import com.datnt.remitextart.adapter.ColorAdapter;
import com.datnt.remitextart.custom.TextStickerCustom;
import com.datnt.remitextart.customview.ColorView;
import com.datnt.remitextart.customview.CropRatioView;
import com.datnt.remitextart.customview.CustomSeekbarRunText;
import com.datnt.remitextart.customview.CustomSeekbarTwoWay;
import com.datnt.remitextart.customview.OnSeekbarResult;
import com.datnt.remitextart.customview.stickerview.Sticker;
import com.datnt.remitextart.customview.stickerview.StickerView;
import com.datnt.remitextart.data.DataColor;
import com.datnt.remitextart.model.ColorModel;
import com.datnt.remitextart.model.ShadowModel;
import com.datnt.remitextart.model.background.BackgroundModel;
import com.datnt.remitextart.model.text.ShearTextModel;
import com.datnt.remitextart.model.text.TextModel;
import com.datnt.remitextart.sharepref.DataLocalManager;
import com.datnt.remitextart.utils.Utils;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public class EditActivity extends BaseActivity {

    private final int positionCrop = 0, positionMain = 1, positionColor = 2;
    private final int positionOriginal = 0, position1_1 = 1, position9_16 = 2, position4_5 = 3,
            position16_9 = 4;
    private final int positionAddText = 0, positionEmoji = 1, positionImage = 2, positionBackground = 3,
            positionOverlay = 4, positionDecor = 5, positionSize = 6;

    private HorizontalScrollView vSize, vOperation, vEditText;
    private LinearLayout llLayerExport;
    private RelativeLayout rlMain, rlAddText, rlEmoji, rlImage, rlBackground, rlOverlay, rlDecor, rlCrop,
            rlExpandEditText;

    //addText
    private RelativeLayout rlEditText, rlDelText, rlET, rlDuplicateText, rlFontSize, rlColorText, rlTransformText,
            rlShadowText, rlOpacityText, rlEditFontSize, rlEditColorText, rlEditOpacityText;
    private LinearLayout llEditTransformText, llEditShadowText;
    private SeekBar sbFontText;
    private CustomSeekbarTwoWay sbShearX, sbShearY, sbStretch, sbXPosText, sbYPosText, sbBlurText;
    private CustomSeekbarRunText sbOpacityText;
    private TextView tvTitleEditText, tvResetText, tvFontText, tvShearX, tvShearY, tvStretch,
            tvXPosText, tvYPosText, tvBlurText;
    private ImageView ivColorText, ivColorBlurText;
    private RecyclerView rcvColorText;
    private float blur = 0f;

    private ImageView ivBack, vMain, ivTick, ivExport, ivLayer, ivOriginal, iv1_1, iv9_16, iv4_5,
            iv16_9;
    private TextView tvToolBar, tvOriginal, tv1_1, tv9_16, tv4_5, tv16_9;
    private CropRatioView vCrop;
    private ColorView vColor;
    private StickerView vSticker;

    private Animation animation;
    private Bitmap bmMain, bmRoot;
    private BackgroundModel backgroundModel;
    private boolean isColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        init();
    }

    private void init() {
        setUpLayout();
        evenClick();

        vSticker.setOnStickerOperationListener(new StickerView.OnStickerOperationListener() {
            @Override
            public void onStickerAdded(@NonNull Sticker sticker) {
                vSticker.hideBorderAndIcon(1);
                vSticker.invalidate();
            }

            @Override
            public void onStickerClicked(@NonNull Sticker sticker) {
                vSticker.hideBorderAndIcon(1);
                vSticker.invalidate();

                if (sticker instanceof TextStickerCustom) seekAndHideOperation(positionAddText);
            }

            @Override
            public void onStickerDeleted(@NonNull Sticker sticker) {
                Utils.showToast(EditActivity.this, "Deleted!");
            }

            @Override
            public void onStickerDragFinished(@NonNull Sticker sticker) {
                vSticker.hideBorderAndIcon(1);
                vSticker.invalidate();

                if (sticker instanceof TextStickerCustom) seekAndHideOperation(positionAddText);
            }

            @Override
            public void onStickerTouchedDown(@NonNull Sticker sticker) {

            }

            @Override
            public void onStickerZoomFinished(@NonNull Sticker sticker) {
                vSticker.hideBorderAndIcon(1);
                vSticker.invalidate();
            }

            @Override
            public void onStickerFlipped(@NonNull Sticker sticker) {

            }

            @Override
            public void onStickerDoubleTapped(@NonNull Sticker sticker) {

            }
        });
    }

    private void evenClick() {
        //toolbar
        ivTick.setOnClickListener(v -> clickTick());
        ivBack.setOnClickListener(v -> onBackPressed());
        ivExport.setOnClickListener(v -> exportPhoto());

        rlMain.setOnClickListener(v -> {
            vSticker.setCurrentSticker(null);
            seekAndHideOperation(-1);
        });
        rlEditText.setOnClickListener(v -> {
            if (vEditText.getVisibility() == View.VISIBLE) {
                vSticker.setCurrentSticker(null);
                seekAndHideOperation(-1);
            } else seekAndHideOperation(positionAddText);
        });

        //addSticker
        rlAddText.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddTextActivity.class);
            launcherEditText.launch(intent, ActivityOptionsCompat.makeCustomAnimation(this, R.anim.slide_in_right, R.anim.slide_out_left));
        });
        rlDelText.setOnClickListener(v -> delStick());
        rlET.setOnClickListener(v -> replaceText(vSticker.getCurrentSticker()));
        rlDuplicateText.setOnClickListener(v -> duplicateText(vSticker.getCurrentSticker()));
        rlFontSize.setOnClickListener(v -> fontSizeText(vSticker.getCurrentSticker()));
        rlColorText.setOnClickListener(v -> colorText(vSticker.getCurrentSticker()));
//        rlTransformText.setOnClickListener(v -> transformText(vSticker.getCurrentSticker()));
        rlShadowText.setOnClickListener(v -> shadowText(vSticker.getCurrentSticker()));
        rlOpacityText.setOnClickListener(v -> opacityText(vSticker.getCurrentSticker()));

        //size
        rlCrop.setOnClickListener(v -> {
            seekAndHideOperation(positionSize);
            vSticker.getLayoutParams().height = LinearLayout.LayoutParams.MATCH_PARENT;
            vSticker.getLayoutParams().width = LinearLayout.LayoutParams.MATCH_PARENT;
        });
        ivOriginal.setOnClickListener(v -> checkSize(positionOriginal));
        iv1_1.setOnClickListener(v -> checkSize(position1_1));
        iv9_16.setOnClickListener(v -> checkSize(position9_16));
        iv4_5.setOnClickListener(v -> checkSize(position4_5));
        iv16_9.setOnClickListener(v -> checkSize(position16_9));
    }

    private void exportPhoto() {
        @SuppressLint("InflateParams")
        View v = LayoutInflater.from(this).inflate(R.layout.dialog_export, null);
        RelativeLayout rlExport = v.findViewById(R.id.rlExport);
        LinearLayout llSavePhoto = v.findViewById(R.id.llSavePhoto);
        LinearLayout llRemove = v.findViewById(R.id.llRemovePhoto);

        BottomSheetDialog dialog = new BottomSheetDialog(this, R.style.SheetDialog);
        dialog.setContentView(v);
        dialog.setCancelable(true);
        dialog.create();
        dialog.show();

        rlExport.setOnClickListener(vCancel -> dialog.cancel());
        llSavePhoto.setOnClickListener(vSave -> {
            Utils.saveImage(this, Utils.overlay(Utils.loadBitmapFromView(vMain), vSticker.saveImage(this)), "");
            dialog.cancel();
        });
        llRemove.setOnClickListener(vRemove -> {
            dialog.cancel();
            Utils.showToast(this, "Remove");
        });
    }

    private void clickTick() {
        int wMain = getResources().getDisplayMetrics().widthPixels;
        int hMain = (int) (getResources().getDisplayMetrics().heightPixels - getResources().getDimension(com.intuit.sdp.R.dimen._245sdp));

        float scaleScreen = (float) wMain / hMain;

        if (!isColor) {
            Bitmap bm = vCrop.getCroppedImage();
            if ((float) bm.getWidth() / bm.getHeight() > scaleScreen)
                bmMain = Bitmap.createScaledBitmap(bm, wMain, wMain * bm.getHeight() / bm.getWidth(), false);
            else
                bmMain = Bitmap.createScaledBitmap(bm, hMain * bm.getWidth() / bm.getHeight(), hMain, false);

            backgroundModel.setUriCache(Utils.saveBitmapToApp(this, bmMain));

            //setSize
            vSticker.getLayoutParams().width = bmMain.getWidth();
            vMain.getLayoutParams().width = bmMain.getWidth();
            vSticker.getLayoutParams().height = bmMain.getHeight();
            vMain.getLayoutParams().height = bmMain.getHeight();

            vMain.setAlpha(backgroundModel.getOpacity());

            seekAndHideViewMain(positionMain, bmMain, null);
        } else {
            vSticker.getLayoutParams().height = (int) vColor.getH();
            vSticker.getLayoutParams().width = (int) vColor.getW();
            backgroundModel.setSizeViewColor(vColor.getSize());
        }
    }

    //AddText
    private final ActivityResultLauncher<Intent> launcherEditText =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    if (result.getData() != null) {
                        boolean isAdd = result.getData().getBooleanExtra("isAdd", true);
                        TextModel textModel = (TextModel) result.getData().getSerializableExtra("text");

                        if (textModel == null) {
                            Utils.showToast(this, "Can't create Sticker");
                            return;
                        }

                        if (isAdd) {
                            TextStickerCustom textSticker = new TextStickerCustom(this, textModel, getId());
                            vSticker.addSticker(textSticker);
                            seekAndHideOperation(positionAddText);
                        } else {
                            TextStickerCustom textSticker = (TextStickerCustom) vSticker.getCurrentSticker();

                            if (textSticker != null) {
                                textSticker.setTextModel(textModel);
                                vSticker.invalidate();
                            }
                        }
                    }
                }
            });

    //Replace Text
    private void replaceText(Sticker sticker) {
        if (!checkCurrentSticker()) return;

        if (sticker instanceof TextStickerCustom) {
            TextStickerCustom textSticker = (TextStickerCustom) sticker;

            Intent intent = new Intent(this, AddTextActivity.class);
            intent.putExtra("text", textSticker.getTextModel());
            launcherEditText.launch(intent, ActivityOptionsCompat.makeCustomAnimation(this, R.anim.slide_in_right, R.anim.slide_out_left));
        }
    }

    //Duplicate Text
    private void duplicateText(Sticker sticker) {
        if (!checkCurrentSticker()) return;

        if (sticker instanceof TextStickerCustom) {
            TextStickerCustom textSticker = (TextStickerCustom) sticker;

            TextStickerCustom newSticker = (TextStickerCustom) textSticker.getTextModel().duplicate(this, getId());
            vSticker.addSticker(newSticker);

            vSticker.invalidate();

//            if (textSticker.getTextModel().getShearTextModel() != null)
//                vSticker.duplicateShearModel(newSticker);
        }
    }

    //Size Text
    private void fontSizeText(Sticker sticker) {
        if (!checkCurrentSticker()) return;
        seeAndHideViewText(0);

        if (sticker instanceof TextStickerCustom) {
            TextStickerCustom textSticker = (TextStickerCustom) sticker;
            int sizeOld = (int) textSticker.getTextSize();
            sbFontText.setProgress(sizeOld);
            tvFontText.setText(String.valueOf(sizeOld));
            tvResetText.setOnClickListener(v -> resetText(0, textSticker, sizeOld, null, null, null, -1));

            sbFontText.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                    vSticker.hideBorderAndIcon(0);
                    textSticker.setTextSize(i);
                    vSticker.invalidate();

                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    vSticker.hideBorderAndIcon(1);
                    vSticker.invalidate();
                }
            });
        }
    }

    //Color Text
    private void colorText(Sticker sticker) {
        seeAndHideViewText(1);
        ArrayList<ColorModel> lstColor = DataColor.getListColor(this);

        ColorAdapter colorAdapter = new ColorAdapter(this, R.layout.item_color_edit, (o, pos) -> {
            ColorModel color = (ColorModel) o;
            if (pos == 0) DataColor.pickColor(this, coloModel -> setTextColor(sticker, color));
            else if (color.getColorStart() == color.getColorEnd()) setTextColor(sticker, color);
            else DataColor.pickDirection(this, color, coloModel -> setTextColor(sticker, color));
        });
        if (!lstColor.isEmpty())
            colorAdapter.setData(lstColor);

        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rcvColorText.setLayoutManager(manager);
        rcvColorText.setAdapter(colorAdapter);
    }

    private void setTextColor(Sticker sticker, ColorModel color) {
        if (!checkCurrentSticker()) return;

        if (sticker instanceof TextStickerCustom) {
            TextStickerCustom textSticker = (TextStickerCustom) sticker;
            if (textSticker.getTextModel().getColorModel() != null) {
                ColorModel colorModel = textSticker.getTextModel().getColorModel();
                tvResetText.setOnClickListener(v -> resetText(1, textSticker, -1, colorModel, null, null, -1));
            } else
                tvResetText.setOnClickListener(v -> resetText(1, textSticker, -1, null, null, null, -1));
            textSticker.setTextColor(color);
            vSticker.invalidate();
        }
    }

    //Transform Text
    private void transformText(Sticker sticker) {
        if (!checkCurrentSticker()) return;
        seeAndHideViewText(2);

        if (sticker instanceof TextStickerCustom) {
            TextStickerCustom textSticker = (TextStickerCustom) sticker;

            setShearCurrent(textSticker);

            sbShearX.setOnSeekbarResult(new OnSeekbarResult() {
                @Override
                public void onDown(View v) {

                }

                @Override
                public void onMove(View v, int value) {
                    tvShearX.setText(String.valueOf(value));
                    textSticker.getTextModel().getShearTextModel().setShearX(value / 100f);
                    vSticker.shearSticker(value / 100f, true);
                }

                @Override
                public void onUp(View v, int value) {

                }
            });
            sbShearY.setOnSeekbarResult(new OnSeekbarResult() {
                @Override
                public void onDown(View v) {

                }

                @Override
                public void onMove(View v, int value) {
                    tvShearY.setText(String.valueOf(value));
                    textSticker.getTextModel().getShearTextModel().setShearY(value / 100f);
                    vSticker.shearSticker(value / 100f, false);
                }

                @Override
                public void onUp(View v, int value) {

                }
            });
            sbStretch.setOnSeekbarResult(new OnSeekbarResult() {
                @Override
                public void onDown(View v) {

                }

                @Override
                public void onMove(View v, int value) {
                    tvStretch.setText(String.valueOf(value));
                    textSticker.getTextModel().getShearTextModel().setStretch(value / 100f);
                    vSticker.stretchSticker(value / 100f);
                }

                @Override
                public void onUp(View v, int value) {

                }
            });
        }
    }

    private void setShearCurrent(TextStickerCustom textSticker) {
        int shearX = 0, shearY = 0, stretch = 0;
        ShearTextModel shearTextModel = textSticker.getTextModel().getShearTextModel();
        if (shearTextModel != null) {
            shearX = (int) (shearTextModel.getShearX() * 100);
            shearY = (int) (shearTextModel.getShearY() * 100);
            stretch = (int) (shearTextModel.getStretch() * 100);

            if (shearX != 0 || shearY != 0 || stretch != 0)
                vSticker.resetShear(shearX / 100f, shearY / 100f, stretch / 100f);
        } else
            textSticker.getTextModel().setShearTextModel(new ShearTextModel(shearX, shearY, stretch));

        ShearTextModel shearTextModelOld = new ShearTextModel(shearX / 100f,
                shearY / 100f, stretch / 100f);

        tvResetText.setOnClickListener(v -> resetText(2, textSticker, -1,
                null, shearTextModelOld, null, -1));

        tvShearX.setText(String.valueOf(shearX));
        sbShearX.setProgress(shearX);
        tvShearY.setText(String.valueOf(shearY));
        sbShearY.setProgress(shearY);
        tvStretch.setText(String.valueOf(stretch));
        sbStretch.setProgress(stretch);
    }

    //Shadow Text
    private void shadowText(Sticker sticker) {
        if (!checkCurrentSticker()) return;
        seeAndHideViewText(3);

        if (sticker instanceof TextStickerCustom) {
            TextStickerCustom textSticker = (TextStickerCustom) sticker;

            setShadowCurrent(textSticker);

            sbXPosText.setOnSeekbarResult(new OnSeekbarResult() {
                @Override
                public void onDown(View v) {

                }

                @Override
                public void onMove(View v, int value) {
                    tvXPosText.setText(String.valueOf(value));
                    textSticker.getTextModel().getShadowModel().setXPos(value);
                    textSticker.getTextModel().getShadowModel().setBlur(blur);
                    vSticker.replace(textSticker.getTextModel().shadow(EditActivity.this, textSticker), true);
                }

                @Override
                public void onUp(View v, int value) {

                }
            });

            sbYPosText.setOnSeekbarResult(new OnSeekbarResult() {
                @Override
                public void onDown(View v) {

                }

                @Override
                public void onMove(View v, int value) {
                    tvYPosText.setText(String.valueOf(value));
                    textSticker.getTextModel().getShadowModel().setYPos(value);
                    textSticker.getTextModel().getShadowModel().setBlur(blur);
                    vSticker.replace(textSticker.getTextModel().shadow(EditActivity.this, textSticker), true);
                }

                @Override
                public void onUp(View v, int value) {

                }
            });

            sbBlurText.setOnSeekbarResult(new OnSeekbarResult() {
                @Override
                public void onDown(View v) {

                }

                @Override
                public void onMove(View v, int value) {
                    if (value == -50) blur = 1f;
                    else if (value == 50) blur = 10f;
                    else if (value == 0) blur = 5f;
                    else blur = 5 + (value * 5 / 50f);

                    tvBlurText.setText(String.valueOf(value));
                    textSticker.getTextModel().getShadowModel().setBlur(blur);
                    vSticker.replace(textSticker.getTextModel().shadow(EditActivity.this, textSticker), true);
                }

                @Override
                public void onUp(View v, int value) {

                }
            });

            ivColorBlurText.setOnClickListener(v -> DataColor.pickColor(this, (color) -> {
                textSticker.getTextModel().getShadowModel().setColorBlur(color.getColorStart());
                vSticker.replace(textSticker.getTextModel().shadow(EditActivity.this, textSticker), true);
            }));
        }
    }

    private void setShadowCurrent(TextStickerCustom textSticker) {
        float xPos = 0f, yPos = 0f;
        int color = 0;
        ShadowModel shadowModel = textSticker.getTextModel().getShadowModel();
        if (shadowModel != null) {
            xPos = (shadowModel.getXPos());
            yPos = (shadowModel.getYPos());
            blur = (shadowModel.getBlur());
            color = shadowModel.getColorBlur();
        } else
            textSticker.getTextModel().setShadowModel(new ShadowModel(xPos, yPos, blur, color));

        ShadowModel shadowModelOld = new ShadowModel(xPos, yPos, blur, color);

        tvResetText.setOnClickListener(v -> resetText(3, textSticker, -1,
                null, null, shadowModelOld, -1));

        tvXPosText.setText(String.valueOf((int) xPos));
        sbXPosText.setProgress((int) xPos);
        tvYPosText.setText(String.valueOf((int) yPos));
        sbYPosText.setProgress((int) yPos);
        if (blur == 0) tvBlurText.setText(String.valueOf(0));
        else tvBlurText.setText(String.valueOf((int) ((blur - 5) * 10f)));

        if (blur > 5) sbBlurText.setProgress((int) ((blur - 5) * 10f));
        else if (blur == 0f) {
            sbBlurText.setProgress(0);
            blur = 5f;
        } else sbBlurText.setProgress((int) ((5 - blur) * 10f));

        textSticker.getTextModel().getShadowModel().setBlur(blur);
    }

    //Opacity Text
    private void opacityText(Sticker sticker) {
        if (!checkCurrentSticker()) return;
        seeAndHideViewText(4);

        if (sticker instanceof TextStickerCustom) {
            TextStickerCustom textSticker = (TextStickerCustom) sticker;

            int opacityOld = textSticker.getTextModel().getOpacity();
            sbOpacityText.setProgress(opacityOld);
            tvResetText.setOnClickListener(v -> resetText(4, textSticker, -1, null,
                    null, null, opacityOld));

            sbOpacityText.setOnSeekbarResult(new OnSeekbarResult() {
                @Override
                public void onDown(View v) {

                }

                @Override
                public void onMove(View v, int value) {
                    textSticker.getTextModel().setOpacity(value);
                    vSticker.replace(textSticker.getTextModel().opacity(EditActivity.this, textSticker), true);
                }

                @Override
                public void onUp(View v, int value) {

                }
            });
        }
    }

    //ResetText
    private void resetText(int position, TextStickerCustom textSticker, int sizeText, ColorModel colorModel,
                           ShearTextModel shearTextModel, ShadowModel shadowModel, int opacity) {
        switch (position) {
            case 0:
                textSticker.setTextSize(sizeText);
                sbFontText.setProgress(sizeText);
                tvFontText.setText(String.valueOf(sizeText));
                break;
            case 1:
                if (colorModel != null) textSticker.setTextColor(colorModel);
                else
                    textSticker.setTextColor(new ColorModel(Color.BLACK, Color.BLACK, 0, false));

                vSticker.invalidate();
                break;
            case 2:
                int shearX = (int) shearTextModel.getShearX() * 100;
                int shearY = (int) shearTextModel.getShearY() * 100;
                int stretch = (int) shearTextModel.getStretch() * 100;

                tvShearX.setText(String.valueOf(shearX));
                sbShearX.setProgress(shearX);
                tvShearY.setText(String.valueOf(shearY));
                sbShearY.setProgress(shearY);
                tvStretch.setText(String.valueOf(stretch));
                sbStretch.setProgress(stretch);

                //resetShear
                vSticker.resetShear(0, 0, 0);

                //setShear
                vSticker.shearSticker(shearX / 100f, true);
                vSticker.shearSticker(shearY / 100f, false);
                vSticker.stretchSticker(stretch / 100f);

                textSticker.getTextModel().setShearTextModel(new ShearTextModel(shearX / 100f,
                        shearY / 100f, stretch / 100f));
                break;
            case 3:
                float xPos = shadowModel.getXPos();
                float yPos = shadowModel.getYPos();
                float blurOld = shadowModel.getBlur();
                int color = shadowModel.getColorBlur();

                tvXPosText.setText(String.valueOf((int) xPos));
                sbXPosText.setProgress((int) xPos);
                tvYPosText.setText(String.valueOf((int) yPos));
                sbYPosText.setProgress((int) yPos);
                if (blurOld == 0f) tvBlurText.setText(String.valueOf(0));
                else tvBlurText.setText(String.valueOf((int) ((blurOld - 5) * 10f)));
                if (blurOld > 5) sbBlurText.setProgress((int) ((blurOld - 5) * 10f));
                else if (blurOld == 0f) {
                    sbBlurText.setProgress(0);
                } else sbBlurText.setProgress((int) ((5 - blurOld) * 10f));

                textSticker.getTextModel().getShadowModel().setXPos(xPos);
                textSticker.getTextModel().getShadowModel().setYPos(yPos);
                textSticker.getTextModel().getShadowModel().setBlur(blurOld);
                textSticker.getTextModel().getShadowModel().setColorBlur(color);

                vSticker.replace(textSticker.getTextModel().shadow(EditActivity.this, textSticker), true);
                break;
            case 4:
                textSticker.getTextModel().setOpacity(opacity);
                textSticker.setAlpha((int) (opacity * 255 / 100f));
                sbOpacityText.setProgress(opacity);

                vSticker.invalidate();
                break;
        }
    }

    private void seeAndHideViewText(int position) {
        animation = AnimationUtils.loadAnimation(this, R.anim.slide_down_out);
        if (vEditText.getVisibility() == View.VISIBLE) {
            vEditText.setAnimation(animation);
            vEditText.setVisibility(View.GONE);
        }

        switch (position) {
            case 0:
                animation = AnimationUtils.loadAnimation(this, R.anim.slide_up_in);
                if (rlEditFontSize.getVisibility() == View.GONE) {
                    rlEditFontSize.setAnimation(animation);
                    rlEditFontSize.setVisibility(View.VISIBLE);
                }

                tvTitleEditText.setText(getResources().getString(R.string.font_size));

                if (tvResetText.getVisibility() == View.GONE)
                    tvResetText.setVisibility(View.VISIBLE);
                break;
            case 1:
                animation = AnimationUtils.loadAnimation(this, R.anim.slide_up_in);
                if (rlEditColorText.getVisibility() == View.GONE) {
                    rlEditColorText.setAnimation(animation);
                    rlEditColorText.setVisibility(View.VISIBLE);
                }

                tvTitleEditText.setText(getResources().getString(R.string.color));

                if (tvResetText.getVisibility() == View.GONE)
                    tvResetText.setVisibility(View.VISIBLE);
                break;
            case 2:
                animation = AnimationUtils.loadAnimation(this, R.anim.slide_up_in);
                if (llEditTransformText.getVisibility() == View.GONE) {
                    llEditTransformText.setAnimation(animation);
                    llEditTransformText.setVisibility(View.VISIBLE);
                }

                tvTitleEditText.setText(getResources().getString(R.string.transform));
                tvShearX.setText(String.valueOf(0));
                tvShearY.setText(String.valueOf(0));
                tvStretch.setText(String.valueOf(0));
                sbShearX.setMax(100);
                sbShearY.setMax(100);
                sbStretch.setMax(200);

                if (tvResetText.getVisibility() == View.GONE)
                    tvResetText.setVisibility(View.VISIBLE);
                break;
            case 3:
                animation = AnimationUtils.loadAnimation(this, R.anim.slide_up_in);
                if (llEditShadowText.getVisibility() == View.GONE) {
                    llEditShadowText.setAnimation(animation);
                    llEditShadowText.setVisibility(View.VISIBLE);
                }

                tvTitleEditText.setText(getResources().getString(R.string.shadow));
                tvXPosText.setText(String.valueOf(0));
                tvYPosText.setText(String.valueOf(0));
                tvBlurText.setText(String.valueOf(0));
                sbXPosText.setMax(100);
                sbYPosText.setMax(100);
                sbBlurText.setMax(100);

                if (tvResetText.getVisibility() == View.GONE)
                    tvResetText.setVisibility(View.VISIBLE);
                break;
            case 4:
                animation = AnimationUtils.loadAnimation(this, R.anim.slide_up_in);
                if (rlEditOpacityText.getVisibility() == View.GONE) {
                    rlEditOpacityText.setAnimation(animation);
                    rlEditOpacityText.setVisibility(View.VISIBLE);
                }

                tvTitleEditText.setText(getResources().getString(R.string.opacity));
                sbOpacityText.setColorText(getResources().getColor(R.color.green));
                sbOpacityText.setSizeText(com.intuit.ssp.R.dimen._10ssp);
                sbOpacityText.setProgress(100);
                sbOpacityText.setMax(100);

                if (tvResetText.getVisibility() == View.GONE)
                    tvResetText.setVisibility(View.VISIBLE);
                break;
        }
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                rlExpandEditText.clearAnimation();
                rlEditFontSize.clearAnimation();
                llEditTransformText.clearAnimation();
                llEditShadowText.clearAnimation();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    public void getData() {
        String strPicUser = DataLocalManager.getOption("bitmap");
        String strPicApp = DataLocalManager.getOption("bitmap_myapp");
        ColorModel colorModel = DataLocalManager.getColor("color");
        if (!strPicUser.equals("")) {
            backgroundModel.setUriRoot(strPicUser);
            bmRoot = Utils.getBitmapFromUri(this, Uri.parse(strPicUser));
            seekAndHideViewMain(positionCrop, bmRoot, null);
            return;
        }
        if (!strPicApp.equals("")) {
            backgroundModel.setUriRoot(strPicApp);
            bmRoot = Utils.getBitmapFromAsset(this, "offline_myapp", strPicApp, false, false);
            seekAndHideViewMain(positionCrop, bmRoot, null);
            return;
        }
        if (colorModel != null) {
            backgroundModel.setColorModel(colorModel);
            seekAndHideViewMain(positionColor, null, colorModel);
        }
    }

    private void seekAndHideOperation(int position) {
        if (vSticker.getStickerCount() == 0)
            ivLayer.setImageResource(R.drawable.ic_layer_uncheck);
        else ivLayer.setImageResource(R.drawable.ic_layer);

        switch (position) {
            case positionAddText:
                animation = AnimationUtils.loadAnimation(this, R.anim.slide_down_out);
                if (vSize.getVisibility() == View.VISIBLE) {
                    vSize.setAnimation(animation);
                    vSize.setVisibility(View.GONE);
                    llLayerExport.setVisibility(View.GONE);
                    tvToolBar.setVisibility(View.VISIBLE);
                    ivTick.setVisibility(View.VISIBLE);
                }

                if (vOperation.getVisibility() == View.VISIBLE) {
                    vOperation.setAnimation(animation);
                    vOperation.setVisibility(View.GONE);
                    tvToolBar.setVisibility(View.GONE);
                    ivTick.setVisibility(View.GONE);
                }

                if (rlEditFontSize.getVisibility() == View.VISIBLE) {
                    rlEditFontSize.setAnimation(animation);
                    rlEditFontSize.setVisibility(View.GONE);
                }

                if (rlEditColorText.getVisibility() == View.VISIBLE) {
                    rlEditColorText.setAnimation(animation);
                    rlEditColorText.setVisibility(View.GONE);
                }

                if (llEditTransformText.getVisibility() == View.VISIBLE) {
                    llEditTransformText.setAnimation(animation);
                    llEditTransformText.setVisibility(View.GONE);
                }

                if (llEditShadowText.getVisibility() == View.VISIBLE) {
                    llEditShadowText.setAnimation(animation);
                    llEditShadowText.setVisibility(View.GONE);
                }

                if (rlEditOpacityText.getVisibility() == View.VISIBLE) {
                    rlEditOpacityText.setAnimation(animation);
                    rlEditOpacityText.setVisibility(View.GONE);
                }

                if (tvResetText.getVisibility() == View.VISIBLE)
                    tvResetText.setVisibility(View.GONE);

                animation = AnimationUtils.loadAnimation(this, R.anim.slide_up_in);
                if (rlExpandEditText.getVisibility() == View.GONE) {
                    rlExpandEditText.setAnimation(animation);
                    rlExpandEditText.setVisibility(View.VISIBLE);
                }
                if (vEditText.getVisibility() == View.GONE) {
                    vEditText.setAnimation(animation);
                    vEditText.setVisibility(View.VISIBLE);
                }
                TextStickerCustom textSticker = (TextStickerCustom) vSticker.getCurrentSticker();
                if (textSticker != null)
                    if (textSticker.getTextModel().getColorModel() != null)
                        ivColorText.setImageDrawable(createGradientDrawable(textSticker.getTextModel().getColorModel()));
                    else
                        ivColorText.setImageDrawable(createGradientDrawable(new ColorModel(Color.BLACK, Color.BLACK, 0, false)));
                break;
            case positionSize:
                seekAndHideViewMain(positionCrop, bmRoot, null);
                break;
            default:
                animation = AnimationUtils.loadAnimation(this, R.anim.slide_down_out);
                if (vSize.getVisibility() == View.VISIBLE) {
                    vSize.setAnimation(animation);
                    vSize.setVisibility(View.GONE);
                    llLayerExport.setVisibility(View.GONE);
                    tvToolBar.setVisibility(View.VISIBLE);
                    ivTick.setVisibility(View.VISIBLE);
                }

                if (rlExpandEditText.getVisibility() == View.VISIBLE) {
                    rlExpandEditText.setAnimation(animation);
                    rlExpandEditText.setVisibility(View.GONE);
                }

                animation = AnimationUtils.loadAnimation(this, R.anim.slide_up_in);
                if (vOperation.getVisibility() == View.GONE) {
                    vOperation.setAnimation(animation);
                    vOperation.setVisibility(View.VISIBLE);
                    llLayerExport.setVisibility(View.VISIBLE);
                    tvToolBar.setVisibility(View.GONE);
                    ivTick.setVisibility(View.GONE);
                }
                break;
        }

        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                vSize.clearAnimation();
                vOperation.clearAnimation();
                rlExpandEditText.clearAnimation();
                llEditTransformText.clearAnimation();
                llEditShadowText.clearAnimation();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void seekAndHideViewMain(@NotNull int position, @Nullable Bitmap bitmap, @Nullable ColorModel colorModel) {
        isColor = false;

        if (vSticker.getStickerCount() == 0)
            ivLayer.setImageResource(R.drawable.ic_layer_uncheck);
        else ivLayer.setImageResource(R.drawable.ic_layer);

        animation = AnimationUtils.loadAnimation(this, R.anim.slide_down_out);
        if (vSize.getVisibility() == View.VISIBLE) {
            vSize.setAnimation(animation);
            vSize.setVisibility(View.GONE);
            llLayerExport.setVisibility(View.GONE);
            tvToolBar.setVisibility(View.VISIBLE);
            ivTick.setVisibility(View.VISIBLE);
        }

        animation = AnimationUtils.loadAnimation(this, R.anim.slide_up_in);
        if (vOperation.getVisibility() == View.GONE) {
            vOperation.setAnimation(animation);
            vOperation.setVisibility(View.VISIBLE);
            llLayerExport.setVisibility(View.VISIBLE);
            tvToolBar.setVisibility(View.GONE);
            ivTick.setVisibility(View.GONE);
        }

        switch (position) {
            case 0:
                animation = AnimationUtils.loadAnimation(this, R.anim.slide_down_out);
                if (vOperation.getVisibility() == View.VISIBLE) {
                    vOperation.setAnimation(animation);
                    vOperation.setVisibility(View.GONE);
                    llLayerExport.setVisibility(View.GONE);
                    tvToolBar.setVisibility(View.GONE);
                    ivTick.setVisibility(View.GONE);
                }

                animation = AnimationUtils.loadAnimation(this, R.anim.slide_up_in);
                if (vSize.getVisibility() == View.GONE) {
                    vSize.setAnimation(animation);
                    vSize.setVisibility(View.VISIBLE);
                    tvToolBar.setVisibility(View.VISIBLE);
                    ivTick.setVisibility(View.VISIBLE);
                }

                if (vCrop.getVisibility() == View.GONE) vCrop.setVisibility(View.VISIBLE);
                if (bitmap != null)
                    vCrop.setData(bitmap);

                if (vMain.getVisibility() == View.VISIBLE) vMain.setVisibility(View.GONE);
                if (vColor.getVisibility() == View.VISIBLE) vColor.setVisibility(View.GONE);
                break;
            case 1:
                if (vMain.getVisibility() == View.GONE) vMain.setVisibility(View.VISIBLE);
                if (bitmap != null)
                    vMain.setImageBitmap(bitmap);

                if (vCrop.getVisibility() == View.VISIBLE) vCrop.setVisibility(View.GONE);
                if (vColor.getVisibility() == View.VISIBLE) vColor.setVisibility(View.GONE);
                break;
            case 2:
                isColor = true;
                if (vColor.getVisibility() == View.GONE) vColor.setVisibility(View.VISIBLE);
                if (colorModel != null)
                    vColor.setData(colorModel);

                if (vMain.getVisibility() == View.VISIBLE) vMain.setVisibility(View.GONE);
                if (vCrop.getVisibility() == View.VISIBLE) vCrop.setVisibility(View.GONE);
                break;
        }

        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                vSize.clearAnimation();
                vOperation.clearAnimation();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void checkSize(int pos) {
        switch (pos) {
            case 0:
                ivOriginal.setBackgroundResource(R.drawable.boder_size_check);
                tvOriginal.setTextColor(getResources().getColor(R.color.black));
                iv1_1.setBackgroundResource(R.drawable.boder_size_uncheck);
                tv1_1.setTextColor(getResources().getColor(R.color.gray));
                iv9_16.setBackgroundResource(R.drawable.boder_size_uncheck);
                tv9_16.setTextColor(getResources().getColor(R.color.gray));
                iv4_5.setBackgroundResource(R.drawable.boder_size_uncheck);
                tv4_5.setTextColor(getResources().getColor(R.color.gray));
                iv16_9.setBackgroundResource(R.drawable.boder_size_uncheck);
                tv16_9.setTextColor(getResources().getColor(R.color.gray));

                if (!isColor) {
                    vCrop.setSize(0);
                    vCrop.setData(bmRoot);
                } else vColor.setSize(0);
                break;
            case 1:
                ivOriginal.setBackgroundResource(R.drawable.boder_size_uncheck);
                tvOriginal.setTextColor(getResources().getColor(R.color.gray));
                iv1_1.setBackgroundResource(R.drawable.boder_size_check);
                tv1_1.setTextColor(getResources().getColor(R.color.black));
                iv9_16.setBackgroundResource(R.drawable.boder_size_uncheck);
                tv9_16.setTextColor(getResources().getColor(R.color.gray));
                iv4_5.setBackgroundResource(R.drawable.boder_size_uncheck);
                tv4_5.setTextColor(getResources().getColor(R.color.gray));
                iv16_9.setBackgroundResource(R.drawable.boder_size_uncheck);
                tv16_9.setTextColor(getResources().getColor(R.color.gray));

                if (!isColor) {
                    vCrop.setSize(1);
                    vCrop.setData(bmRoot);
                } else vColor.setSize(1);
                break;
            case 2:
                ivOriginal.setBackgroundResource(R.drawable.boder_size_uncheck);
                tvOriginal.setTextColor(getResources().getColor(R.color.gray));
                iv1_1.setBackgroundResource(R.drawable.boder_size_uncheck);
                tv1_1.setTextColor(getResources().getColor(R.color.gray));
                iv9_16.setBackgroundResource(R.drawable.boder_size_check);
                tv9_16.setTextColor(getResources().getColor(R.color.black));
                iv4_5.setBackgroundResource(R.drawable.boder_size_uncheck);
                tv4_5.setTextColor(getResources().getColor(R.color.gray));
                iv16_9.setBackgroundResource(R.drawable.boder_size_uncheck);
                tv16_9.setTextColor(getResources().getColor(R.color.gray));

                if (!isColor) {
                    vCrop.setSize(2);
                    vCrop.setData(bmRoot);
                } else vColor.setSize(2);
                break;
            case 3:
                ivOriginal.setBackgroundResource(R.drawable.boder_size_uncheck);
                tvOriginal.setTextColor(getResources().getColor(R.color.gray));
                iv1_1.setBackgroundResource(R.drawable.boder_size_uncheck);
                tv1_1.setTextColor(getResources().getColor(R.color.gray));
                iv9_16.setBackgroundResource(R.drawable.boder_size_uncheck);
                tv9_16.setTextColor(getResources().getColor(R.color.gray));
                iv4_5.setBackgroundResource(R.drawable.boder_size_check);
                tv4_5.setTextColor(getResources().getColor(R.color.black));
                iv16_9.setBackgroundResource(R.drawable.boder_size_uncheck);
                tv16_9.setTextColor(getResources().getColor(R.color.gray));

                if (!isColor) {
                    vCrop.setSize(3);
                    vCrop.setData(bmRoot);
                } else vColor.setSize(3);
                break;
            case 4:
                ivOriginal.setBackgroundResource(R.drawable.boder_size_uncheck);
                tvOriginal.setTextColor(getResources().getColor(R.color.gray));
                iv1_1.setBackgroundResource(R.drawable.boder_size_uncheck);
                tv1_1.setTextColor(getResources().getColor(R.color.gray));
                iv9_16.setBackgroundResource(R.drawable.boder_size_uncheck);
                tv9_16.setTextColor(getResources().getColor(R.color.gray));
                iv4_5.setBackgroundResource(R.drawable.boder_size_uncheck);
                tv4_5.setTextColor(getResources().getColor(R.color.gray));
                iv16_9.setBackgroundResource(R.drawable.boder_size_check);
                tv16_9.setTextColor(getResources().getColor(R.color.black));


                if (!isColor) {
                    vCrop.setSize(4);
                    vCrop.setData(bmRoot);
                } else vColor.setSize(4);
                break;
        }
    }

    private void setUpLayout() {
        rlMain = findViewById(R.id.rlMain);

        //toolbar
        ivBack = findViewById(R.id.ivBack);
        ivTick = findViewById(R.id.ivTick);
        tvToolBar = findViewById(R.id.tvTitle);
        ivExport = findViewById(R.id.ivExport);
        ivLayer = findViewById(R.id.ivLayer);
        llLayerExport = findViewById(R.id.llLayerExport);

        //view Main
        vSticker = findViewById(R.id.stickerView);
        vMain = findViewById(R.id.vMain);
        vCrop = findViewById(R.id.vCrop);
        vColor = findViewById(R.id.vColor);

        //size
        vSize = findViewById(R.id.vSize);
        ivOriginal = findViewById(R.id.ivOriginal);
        iv1_1 = findViewById(R.id.iv1_1);
        iv9_16 = findViewById(R.id.iv9_16);
        iv4_5 = findViewById(R.id.iv4_5);
        iv16_9 = findViewById(R.id.iv16_9);
        tvOriginal = findViewById(R.id.tvOriginal);
        tv1_1 = findViewById(R.id.tv1_1);
        tv9_16 = findViewById(R.id.tv9_16);
        tv4_5 = findViewById(R.id.tv4_5);
        tv16_9 = findViewById(R.id.tv16_9);

        //operation
        vOperation = findViewById(R.id.vOperation);
        rlAddText = findViewById(R.id.rlAddText);
        rlEmoji = findViewById(R.id.rlStick);
        rlImage = findViewById(R.id.rlImage);
        rlBackground = findViewById(R.id.rlBackground);
        rlOverlay = findViewById(R.id.rlBlend);
        rlDecor = findViewById(R.id.rlDecor);
        rlCrop = findViewById(R.id.rlCrop);

        //AddText
        rlExpandEditText = findViewById(R.id.rlExpandEditText);
        vEditText = findViewById(R.id.vEditText);
        rlEditText = findViewById(R.id.rlEditText);
        tvTitleEditText = findViewById(R.id.tvTitleEditText);
        tvResetText = findViewById(R.id.tvResetEditText);
        rlDelText = findViewById(R.id.rlDelText);
        rlET = findViewById(R.id.rlET);
        rlDuplicateText = findViewById(R.id.rlDuplicateText);
        rlFontSize = findViewById(R.id.rlFontSize);
        rlColorText = findViewById(R.id.rlColor);
        ivColorText = findViewById(R.id.ivColor);
        rlTransformText = findViewById(R.id.rlTransform);
        rlShadowText = findViewById(R.id.rlShadow);
        rlOpacityText = findViewById(R.id.rlOpacity);
        rlEditFontSize = findViewById(R.id.rlEditFontSize);
        sbFontText = findViewById(R.id.sbFontSize);
        tvFontText = findViewById(R.id.tvFontSize);
        rlEditColorText = findViewById(R.id.rlEditColor);
        rcvColorText = findViewById(R.id.rcvEditColor);
        llEditTransformText = findViewById(R.id.llEditTransform);
        tvShearX = findViewById(R.id.tvShearX);
        tvShearY = findViewById(R.id.tvShearY);
        tvStretch = findViewById(R.id.tvStretch);
        sbShearX = findViewById(R.id.sbShearX);
        sbShearY = findViewById(R.id.sbShearY);
        sbStretch = findViewById(R.id.sbStretch);
        llEditShadowText = findViewById(R.id.llEditShadow);
        sbXPosText = findViewById(R.id.sbXPos);
        sbYPosText = findViewById(R.id.sbYPos);
        sbBlurText = findViewById(R.id.sbBlur);
        tvXPosText = findViewById(R.id.tvXPos);
        tvYPosText = findViewById(R.id.tvYPos);
        tvBlurText = findViewById(R.id.tvBlur);
        ivColorBlurText = findViewById(R.id.ivColorBlur);
        rlOpacityText = findViewById(R.id.rlOpacity);
        rlEditOpacityText = findViewById(R.id.rlEditOpacityText);
        sbOpacityText = findViewById(R.id.sbOpacityText);

        backgroundModel = new BackgroundModel();

        getData();
    }

    private int getId() {

        if (vSticker.getStickerCount() == 0) return 0;

        return vSticker.getStickerCount() + 1;
    }

    //delete Sticker
    private void delStick() {
        if (!checkCurrentSticker()) return;

        @SuppressLint("InflateParams")
        View v = LayoutInflater.from(EditActivity.this).inflate(R.layout.dialog_del_sticker, null, false);
        TextView tvNo = v.findViewById(R.id.tvNo);
        TextView tvYes = v.findViewById(R.id.tvYes);

        AlertDialog dialog = new AlertDialog.Builder(EditActivity.this, R.style.SheetDialog).create();
        dialog.setView(v);
        dialog.setCancelable(false);
        dialog.show();

        tvNo.setOnClickListener(vNo -> dialog.cancel());
        tvYes.setOnClickListener(vYes -> {
            vSticker.remove(vSticker.getCurrentSticker());
            seekAndHideOperation(-1);
            dialog.cancel();
        });
    }

    private GradientDrawable createGradientDrawable(ColorModel color) {
        switch (color.getDirec()) {
            case 0:
                return createGradient(GradientDrawable.Orientation.TOP_BOTTOM, color.getColorStart(), color.getColorEnd());
            case 1:
                return createGradient(GradientDrawable.Orientation.TL_BR, color.getColorStart(), color.getColorEnd());
            case 2:
                return createGradient(GradientDrawable.Orientation.LEFT_RIGHT, color.getColorStart(), color.getColorEnd());
            case 3:
                return createGradient(GradientDrawable.Orientation.BL_TR, color.getColorStart(), color.getColorEnd());
            case 4:
                return createGradient(GradientDrawable.Orientation.BOTTOM_TOP, color.getColorStart(), color.getColorEnd());
            case 5:
                return createGradient(GradientDrawable.Orientation.RIGHT_LEFT, color.getColorStart(), color.getColorEnd());
            default:
                return null;
        }
    }

    private GradientDrawable createGradient(GradientDrawable.Orientation direc, int start, int end) {
        GradientDrawable gradientDrawable = new GradientDrawable(direc, new int[]{start, end});
        gradientDrawable.setGradientType(GradientDrawable.LINEAR_GRADIENT);
        gradientDrawable.setCornerRadius(18f);

        return gradientDrawable;
    }

    private boolean checkCurrentSticker() {
        if (vSticker.getCurrentSticker() == null) {
            Utils.showToast(this, getResources().getString(R.string.choose_sticker_text));
            return false;
        }
        return true;
    }
}