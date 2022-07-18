package com.datnt.remitextart.activity.edit;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.datnt.remitextart.R;
import com.datnt.remitextart.activity.base.BaseActivity;
import com.datnt.remitextart.activity.project.CreateProjectActivity;
import com.datnt.remitextart.adapter.ColorAdapter;
import com.datnt.remitextart.adapter.FilterBlendImageAdapter;
import com.datnt.remitextart.adapter.ViewPagerAddFragmentsAdapter;
import com.datnt.remitextart.adapter.emoji.TitleEmojiAdapter;
import com.datnt.remitextart.adapter.image.CropImageAdapter;
import com.datnt.remitextart.customsticker.DrawableStickerCustom;
import com.datnt.remitextart.customsticker.TextStickerCustom;
import com.datnt.remitextart.customview.ColorView;
import com.datnt.remitextart.customview.CropImage;
import com.datnt.remitextart.customview.CropRatioView;
import com.datnt.remitextart.customview.CustomSeekbarRunText;
import com.datnt.remitextart.customview.CustomSeekbarTwoWay;
import com.datnt.remitextart.customview.OnSeekbarResult;
import com.datnt.remitextart.customview.stickerview.Sticker;
import com.datnt.remitextart.customview.stickerview.StickerView;
import com.datnt.remitextart.data.DataColor;
import com.datnt.remitextart.data.DataEmoji;
import com.datnt.remitextart.data.DataPic;
import com.datnt.remitextart.data.FilterBlendImage;
import com.datnt.remitextart.fragment.EmojiFragment;
import com.datnt.remitextart.fragment.ImageFragment;
import com.datnt.remitextart.model.ColorModel;
import com.datnt.remitextart.model.EmojiModel;
import com.datnt.remitextart.model.FilterBlendModel;
import com.datnt.remitextart.model.background.AdjustModel;
import com.datnt.remitextart.model.image.ImageModel;
import com.datnt.remitextart.model.ShadowModel;
import com.datnt.remitextart.model.background.BackgroundModel;
import com.datnt.remitextart.model.picture.PicModel;
import com.datnt.remitextart.model.text.ShearTextModel;
import com.datnt.remitextart.model.text.TextModel;
import com.datnt.remitextart.sharepref.DataLocalManager;
import com.datnt.remitextart.utils.Utils;
import com.datnt.remitextart.utils.UtilsAdjust;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.wysaid.nativePort.CGENativeLibrary;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class EditActivity extends BaseActivity {

    private String nameFolder = "";
    private String nameFolderBackground = "";
    private String nameFolderImage = "";
    private final int positionCrop = 0, positionMain = 1, positionColor = 2;
    private final int positionOriginal = 0, position1_1 = 1, position9_16 = 2, position4_5 = 3,
            position16_9 = 4;
    private final int positionAddText = 0, positionEmoji = 1, positionImage = 2, positionBackground = 3,
            positionOverlay = 4, positionDecor = 5, positionSize = 6;

    //background
    private RelativeLayout rlExpandEditBackground, rlDelBackground, rlReplaceBackground, rlAdjustBackground,
            rlFilterBackground, rlOpacityBackground, rlFlipYBackground, rlFlipXBackground, rlEditBackground,
            rlEditAdjust, rlVignette, rlVibrance, rlWarmth, rlHue, rlSaturation, rlWhites, rlBlacks,
            rlShadows, rlHighLight, rlExposure, rlContrast, rlBrightness;
    private TextView tvTitleEditBackground, tvResetBackground, tvVignette, tvVibrance, tvWarmth,
            tvHue, tvSaturation, tvWhites, tvBlacks, tvShadows, tvHighLight, tvExposure, tvContrast,
            tvBrightness, tvAdjustBackground;
    private ImageView ivVignette, ivVibrance, ivWarmth, ivHue, ivSaturation, ivWhites, ivBlacks,
            ivShadows, ivHighLight, ivExposure, ivContrast, ivBrightness;
    private CustomSeekbarTwoWay sbAdjust;

    private Bitmap bmAdjust, bitmap;
    private HorizontalScrollView vEditBackground;

    //image
    private RelativeLayout rlExpandEditImage, rlDelImage, rlReplaceImage, rlDuplicateImage, rlCropImage,
            rlFilterImage, rlShadowImage, rlOpacityImage, rlBlendImage, rlCancelEditImage, rlEditCrop,
            rlEditFilterImage, rlEditOpacityImage, rlEditBlendImage;
    private LinearLayout llEditShadowImage;
    private HorizontalScrollView vEditImage;
    private TextView tvResetImage, tvTitleEditImage, tvCancelCropImage, tvXPosImage, tvYPosImage,
            tvBlurImage;
    private ImageView ivTickCropImage, ivColorBlurImage;
    private CropImage pathCrop;
    private CustomSeekbarTwoWay sbXPosImage, sbYPosImage, sbBlurImage;
    private CustomSeekbarRunText sbOpacityImage;
    private RecyclerView rcvCropImage, rcvEditFilterImage, rcvEditBlendImage;
    private FilterBlendImageAdapter filterBlendImageAdapter;
    private boolean isReplaceImage;

    //emoji
    private RelativeLayout rlExpandEmoji, rlExpandEditEmoji, rlCancelPickEmoji, rlCancelEditEmoji,
            rlDelEmoji, rlReplaceEmoji, rlOpacityEmoji, rlFlipYEmoji, rlFlipXEmoji, rlEditOpacityEmoji;
    private LinearLayout llEditEmoji;
    private RecyclerView rcvTypeEmoji;
    private CustomSeekbarRunText sbOpacityEmoji;
    private TitleEmojiAdapter titleEmojiAdapter;
    private TextView tvTitleEditEmoji, tvResetEmoji;
    private boolean isFirstEmoji, isReplaceEmoji;

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

    //Main
    private HorizontalScrollView vSize, vOperation, vEditText;
    private LinearLayout llLayerExport;
    private RelativeLayout rlMain, rlAddText, rlEmoji, rlImage, rlBackground, rlOverlay, rlDecor, rlCrop,
            rlExpandEditText;
    private ImageView ivBack, vMain, ivTick, ivExport, ivLayer, ivOriginal, iv1_1, iv9_16, iv4_5,
            iv16_9;
    private TextView tvToolBar, tvOriginal, tv1_1, tv9_16, tv4_5, tv16_9;
    private CropRatioView vCrop;
    private ColorView vColor;
    private StickerView vSticker;

    private Sticker stickerOld = null;
    private ViewPager2 vpEmoji;
    private ViewPagerAddFragmentsAdapter addFragmentsAdapter;
    private Animation animation;
    private Bitmap bmMain, bmRoot;
    private BackgroundModel backgroundModel;
    private ArrayList<FilterBlendModel> lstFilter, lstBlend;
    private boolean isColor, isReplaceBackground;
    private String strPicUserOld = "old", strPicAppOld = "old";
    private ColorModel colorModelOld = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        init();
        createProject();
    }

    private void createProject() {
        if (DataLocalManager.getInt(Utils.PROJECT) == -1)
            DataLocalManager.setInt(1, Utils.PROJECT);
        else {
            int count = DataLocalManager.getInt(Utils.PROJECT) + 1;
            DataLocalManager.setInt(count, Utils.PROJECT);
        }
        nameFolder = Utils.PROJECT + "_" + DataLocalManager.getInt(Utils.PROJECT);
        Utils.makeFolder(this, nameFolder);
        nameFolderBackground = nameFolder + "/" + Utils.BACKGROUND;
        Utils.makeFolder(this, nameFolderBackground);
        nameFolderImage = nameFolder + "/" + Utils.IMAGE;
        Utils.makeFolder(this, nameFolderImage);
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

                checkTypeSticker(sticker);
            }

            @Override
            public void onStickerDeleted(@NonNull Sticker sticker) {
                Utils.showToast(EditActivity.this, "Deleted!");
            }

            @Override
            public void onStickerDragFinished(@NonNull Sticker sticker) {
                vSticker.hideBorderAndIcon(1);
                vSticker.invalidate();

                checkTypeSticker(sticker);
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

    private void checkTypeSticker(Sticker sticker) {
        if (sticker != stickerOld || vOperation.getVisibility() == View.VISIBLE) {
            if (sticker instanceof TextStickerCustom) seekAndHideOperation(positionAddText);
            if (sticker instanceof DrawableStickerCustom) {
                DrawableStickerCustom drawableSticker = (DrawableStickerCustom) sticker;

                switch (drawableSticker.getTypeSticker()) {
                    case Utils.EMOJI:
                        seekAndHideOperation(positionEmoji);
                        break;
                    case Utils.IMAGE:
                        seekAndHideOperation(positionImage);
                        break;
                    case Utils.OVERLAY:
                        break;
                    case Utils.DECOR:
                        break;
                    case Utils.TEMPLATE:
                        break;

                }
            }
            stickerOld = sticker;
        }
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
        rlCancelPickEmoji.setOnClickListener(v -> seekAndHideOperation(-1));
        rlCancelEditEmoji.setOnClickListener(v -> {
            if (llEditEmoji.getVisibility() == View.VISIBLE) {
                vSticker.setCurrentSticker(null);
                seekAndHideOperation(-1);
            } else seekAndHideOperation(positionEmoji);
        });
        rlCancelEditImage.setOnClickListener(V -> {
            if (vEditImage.getVisibility() == View.VISIBLE) {
                vSticker.setCurrentSticker(null);
                seekAndHideOperation(-1);
            } else seekAndHideOperation(positionImage);
        });

        //addSticker
        rlAddText.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddTextActivity.class);
            launcherEditText.launch(intent, ActivityOptionsCompat.makeCustomAnimation(this, R.anim.slide_in_right, R.anim.slide_out_left));
        });
        rlDelText.setOnClickListener(v -> delStick(vSticker.getCurrentSticker()));
        rlET.setOnClickListener(v -> replaceText(vSticker.getCurrentSticker()));
        rlDuplicateText.setOnClickListener(v -> duplicateText(vSticker.getCurrentSticker()));
        rlFontSize.setOnClickListener(v -> fontSizeText(vSticker.getCurrentSticker()));
        rlColorText.setOnClickListener(v -> colorText(vSticker.getCurrentSticker()));
//        rlTransformText.setOnClickListener(v -> transformText(vSticker.getCurrentSticker()));
        rlShadowText.setOnClickListener(v -> shadowText(vSticker.getCurrentSticker()));
        rlOpacityText.setOnClickListener(v -> opacityText(vSticker.getCurrentSticker()));

        //emoji
        rlEmoji.setOnClickListener(v -> {
            pickEmoji();
            isReplaceEmoji = false;
        });
        rlDelEmoji.setOnClickListener(v -> delStick(vSticker.getCurrentSticker()));
        rlReplaceEmoji.setOnClickListener(v -> replace(vSticker.getCurrentSticker()));
        rlOpacityEmoji.setOnClickListener(v -> opacity(vSticker.getCurrentSticker()));
        rlFlipXEmoji.setOnClickListener(v -> {
            vSticker.flipCurrentSticker(0);
            flip(vSticker.getCurrentSticker(), true, false);
        });
        rlFlipYEmoji.setOnClickListener(v -> {
            vSticker.flipCurrentSticker(1);
            flip(vSticker.getCurrentSticker(), false, true);
        });

        //image
        rlImage.setOnClickListener(v -> {
            pickImage();
            isReplaceImage = false;
        });
        rlDelImage.setOnClickListener(v -> delStick(vSticker.getCurrentSticker()));
        rlReplaceImage.setOnClickListener(v -> replace(vSticker.getCurrentSticker()));
        rlDuplicateImage.setOnClickListener(v -> duplicate(vSticker.getCurrentSticker()));
        rlCropImage.setOnClickListener(v -> cropImage(vSticker.getCurrentSticker()));
        rlFilterImage.setOnClickListener(v -> filterImage(vSticker.getCurrentSticker()));
        rlShadowImage.setOnClickListener(v -> shadowImage(vSticker.getCurrentSticker()));
        rlOpacityImage.setOnClickListener(v -> opacity(vSticker.getCurrentSticker()));
        rlBlendImage.setOnClickListener(v -> blendImage(vSticker.getCurrentSticker()));

        //background
        rlBackground.setOnClickListener(v -> seekAndHideOperation(positionBackground));
        rlDelBackground.setOnClickListener(v -> {
            DataLocalManager.setOption("", "bitmap");
            DataLocalManager.setOption("", "bitmap_myapp");
            DataLocalManager.setColor(new ColorModel(Color.WHITE, Color.WHITE, 0, false), "color");

            getData();
            Utils.showToast(this, getResources().getString(R.string.del));
        });
        rlReplaceBackground.setOnClickListener(v -> replaceBackground());
        rlAdjustBackground.setOnClickListener(v -> adjustBackground());

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
            Utils.saveImage(this, Utils.overlay(Utils.loadBitmapFromView(vMain),
                    vSticker.saveImage(this)), "remiTextArt");
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

            backgroundModel.setUriCache(Utils.saveBitmapToApp(this, bmMain,
                    nameFolderBackground, Utils.BACKGROUND_ROOT));
            backgroundModel.setUriRoot(Utils.saveBitmapToApp(this, bmMain,
                    nameFolderBackground, Utils.BACKGROUND));

            //setSize
            vSticker.getLayoutParams().width = bmMain.getWidth();
            vMain.getLayoutParams().width = bmMain.getWidth();
            vSticker.getLayoutParams().height = bmMain.getHeight();
            vMain.getLayoutParams().height = bmMain.getHeight();

            vMain.setAlpha(backgroundModel.getOpacity());

            seekAndHideViewMain(positionMain, bmMain, null, false);
        } else {
            vSticker.getLayoutParams().height = (int) vColor.getH();
            vSticker.getLayoutParams().width = (int) vColor.getW();
            backgroundModel.setSizeViewColor(vColor.getSize());
        }
    }

    //Flip
    private void flip(Sticker sticker, boolean flipX, boolean flipY) {
        if (!checkCurrentSticker(sticker)) return;

        DrawableStickerCustom drawableSticker = (DrawableStickerCustom) sticker;
        switch (drawableSticker.getTypeSticker()) {
            case Utils.EMOJI:
                if (flipX)
                    drawableSticker.getEmojiModel().setFlipX(!drawableSticker.getEmojiModel().isFlipX());
                if (flipY)
                    drawableSticker.getEmojiModel().setFlipY(!drawableSticker.getEmojiModel().isFlipY());
                break;
        }
    }

    //Opacity
    private void opacity(Sticker sticker) {
        if (!checkCurrentSticker(sticker)) return;

        DrawableStickerCustom drawableSticker = (DrawableStickerCustom) sticker;

        switch (drawableSticker.getTypeSticker()) {
            case Utils.EMOJI:
                opacityEmoji(drawableSticker);
                break;
            case Utils.IMAGE:
                opacityImage(drawableSticker);
                break;
        }
    }

    //Duplicate
    private void duplicate(Sticker sticker) {
        if (!checkCurrentSticker(sticker)) return;

        DrawableStickerCustom drawableSticker = (DrawableStickerCustom) sticker;
        switch (drawableSticker.getTypeSticker()) {
            case Utils.EMOJI:
                break;
            case Utils.IMAGE:
                vSticker.addSticker(drawableSticker.getImageModel().duplicate(this, getId()));
                break;
        }
    }

    //replace
    private void replace(Sticker sticker) {
        if (!checkCurrentSticker(sticker)) return;

        DrawableStickerCustom drawableSticker = (DrawableStickerCustom) sticker;

        switch (drawableSticker.getTypeSticker()) {
            case Utils.EMOJI:
                isReplaceEmoji = true;
                pickEmoji();
                break;
            case Utils.IMAGE:
                isReplaceImage = true;
                pickImage();
                break;
        }
    }

    //Background
    private void replaceBackground() {
        isReplaceBackground = true;
        Intent intent = new Intent();
        intent.putExtra("pickBG", isReplaceBackground);
        intent.setComponent(new ComponentName(getPackageName(), CreateProjectActivity.class.getName()));
        startActivity(intent, ActivityOptions.makeCustomAnimation(this, R.anim.slide_in_right, R.anim.slide_out_left).toBundle());
    }

    private void adjustBackground() {
        seekAndHideViewBackground(0);
        setUpOptionAdjustBackground(0);

        rlBrightness.setOnClickListener(v -> {
            backgroundModel.setUriCache(Utils.saveBitmapToApp(this, bitmap,
                    nameFolderBackground, Utils.BACKGROUND));
            setUpOptionAdjustBackground(0);
        });
        rlContrast.setOnClickListener(v -> {
            backgroundModel.setUriCache(Utils.saveBitmapToApp(this, bitmap,
                    nameFolderBackground, Utils.BACKGROUND));
            setUpOptionAdjustBackground(1);
        });
        rlExposure.setOnClickListener(v -> {
            backgroundModel.setUriCache(Utils.saveBitmapToApp(this, bitmap, nameFolderBackground, Utils.BACKGROUND));
            setUpOptionAdjustBackground(2);
        });
        rlHighLight.setOnClickListener(v -> {
            backgroundModel.setUriCache(Utils.saveBitmapToApp(this, bitmap, nameFolderBackground, Utils.BACKGROUND));
            setUpOptionAdjustBackground(3);
        });
        rlShadows.setOnClickListener(v -> {
            backgroundModel.setUriCache(Utils.saveBitmapToApp(this, bitmap, nameFolderBackground, Utils.BACKGROUND));
            setUpOptionAdjustBackground(4);
        });
        rlBlacks.setOnClickListener(v -> {
            backgroundModel.setUriCache(Utils.saveBitmapToApp(this, bitmap, nameFolderBackground, Utils.BACKGROUND));
            setUpOptionAdjustBackground(5);
        });
        rlWhites.setOnClickListener(v -> {
            backgroundModel.setUriCache(Utils.saveBitmapToApp(this, bitmap, nameFolderBackground, Utils.BACKGROUND));
            setUpOptionAdjustBackground(6);
        });
        rlSaturation.setOnClickListener(v -> {
            backgroundModel.setUriCache(Utils.saveBitmapToApp(this, bitmap, nameFolderBackground, Utils.BACKGROUND));
            setUpOptionAdjustBackground(7);
        });
        rlHue.setOnClickListener(v -> {
            backgroundModel.setUriCache(Utils.saveBitmapToApp(this, bitmap, nameFolderBackground, Utils.BACKGROUND));
            setUpOptionAdjustBackground(8);
        });
        rlWarmth.setOnClickListener(v -> {
            backgroundModel.setUriCache(Utils.saveBitmapToApp(this, bitmap, nameFolderBackground, Utils.BACKGROUND));
            setUpOptionAdjustBackground(9);
        });
        rlVibrance.setOnClickListener(v -> {
            backgroundModel.setUriCache(Utils.saveBitmapToApp(this, bitmap, nameFolderBackground, Utils.BACKGROUND));
            setUpOptionAdjustBackground(10);
        });
        rlVignette.setOnClickListener(v -> {
            backgroundModel.setUriCache(Utils.saveBitmapToApp(this, bitmap, nameFolderBackground, Utils.BACKGROUND));
            setUpOptionAdjustBackground(11);
        });
    }

    private void setUpOptionAdjustBackground(int pos) {
        switch (pos) {
            case 0:
                ivBrightness.setImageResource(R.drawable.ic_brightness);
                tvBrightness.setTextColor(getResources().getColor(R.color.pink));

                ivContrast.setImageResource(R.drawable.ic_un_contrast);
                tvContrast.setTextColor(getResources().getColor(R.color.black));
                ivExposure.setImageResource(R.drawable.ic_un_exposure);
                tvExposure.setTextColor(getResources().getColor(R.color.black));
                ivHighLight.setImageResource(R.drawable.ic_un_hightlight);
                tvHighLight.setTextColor(getResources().getColor(R.color.black));
                ivShadows.setImageResource(R.drawable.ic_un_shadows);
                tvShadows.setTextColor(getResources().getColor(R.color.black));
                ivBlacks.setImageResource(R.drawable.ic_un_blacks);
                tvBlacks.setTextColor(getResources().getColor(R.color.black));
                ivWhites.setImageResource(R.drawable.ic_un_whites);
                tvWhites.setTextColor(getResources().getColor(R.color.black));
                ivSaturation.setImageResource(R.drawable.ic_un_saturation);
                tvSaturation.setTextColor(getResources().getColor(R.color.black));
                ivHue.setImageResource(R.drawable.ic_un_hue);
                tvHue.setTextColor(getResources().getColor(R.color.black));
                ivWarmth.setImageResource(R.drawable.ic_un_warmth);
                tvWarmth.setTextColor(getResources().getColor(R.color.black));
                ivVibrance.setImageResource(R.drawable.ic_un_vibrance);
                tvVibrance.setTextColor(getResources().getColor(R.color.black));
                ivVignette.setImageResource(R.drawable.ic_un_vignette);
                tvVignette.setTextColor(getResources().getColor(R.color.black));

                adjustEditOption(0);
                break;
            case 1:
                ivContrast.setImageResource(R.drawable.ic_contrast);
                tvContrast.setTextColor(getResources().getColor(R.color.pink));

                ivBrightness.setImageResource(R.drawable.ic_un_brightness);
                tvBrightness.setTextColor(getResources().getColor(R.color.black));
                ivExposure.setImageResource(R.drawable.ic_un_exposure);
                tvExposure.setTextColor(getResources().getColor(R.color.black));
                ivHighLight.setImageResource(R.drawable.ic_un_hightlight);
                tvHighLight.setTextColor(getResources().getColor(R.color.black));
                ivShadows.setImageResource(R.drawable.ic_un_shadows);
                tvShadows.setTextColor(getResources().getColor(R.color.black));
                ivBlacks.setImageResource(R.drawable.ic_un_blacks);
                tvBlacks.setTextColor(getResources().getColor(R.color.black));
                ivWhites.setImageResource(R.drawable.ic_un_whites);
                tvWhites.setTextColor(getResources().getColor(R.color.black));
                ivSaturation.setImageResource(R.drawable.ic_un_saturation);
                tvSaturation.setTextColor(getResources().getColor(R.color.black));
                ivHue.setImageResource(R.drawable.ic_un_hue);
                tvHue.setTextColor(getResources().getColor(R.color.black));
                ivWarmth.setImageResource(R.drawable.ic_un_warmth);
                tvWarmth.setTextColor(getResources().getColor(R.color.black));
                ivVibrance.setImageResource(R.drawable.ic_un_vibrance);
                tvVibrance.setTextColor(getResources().getColor(R.color.black));
                ivVignette.setImageResource(R.drawable.ic_un_vignette);
                tvVignette.setTextColor(getResources().getColor(R.color.black));

                adjustEditOption(1);
                break;
            case 2:
                ivExposure.setImageResource(R.drawable.ic_exposure);
                tvExposure.setTextColor(getResources().getColor(R.color.pink));

                ivContrast.setImageResource(R.drawable.ic_un_contrast);
                tvContrast.setTextColor(getResources().getColor(R.color.black));
                ivBrightness.setImageResource(R.drawable.ic_un_brightness);
                tvBrightness.setTextColor(getResources().getColor(R.color.black));
                ivHighLight.setImageResource(R.drawable.ic_un_hightlight);
                tvHighLight.setTextColor(getResources().getColor(R.color.black));
                ivShadows.setImageResource(R.drawable.ic_un_shadows);
                tvShadows.setTextColor(getResources().getColor(R.color.black));
                ivBlacks.setImageResource(R.drawable.ic_un_blacks);
                tvBlacks.setTextColor(getResources().getColor(R.color.black));
                ivWhites.setImageResource(R.drawable.ic_un_whites);
                tvWhites.setTextColor(getResources().getColor(R.color.black));
                ivSaturation.setImageResource(R.drawable.ic_un_saturation);
                tvSaturation.setTextColor(getResources().getColor(R.color.black));
                ivHue.setImageResource(R.drawable.ic_un_hue);
                tvHue.setTextColor(getResources().getColor(R.color.black));
                ivWarmth.setImageResource(R.drawable.ic_un_warmth);
                tvWarmth.setTextColor(getResources().getColor(R.color.black));
                ivVibrance.setImageResource(R.drawable.ic_un_vibrance);
                tvVibrance.setTextColor(getResources().getColor(R.color.black));
                ivVignette.setImageResource(R.drawable.ic_un_vignette);
                tvVignette.setTextColor(getResources().getColor(R.color.black));

                adjustEditOption(2);
                break;
            case 3:
                ivHighLight.setImageResource(R.drawable.ic_hightlight);
                tvHighLight.setTextColor(getResources().getColor(R.color.pink));

                ivExposure.setImageResource(R.drawable.ic_un_exposure);
                tvExposure.setTextColor(getResources().getColor(R.color.black));
                ivContrast.setImageResource(R.drawable.ic_un_contrast);
                tvContrast.setTextColor(getResources().getColor(R.color.black));
                ivBrightness.setImageResource(R.drawable.ic_un_brightness);
                tvBrightness.setTextColor(getResources().getColor(R.color.black));
                ivShadows.setImageResource(R.drawable.ic_un_shadows);
                tvShadows.setTextColor(getResources().getColor(R.color.black));
                ivBlacks.setImageResource(R.drawable.ic_un_blacks);
                tvBlacks.setTextColor(getResources().getColor(R.color.black));
                ivWhites.setImageResource(R.drawable.ic_un_whites);
                tvWhites.setTextColor(getResources().getColor(R.color.black));
                ivSaturation.setImageResource(R.drawable.ic_un_saturation);
                tvSaturation.setTextColor(getResources().getColor(R.color.black));
                ivHue.setImageResource(R.drawable.ic_un_hue);
                tvHue.setTextColor(getResources().getColor(R.color.black));
                ivWarmth.setImageResource(R.drawable.ic_un_warmth);
                tvWarmth.setTextColor(getResources().getColor(R.color.black));
                ivVibrance.setImageResource(R.drawable.ic_un_vibrance);
                tvVibrance.setTextColor(getResources().getColor(R.color.black));
                ivVignette.setImageResource(R.drawable.ic_un_vignette);
                tvVignette.setTextColor(getResources().getColor(R.color.black));

                adjustEditOption(3);
                break;
            case 4:
                ivShadows.setImageResource(R.drawable.ic_shadows);
                tvShadows.setTextColor(getResources().getColor(R.color.pink));

                ivHighLight.setImageResource(R.drawable.ic_un_hightlight);
                tvHighLight.setTextColor(getResources().getColor(R.color.black));
                ivExposure.setImageResource(R.drawable.ic_un_exposure);
                tvExposure.setTextColor(getResources().getColor(R.color.black));
                ivContrast.setImageResource(R.drawable.ic_un_contrast);
                tvContrast.setTextColor(getResources().getColor(R.color.black));
                ivBrightness.setImageResource(R.drawable.ic_un_brightness);
                tvBrightness.setTextColor(getResources().getColor(R.color.black));
                ivBlacks.setImageResource(R.drawable.ic_un_blacks);
                tvBlacks.setTextColor(getResources().getColor(R.color.black));
                ivWhites.setImageResource(R.drawable.ic_un_whites);
                tvWhites.setTextColor(getResources().getColor(R.color.black));
                ivSaturation.setImageResource(R.drawable.ic_un_saturation);
                tvSaturation.setTextColor(getResources().getColor(R.color.black));
                ivHue.setImageResource(R.drawable.ic_un_hue);
                tvHue.setTextColor(getResources().getColor(R.color.black));
                ivWarmth.setImageResource(R.drawable.ic_un_warmth);
                tvWarmth.setTextColor(getResources().getColor(R.color.black));
                ivVibrance.setImageResource(R.drawable.ic_un_vibrance);
                tvVibrance.setTextColor(getResources().getColor(R.color.black));
                ivVignette.setImageResource(R.drawable.ic_un_vignette);
                tvVignette.setTextColor(getResources().getColor(R.color.black));

                adjustEditOption(4);
                break;
            case 5:
                ivBlacks.setImageResource(R.drawable.ic_blacks);
                tvBlacks.setTextColor(getResources().getColor(R.color.pink));

                ivShadows.setImageResource(R.drawable.ic_un_shadows);
                tvShadows.setTextColor(getResources().getColor(R.color.black));
                ivHighLight.setImageResource(R.drawable.ic_un_hightlight);
                tvHighLight.setTextColor(getResources().getColor(R.color.black));
                ivExposure.setImageResource(R.drawable.ic_un_exposure);
                tvExposure.setTextColor(getResources().getColor(R.color.black));
                ivContrast.setImageResource(R.drawable.ic_un_contrast);
                tvContrast.setTextColor(getResources().getColor(R.color.black));
                ivBrightness.setImageResource(R.drawable.ic_un_brightness);
                tvBrightness.setTextColor(getResources().getColor(R.color.black));
                ivWhites.setImageResource(R.drawable.ic_un_whites);
                tvWhites.setTextColor(getResources().getColor(R.color.black));
                ivSaturation.setImageResource(R.drawable.ic_un_saturation);
                tvSaturation.setTextColor(getResources().getColor(R.color.black));
                ivHue.setImageResource(R.drawable.ic_un_hue);
                tvHue.setTextColor(getResources().getColor(R.color.black));
                ivWarmth.setImageResource(R.drawable.ic_un_warmth);
                tvWarmth.setTextColor(getResources().getColor(R.color.black));
                ivVibrance.setImageResource(R.drawable.ic_un_vibrance);
                tvVibrance.setTextColor(getResources().getColor(R.color.black));
                ivVignette.setImageResource(R.drawable.ic_un_vignette);
                tvVignette.setTextColor(getResources().getColor(R.color.black));

                adjustEditOption(5);
                break;
            case 6:
                ivWhites.setImageResource(R.drawable.ic_whites);
                tvWhites.setTextColor(getResources().getColor(R.color.pink));

                ivBlacks.setImageResource(R.drawable.ic_un_blacks);
                tvBlacks.setTextColor(getResources().getColor(R.color.black));
                ivShadows.setImageResource(R.drawable.ic_un_shadows);
                tvShadows.setTextColor(getResources().getColor(R.color.black));
                ivHighLight.setImageResource(R.drawable.ic_un_hightlight);
                tvHighLight.setTextColor(getResources().getColor(R.color.black));
                ivExposure.setImageResource(R.drawable.ic_un_exposure);
                tvExposure.setTextColor(getResources().getColor(R.color.black));
                ivContrast.setImageResource(R.drawable.ic_un_contrast);
                tvContrast.setTextColor(getResources().getColor(R.color.black));
                ivBrightness.setImageResource(R.drawable.ic_un_brightness);
                tvBrightness.setTextColor(getResources().getColor(R.color.black));
                ivSaturation.setImageResource(R.drawable.ic_un_saturation);
                tvSaturation.setTextColor(getResources().getColor(R.color.black));
                ivHue.setImageResource(R.drawable.ic_un_hue);
                tvHue.setTextColor(getResources().getColor(R.color.black));
                ivWarmth.setImageResource(R.drawable.ic_un_warmth);
                tvWarmth.setTextColor(getResources().getColor(R.color.black));
                ivVibrance.setImageResource(R.drawable.ic_un_vibrance);
                tvVibrance.setTextColor(getResources().getColor(R.color.black));
                ivVignette.setImageResource(R.drawable.ic_un_vignette);
                tvVignette.setTextColor(getResources().getColor(R.color.black));

                adjustEditOption(6);
                break;
            case 7:
                ivSaturation.setImageResource(R.drawable.ic_saturation);
                tvSaturation.setTextColor(getResources().getColor(R.color.pink));

                ivWhites.setImageResource(R.drawable.ic_un_whites);
                tvWhites.setTextColor(getResources().getColor(R.color.black));
                ivBlacks.setImageResource(R.drawable.ic_un_blacks);
                tvBlacks.setTextColor(getResources().getColor(R.color.black));
                ivShadows.setImageResource(R.drawable.ic_un_shadows);
                tvShadows.setTextColor(getResources().getColor(R.color.black));
                ivHighLight.setImageResource(R.drawable.ic_un_hightlight);
                tvHighLight.setTextColor(getResources().getColor(R.color.black));
                ivExposure.setImageResource(R.drawable.ic_un_exposure);
                tvExposure.setTextColor(getResources().getColor(R.color.black));
                ivContrast.setImageResource(R.drawable.ic_un_contrast);
                tvContrast.setTextColor(getResources().getColor(R.color.black));
                ivBrightness.setImageResource(R.drawable.ic_un_brightness);
                tvBrightness.setTextColor(getResources().getColor(R.color.black));
                ivHue.setImageResource(R.drawable.ic_un_hue);
                tvHue.setTextColor(getResources().getColor(R.color.black));
                ivWarmth.setImageResource(R.drawable.ic_un_warmth);
                tvWarmth.setTextColor(getResources().getColor(R.color.black));
                ivVibrance.setImageResource(R.drawable.ic_un_vibrance);
                tvVibrance.setTextColor(getResources().getColor(R.color.black));
                ivVignette.setImageResource(R.drawable.ic_un_vignette);
                tvVignette.setTextColor(getResources().getColor(R.color.black));

                adjustEditOption(7);
                break;
            case 8:
                ivHue.setImageResource(R.drawable.ic_hue);
                tvHue.setTextColor(getResources().getColor(R.color.pink));

                ivSaturation.setImageResource(R.drawable.ic_un_saturation);
                tvSaturation.setTextColor(getResources().getColor(R.color.black));
                ivWhites.setImageResource(R.drawable.ic_un_whites);
                tvWhites.setTextColor(getResources().getColor(R.color.black));
                ivBlacks.setImageResource(R.drawable.ic_un_blacks);
                tvBlacks.setTextColor(getResources().getColor(R.color.black));
                ivShadows.setImageResource(R.drawable.ic_un_shadows);
                tvShadows.setTextColor(getResources().getColor(R.color.black));
                ivHighLight.setImageResource(R.drawable.ic_un_hightlight);
                tvHighLight.setTextColor(getResources().getColor(R.color.black));
                ivExposure.setImageResource(R.drawable.ic_un_exposure);
                tvExposure.setTextColor(getResources().getColor(R.color.black));
                ivContrast.setImageResource(R.drawable.ic_un_contrast);
                tvContrast.setTextColor(getResources().getColor(R.color.black));
                ivBrightness.setImageResource(R.drawable.ic_un_brightness);
                tvBrightness.setTextColor(getResources().getColor(R.color.black));
                ivWarmth.setImageResource(R.drawable.ic_un_warmth);
                tvWarmth.setTextColor(getResources().getColor(R.color.black));
                ivVibrance.setImageResource(R.drawable.ic_un_vibrance);
                tvVibrance.setTextColor(getResources().getColor(R.color.black));
                ivVignette.setImageResource(R.drawable.ic_un_vignette);
                tvVignette.setTextColor(getResources().getColor(R.color.black));

                adjustEditOption(8);
                break;
            case 9:
                ivWarmth.setImageResource(R.drawable.ic_warmth);
                tvWarmth.setTextColor(getResources().getColor(R.color.pink));

                ivHue.setImageResource(R.drawable.ic_un_hue);
                tvHue.setTextColor(getResources().getColor(R.color.black));
                ivSaturation.setImageResource(R.drawable.ic_un_saturation);
                tvSaturation.setTextColor(getResources().getColor(R.color.black));
                ivWhites.setImageResource(R.drawable.ic_un_whites);
                tvWhites.setTextColor(getResources().getColor(R.color.black));
                ivBlacks.setImageResource(R.drawable.ic_un_blacks);
                tvBlacks.setTextColor(getResources().getColor(R.color.black));
                ivShadows.setImageResource(R.drawable.ic_un_shadows);
                tvShadows.setTextColor(getResources().getColor(R.color.black));
                ivHighLight.setImageResource(R.drawable.ic_un_hightlight);
                tvHighLight.setTextColor(getResources().getColor(R.color.black));
                ivExposure.setImageResource(R.drawable.ic_un_exposure);
                tvExposure.setTextColor(getResources().getColor(R.color.black));
                ivContrast.setImageResource(R.drawable.ic_un_contrast);
                tvContrast.setTextColor(getResources().getColor(R.color.black));
                ivBrightness.setImageResource(R.drawable.ic_un_brightness);
                tvBrightness.setTextColor(getResources().getColor(R.color.black));
                ivVibrance.setImageResource(R.drawable.ic_un_vibrance);
                tvVibrance.setTextColor(getResources().getColor(R.color.black));
                ivVignette.setImageResource(R.drawable.ic_un_vignette);
                tvVignette.setTextColor(getResources().getColor(R.color.black));

                adjustEditOption(9);
                break;
            case 10:
                ivVibrance.setImageResource(R.drawable.ic_vibrance);
                tvVibrance.setTextColor(getResources().getColor(R.color.pink));

                ivWarmth.setImageResource(R.drawable.ic_un_warmth);
                tvWarmth.setTextColor(getResources().getColor(R.color.black));
                ivHue.setImageResource(R.drawable.ic_un_hue);
                tvHue.setTextColor(getResources().getColor(R.color.black));
                ivSaturation.setImageResource(R.drawable.ic_un_saturation);
                tvSaturation.setTextColor(getResources().getColor(R.color.black));
                ivWhites.setImageResource(R.drawable.ic_un_whites);
                tvWhites.setTextColor(getResources().getColor(R.color.black));
                ivBlacks.setImageResource(R.drawable.ic_un_blacks);
                tvBlacks.setTextColor(getResources().getColor(R.color.black));
                ivShadows.setImageResource(R.drawable.ic_un_shadows);
                tvShadows.setTextColor(getResources().getColor(R.color.black));
                ivHighLight.setImageResource(R.drawable.ic_un_hightlight);
                tvHighLight.setTextColor(getResources().getColor(R.color.black));
                ivExposure.setImageResource(R.drawable.ic_un_exposure);
                tvExposure.setTextColor(getResources().getColor(R.color.black));
                ivContrast.setImageResource(R.drawable.ic_un_contrast);
                tvContrast.setTextColor(getResources().getColor(R.color.black));
                ivBrightness.setImageResource(R.drawable.ic_un_brightness);
                tvBrightness.setTextColor(getResources().getColor(R.color.black));
                ivVignette.setImageResource(R.drawable.ic_un_vignette);
                tvVignette.setTextColor(getResources().getColor(R.color.black));

                adjustEditOption(10);
                break;
            case 11:
                ivVignette.setImageResource(R.drawable.ic_vignette);
                tvVignette.setTextColor(getResources().getColor(R.color.pink));

                ivVibrance.setImageResource(R.drawable.ic_un_vibrance);
                tvVibrance.setTextColor(getResources().getColor(R.color.black));
                ivWarmth.setImageResource(R.drawable.ic_un_warmth);
                tvWarmth.setTextColor(getResources().getColor(R.color.black));
                ivHue.setImageResource(R.drawable.ic_un_hue);
                tvHue.setTextColor(getResources().getColor(R.color.black));
                ivSaturation.setImageResource(R.drawable.ic_un_saturation);
                tvSaturation.setTextColor(getResources().getColor(R.color.black));
                ivWhites.setImageResource(R.drawable.ic_un_whites);
                tvWhites.setTextColor(getResources().getColor(R.color.black));
                ivBlacks.setImageResource(R.drawable.ic_un_blacks);
                tvBlacks.setTextColor(getResources().getColor(R.color.black));
                ivShadows.setImageResource(R.drawable.ic_un_shadows);
                tvShadows.setTextColor(getResources().getColor(R.color.black));
                ivHighLight.setImageResource(R.drawable.ic_un_hightlight);
                tvHighLight.setTextColor(getResources().getColor(R.color.black));
                ivExposure.setImageResource(R.drawable.ic_un_exposure);
                tvExposure.setTextColor(getResources().getColor(R.color.black));
                ivContrast.setImageResource(R.drawable.ic_un_contrast);
                tvContrast.setTextColor(getResources().getColor(R.color.black));
                ivBrightness.setImageResource(R.drawable.ic_un_brightness);
                tvBrightness.setTextColor(getResources().getColor(R.color.black));

                adjustEditOption(11);
                break;
        }
    }

    private void adjustEditOption(int pos) {
        if (backgroundModel.getAdjustModel() == null)
            backgroundModel.setAdjustModel(new AdjustModel(0f, 0f, 0f,
                    0f, 0f, 0f, 0f, 0f, 0f, 0f,
                    0f, 0f));
        switch (pos) {
            case 0:
                sbAdjust.setProgress((int) backgroundModel.getAdjustModel().getBrightness() / 2);
                break;
            case 1:
                sbAdjust.setProgress((int) backgroundModel.getAdjustModel().getContrast());
                break;
            case 2:
                sbAdjust.setProgress((int) backgroundModel.getAdjustModel().getExposure() / 4);
                break;
            case 3:
                int highlight = (int) backgroundModel.getAdjustModel().getHighlight();
                if (highlight > 0) sbAdjust.setProgress(highlight / 4);
                else sbAdjust.setProgress(highlight / 2);
                break;
            case 4:
                int shadow = (int) backgroundModel.getAdjustModel().getShadows();
                if (shadow > 0) sbAdjust.setProgress(shadow / 4);
                else sbAdjust.setProgress(shadow / 2);
                break;
            case 5:
                sbAdjust.setProgress((int) backgroundModel.getAdjustModel().getBlacks() / 2);
                break;
            case 6:
                sbAdjust.setProgress((int) backgroundModel.getAdjustModel().getWhites() / 2);
                break;
            case 7:
                sbAdjust.setProgress((int) backgroundModel.getAdjustModel().getSaturation() / 2);
                break;
            case 8:
                sbAdjust.setProgress((int) backgroundModel.getAdjustModel().getHue() * 100 / 360);
                break;
            case 9:
                sbAdjust.setProgress((int) backgroundModel.getAdjustModel().getWarmth() / 2);
                break;
            case 10:
                sbAdjust.setProgress((int) backgroundModel.getAdjustModel().getVibrance() / 2);
                break;
            case 11:
                sbAdjust.setProgress((int) backgroundModel.getAdjustModel().getVignette() / 2);
                break;
        }

        tvAdjustBackground.setText(String.valueOf(sbAdjust.getProgress()));

        bmAdjust = BitmapFactory.decodeFile(backgroundModel.getUriCache());
        sbAdjust.setOnSeekbarResult(new OnSeekbarResult() {
            @Override
            public void onDown(View v) {

            }

            @Override
            public void onMove(View v, int value) {
                tvAdjustBackground.setText(String.valueOf(value));
                setValueAdjust(value, pos);
            }

            @Override
            public void onUp(View v, int value) {
//                setValueAdjust(value, pos);
            }
        });
    }

    private void setValueAdjust(int value, int pos) {
        switch (pos) {
            case 0:
                backgroundModel.getAdjustModel().setBrightness(value * 2f);
                bitmap = UtilsAdjust.adjustBrightness(bmAdjust, value * 2f);
                break;
            case 1:
                backgroundModel.getAdjustModel().setContrast(value);
                bitmap = UtilsAdjust.adjustContrast(bmAdjust, value);
                break;
            case 2:
                backgroundModel.getAdjustModel().setExposure(value * 4f);
                bitmap = UtilsAdjust.adjustExposure(bmAdjust, value * 4f);
                break;
            case 3:
                if (value > 0) {
                    backgroundModel.getAdjustModel().setHighlight(value * 4f);
                    bitmap = UtilsAdjust.adjustHighLight(bmAdjust, value * 4f);
                } else if (value < 0) {
                    backgroundModel.getAdjustModel().setHighlight(value * 2f);
                    bitmap = UtilsAdjust.adjustHighLight(bmAdjust, value * 2f);
                }
                break;
            case 4:
                if (value > 0) {
                    backgroundModel.getAdjustModel().setShadows(value * 4f);
                    bitmap = UtilsAdjust.adjustShadow(bmAdjust, value * 4f);
                } else if (value < 0) {
                    backgroundModel.getAdjustModel().setShadows(value * 2f);
                    bitmap = UtilsAdjust.adjustShadow(bmAdjust, value * 2f);
                }
                break;
            case 5:
                backgroundModel.getAdjustModel().setBlacks(value * 2f);
                bitmap = UtilsAdjust.adjustBlacks(bmAdjust, value * 2f);
                break;
            case 6:
                backgroundModel.getAdjustModel().setWhites(value * 2f);
                bitmap = UtilsAdjust.adjustWhites(bmAdjust, value * 2f);
                break;
            case 7:
                backgroundModel.getAdjustModel().setSaturation(value * 2f);
                bitmap = UtilsAdjust.adjustSaturation(bmAdjust, value * 2f);
                break;
            case 8:
                backgroundModel.getAdjustModel().setHue(value * 360 / 100f);
                bitmap = UtilsAdjust.adjustHue(bmAdjust, value * 360 / 100f);
                break;
            case 9:
                backgroundModel.getAdjustModel().setWarmth(value * 2f);
                bitmap = UtilsAdjust.adjustWarmth(bmAdjust, value * 2f);
                break;
            case 10:
                backgroundModel.getAdjustModel().setVibrance(value * 2f);
                bitmap = UtilsAdjust.adjustVibrance(bmAdjust, value * 2f);
                break;
            case 11:
                backgroundModel.getAdjustModel().setVignette(value * 2f);
                bitmap = UtilsAdjust.adjustVignette(bmAdjust, value * 2f);
                break;
        }
        if (bitmap != null) vMain.setImageBitmap(bitmap);
    }

    private void seekAndHideViewBackground(int position) {
        animation = AnimationUtils.loadAnimation(this, R.anim.slide_down_out);
        if (vEditBackground.getVisibility() == View.VISIBLE) {
            vEditBackground.setAnimation(animation);
            vEditBackground.setVisibility(View.GONE);
        }

        if (tvResetBackground.getVisibility() == View.VISIBLE)
            tvResetBackground.setVisibility(View.GONE);

        switch (position) {
            case 0:
                animation = AnimationUtils.loadAnimation(this, R.anim.slide_up_in);
                if (rlEditAdjust.getVisibility() == View.GONE) {
                    rlEditAdjust.setAnimation(animation);
                    rlEditAdjust.setVisibility(View.VISIBLE);

                    tvAdjustBackground.setText(String.valueOf(0));
                    sbAdjust.setMax(100);
                }

                tvTitleEditBackground.setText(R.string.adjust);
                break;
        }
    }

    Handler handler = new Handler(Looper.getMainLooper(), new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message message) {
            Sticker sticker = vSticker.getCurrentSticker();
            switch (message.what) {
                case 0:
                    PicModel picModel = (PicModel) message.obj;
                    try {
                        Uri uri = Uri.parse(picModel.getUri());
                        Bitmap bitmap = Utils.getBitmapFromUri(EditActivity.this, uri);
                        if (bitmap != null) {
                            bitmap = Bitmap.createBitmap(Utils.modifyOrientation(EditActivity.this,
                                    Bitmap.createScaledBitmap(bitmap, 512,
                                            512 * bitmap.getHeight() / bitmap.getWidth(), false), uri));

                            Utils.saveBitmapToApp(EditActivity.this, bitmap,
                                    nameFolderImage, Utils.IMAGE_ROOT + "_" + picModel.getId());
                            String path = Utils.saveBitmapToApp(EditActivity.this, bitmap,
                                    nameFolderImage, Utils.IMAGE + "_" + picModel.getId());

                            setUpDataFilter(bitmap);
                            setUpDataBlend(bitmap, path);

                            if (!isReplaceImage) {
                                ImageModel imageModel = new ImageModel(path, path, "", 0,
                                        null, 255, 0, null);

                                DrawableStickerCustom drawableSticker =
                                        new DrawableStickerCustom(EditActivity.this, imageModel, getId(), Utils.IMAGE);

                                vSticker.addSticker(drawableSticker);
                                seekAndHideOperation(positionImage);
                            } else replaceImage(path);
                        } else
                            Utils.showToast(getBaseContext(), getString(R.string.cant_get_image));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case 1:
                    if (checkCurrentSticker(sticker)) {
                        if (sticker instanceof DrawableStickerCustom) {
                            DrawableStickerCustom drawableSticker = (DrawableStickerCustom) sticker;
                            if (filterBlendImageAdapter != null) {
                                filterBlendImageAdapter.setData(lstFilter);
                                rcvEditFilterImage.smoothScrollToPosition(drawableSticker.getImageModel().getPosFilter());
                                filterBlendImageAdapter.setCurrent(drawableSticker.getImageModel().getPosFilter());
                                filterBlendImageAdapter.changeNotify();
                            }
                        }
                    }
                    break;
                case 2:
                    if (checkCurrentSticker(sticker)) {
                        if (sticker instanceof DrawableStickerCustom) {
                            DrawableStickerCustom drawableSticker = (DrawableStickerCustom) sticker;
                            if (filterBlendImageAdapter != null) {
                                filterBlendImageAdapter.setData(lstBlend);
                                rcvEditFilterImage.smoothScrollToPosition(drawableSticker.getImageModel().getPosBlend());
                                filterBlendImageAdapter.setCurrent(drawableSticker.getImageModel().getPosBlend());
                                filterBlendImageAdapter.changeNotify();
                            }
                        }
                    }
                    break;
            }
            return true;
        }
    });

    private void setUpDataFilter(Bitmap bitmap) {
        if (bitmap == null) return;
        lstFilter = new ArrayList<>();
        new Thread(() -> {
            lstFilter = FilterBlendImage.getDataFilter(
                    Bitmap.createScaledBitmap(bitmap, 400, 400 * bitmap.getHeight() / bitmap.getWidth(), false));

            handler.sendEmptyMessage(1);
        }).start();
    }

    private void setUpDataBlend(Bitmap bitmap, String name) {
        if (bitmap == null) return;
        lstBlend = new ArrayList<>();
        Log.d("2tdp", "setUpDataBlend: " + name);
        new Thread(() -> {
            lstBlend = FilterBlendImage.getDataBlend(
                    Bitmap.createScaledBitmap(bitmap, 400, 400 * bitmap.getHeight() / bitmap.getWidth(), false), name);

            handler.sendEmptyMessage(2);
        }).start();
    }

    //Image
    private void pickImage() {
        ImageFragment imageFragment = ImageFragment.newInstance((o, pos) -> {
            PicModel picModel = (PicModel) o;
            Message message = new Message();
            message.what = 0;
            message.obj = picModel;
            handler.sendMessage(message);
        });
        Utils.replaceFragment(getSupportFragmentManager(), imageFragment, false, true);
    }

    //ReplaceImage
    private void replaceImage(String path) {
        DrawableStickerCustom drawableSticker = (DrawableStickerCustom) vSticker.getCurrentSticker();
        if (drawableSticker != null && drawableSticker.getTypeSticker().equals(Utils.IMAGE)) {
            drawableSticker.getImageModel().setUri(path);
            drawableSticker.getImageModel().setUriRoot(path);

            drawableSticker.replaceImage();
        }
        vSticker.invalidate();
    }

    //Crop Image
    private void cropImage(Sticker sticker) {
        if (!checkCurrentSticker(sticker)) return;

        rlEditCrop.getLayoutParams().height = getResources().getDisplayMetrics().heightPixels * 90 / 100;
        seekAndHideViewImage(0);
        tvCancelCropImage.setOnClickListener(v -> seekAndHideOperation(positionImage));

        DrawableStickerCustom drawableSticker = (DrawableStickerCustom) sticker;
        ivTickCropImage.setOnClickListener(v -> afterCropImage(drawableSticker));

        pathCrop.setPath(DataPic.getPathDataCrop(0));
        pathCrop.setBitmap(drawableSticker.getImageModel());

        CropImageAdapter cropImageAdapter = new CropImageAdapter(this, (o, pos) -> {
            pathCrop.setPath((String) o);
            drawableSticker.getImageModel().setPathShape((String) o);
        });

        GridLayoutManager manager = new GridLayoutManager(this, 2, LinearLayoutManager.HORIZONTAL, false);
        cropImageAdapter.setData(Arrays.asList(DataPic.lstPathData));

        rcvCropImage.setLayoutManager(manager);
        rcvCropImage.setAdapter(cropImageAdapter);
    }

    private void afterCropImage(DrawableStickerCustom drawableSticker) {
        seekAndHideOperation(positionImage);

        drawableSticker.getImageModel().setUri(pathCrop.getBitmapCreate(this, nameFolderImage));
        drawableSticker.replaceImage();
        vSticker.invalidate();
    }

    //Filter Image
    private void filterImage(Sticker sticker) {
        if (!checkCurrentSticker(sticker)) return;
        seekAndHideViewImage(1);

        if (sticker instanceof DrawableStickerCustom) {
            DrawableStickerCustom drawableSticker = (DrawableStickerCustom) sticker;
            if (drawableSticker.getTypeSticker().equals(Utils.IMAGE)) {
                Bitmap bitmap = BitmapFactory.decodeFile(drawableSticker.getImageModel().getUri());

                filterBlendImageAdapter = new FilterBlendImageAdapter(this, (o, pos) -> {
                    FilterBlendModel filter = (FilterBlendModel) o;
                    filter.setCheck(true);
                    filterBlendImageAdapter.setCurrent(pos);
                    filterBlendImageAdapter.changeNotify();

                    Bitmap bm = CGENativeLibrary.cgeFilterImage_MultipleEffects(bitmap, filter.getParameterFilter(), 0.8f);

                    drawableSticker.getImageModel().setPosFilter(pos);
                    drawableSticker.getImageModel().setUri(Utils.saveBitmapToApp(EditActivity.this,
                            bm, nameFolderImage, Utils.IMAGE));
                    drawableSticker.replaceImage();
                    vSticker.invalidate();
                });

                if (!lstFilter.isEmpty()) filterBlendImageAdapter.setData(lstFilter);

                LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
                rcvEditFilterImage.setLayoutManager(manager);
                rcvEditFilterImage.setAdapter(filterBlendImageAdapter);

                rcvEditFilterImage.smoothScrollToPosition(drawableSticker.getImageModel().getPosFilter());
                filterBlendImageAdapter.setCurrent(drawableSticker.getImageModel().getPosFilter());
                filterBlendImageAdapter.changeNotify();
            }
        }
    }

    //Shadow Image
    private void shadowImage(Sticker sticker) {
        if (!checkCurrentSticker(sticker)) return;
        seekAndHideViewImage(2);

        if (sticker instanceof DrawableStickerCustom) {
            DrawableStickerCustom drawableSticker = (DrawableStickerCustom) sticker;
            if (drawableSticker.getTypeSticker().equals(Utils.IMAGE)) {

                setShadowImageCurrent(drawableSticker);

                sbXPosImage.setOnSeekbarResult(new OnSeekbarResult() {
                    @Override
                    public void onDown(View v) {

                    }

                    @Override
                    public void onMove(View v, int value) {
                        tvXPosImage.setText(String.valueOf(value));
                        drawableSticker.getImageModel().getShadowModel().setXPos(value);
                        drawableSticker.getImageModel().getShadowModel().setBlur(blur);
                        vSticker.replace(drawableSticker.getImageModel().shadow(EditActivity.this, drawableSticker), true);
                    }

                    @Override
                    public void onUp(View v, int value) {

                    }
                });

                sbYPosImage.setOnSeekbarResult(new OnSeekbarResult() {
                    @Override
                    public void onDown(View v) {

                    }

                    @Override
                    public void onMove(View v, int value) {
                        tvYPosImage.setText(String.valueOf(value));
                        drawableSticker.getImageModel().getShadowModel().setYPos(value);
                        drawableSticker.getImageModel().getShadowModel().setBlur(blur);
                        vSticker.replace(drawableSticker.getImageModel().shadow(EditActivity.this, drawableSticker), true);
                    }

                    @Override
                    public void onUp(View v, int value) {

                    }
                });

                sbBlurImage.setOnSeekbarResult(new OnSeekbarResult() {
                    @Override
                    public void onDown(View v) {

                    }

                    @Override
                    public void onMove(View v, int value) {
                        if (value == -50) blur = 1f;
                        else if (value == 50) blur = 10f;
                        else if (value == 0) blur = 5f;
                        else blur = 5 + (value * 5 / 50f);

                        tvBlurImage.setText(String.valueOf(value));
                        drawableSticker.getImageModel().getShadowModel().setBlur(blur);
                        vSticker.replace(drawableSticker.getImageModel().shadow(EditActivity.this, drawableSticker), true);
                    }

                    @Override
                    public void onUp(View v, int value) {

                    }
                });

                ivColorBlurImage.setOnClickListener(v -> DataColor.pickColor(this, (color -> {
                    drawableSticker.getImageModel().getShadowModel().setColorBlur(color.getColorStart());
                    vSticker.replace(drawableSticker.getImageModel().shadow(EditActivity.this, drawableSticker), true);
                })));
            }
        }

    }

    private void setShadowImageCurrent(DrawableStickerCustom drawableSticker) {
        float xPos = 0f, yPos = 0f;
        int color = 0;
        ShadowModel shadowModel = drawableSticker.getImageModel().getShadowModel();
        if (shadowModel != null) {
            xPos = (shadowModel.getXPos());
            yPos = (shadowModel.getYPos());
            blur = (shadowModel.getBlur());
            color = shadowModel.getColorBlur();
        } else
            drawableSticker.getImageModel().setShadowModel(new ShadowModel(xPos, yPos, blur, color));

        ShadowModel shadowModelOld = new ShadowModel(xPos, yPos, blur, color);

        tvResetImage.setOnClickListener(v -> resetImage(0, drawableSticker, shadowModelOld, -1));

        tvXPosImage.setText(String.valueOf((int) xPos));
        sbXPosImage.setProgress((int) xPos);
        tvYPosImage.setText(String.valueOf((int) yPos));
        sbYPosImage.setProgress((int) yPos);
        if (blur == 0) tvBlurImage.setText(String.valueOf(0));
        else tvBlurImage.setText(String.valueOf((int) ((blur - 5) * 10f)));

        if (blur > 5) sbBlurImage.setProgress((int) ((blur - 5) * 10f));
        else if (blur == 0f) {
            sbBlurImage.setProgress(0);
            blur = 5f;
        } else sbBlurImage.setProgress((int) ((5 - blur) * 10f));

        drawableSticker.getImageModel().getShadowModel().setBlur(blur);
    }

    //Opacity Image
    private void opacityImage(DrawableStickerCustom drawableSticker) {
        seekAndHideViewImage(3);

        int opacityOld = drawableSticker.getImageModel().getOpacity();
        sbOpacityImage.setProgress(opacityOld);
        tvResetImage.setOnClickListener(v -> resetImage(1, drawableSticker, null, opacityOld));

        sbOpacityImage.setOnSeekbarResult(new OnSeekbarResult() {
            @Override
            public void onDown(View v) {

            }

            @Override
            public void onMove(View v, int value) {
                drawableSticker.getImageModel().setOpacity(value);
                if (!drawableSticker.isShadowImage()) {
                    drawableSticker.getImageModel().setOpacity(value);
                    vSticker.replace(drawableSticker.getImageModel().opacity(EditActivity.this, drawableSticker), true);
                } else {
                    drawableSticker.setAlpha(value * 255 / 100);
                    vSticker.invalidate();
                }
            }

            @Override
            public void onUp(View v, int value) {

            }
        });
    }

    //Blend Image
    private void blendImage(Sticker sticker) {
        if (!checkCurrentSticker(sticker)) return;
        seekAndHideViewImage(4);

        if (sticker instanceof DrawableStickerCustom) {
            DrawableStickerCustom drawableSticker = (DrawableStickerCustom) sticker;
            if (drawableSticker.getTypeSticker().equals(Utils.IMAGE)) {
                Bitmap bitmap = BitmapFactory.decodeFile(drawableSticker.getImageModel().getUri());

                filterBlendImageAdapter = new FilterBlendImageAdapter(this, (o, pos) -> {
                    FilterBlendModel filter = (FilterBlendModel) o;
                    filter.setCheck(true);
                    filterBlendImageAdapter.setCurrent(pos);
                    filterBlendImageAdapter.changeNotify();

                    Log.d("2tdp", "blendImage: " + filter.getParameterFilter());

                    Bitmap bm = CGENativeLibrary.cgeFilterImage_MultipleEffects(bitmap, filter.getParameterFilter(), 0.8f);

                    drawableSticker.getImageModel().setPosBlend(pos);
                    drawableSticker.getImageModel().setUri(Utils.saveBitmapToApp(EditActivity.this,
                            bm, nameFolderImage, Utils.IMAGE));
                    drawableSticker.replaceImage();
                    vSticker.invalidate();
                });

                if (!lstBlend.isEmpty()) filterBlendImageAdapter.setData(lstBlend);

                LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
                rcvEditBlendImage.setLayoutManager(manager);
                rcvEditBlendImage.setAdapter(filterBlendImageAdapter);

                rcvEditBlendImage.smoothScrollToPosition(drawableSticker.getImageModel().getPosBlend());
                filterBlendImageAdapter.setCurrent(drawableSticker.getImageModel().getPosBlend());
                filterBlendImageAdapter.changeNotify();
            }
        }
    }

    private void resetImage(int position, DrawableStickerCustom drawableSticker, ShadowModel shadowModel,
                            int opacity) {
        switch (position) {
            case 0:
                float xPos = shadowModel.getXPos();
                float yPos = shadowModel.getYPos();
                float blurOld = shadowModel.getBlur();
                int color = shadowModel.getColorBlur();

                tvXPosImage.setText(String.valueOf((int) xPos));
                sbXPosImage.setProgress((int) xPos);

                tvYPosImage.setText(String.valueOf((int) yPos));
                sbYPosImage.setProgress((int) yPos);

                if (blurOld == 0f) tvBlurImage.setText(String.valueOf(0));
                else tvBlurImage.setText(String.valueOf((int) ((blurOld - 5) * 10f)));
                if (blurOld > 5) sbBlurImage.setProgress((int) ((blurOld - 5) * 10f));
                else if (blurOld == 0f) sbBlurImage.setProgress(0);
                else sbBlurImage.setProgress((int) ((5 - blurOld) * 10f));

                drawableSticker.getImageModel().getShadowModel().setXPos(xPos);
                drawableSticker.getImageModel().getShadowModel().setYPos(yPos);
                drawableSticker.getImageModel().getShadowModel().setBlur(blurOld);
                drawableSticker.getImageModel().getShadowModel().setColorBlur(color);

                vSticker.replace(drawableSticker.getImageModel().shadow(EditActivity.this, drawableSticker), true);
                break;
            case 1:
                drawableSticker.getImageModel().setOpacity(opacity);
                drawableSticker.setAlpha((int) (opacity * 255 / 100f));
                sbOpacityImage.setProgress(opacity);

                vSticker.invalidate();
                break;
        }
    }

    private void seekAndHideViewImage(int position) {
        animation = AnimationUtils.loadAnimation(this, R.anim.slide_down_out);
        if (vEditImage.getVisibility() == View.VISIBLE) {
            vEditImage.setAnimation(animation);
            vEditImage.setVisibility(View.GONE);
        }

        if (tvResetImage.getVisibility() == View.VISIBLE)
            tvResetImage.setVisibility(View.GONE);

        switch (position) {
            case 0:
                animation = AnimationUtils.loadAnimation(this, R.anim.slide_up_in);
                if (rlEditCrop.getVisibility() == View.GONE) {
                    rlEditCrop.setAnimation(animation);
                    rlEditCrop.setVisibility(View.VISIBLE);
                }
                break;
            case 1:
                animation = AnimationUtils.loadAnimation(this, R.anim.slide_up_in);
                if (rlEditFilterImage.getVisibility() == View.GONE) {
                    rlEditFilterImage.setAnimation(animation);
                    rlEditFilterImage.setVisibility(View.VISIBLE);
                }

                if (tvResetImage.getVisibility() == View.VISIBLE)
                    tvResetImage.setVisibility(View.GONE);

                tvTitleEditImage.setText(R.string.filter);
                break;
            case 2:
                animation = AnimationUtils.loadAnimation(this, R.anim.slide_up_in);
                if (llEditShadowImage.getVisibility() == View.GONE) {
                    llEditShadowImage.setAnimation(animation);
                    llEditShadowImage.setVisibility(View.VISIBLE);
                }

                if (tvResetImage.getVisibility() == View.GONE)
                    tvResetImage.setVisibility(View.VISIBLE);

                tvXPosImage.setText(String.valueOf(0));
                tvYPosImage.setText(String.valueOf(0));
                tvBlurImage.setText(String.valueOf(0));
                sbXPosImage.setMax(100);
                sbYPosImage.setMax(100);
                sbBlurImage.setMax(100);

                tvTitleEditImage.setText(R.string.shadow);
                break;
            case 3:
                animation = AnimationUtils.loadAnimation(this, R.anim.slide_up_in);
                if (rlEditOpacityImage.getVisibility() == View.GONE) {
                    rlEditOpacityImage.setAnimation(animation);
                    rlEditOpacityImage.setVisibility(View.VISIBLE);
                }

                if (tvResetImage.getVisibility() == View.GONE)
                    tvResetImage.setVisibility(View.VISIBLE);

                sbOpacityImage.setColorText(getResources().getColor(R.color.green));
                sbOpacityImage.setSizeText(com.intuit.ssp.R.dimen._10ssp);
                sbOpacityImage.setProgress(100);
                sbOpacityImage.setMax(100);

                tvTitleEditImage.setText(R.string.opacity);
                break;
            case 4:
                animation = AnimationUtils.loadAnimation(this, R.anim.slide_up_in);
                if (rlEditBlendImage.getVisibility() == View.GONE) {
                    rlEditBlendImage.setAnimation(animation);
                    rlEditBlendImage.setVisibility(View.VISIBLE);
                }

                if (tvResetImage.getVisibility() == View.VISIBLE)
                    tvResetImage.setVisibility(View.GONE);

                tvTitleEditImage.setText(R.string.blend);
                break;
        }

        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                vEditImage.clearAnimation();
                rlEditCrop.clearAnimation();
                rlEditFilterImage.clearAnimation();
                rlEditOpacityImage.clearAnimation();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    //Emoji
    private void pickEmoji() {
        rlExpandEmoji.getLayoutParams().height = getResources().getDisplayMetrics().heightPixels * 90 / 100;
        seeAndHideViewEmoji(-1);
        setUpTitleEmoji();
        setUpDataEmoji();
    }

    private void addNewEmoji(EmojiModel emoji) {
        DrawableStickerCustom emojiSticker = new DrawableStickerCustom(this, emoji, getId(), Utils.EMOJI);
        vSticker.addSticker(emojiSticker);
    }

    private void setUpDataEmoji() {
        addFragmentsAdapter = new ViewPagerAddFragmentsAdapter(getSupportFragmentManager(), getLifecycle());

        String[] arrNameEmoji = new String[]{"bear", "cat", "chicken", "face", "fox", "frankenstein",
                "ghost", "heart", "icon", "pillow", "pumpkin", "santa", "tiger"};
        for (String s : arrNameEmoji) {
            EmojiFragment emojiFragment = EmojiFragment.newInstance(s, (o, pos) -> {
                EmojiModel emoji = (EmojiModel) o;
                if (!isReplaceEmoji) addNewEmoji(emoji);
                else replaceEmoji(emoji);
                seekAndHideOperation(positionEmoji);
            });
            addFragmentsAdapter.addFrag(emojiFragment);
        }

        vpEmoji.setAdapter(addFragmentsAdapter);
        vpEmoji.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                titleEmojiAdapter.setCurrent(position);
                rcvTypeEmoji.smoothScrollToPosition(position);
            }
        });
    }

    private void setUpTitleEmoji() {


        titleEmojiAdapter = new TitleEmojiAdapter(this, (o, pos) -> {
            titleEmojiAdapter.setCurrent(pos);
            vpEmoji.setCurrentItem(pos, true);
        });

        titleEmojiAdapter.setData(DataEmoji.getTitleEmoji(this, "title"));
        if (!isFirstEmoji) {
            titleEmojiAdapter.setCurrent(0);
            isFirstEmoji = true;
        }
        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rcvTypeEmoji.setLayoutManager(manager);
        rcvTypeEmoji.setAdapter(titleEmojiAdapter);
    }

    //Opacity Emoji
    private void opacityEmoji(DrawableStickerCustom drawableSticker) {
        seeAndHideViewEmoji(0);

        int opacityOld = drawableSticker.getEmojiModel().getOpacity() * 100 / 255;
        sbOpacityEmoji.setProgress(opacityOld);
        tvResetEmoji.setOnClickListener(v -> {
            sbOpacityEmoji.setProgress(opacityOld);
            drawableSticker.getEmojiModel().setOpacity(opacityOld * 255 / 100);
            vSticker.replace(drawableSticker.getEmojiModel().opacity(EditActivity.this, drawableSticker), true);
        });

        sbOpacityEmoji.setOnSeekbarResult(new OnSeekbarResult() {
            @Override
            public void onDown(View v) {

            }

            @Override
            public void onMove(View v, int value) {
                drawableSticker.getEmojiModel().setOpacity(value * 255 / 100);
                vSticker.replace(drawableSticker.getEmojiModel().opacity(EditActivity.this, drawableSticker), true);
            }

            @Override
            public void onUp(View v, int value) {

            }
        });
    }

    //Replace Emoji
    private void replaceEmoji(EmojiModel emoji) {
        DrawableStickerCustom drawableSticker = (DrawableStickerCustom) vSticker.getCurrentSticker();
        if (drawableSticker != null && drawableSticker.getTypeSticker().equals(Utils.EMOJI))
            drawableSticker.setEmojiModel(emoji);

        vSticker.invalidate();
    }

    private void seeAndHideViewEmoji(int position) {
        animation = AnimationUtils.loadAnimation(this, R.anim.slide_down_out);
        if (vOperation.getVisibility() == View.VISIBLE) {
            vOperation.setAnimation(animation);
            vOperation.setVisibility(View.GONE);
        }

        switch (position) {
            case 0:
                animation = AnimationUtils.loadAnimation(this, R.anim.slide_down_out);
                if (llEditEmoji.getVisibility() == View.VISIBLE) {
                    llEditEmoji.setAnimation(animation);
                    llEditEmoji.setVisibility(View.GONE);
                }

                animation = AnimationUtils.loadAnimation(this, R.anim.slide_up_in);
                if (rlEditOpacityEmoji.getVisibility() == View.GONE) {
                    rlEditOpacityEmoji.setAnimation(animation);
                    rlEditOpacityEmoji.setVisibility(View.VISIBLE);
                    sbOpacityEmoji.setColorText(getResources().getColor(R.color.green));
                    sbOpacityEmoji.setSizeText(com.intuit.ssp.R.dimen._10ssp);
                    sbOpacityEmoji.setProgress(100);
                    sbOpacityEmoji.setMax(100);
                }

                if (tvResetEmoji.getVisibility() == View.GONE)
                    tvResetEmoji.setVisibility(View.VISIBLE);

                tvTitleEditEmoji.setText(R.string.opacity);
                break;
            case 1:
                animation = AnimationUtils.loadAnimation(this, R.anim.slide_down_out);
                if (rlEditOpacityEmoji.getVisibility() == View.VISIBLE) {
                    rlEditOpacityEmoji.setAnimation(animation);
                    rlEditOpacityEmoji.setVisibility(View.GONE);
                }

                animation = AnimationUtils.loadAnimation(this, R.anim.slide_up_in);
                if (llEditEmoji.getVisibility() == View.GONE) {
                    llEditEmoji.setAnimation(animation);
                    llEditEmoji.setVisibility(View.VISIBLE);
                }

                if (tvResetEmoji.getVisibility() == View.VISIBLE)
                    tvResetEmoji.setVisibility(View.GONE);

                tvTitleEditEmoji.setText(R.string.sticker);
                break;
            default:
                animation = AnimationUtils.loadAnimation(this, R.anim.slide_up_in);
                if (rlExpandEmoji.getVisibility() == View.GONE) {
                    rlExpandEmoji.setAnimation(animation);
                    rlExpandEmoji.setVisibility(View.VISIBLE);
                }

                if (rlExpandEditEmoji.getVisibility() == View.VISIBLE)
                    rlExpandEditEmoji.setVisibility(View.GONE);

                if (tvResetEmoji.getVisibility() == View.VISIBLE)
                    tvResetEmoji.setVisibility(View.GONE);

                tvTitleEditEmoji.setText(R.string.sticker);
                break;
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
        if (!checkCurrentSticker(sticker)) return;

        if (sticker instanceof TextStickerCustom) {
            TextStickerCustom textSticker = (TextStickerCustom) sticker;

            Intent intent = new Intent(this, AddTextActivity.class);
            intent.putExtra("text", textSticker.getTextModel());
            launcherEditText.launch(intent, ActivityOptionsCompat.makeCustomAnimation(this, R.anim.slide_in_right, R.anim.slide_out_left));
        }
    }

    //Duplicate Text
    private void duplicateText(Sticker sticker) {
        if (!checkCurrentSticker(sticker)) return;

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
        if (!checkCurrentSticker(sticker)) return;
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
                    tvFontText.setText(String.valueOf(i));
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
        if (!checkCurrentSticker(sticker)) return;
        seeAndHideViewText(1);
        ArrayList<ColorModel> lstColor = DataColor.getListColor(this);

        if (sticker instanceof TextStickerCustom) {
            TextStickerCustom textSticker = (TextStickerCustom) sticker;

            ColorModel colorModelOld = textSticker.getTextModel().getColorModel();
            tvResetText.setOnClickListener(v -> resetText(1, textSticker, -1,
                    colorModelOld, null, null, -1));

            ColorAdapter colorAdapter = new ColorAdapter(this, R.layout.item_color_edit, (o, pos) -> {


                ColorModel color = (ColorModel) o;
                if (pos == 0)
                    DataColor.pickColor(this, coloModel -> setTextColor(textSticker, coloModel));
                else if (color.getColorStart() == color.getColorEnd())
                    setTextColor(textSticker, color);
                else
                    DataColor.pickDirection(this, color, coloModel -> setTextColor(textSticker, color));
            });
            if (!lstColor.isEmpty()) colorAdapter.setData(lstColor);

            LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
            rcvColorText.setLayoutManager(manager);
            rcvColorText.setAdapter(colorAdapter);
        }
    }

    private void setTextColor(TextStickerCustom textSticker, ColorModel color) {
        if (!checkCurrentSticker(textSticker)) return;

        if (color != null) {
            textSticker.getTextModel().setColorModel(color);
            textSticker.setTextColor(color);
            vSticker.invalidate();
        }
    }

    //Transform Text
    private void transformText(Sticker sticker) {
        if (!checkCurrentSticker(sticker)) return;
        seeAndHideViewText(2);

        if (sticker instanceof TextStickerCustom) {
            TextStickerCustom textSticker = (TextStickerCustom) sticker;

            setShearTextCurrent(textSticker);

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

    private void setShearTextCurrent(TextStickerCustom textSticker) {
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
        if (!checkCurrentSticker(sticker)) return;
        seeAndHideViewText(3);

        if (sticker instanceof TextStickerCustom) {
            TextStickerCustom textSticker = (TextStickerCustom) sticker;

            setShadowTextCurrent(textSticker);

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

    private void setShadowTextCurrent(TextStickerCustom textSticker) {
        float xPos = 0f, yPos = 0f;
        int color = 0;
        ShadowModel shadowModel = textSticker.getTextModel().getShadowModel();
        if (shadowModel != null) {
            xPos = (shadowModel.getXPos());
            yPos = (shadowModel.getYPos());
            blur = (shadowModel.getBlur());
            color = shadowModel.getColorBlur();
        } else textSticker.getTextModel().setShadowModel(new ShadowModel(xPos, yPos, blur, color));

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
        if (!checkCurrentSticker(sticker)) return;
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
                if (colorModel != null) textSticker.getTextModel().setColorModel(colorModel);
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
                else if (blurOld == 0f) sbBlurText.setProgress(0);
                else sbBlurText.setProgress((int) ((5 - blurOld) * 10f));

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

        if (!strPicUser.equals("") && !strPicUser.equals(strPicUserOld)) {
            strPicUserOld = strPicUser;
            try {
                bmRoot = Utils.modifyOrientation(this, Utils.getBitmapFromUri(this, Uri.parse(strPicUser)), Uri.parse(strPicUserOld));
            } catch (IOException e) {
                e.printStackTrace();
            }
            seekAndHideViewMain(positionCrop, bmRoot, null, true);
        } else if (!strPicApp.equals("") && !strPicApp.equals(strPicAppOld)) {
            strPicAppOld = strPicApp;
            bmRoot = Utils.getBitmapFromAsset(this, "offline_myapp", strPicAppOld, false, false);
            seekAndHideViewMain(positionCrop, bmRoot, null, true);
        } else if (colorModel != null && colorModel != colorModelOld) {
            colorModelOld = colorModel;
            backgroundModel.setColorModel(colorModelOld);
            seekAndHideViewMain(positionColor, null, colorModel, true);
        } else isReplaceBackground = false;
    }

    private void seekAndHideOperation(int position) {
        if (vSticker.getStickerCount() == 0)
            ivLayer.setImageResource(R.drawable.ic_layer_uncheck);
        else ivLayer.setImageResource(R.drawable.ic_layer);

        switch (position) {
            case positionBackground:
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

                if (rlExpandEditText.getVisibility() == View.VISIBLE) {
                    rlExpandEditText.setAnimation(animation);
                    rlExpandEditText.setVisibility(View.GONE);
                }

                if (rlExpandEditEmoji.getVisibility() == View.VISIBLE) {
                    rlExpandEditEmoji.setAnimation(animation);
                    rlExpandEditEmoji.setVisibility(View.GONE);
                }

                if (rlExpandEditImage.getVisibility() == View.VISIBLE) {
                    rlExpandEditImage.setAnimation(animation);
                    rlExpandEditImage.setVisibility(View.GONE);
                }

                animation = AnimationUtils.loadAnimation(this, R.anim.slide_up_in);
                if (rlExpandEditBackground.getVisibility() == View.GONE) {
                    rlExpandEditBackground.setAnimation(animation);
                    rlExpandEditBackground.setVisibility(View.VISIBLE);
                }

                if (vEditBackground.getVisibility() == View.GONE) {
                    vEditBackground.setAnimation(animation);
                    vEditBackground.setVisibility(View.VISIBLE);
                }

                if (tvResetBackground.getVisibility() == View.VISIBLE)
                    tvResetBackground.setVisibility(View.GONE);

                tvTitleEditBackground.setText(R.string.background);
                break;
            case positionImage:
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

                if (rlExpandEditText.getVisibility() == View.VISIBLE) {
                    rlExpandEditText.setAnimation(animation);
                    rlExpandEditText.setVisibility(View.GONE);
                }

                if (rlExpandEditEmoji.getVisibility() == View.VISIBLE) {
                    rlExpandEditEmoji.setAnimation(animation);
                    rlExpandEditEmoji.setVisibility(View.GONE);
                }

                if (rlEditCrop.getVisibility() == View.VISIBLE) {
                    rlEditCrop.setAnimation(animation);
                    rlEditCrop.setVisibility(View.GONE);
                }

                if (rlEditFilterImage.getVisibility() == View.VISIBLE) {
                    rlEditFilterImage.setAnimation(animation);
                    rlEditFilterImage.setVisibility(View.GONE);
                }

                if (llEditShadowImage.getVisibility() == View.VISIBLE) {
                    llEditShadowImage.setAnimation(animation);
                    llEditShadowImage.setVisibility(View.GONE);
                }

                if (rlEditOpacityImage.getVisibility() == View.VISIBLE) {
                    rlEditOpacityImage.setAnimation(animation);
                    rlEditOpacityImage.setVisibility(View.GONE);
                }

                if (rlEditBlendImage.getVisibility() == View.VISIBLE) {
                    rlEditBlendImage.setAnimation(animation);
                    rlEditBlendImage.setVisibility(View.GONE);
                }

                animation = AnimationUtils.loadAnimation(this, R.anim.slide_up_in);
                if (rlExpandEditImage.getVisibility() == View.GONE) {
                    rlExpandEditImage.setAnimation(animation);
                    rlExpandEditImage.setVisibility(View.VISIBLE);
                }
                if (vEditImage.getVisibility() == View.GONE) {
                    vEditImage.setAnimation(animation);
                    vEditImage.setVisibility(View.VISIBLE);
                }

                if (tvResetImage.getVisibility() == View.VISIBLE)
                    tvResetImage.setVisibility(View.GONE);

                tvTitleEditImage.setText(R.string.image);

                break;
            case positionEmoji:
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

                if (rlExpandEditText.getVisibility() == View.VISIBLE) {
                    rlExpandEditText.setAnimation(animation);
                    rlExpandEditText.setVisibility(View.GONE);
                }

                if (rlExpandEmoji.getVisibility() == View.VISIBLE) {
                    rlExpandEmoji.setAnimation(animation);
                    rlExpandEmoji.setVisibility(View.GONE);
                }

                if (rlEditOpacityEmoji.getVisibility() == View.VISIBLE) {
                    rlEditOpacityEmoji.setAnimation(animation);
                    rlEditOpacityEmoji.setVisibility(View.GONE);
                }

                if (rlExpandEditImage.getVisibility() == View.VISIBLE) {
                    rlExpandEditImage.setAnimation(animation);
                    rlExpandEditImage.setVisibility(View.GONE);
                }

                if (tvResetEmoji.getVisibility() == View.VISIBLE)
                    tvResetEmoji.setVisibility(View.GONE);

                tvTitleEditEmoji.setText(R.string.sticker);

                animation = AnimationUtils.loadAnimation(this, R.anim.slide_up_in);
                if (rlExpandEditEmoji.getVisibility() == View.GONE) {
                    rlExpandEditEmoji.setAnimation(animation);
                    rlExpandEditEmoji.setVisibility(View.VISIBLE);
                }

                if (llEditEmoji.getVisibility() == View.GONE) {
                    llEditEmoji.setAnimation(animation);
                    llEditEmoji.setVisibility(View.VISIBLE);
                }
                break;
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

                if (rlExpandEditEmoji.getVisibility() == View.VISIBLE) {
                    rlExpandEditEmoji.setAnimation(animation);
                    rlExpandEditEmoji.setVisibility(View.GONE);
                }

                if (rlExpandEditImage.getVisibility() == View.VISIBLE) {
                    rlExpandEditImage.setAnimation(animation);
                    rlExpandEditImage.setVisibility(View.GONE);
                }

                tvTitleEditText.setText(R.string.text);

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
                seekAndHideViewMain(positionCrop, bmRoot, null, true);
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

                if (rlExpandEmoji.getVisibility() == View.VISIBLE) {
                    rlExpandEmoji.setAnimation(animation);
                    rlExpandEmoji.setVisibility(View.GONE);
                }

                if (rlExpandEditEmoji.getVisibility() == View.VISIBLE) {
                    rlExpandEditEmoji.setAnimation(animation);
                    rlExpandEditEmoji.setVisibility(View.GONE);
                }

                if (rlExpandEditImage.getVisibility() == View.VISIBLE) {
                    rlExpandEditImage.setAnimation(animation);
                    rlExpandEditImage.setVisibility(View.GONE);
                }

                if (rlExpandEditBackground.getVisibility() == View.VISIBLE) {
                    rlExpandEditBackground.setAnimation(animation);
                    rlExpandEditBackground.setVisibility(View.GONE);
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

                rlExpandEmoji.clearAnimation();
                rlExpandEditEmoji.clearAnimation();

                rlExpandEditImage.clearAnimation();
                vEditImage.clearAnimation();
                rlEditCrop.clearAnimation();
                rlEditFilterImage.clearAnimation();
                rlEditOpacityImage.clearAnimation();
                rlEditBlendImage.clearAnimation();

                rlExpandEditBackground.clearAnimation();
                vEditBackground.clearAnimation();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void seekAndHideViewMain(@NotNull int position, @Nullable Bitmap bitmap,
                                     @Nullable ColorModel colorModel, @NotNull boolean isNew) {
        isColor = false;

        if (vSticker.getStickerCount() == 0)
            ivLayer.setImageResource(R.drawable.ic_layer_uncheck);
        else ivLayer.setImageResource(R.drawable.ic_layer);

        if (isNew) {
            animation = AnimationUtils.loadAnimation(this, R.anim.slide_down_out);
            if (vOperation.getVisibility() == View.VISIBLE) {
                vOperation.setAnimation(animation);
                vOperation.setVisibility(View.GONE);
            }

            llLayerExport.setVisibility(View.GONE);

            animation = AnimationUtils.loadAnimation(this, R.anim.slide_up_in);
            if (vSize.getVisibility() == View.GONE) {
                vSize.setAnimation(animation);
                vSize.setVisibility(View.VISIBLE);
            }

            tvToolBar.setVisibility(View.VISIBLE);
            ivTick.setVisibility(View.VISIBLE);
        } else {
            animation = AnimationUtils.loadAnimation(this, R.anim.slide_down_out);
            if (vSize.getVisibility() == View.VISIBLE) {
                vSize.setAnimation(animation);
                vSize.setVisibility(View.GONE);
            }

            tvToolBar.setVisibility(View.GONE);
            ivTick.setVisibility(View.GONE);

            animation = AnimationUtils.loadAnimation(this, R.anim.slide_up_in);
            if (vOperation.getVisibility() == View.GONE) {
                vOperation.setAnimation(animation);
                vOperation.setVisibility(View.VISIBLE);
            }

            llLayerExport.setVisibility(View.VISIBLE);
        }

        switch (position) {
            case positionCrop:
                animation = AnimationUtils.loadAnimation(this, R.anim.slide_down_out);
                if (vOperation.getVisibility() == View.VISIBLE) {
                    vOperation.setAnimation(animation);
                    vOperation.setVisibility(View.GONE);
                }

                llLayerExport.setVisibility(View.GONE);

                animation = AnimationUtils.loadAnimation(this, R.anim.slide_up_in);
                if (vSize.getVisibility() == View.GONE) {
                    vSize.setAnimation(animation);
                    vSize.setVisibility(View.VISIBLE);
                }

                tvToolBar.setVisibility(View.VISIBLE);
                ivTick.setVisibility(View.VISIBLE);

                if (vCrop.getVisibility() == View.GONE) vCrop.setVisibility(View.VISIBLE);
                if (bitmap != null) vCrop.setData(bitmap);

                if (vMain.getVisibility() == View.VISIBLE) vMain.setVisibility(View.GONE);
                if (vColor.getVisibility() == View.VISIBLE) vColor.setVisibility(View.GONE);
                break;
            case positionMain:
                if (vMain.getVisibility() == View.GONE) vMain.setVisibility(View.VISIBLE);
                if (bitmap != null) vMain.setImageBitmap(bitmap);

                if (vCrop.getVisibility() == View.VISIBLE) vCrop.setVisibility(View.GONE);
                if (vColor.getVisibility() == View.VISIBLE) vColor.setVisibility(View.GONE);
                break;
            case positionColor:
                isColor = true;
                if (vColor.getVisibility() == View.GONE) vColor.setVisibility(View.VISIBLE);
                if (colorModel != null) vColor.setData(colorModel);

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

                rlExpandEditBackground.clearAnimation();
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

        //Emoji
        tvTitleEditEmoji = findViewById(R.id.tvTitleEmoji);
        rlExpandEmoji = findViewById(R.id.rlExpandEmoji);
        rlExpandEditEmoji = findViewById(R.id.rlExpandEditEmoji);
        rlCancelPickEmoji = findViewById(R.id.rlCancelPickEmoji);
        rcvTypeEmoji = findViewById(R.id.rcvTitleEmoji);
        vpEmoji = findViewById(R.id.vpEmoji);
        rlCancelEditEmoji = findViewById(R.id.rlCancelEditEmoji);
        llEditEmoji = findViewById(R.id.llEditEmoji);
        rlDelEmoji = findViewById(R.id.rlDelEmoji);
        rlReplaceEmoji = findViewById(R.id.rlReplaceEmoji);
        rlOpacityEmoji = findViewById(R.id.rlOpacityEmoji);
        rlFlipYEmoji = findViewById(R.id.rlFlipY);
        rlFlipXEmoji = findViewById(R.id.rlFlipX);
        rlEditOpacityEmoji = findViewById(R.id.rlEditOpacityEmoji);
        sbOpacityEmoji = findViewById(R.id.sbOpacityEmoji);
        tvResetEmoji = findViewById(R.id.tvResetEditEmoji);

        //Image
        rlExpandEditImage = findViewById(R.id.rlExpandEditImage);
        vEditImage = findViewById(R.id.vEditImage);
        tvTitleEditImage = findViewById(R.id.tvTitleEditImage);
        tvResetImage = findViewById(R.id.tvResetImage);
        rlDelImage = findViewById(R.id.rlDelImage);
        rlReplaceImage = findViewById(R.id.rlReplaceImage);
        rlDuplicateImage = findViewById(R.id.rlDuplicateImage);
        rlCropImage = findViewById(R.id.rlCropImage);
        rlEditCrop = findViewById(R.id.rlEditCrop);
        tvCancelCropImage = findViewById(R.id.tvCancelCropImage);
        ivTickCropImage = findViewById(R.id.ivTickCropImage);
        rcvCropImage = findViewById(R.id.rcvEditCrop);
        pathCrop = findViewById(R.id.pathCrop);
        rlFilterImage = findViewById(R.id.rlFilterImage);
        rlEditFilterImage = findViewById(R.id.rlEditFilter);
        rcvEditFilterImage = findViewById(R.id.rcvEditFilter);
        rlCancelEditImage = findViewById(R.id.rlCancelEditImage);
        rlShadowImage = findViewById(R.id.rlShadowImage);
        llEditShadowImage = findViewById(R.id.llEditShadowImage);
        tvXPosImage = findViewById(R.id.tvXPosImage);
        tvYPosImage = findViewById(R.id.tvYPosImage);
        tvBlurImage = findViewById(R.id.tvBlurImage);
        sbXPosImage = findViewById(R.id.sbXPosImage);
        sbYPosImage = findViewById(R.id.sbYPosImage);
        sbBlurImage = findViewById(R.id.sbBlurImage);
        ivColorBlurImage = findViewById(R.id.ivColorBlurImage);
        rlOpacityImage = findViewById(R.id.rlOpacityImage);
        rlEditOpacityImage = findViewById(R.id.rlEditOpacityImage);
        sbOpacityImage = findViewById(R.id.sbOpacityImage);
        rlBlendImage = findViewById(R.id.rlBlendImage);
        rlEditBlendImage = findViewById(R.id.rlEditBlend);
        rcvEditBlendImage = findViewById(R.id.rcvEditBlend);

        //background
        rlExpandEditBackground = findViewById(R.id.rlExpandEditBackground);
        vEditBackground = findViewById(R.id.vEditBackground);
        rlEditBackground = findViewById(R.id.rlEditBackground);
        tvResetBackground = findViewById(R.id.tvResetBackground);
        tvTitleEditBackground = findViewById(R.id.tvTitleEditBackground);
        rlDelBackground = findViewById(R.id.rlDelBackground);
        rlReplaceBackground = findViewById(R.id.rlReplaceBackground);
        rlAdjustBackground = findViewById(R.id.rlAdjustBackground);
        rlFilterBackground = findViewById(R.id.rlFilterBackground);
        rlOpacityBackground = findViewById(R.id.rlOpacityBackground);
        rlFlipXBackground = findViewById(R.id.rlFlipXBackground);
        rlFlipYBackground = findViewById(R.id.rlFlipYBackground);
        rlEditAdjust = findViewById(R.id.rlEditAdjust);

        rlVignette = findViewById(R.id.rlVignette);
        rlVibrance = findViewById(R.id.rlVibrance);
        rlWarmth = findViewById(R.id.rlWarmth);
        rlHue = findViewById(R.id.rlHue);
        rlSaturation = findViewById(R.id.rlSaturation);
        rlWhites = findViewById(R.id.rlWhites);
        rlBlacks = findViewById(R.id.rlBlacks);
        rlShadows = findViewById(R.id.rlShadows);
        rlHighLight = findViewById(R.id.rlHighLight);
        rlExposure = findViewById(R.id.rlExposure);
        rlContrast = findViewById(R.id.rlContrast);
        rlBrightness = findViewById(R.id.rlBrightness);
        ivBrightness = findViewById(R.id.ivBrightness);
        tvBrightness = findViewById(R.id.tvBrightness);
        ivContrast = findViewById(R.id.ivContrast);
        tvContrast = findViewById(R.id.tvContrast);
        ivExposure = findViewById(R.id.ivExposure);
        tvExposure = findViewById(R.id.tvExposure);
        ivHighLight = findViewById(R.id.ivHighLight);
        tvHighLight = findViewById(R.id.tvHighLight);
        ivShadows = findViewById(R.id.ivShadows);
        tvShadows = findViewById(R.id.tvShadows);
        ivBlacks = findViewById(R.id.ivBlacks);
        tvBlacks = findViewById(R.id.tvBlacks);
        ivWhites = findViewById(R.id.ivWhites);
        tvWhites = findViewById(R.id.tvWhites);
        ivSaturation = findViewById(R.id.ivSaturation);
        tvSaturation = findViewById(R.id.tvSaturation);
        ivHue = findViewById(R.id.ivHue);
        tvHue = findViewById(R.id.tvHue);
        ivWarmth = findViewById(R.id.ivWarmth);
        tvWarmth = findViewById(R.id.tvWarmth);
        ivVibrance = findViewById(R.id.ivVibrance);
        tvVibrance = findViewById(R.id.tvVibrance);
        ivVignette = findViewById(R.id.ivVignette);
        tvVignette = findViewById(R.id.tvVignette);
        sbAdjust = findViewById(R.id.sbAdjust);
        tvAdjustBackground = findViewById(R.id.tvAdjustBackground);

        backgroundModel = new BackgroundModel();

        getData();
    }

    private int getId() {

        if (vSticker.getStickerCount() == 0) return 0;

        return vSticker.getStickerCount() + 1;
    }

    //delete Sticker
    private void delStick(Sticker sticker) {
        if (!checkCurrentSticker(sticker)) return;

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
            vSticker.remove(sticker);
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

    private boolean checkCurrentSticker(Sticker sticker) {
        if (sticker == null) {
            Utils.showToast(this, getResources().getString(R.string.choose_sticker_text));
            return false;
        }
        return true;
    }

    @Override
    protected void onStop() {
        isReplaceBackground = false;
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (isReplaceBackground) getData();
    }
}