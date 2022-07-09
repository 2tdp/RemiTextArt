package com.datnt.remitextart.activity.edit;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityOptionsCompat;

import com.datnt.remitextart.R;
import com.datnt.remitextart.activity.base.BaseActivity;
import com.datnt.remitextart.activity.edit.AddTextActivity;
import com.datnt.remitextart.custom.TextStickerCustom;
import com.datnt.remitextart.customview.ColorView;
import com.datnt.remitextart.customview.CropRatioView;
import com.datnt.remitextart.customview.stickerview.Sticker;
import com.datnt.remitextart.customview.stickerview.StickerView;
import com.datnt.remitextart.model.ColorModel;
import com.datnt.remitextart.model.background.BackgroundModel;
import com.datnt.remitextart.model.text.TextModel;
import com.datnt.remitextart.sharepref.DataLocalManager;
import com.datnt.remitextart.utils.Utils;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class EditActivity extends BaseActivity {

    private final int positionCrop = 0, positionMain = 1, positionColor = 2;
    private final int positionOriginal = 0, position1_1 = 1, position9_16 = 2, position4_5 = 3,
            position16_9 = 4;
    private final int positionAddText = 0, positionEmoji = 1, positionImage = 2, positionBackground = 3,
            positionOverlay = 4, positionDecor = 5, positionSize = 6;

    private HorizontalScrollView vSize, vOperation;
    private LinearLayout llLayerExport;
    private RelativeLayout rlMain, rlAddText, rlEmoji, rlImage, rlBackground, rlOverlay, rlDecor, rlCrop,
            rlExpandEditText;

    //addText
    private RelativeLayout rlDelText, rlET, rlDuplicateText, rlFontSize, rlColorText, rlTransformText,
            rlShadowText, rlOpacityText;

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

        //addSticker
        rlAddText.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddTextActivity.class);
            launcherEditText.launch(intent, ActivityOptionsCompat.makeCustomAnimation(this, R.anim.slide_in_right, R.anim.slide_out_left));
        });
        rlDelText.setOnClickListener(v -> delStick());
        rlET.setOnClickListener(v -> replaceText(vSticker.getCurrentSticker()));
        rlDuplicateText.setOnClickListener(v -> duplicateText(vSticker.getCurrentSticker()));

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
                            Sticker sticker = vSticker.getCurrentSticker();

                            if (sticker instanceof TextStickerCustom) {
                                TextStickerCustom textSticker = (TextStickerCustom) sticker;
                                vSticker.replace(textSticker.getTextModel().replace(this, textSticker, textModel), true);
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
            vSticker.addSticker(textSticker.getTextModel().duplicate(this, getId()));
        }
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
//                    llLayerExport.setVisibility(View.GONE);
                    tvToolBar.setVisibility(View.GONE);
                    ivTick.setVisibility(View.GONE);
                }

                animation = AnimationUtils.loadAnimation(this, R.anim.slide_up_in);
                if (rlExpandEditText.getVisibility() == View.GONE) {
                    rlExpandEditText.setAnimation(animation);
                    rlExpandEditText.setVisibility(View.VISIBLE);
                }
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
        rlDelText = findViewById(R.id.rlDelText);
        rlET = findViewById(R.id.rlET);
        rlDuplicateText = findViewById(R.id.rlDuplicateText);
        rlFontSize = findViewById(R.id.rlFontSize);
        rlColorText = findViewById(R.id.rlColor);
        rlTransformText = findViewById(R.id.rlTransform);
        rlShadowText = findViewById(R.id.rlShadow);
        rlOpacityText = findViewById(R.id.rlOpacity);

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
            dialog.cancel();
        });
    }

    private boolean checkCurrentSticker() {
        if (vSticker.getCurrentSticker() == null) {
            Utils.showToast(this, getResources().getString(R.string.choose_sticker_text));
            return false;
        }
        return true;
    }
}