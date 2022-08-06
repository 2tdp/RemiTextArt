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
import android.graphics.Matrix;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.datnt.remitextart.R;
import com.datnt.remitextart.activity.base.BaseActivity;
import com.datnt.remitextart.activity.project.CreateProjectActivity;
import com.datnt.remitextart.adapter.LayerAdapter;
import com.datnt.remitextart.adapter.TemplateAdapter;
import com.datnt.remitextart.adapter.decor.TitleDecorAdapter;
import com.datnt.remitextart.adapter.filterblend.BlendImageAdapter;
import com.datnt.remitextart.adapter.home.ColorAdapter;
import com.datnt.remitextart.adapter.filterblend.FilterImageAdapter;
import com.datnt.remitextart.adapter.OverlayAdapter;
import com.datnt.remitextart.adapter.ViewPagerAddFragmentsAdapter;
import com.datnt.remitextart.adapter.emoji.TitleEmojiAdapter;
import com.datnt.remitextart.adapter.image.CropImageAdapter;
import com.datnt.remitextart.callback.ItemTouchHelperAdapter;
import com.datnt.remitextart.callback.SimpleItemTouchHelperCallback;
import com.datnt.remitextart.customsticker.DrawableStickerCustom;
import com.datnt.remitextart.customsticker.TextStickerCustom;
import com.datnt.remitextart.customsticker.imgpro.actions.Blend;
import com.datnt.remitextart.customview.ColorView;
import com.datnt.remitextart.customview.CropImage;
import com.datnt.remitextart.customview.CropRatioView;
import com.datnt.remitextart.customview.CustomSeekbarRunText;
import com.datnt.remitextart.customview.CustomSeekbarTwoWay;
import com.datnt.remitextart.customview.OnSeekbarResult;
import com.datnt.remitextart.customview.stickerview.Sticker;
import com.datnt.remitextart.customview.stickerview.StickerView;
import com.datnt.remitextart.data.DataColor;
import com.datnt.remitextart.data.DataDecor;
import com.datnt.remitextart.data.DataEmoji;
import com.datnt.remitextart.data.DataOverlay;
import com.datnt.remitextart.data.DataPic;
import com.datnt.remitextart.data.DataTemplate;
import com.datnt.remitextart.data.FilterImage;
import com.datnt.remitextart.data.blend.BlendImage;
import com.datnt.remitextart.fragment.DecorFragment;
import com.datnt.remitextart.fragment.EmojiFragment;
import com.datnt.remitextart.fragment.ImageFragment;
import com.datnt.remitextart.model.BlendModel;
import com.datnt.remitextart.model.ColorModel;
import com.datnt.remitextart.model.DecorModel;
import com.datnt.remitextart.model.EmojiModel;
import com.datnt.remitextart.model.FilterModel;
import com.datnt.remitextart.model.LayerModel;
import com.datnt.remitextart.model.OverlayModel;
import com.datnt.remitextart.model.Project;
import com.datnt.remitextart.model.TemplateModel;
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
import com.datnt.remitextart.utils.UtilsBitmap;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.wysaid.nativePort.CGENativeLibrary;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class EditActivity extends BaseActivity {

    private String nameFolderBackground = "";
    private String nameFolderImage = "";
    private String nameFolder = "";
    private final int indexDefault = -1;
    private int indexMatrix = 0, indexProject = -1;
    private final int positionCrop = 0, positionMain = 1, positionColor = 2;
    private final int positionOriginal = 0, position1_1 = 1, position9_16 = 2, position4_5 = 3,
            position16_9 = 4;
    private final int positionAddText = 0, positionEmoji = 1, positionImage = 2, positionBackground = 3,
            positionOverlay = 4, positionDecor = 5, positionSize = 6, positionTemp = 7, positionLayer = 8;

    //Layer
    private RelativeLayout rlExpandLayer, rlCancelLayer, rlDelLayer, rlDuplicateLayer, rlLook, rlLock;
    private ImageView ivLook, ivLock;
    private RecyclerView rcvLayer;
    private LayerAdapter layerAdapter;
    private boolean isDelLayer;

    //Template
    private HorizontalScrollView vEditTemp;
    private RelativeLayout rlPickTemp, rlCancelPickTemp, rlExpandEditTemp, rlCancelEditTemp,
            rlDelTemp, rlReplaceTemp, rlDuplicateTemp, rlColorTemp, rlBackgroundTemp, rlShadowTemp,
            rlOpacityTemp, rlFlipXTemp, rlFlipYTemp, rlEditColorTemp, rlEditOpacityTemp;
    private LinearLayout llEditShadowTemp;
    private CustomSeekbarTwoWay sbXPosTemp, sbYPosTemp, sbBlurTemp;
    private CustomSeekbarRunText sbOpacityTemp;
    private TextView tvTitleEditTemp, tvResetEditTemp, tvXPosTemp, tvYPosTemp, tvBlurTemp;
    private ImageView ivColorTemp, ivColorBlurTemp;
    private RecyclerView rcvTextTemp, rcvEditColorTemp;

    //decor
    private HorizontalScrollView vEditDecor;
    private RelativeLayout rlExpandPickDecor, rlCancelPickDecor, rlExpandEditDecor, rlCancelEditDecor,
            rlEditOpacityDecor, rlEditColorDecor, rlDelDecor, rlReplaceDecor, rlDuplicateDecor,
            rlColorDecor, rlShadowDecor, rlOpacityDecor, rlFlipYDecor, rlFlipXDecor;
    private LinearLayout llEditShadowDecor;
    private TextView tvResetEditDecor, tvTitleEditDecor, tvXPosDecor, tvYPosDecor, tvBlurDecor;
    private ImageView ivColorDecor, ivColorBlurDecor;
    private CustomSeekbarRunText sbOpacityDecor;
    private CustomSeekbarTwoWay sbXPosDecor, sbYPosDecor, sbBlurDecor;
    private RecyclerView rcvTypeDecor, rcvEditColorDecor;
    private ViewPager2 vpDecor;
    private TitleDecorAdapter titleDecorAdapter;
    private boolean isReplaceDecor;

    //Overlay
    private RelativeLayout rlPickOverlay, rlCancelPickOverlay, rlExpandEditOverlay, rlCancelEditOverlay,
            rlDelOverlay, rlReplaceOverlay, rlOpacityOverlay, rlFlipYOverlay, rlFlipXOverlay, rlEditOpacityOverlay;
    private LinearLayout llEditOverlay;
    private RecyclerView rcvOverlay;
    private TextView tvTitleEditOverlay, tvResetEditOverlay;
    private CustomSeekbarRunText sbOpacityOverlay;
    private final String strOverlay = "@blend screen image 100";

    //background
    private RelativeLayout rlExpandEditBackground, rlDelBackground, rlReplaceBackground, rlAdjustBackground,
            rlFilterBackground, rlOpacityBackground, rlFlipYBackground, rlFlipXBackground, rlCancelEditBackground,
            rlEditAdjust, rlVignette, rlVibrance, rlWarmth, rlHue, rlSaturation, rlWhites, rlBlacks,
            rlShadows, rlHighLight, rlExposure, rlContrast, rlBrightness, rlEditFilterBackground,
            rlEditOpacityBackground;
    private TextView tvTitleEditBackground, tvResetBackground, tvVignette, tvVibrance, tvWarmth,
            tvHue, tvSaturation, tvWhites, tvBlacks, tvShadows, tvHighLight, tvExposure, tvContrast,
            tvBrightness, tvAdjustBackground;
    private ImageView ivVignette, ivVibrance, ivWarmth, ivHue, ivSaturation, ivWhites, ivBlacks,
            ivShadows, ivHighLight, ivExposure, ivContrast, ivBrightness, ivLoadingFilterBackground;
    private CustomSeekbarTwoWay sbAdjust;
    private CustomSeekbarRunText sbOpacityBackground;
    private RecyclerView rcvFilterBackground;
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
    private FilterImageAdapter filterImageAdapter;
    private BlendImageAdapter blendImageAdapter;
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
            iv16_9, ivLoading;
    private TextView tvToolBar, tvOriginal, tv1_1, tv9_16, tv4_5, tv16_9;
    private CropRatioView vCrop;
    private ColorView vColor;
    private StickerView vSticker;

    private Project project;
    private Sticker stickerOld = null;
    private ViewPager2 vpEmoji;
    private ViewPagerAddFragmentsAdapter addFragmentsAdapter;
    private Animation animation;
    private BackgroundModel backgroundModel;
    private ArrayList<FilterModel> lstFilter;
    private ArrayList<BlendModel> lstBlend;
    private ArrayList<ColorModel> lstColor;
    private boolean isColor, isBackground, isReplaceBackground;
    private String strPicUserOld = "old", strPicAppOld = "old";
    private ColorModel colorModelOld = null;
    private TemplateModel templatelOld = new TemplateModel();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        init();
        createProject();
    }

    private void createProject() {
        if (DataLocalManager.getInt(Utils.NUMB_PROJECT) == indexDefault)
            DataLocalManager.setInt(1, Utils.NUMB_PROJECT);
        else {
            int count = DataLocalManager.getInt(Utils.NUMB_PROJECT) + 1;
            DataLocalManager.setInt(count, Utils.NUMB_PROJECT);
        }
        nameFolder = Utils.NUMB_PROJECT + "_" + DataLocalManager.getInt(Utils.NUMB_PROJECT);
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
                stickerOld = sticker;
                vSticker.hideBorderAndIcon(1);

                if (project != null)
                    checkMatrixStickerProject(sticker);

                setMatrixToModel(sticker);
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
                setMatrixToModel(sticker);
            }

            @Override
            public void onStickerTouchedDown(@NonNull Sticker sticker) {
            }

            @Override
            public void onStickerZoomFinished(@NonNull Sticker sticker) {
                vSticker.hideBorderAndIcon(1);
                vSticker.invalidate();

                setMatrixToModel(sticker);
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
        if ((sticker != stickerOld || vOperation.getVisibility() == View.VISIBLE) && rlExpandLayer.getVisibility() == View.GONE) {
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
                    case Utils.DECOR:
                        seekAndHideOperation(positionDecor);
                        break;
                    case Utils.TEMPLATE:
                        seekAndHideOperation(positionTemp);
                        break;
                }
            }
            stickerOld = sticker;
        } else if (layerAdapter != null) layerAdapter.setCurrent(vSticker.getIndexStickerCurrent());
    }

    private void checkMatrixStickerProject(Sticker sticker) {
        if (!checkCurrentSticker(sticker)) return;

        Matrix matrix = new Matrix();
        if (sticker instanceof TextStickerCustom) {
            TextStickerCustom textSticker = (TextStickerCustom) sticker;
            matrix.setValues(textSticker.getTextModel().getMatrix());
            sticker.setMatrix(matrix);

            indexMatrix++;
            setMatrixText(indexMatrix);
        } else {
            DrawableStickerCustom drawableSticker = (DrawableStickerCustom) sticker;
            switch (drawableSticker.getTypeSticker()) {
                case Utils.EMOJI:
                    matrix.setValues(drawableSticker.getEmojiModel().getMatrix());
                    sticker.setMatrix(matrix);

                    indexMatrix++;
                    setMatrixEmoji(indexMatrix);
                    break;
                case Utils.IMAGE:
                    matrix.setValues(drawableSticker.getImageModel().getMatrix());
                    sticker.setMatrix(matrix);

                    indexMatrix++;
                    setMatrixImage(indexMatrix);
                    break;
                case Utils.DECOR:
                    matrix.setValues(drawableSticker.getDecorModel().getMatrix());
                    sticker.setMatrix(matrix);

                    indexMatrix++;
                    setMatrixDecor(indexMatrix);
                    break;
                case Utils.TEMPLATE:
                    matrix.setValues(drawableSticker.getTemplateModel().getMatrix());
                    sticker.setMatrix(matrix);

                    indexMatrix++;
                    setMatrixTemp(indexMatrix);
                    break;
            }
        }
        vSticker.invalidate();
    }

    private void setMatrixToModel(Sticker sticker) {
        if (checkCurrentSticker(sticker)) {
            float[] matrix = new float[9];
            sticker.getMatrix().getValues(matrix);

            if (sticker instanceof TextStickerCustom) {
                TextStickerCustom textSticker = (TextStickerCustom) sticker;
                textSticker.getTextModel().setMatrix(matrix);
            }
            if (sticker instanceof DrawableStickerCustom) {
                DrawableStickerCustom drawableSticker = (DrawableStickerCustom) sticker;
                switch (drawableSticker.getTypeSticker()) {
                    case Utils.EMOJI:
                        drawableSticker.getEmojiModel().setMatrix(matrix);
                        break;
                    case Utils.IMAGE:
                        drawableSticker.getImageModel().setMatrix(matrix);
                        break;
                    case Utils.DECOR:
                        drawableSticker.getDecorModel().setMatrix(matrix);
                        break;
                    case Utils.TEMPLATE:
                        drawableSticker.getTemplateModel().setMatrix(matrix);
                        break;
                }
            }
        }
    }

    private void evenClick() {
        //toolbar
        ivTick.setOnClickListener(v -> clickTick());
        ivBack.setOnClickListener(v -> onBackPressed());
        ivExport.setOnClickListener(v -> exportPhoto());

        rlMain.setOnClickListener(v -> {
            if (vCrop.getVisibility() == View.GONE) {
                vSticker.setCurrentSticker(null);
                seekAndHideOperation(indexDefault);
            }
        });
        rlEditText.setOnClickListener(v -> {
            if (vEditText.getVisibility() == View.VISIBLE) {
                vSticker.setCurrentSticker(null);
                seekAndHideOperation(indexDefault);
            } else seekAndHideOperation(positionAddText);
        });
        rlCancelPickEmoji.setOnClickListener(v -> seekAndHideOperation(indexDefault));
        rlCancelEditEmoji.setOnClickListener(v -> {
            if (llEditEmoji.getVisibility() == View.VISIBLE) {
                vSticker.setCurrentSticker(null);
                seekAndHideOperation(indexDefault);
            } else seekAndHideOperation(positionEmoji);
        });
        rlCancelEditImage.setOnClickListener(v -> {
            if (vEditImage.getVisibility() == View.VISIBLE) {
                vSticker.setCurrentSticker(null);
                seekAndHideOperation(indexDefault);
            } else seekAndHideOperation(positionImage);
        });
        rlCancelEditBackground.setOnClickListener(v -> {
            if (vEditBackground.getVisibility() == View.VISIBLE) {
                vSticker.setCurrentSticker(null);
                seekAndHideOperation(indexDefault);
            } else
                seekAndHideOperation(positionBackground);
        });
        rlCancelPickOverlay.setOnClickListener(v -> seekAndHideOperation(indexDefault));
        rlCancelEditOverlay.setOnClickListener(v -> {
            if (llEditOverlay.getVisibility() == View.VISIBLE) {
                vSticker.setCurrentSticker(null);
                seekAndHideOperation(indexDefault);
            } else seekAndHideOperation(positionOverlay);
        });
        rlCancelPickDecor.setOnClickListener(v -> seekAndHideOperation(indexDefault));
        rlCancelEditDecor.setOnClickListener(v -> {
            if (vEditDecor.getVisibility() == View.VISIBLE) {
                vSticker.setCurrentSticker(null);
                seekAndHideOperation(indexDefault);
            } else seekAndHideOperation(positionDecor);
        });
        rlCancelPickTemp.setOnClickListener(v -> seekAndHideOperation(indexDefault));
        rlCancelEditTemp.setOnClickListener(v -> {
            if (vEditTemp.getVisibility() == View.VISIBLE) {
                vSticker.setCurrentSticker(null);
                seekAndHideOperation(indexDefault);
            } else seekAndHideOperation(positionTemp);
        });
        rlCancelLayer.setOnClickListener(v -> seekAndHideOperation(indexDefault));

        //addSticker
        rlAddText.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddTextActivity.class);
            launcherEditText.launch(intent, ActivityOptionsCompat.makeCustomAnimation(this, R.anim.slide_in_right, R.anim.slide_out_left));
        });
        rlDelText.setOnClickListener(v -> delStick(vSticker.getCurrentSticker()));
        rlET.setOnClickListener(v -> replaceText(vSticker.getCurrentSticker()));
        rlDuplicateText.setOnClickListener(v -> duplicate(vSticker.getCurrentSticker()));
        rlFontSize.setOnClickListener(v -> fontSizeText(vSticker.getCurrentSticker()));
        rlColorText.setOnClickListener(v -> colorText(vSticker.getCurrentSticker()));
        rlTransformText.setOnClickListener(v -> transformText(vSticker.getCurrentSticker()));
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
            getData(3, "", new ColorModel(Color.WHITE, Color.WHITE, 0, false), null, false);
            Utils.showToast(this, getResources().getString(R.string.del));
        });
        rlReplaceBackground.setOnClickListener(v -> replaceBackground());
        rlAdjustBackground.setOnClickListener(v -> adjustBackground());
        rlFilterBackground.setOnClickListener(v -> filterBackground());
        rlOpacityBackground.setOnClickListener(v -> opacityBackground());
        rlFlipXBackground.setOnClickListener(v -> flipBackground(true));
        rlFlipYBackground.setOnClickListener(v -> flipBackground(false));

        //overlay
        rlOverlay.setOnClickListener(v -> {
            if (backgroundModel.getUriOverlayRoot().equals("")) pickOverlay();
            else seekAndHideOperation(positionOverlay);
        });
        rlDelOverlay.setOnClickListener(v -> delOverlay());
        rlReplaceOverlay.setOnClickListener(v -> pickOverlay());
        rlOpacityOverlay.setOnClickListener(v -> opacityOverlay());
        rlFlipXOverlay.setOnClickListener(v -> flipOverlay(true));
        rlFlipYOverlay.setOnClickListener(v -> flipOverlay(false));

        //decor
        rlDecor.setOnClickListener(v -> {
            isReplaceDecor = false;
            pickDecor();
        });
        rlDelDecor.setOnClickListener(v -> delStick(vSticker.getCurrentSticker()));
        rlReplaceDecor.setOnClickListener(v -> replace(vSticker.getCurrentSticker()));
        rlDuplicateDecor.setOnClickListener(v -> duplicate(vSticker.getCurrentSticker()));
        rlColorDecor.setOnClickListener(v -> colorDecor(vSticker.getCurrentSticker()));
        rlShadowDecor.setOnClickListener(v -> shadowDecor(vSticker.getCurrentSticker()));
        rlOpacityDecor.setOnClickListener(v -> opacity(vSticker.getCurrentSticker()));
        rlFlipXDecor.setOnClickListener(v -> {
            vSticker.flipCurrentSticker(0);
            flip(vSticker.getCurrentSticker(), true, false);
        });
        rlFlipYDecor.setOnClickListener(v -> {
            vSticker.flipCurrentSticker(1);
            flip(vSticker.getCurrentSticker(), false, true);
        });

        //temp
        rlDelTemp.setOnClickListener(v -> delStick(vSticker.getCurrentSticker()));
        rlReplaceTemp.setOnClickListener(v -> replace(vSticker.getCurrentSticker()));
        rlDuplicateTemp.setOnClickListener(v -> duplicate(vSticker.getCurrentSticker()));
        rlColorTemp.setOnClickListener(v -> colorTemp(vSticker.getCurrentSticker()));
        rlBackgroundTemp.setOnClickListener(v -> replaceBackground());
        rlShadowTemp.setOnClickListener(v -> shadowTemp(vSticker.getCurrentSticker()));
        rlOpacityTemp.setOnClickListener(v -> opacity(vSticker.getCurrentSticker()));
        rlFlipXTemp.setOnClickListener(v -> {
            vSticker.flipCurrentSticker(0);
            flip(vSticker.getCurrentSticker(), true, false);
        });
        rlFlipYTemp.setOnClickListener(v -> {
            vSticker.flipCurrentSticker(1);
            flip(vSticker.getCurrentSticker(), false, true);
        });

        //layer
        ivLayer.setOnClickListener(v -> {
            if (vSticker.getStickerCount() > 0) layer();
            else Utils.showToast(EditActivity.this, "No Sticker");
        });
        rlDelLayer.setOnClickListener(v -> {
            isDelLayer = true;
            delStick(vSticker.getCurrentSticker());
        });
        rlDuplicateLayer.setOnClickListener(v -> duplicate(vSticker.getCurrentSticker()));
        rlLock.setOnClickListener(v -> lockLayer(vSticker.getCurrentSticker()));
        rlLook.setOnClickListener(v -> lookLayer(vSticker.getCurrentSticker()));

        //size
        rlCrop.setOnClickListener(v -> {
            isReplaceBackground = true;
            seekAndHideOperation(positionSize);
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
            if (ivLoading.getVisibility() == View.GONE) ivLoading.setVisibility(View.VISIBLE);
            dialog.cancel();
            new Thread(() -> {
                if (!isColor)
                    Utils.saveImage(EditActivity.this, UtilsBitmap.overlay(UtilsBitmap.loadBitmapFromView(vMain, false),
                            vSticker.saveImage(EditActivity.this)), "remiTextArt");
                else
                    Utils.saveImage(EditActivity.this, UtilsBitmap.overlay(UtilsBitmap.loadBitmapFromView(vColor, true),
                            vSticker.saveImage(EditActivity.this)), "remiTextArt");

                handlerLoading.sendEmptyMessage(1);
            }).start();
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
            Bitmap bmMain;
            Bitmap bm = vCrop.getCroppedImage();
            if ((float) bm.getWidth() / bm.getHeight() > scaleScreen)
                bmMain = Bitmap.createScaledBitmap(bm, wMain, wMain * bm.getHeight() / bm.getWidth(), false);
            else
                bmMain = Bitmap.createScaledBitmap(bm, hMain * bm.getWidth() / bm.getHeight(), hMain, false);

            backgroundModel.setUriCache(UtilsBitmap.saveBitmapToApp(this, bmMain,
                    nameFolderBackground, Utils.BACKGROUND));

            if (!backgroundModel.getUriOverlayRoot().equals("")) {
                if (ivLoading.getVisibility() == View.GONE) ivLoading.setVisibility(View.VISIBLE);
                addOverlay(backgroundModel.getOverlayModel());
            }

            //setSize
            vSticker.getLayoutParams().width = bmMain.getWidth();
            vMain.getLayoutParams().width = bmMain.getWidth();
            vSticker.getLayoutParams().height = bmMain.getHeight();
            vMain.getLayoutParams().height = bmMain.getHeight();

            vMain.setAlpha(backgroundModel.getOpacity() / 100f);

            if (templatelOld.getBackground().equals(""))
                seekAndHideViewMain(positionMain, bmMain, colorModelOld, false);
            else seekAndHideViewMain(positionTemp, bmMain, colorModelOld, false);
        } else {
            vSticker.getLayoutParams().height = (int) vColor.getH();
            vSticker.getLayoutParams().width = (int) vColor.getW();
            vColor.getLayoutParams().width = (int) vColor.getW();
            vColor.getLayoutParams().height = (int) vColor.getH();

            backgroundModel.setSizeViewColor(vColor.getSize());

            vMain.setAlpha(backgroundModel.getOpacity() * 255 / 100f);
            seekAndHideViewMain(positionColor, null, backgroundModel.getColorModel(), false);
        }
    }

    //Flip Sticker
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
            case Utils.DECOR:
                if (flipX)
                    drawableSticker.getDecorModel().setFlipX(!drawableSticker.getDecorModel().isFlipX());
                if (flipY)
                    drawableSticker.getDecorModel().setFlipY(!drawableSticker.getDecorModel().isFlipY());
                break;
            case Utils.TEMPLATE:
                if (flipX)
                    drawableSticker.getTemplateModel().setFlipX(!drawableSticker.getTemplateModel().isFlipX());
                if (flipY)
                    drawableSticker.getTemplateModel().setFlipY(!drawableSticker.getTemplateModel().isFlipY());
                break;
        }
    }

    //Opacity Sticker
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
            case Utils.DECOR:
                opacityDecor(drawableSticker);
                break;
            case Utils.TEMPLATE:
                opacityTemplate(drawableSticker);
                break;
        }
    }

    //Duplicate Sticker
    private void duplicate(Sticker sticker) {
        if (!checkCurrentSticker(sticker)) return;

        if (sticker instanceof DrawableStickerCustom) {
            DrawableStickerCustom drawableSticker = (DrawableStickerCustom) sticker;
            switch (drawableSticker.getTypeSticker()) {
                case Utils.EMOJI:
                    vSticker.addSticker(drawableSticker.getEmojiModel().duplicate(this, getId()));
                    break;
                case Utils.IMAGE:
                    vSticker.addSticker(drawableSticker.getImageModel().duplicate(this, getId()));
                    break;
                case Utils.DECOR:
                    vSticker.addSticker(drawableSticker.getDecorModel().duplicate(this, getId()));
                    break;
                case Utils.TEMPLATE:
                    vSticker.addSticker(drawableSticker.getTemplateModel().duplicate(this, getId()));
                    break;
            }
        } else {
            TextStickerCustom textSticker = (TextStickerCustom) sticker;

            TextStickerCustom newSticker = (TextStickerCustom) textSticker.getTextModel().duplicate(this, getId());
            vSticker.addSticker(newSticker);

            vSticker.invalidate();
        }

        if (rlExpandLayer.getVisibility() == View.VISIBLE)
            layerAdapter.setData(vSticker.getListLayer());
    }

    //replace Sticker
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
            case Utils.DECOR:
                isReplaceDecor = true;
                pickDecor();
                break;
            case Utils.TEMPLATE:
                pickTextTemp(drawableSticker);
                break;
        }
    }

    //Template
    private void pickTextTemp(DrawableStickerCustom drawableSticker) {
        rlPickTemp.getLayoutParams().height = getResources().getDisplayMetrics().heightPixels * 90 / 100;
        seekAndHideViewTemp(indexDefault);

        TemplateAdapter templateAdapter = new TemplateAdapter(this, R.layout.item_template_text, (o, pos) -> {
            TemplateModel template = (TemplateModel) o;

            drawableSticker.getTemplateModel().setLstPathDataText(template.getLstPathDataText());
            drawableSticker.replaceTemp();

            vSticker.invalidate();

            seekAndHideOperation(positionTemp);
        });

        templateAdapter.setData(DataTemplate.getTemplate(this, ""));

        GridLayoutManager manager = new GridLayoutManager(this, 2);
        rcvTextTemp.setLayoutManager(manager);
        rcvTextTemp.setAdapter(templateAdapter);
    }

    //Color Temp
    private void colorTemp(Sticker sticker) {
        if (!checkCurrentSticker(sticker)) return;
        seekAndHideViewTemp(0);

        DrawableStickerCustom drawableSticker = (DrawableStickerCustom) sticker;
        ColorModel colorOld = drawableSticker.getTemplateModel().getColorModel();
        tvResetEditTemp.setOnClickListener(v -> resetTemp(0, drawableSticker,
                colorOld, null, indexDefault));

        ColorAdapter colorAdapter = new ColorAdapter(this, R.layout.item_color_edit, (o, pos) -> {
            ColorModel color = (ColorModel) o;
            if (pos == 0)
                DataColor.pickColor(this, c -> setColorDrawableSticker(drawableSticker, c));
            else if (color.getColorStart() == color.getColorEnd())
                setColorDrawableSticker(drawableSticker, color);
            else
                DataColor.pickDirection(this, color, c -> setColorDrawableSticker(drawableSticker, c));
        });
        if (!lstColor.isEmpty()) colorAdapter.setData(lstColor);

        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rcvEditColorTemp.setLayoutManager(manager);
        rcvEditColorTemp.setAdapter(colorAdapter);
    }

    //Shadow Temp
    private void shadowTemp(Sticker sticker) {
        if (!checkCurrentSticker(sticker)) return;
        seekAndHideViewTemp(1);

        if (sticker instanceof DrawableStickerCustom) {
            DrawableStickerCustom drawableSticker = (DrawableStickerCustom) sticker;

            setShadowTempCurrent(drawableSticker);

            sbXPosTemp.setOnSeekbarResult(new OnSeekbarResult() {
                @Override
                public void onDown(View v) {

                }

                @Override
                public void onMove(View v, int value) {
                    tvXPosTemp.setText(String.valueOf(value));
                    drawableSticker.getTemplateModel().getShadowModel().setXPos(value);
                    drawableSticker.getTemplateModel().getShadowModel().setBlur(blur);
                    vSticker.replace(drawableSticker.getTemplateModel().shadow(EditActivity.this, drawableSticker), true);
                }

                @Override
                public void onUp(View v, int value) {

                }
            });

            sbYPosTemp.setOnSeekbarResult(new OnSeekbarResult() {
                @Override
                public void onDown(View v) {

                }

                @Override
                public void onMove(View v, int value) {
                    tvYPosTemp.setText(String.valueOf(value));
                    drawableSticker.getTemplateModel().getShadowModel().setYPos(value);
                    drawableSticker.getTemplateModel().getShadowModel().setBlur(blur);
                    vSticker.replace(drawableSticker.getTemplateModel().shadow(EditActivity.this, drawableSticker), true);
                }

                @Override
                public void onUp(View v, int value) {

                }
            });

            sbBlurTemp.setOnSeekbarResult(new OnSeekbarResult() {
                @Override
                public void onDown(View v) {

                }

                @Override
                public void onMove(View v, int value) {
                    if (value == -50) blur = 1f;
                    else if (value == 50) blur = 10f;
                    else if (value == 0) blur = 5f;
                    else blur = 5 + (value * 5 / 50f);

                    tvBlurTemp.setText(String.valueOf(value));
                    drawableSticker.getTemplateModel().getShadowModel().setBlur(blur);
                    vSticker.replace(drawableSticker.getTemplateModel().shadow(EditActivity.this, drawableSticker), true);
                }

                @Override
                public void onUp(View v, int value) {

                }
            });

            ivColorBlurTemp.setOnClickListener(v -> DataColor.pickColor(this, (color) -> {
                drawableSticker.getTemplateModel().getShadowModel().setColorBlur(color.getColorStart());
                vSticker.replace(drawableSticker.getTemplateModel().shadow(EditActivity.this, drawableSticker), true);
            }));
        }
    }

    private void setShadowTempCurrent(DrawableStickerCustom drawableSticker) {
        float xPos = 0f, yPos = 0f;
        int color = 0;
        ShadowModel shadowModel = drawableSticker.getTemplateModel().getShadowModel();
        if (shadowModel != null) {
            xPos = (shadowModel.getXPos());
            yPos = (shadowModel.getYPos());
            blur = (shadowModel.getBlur());
            color = shadowModel.getColorBlur();
        } else
            drawableSticker.getTemplateModel().setShadowModel(new ShadowModel(xPos, yPos, blur, color));

        ShadowModel shadowModelOld = new ShadowModel(xPos, yPos, blur, color);

        tvResetEditTemp.setOnClickListener(v -> resetTemp(1, drawableSticker, null,
                shadowModelOld, 0));

        tvXPosTemp.setText(String.valueOf((int) xPos));
        sbXPosTemp.setProgress((int) xPos);
        tvYPosTemp.setText(String.valueOf((int) yPos));
        sbYPosTemp.setProgress((int) yPos);
        if (blur == 0) tvBlurTemp.setText(String.valueOf(0));
        else tvBlurTemp.setText(String.valueOf((int) ((blur - 5) * 10f)));

        if (blur > 5) sbBlurTemp.setProgress((int) ((blur - 5) * 10f));
        else if (blur == 0f) {
            sbBlurTemp.setProgress(0);
            blur = 5f;
        } else sbBlurTemp.setProgress((int) ((5 - blur) * 10f));

        drawableSticker.getTemplateModel().getShadowModel().setBlur(blur);
    }

    private void opacityTemplate(DrawableStickerCustom drawableSticker) {
        seekAndHideViewTemp(2);

        int opacityOld = drawableSticker.getTemplateModel().getOpacity() * 100 / 255;
        sbOpacityTemp.setProgress(opacityOld);
        tvResetEditTemp.setOnClickListener(v -> resetTemp(2, drawableSticker, null, null, opacityOld));

        sbOpacityTemp.setOnSeekbarResult(new OnSeekbarResult() {
            @Override
            public void onDown(View v) {

            }

            @Override
            public void onMove(View v, int value) {
                drawableSticker.getTemplateModel().setOpacity(value * 255 / 100);
                vSticker.replace(drawableSticker.getTemplateModel().opacity(EditActivity.this, drawableSticker), true);
            }

            @Override
            public void onUp(View v, int value) {

            }
        });
    }

    private void resetTemp(int position, DrawableStickerCustom drawableSticker, ColorModel colorModel,
                           ShadowModel shadowModel, int opacity) {
        switch (position) {
            case 0:
                if (colorModel == null) setColorDrawableSticker(drawableSticker,
                        new ColorModel(Color.WHITE, Color.WHITE, 1, false));
                else setColorDrawableSticker(drawableSticker, colorModel);
                break;
            case 1:
                float xPos = shadowModel.getXPos();
                float yPos = shadowModel.getYPos();
                float blurOld = shadowModel.getBlur();
                int color = shadowModel.getColorBlur();

                tvXPosTemp.setText(String.valueOf((int) xPos));
                sbXPosTemp.setProgress((int) xPos);
                tvYPosTemp.setText(String.valueOf((int) yPos));
                sbYPosTemp.setProgress((int) yPos);
                if (blurOld == 0f) tvBlurTemp.setText(String.valueOf(0));
                else tvBlurTemp.setText(String.valueOf((int) ((blurOld - 5) * 10f)));
                if (blurOld > 5) sbBlurTemp.setProgress((int) ((blurOld - 5) * 10f));
                else if (blurOld == 0f) sbBlurTemp.setProgress(0);
                else sbBlurTemp.setProgress((int) ((5 - blurOld) * 10f));

                drawableSticker.getTemplateModel().getShadowModel().setXPos(xPos);
                drawableSticker.getTemplateModel().getShadowModel().setYPos(yPos);
                drawableSticker.getTemplateModel().getShadowModel().setBlur(blurOld);
                drawableSticker.getTemplateModel().getShadowModel().setColorBlur(color);

                vSticker.replace(drawableSticker.getTemplateModel().shadow(EditActivity.this, drawableSticker), true);
                break;
            case 2:
                drawableSticker.getTemplateModel().setOpacity(opacity);
                drawableSticker.setAlpha((int) (opacity * 255 / 100f));
                sbOpacityTemp.setProgress(opacity);

                vSticker.invalidate();
                break;
        }
    }

    private void seekAndHideViewTemp(int position) {
        animation = AnimationUtils.loadAnimation(this, R.anim.slide_down_out);
        if (vOperation.getVisibility() == View.VISIBLE) {
            vOperation.setAnimation(animation);
            vOperation.setVisibility(View.GONE);
        }

        if (vEditTemp.getVisibility() == View.VISIBLE) {
            vEditTemp.setAnimation(animation);
            vEditTemp.setVisibility(View.GONE);
        }

        switch (position) {
            case 2:
                animation = AnimationUtils.loadAnimation(this, R.anim.slide_up_in);
                if (rlEditOpacityTemp.getVisibility() == View.GONE) {
                    rlEditOpacityTemp.setAnimation(animation);
                    rlEditOpacityTemp.setVisibility(View.VISIBLE);
                }

                tvTitleEditTemp.setText(getResources().getString(R.string.opacity));
                sbOpacityTemp.setColorText(getResources().getColor(R.color.green));
                sbOpacityTemp.setSizeText(com.intuit.ssp.R.dimen._10ssp);
                sbOpacityTemp.setProgress(100);
                sbOpacityTemp.setMax(100);

                if (tvResetEditTemp.getVisibility() == View.GONE)
                    tvResetEditTemp.setVisibility(View.VISIBLE);
                break;
            case 1:
                animation = AnimationUtils.loadAnimation(this, R.anim.slide_up_in);
                if (llEditShadowTemp.getVisibility() == View.GONE) {
                    llEditShadowTemp.setAnimation(animation);
                    llEditShadowTemp.setVisibility(View.VISIBLE);
                }

                tvTitleEditTemp.setText(R.string.shadow);
                tvXPosTemp.setText(String.valueOf(0));
                tvYPosTemp.setText(String.valueOf(0));
                tvBlurTemp.setText(String.valueOf(0));
                sbXPosTemp.setMax(100);
                sbYPosTemp.setMax(100);
                sbBlurTemp.setMax(100);
                blur = 0f;

                if (tvResetEditTemp.getVisibility() == View.GONE)
                    tvResetEditTemp.setVisibility(View.VISIBLE);
                break;
            case 0:
                animation = AnimationUtils.loadAnimation(this, R.anim.slide_up_in);
                if (rlEditColorTemp.getVisibility() == View.GONE) {
                    rlEditColorTemp.setAnimation(animation);
                    rlEditColorTemp.setVisibility(View.VISIBLE);
                }

                if (tvResetEditTemp.getVisibility() == View.GONE)
                    tvResetEditTemp.setVisibility(View.VISIBLE);

                tvTitleEditTemp.setText(R.string.color);
                break;
            default:
                animation = AnimationUtils.loadAnimation(this, R.anim.slide_up_in);
                if (rlPickTemp.getVisibility() == View.GONE) {
                    rlPickTemp.setAnimation(animation);
                    rlPickTemp.setVisibility(View.VISIBLE);
                }

                if (rlExpandEditTemp.getVisibility() == View.VISIBLE)
                    rlExpandEditTemp.setVisibility(View.GONE);

                if (tvResetEditTemp.getVisibility() == View.VISIBLE)
                    tvResetEditTemp.setVisibility(View.GONE);

                tvTitleEditTemp.setText(R.string.temp);
                break;
        }
    }

    //Layer
    private void layer() {
        seekAndHideOperation(positionLayer);

        ArrayList<LayerModel> lstLayer = vSticker.getListLayer();

        layerAdapter = new LayerAdapter(this, (o, pos) -> {
            LayerModel layer = (LayerModel) o;
            Sticker sticker = layer.getSticker();
            layerAdapter.setCurrent(pos);

            vSticker.setCurrentSticker(sticker);

            if (sticker.isLock()) setUpLayoutLockLayer(0);
            else setUpLayoutLockLayer(1);

            if (sticker.isLook()) setUpLayoutLookLayer(0);
            else setUpLayoutLookLayer(1);
        });


        layerAdapter.setData(lstLayer);
        if (!vSticker.getListLayer().isEmpty()) {
            layerAdapter.setCurrent(0);
            vSticker.setCurrentSticker(lstLayer.get(0).getSticker());
        }

        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rcvLayer.setLayoutManager(manager);
        rcvLayer.setAdapter(layerAdapter);

        ItemTouchHelper touchHelper = new ItemTouchHelper(new SimpleItemTouchHelperCallback(new ItemTouchHelperAdapter() {
            @Override
            public void onItemMove(int fromPosition, int toPosition) {
                if (fromPosition < toPosition)
                    for (int i = fromPosition; i < toPosition; i++) {
                        Collections.swap(lstLayer, i, i + 1);
                    }
                else
                    for (int i = fromPosition; i > toPosition; i--) {
                        Collections.swap(lstLayer, i, i - 1);
                    }

                vSticker.swapLayers(fromPosition, toPosition);

                layerAdapter.notifyItemMoved(fromPosition, toPosition);
            }

            @Override
            public void onItemDismiss(int position) {

            }
        }));
        touchHelper.attachToRecyclerView(rcvLayer);


    }

    //lock
    private void lockLayer(Sticker sticker) {
        if (sticker != null) {
            if (sticker.isLock()) {
                sticker.setLock(false);
                setUpLayoutLockLayer(1);
            } else {
                sticker.setLock(true);
                setUpLayoutLockLayer(0);
            }
            layerAdapter.changeNotify();
        }
    }

    private void setUpLayoutLockLayer(int pos) {
        switch (pos) {
            case 0:
                ivLock.setImageResource(R.drawable.ic_lock);
                break;
            case 1:
                ivLock.setImageResource(R.drawable.ic_unlock);
                break;
        }
    }

    //look
    private void lookLayer(Sticker sticker) {
        if (sticker != null) {
            if (sticker.isLook()) {
                sticker.setLook(false);
                setUpLayoutLookLayer(1);
            } else {
                sticker.setLook(true);
                setUpLayoutLookLayer(0);
            }
            layerAdapter.changeNotify();
            vSticker.invalidate();
        }
    }

    private void setUpLayoutLookLayer(int pos) {
        switch (pos) {
            case 0:
                ivLook.setImageResource(R.drawable.ic_unlook);
                break;
            case 1:
                ivLook.setImageResource(R.drawable.ic_look);
                break;
        }
    }

    //Decor
    private void pickDecor() {
        rlExpandPickDecor.getLayoutParams().height = getResources().getDisplayMetrics().heightPixels * 90 / 100;
        seekAndHideViewDecor(indexDefault);
        setUpTitleDecor();
        setUpDataDecor();
    }

    private void addNewDecor(DecorModel decor) {
        DrawableStickerCustom drawableSticker = new DrawableStickerCustom(this, decor, getId(), Utils.DECOR);
        vSticker.addSticker(drawableSticker);
    }

    private void replaceDecor(DecorModel decor) {
        DrawableStickerCustom drawableSticker = (DrawableStickerCustom) vSticker.getCurrentSticker();
        if (drawableSticker != null && drawableSticker.getTypeSticker().equals(Utils.DECOR)) {
            drawableSticker.getDecorModel().setNameDecor(decor.getNameDecor());
            drawableSticker.getDecorModel().setNameFolder(decor.getNameFolder());
            drawableSticker.getDecorModel().setLstPathData(decor.getLstPathData());
            drawableSticker.replaceDecor();
        }

        vSticker.invalidate();
    }

    private void setUpDataDecor() {
        addFragmentsAdapter = new ViewPagerAddFragmentsAdapter(getSupportFragmentManager(), getLifecycle());

        String[] nameDecor = new String[]{"box", "draw", "frame", "shape"};
        for (String s : nameDecor) {
            DecorFragment decorFragment = DecorFragment.newInstance(s, (o, pos) -> {
                DecorModel decor = (DecorModel) o;
                if (!isReplaceDecor) addNewDecor(decor);
                else replaceDecor(decor);

                seekAndHideOperation(positionDecor);
            });
            addFragmentsAdapter.addFrag(decorFragment);
        }

        vpDecor.setAdapter(addFragmentsAdapter);
        vpDecor.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                titleDecorAdapter.setCurrent(position);
                rcvTypeDecor.smoothScrollToPosition(position);
            }
        });
    }

    private void setUpTitleDecor() {
        titleDecorAdapter = new TitleDecorAdapter(this, (o, pos) -> {
            titleDecorAdapter.setCurrent(pos);
            vpDecor.setCurrentItem(pos, true);
        });

        titleDecorAdapter.setData(DataDecor.getTitleDecor(this, "title"));
        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rcvTypeDecor.setLayoutManager(manager);
        rcvTypeDecor.setAdapter(titleDecorAdapter);
    }

    //Color Decor
    private void colorDecor(Sticker sticker) {
        if (!checkCurrentSticker(sticker)) return;
        seekAndHideViewDecor(0);

        DrawableStickerCustom drawableSticker = (DrawableStickerCustom) sticker;
        ColorModel colorOld = drawableSticker.getDecorModel().getColorModel();
        tvResetEditDecor.setOnClickListener(v -> resetDecor(0, drawableSticker,
                colorOld, null, indexDefault));

        ColorAdapter colorAdapter = new ColorAdapter(this, R.layout.item_color_edit, (o, pos) -> {
            ColorModel color = (ColorModel) o;
            if (pos == 0)
                DataColor.pickColor(this, c -> setColorDrawableSticker(drawableSticker, c));
            else if (color.getColorStart() == color.getColorEnd())
                setColorDrawableSticker(drawableSticker, color);
            else
                DataColor.pickDirection(this, color, c -> setColorDrawableSticker(drawableSticker, c));
        });
        if (!lstColor.isEmpty()) colorAdapter.setData(lstColor);

        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rcvEditColorDecor.setLayoutManager(manager);
        rcvEditColorDecor.setAdapter(colorAdapter);
    }

    private void setColorDrawableSticker(DrawableStickerCustom drawableSticker, ColorModel color) {
        drawableSticker.setColor(color);
        vSticker.invalidate();
    }

    //Shadow Decor
    private void shadowDecor(Sticker sticker) {
        if (!checkCurrentSticker(sticker)) return;
        seekAndHideViewDecor(1);

        if (sticker instanceof DrawableStickerCustom) {
            DrawableStickerCustom drawableSticker = (DrawableStickerCustom) sticker;

            setShadowDecorCurrent(drawableSticker);

            sbXPosDecor.setOnSeekbarResult(new OnSeekbarResult() {
                @Override
                public void onDown(View v) {

                }

                @Override
                public void onMove(View v, int value) {
                    tvXPosDecor.setText(String.valueOf(value));
                    drawableSticker.getDecorModel().getShadowModel().setXPos(value);
                    drawableSticker.getDecorModel().getShadowModel().setBlur(blur);
                    vSticker.replace(drawableSticker.getDecorModel().shadow(EditActivity.this, drawableSticker), true);
                }

                @Override
                public void onUp(View v, int value) {

                }
            });

            sbYPosDecor.setOnSeekbarResult(new OnSeekbarResult() {
                @Override
                public void onDown(View v) {

                }

                @Override
                public void onMove(View v, int value) {
                    tvYPosDecor.setText(String.valueOf(value));
                    drawableSticker.getDecorModel().getShadowModel().setYPos(value);
                    drawableSticker.getDecorModel().getShadowModel().setBlur(blur);
                    vSticker.replace(drawableSticker.getDecorModel().shadow(EditActivity.this, drawableSticker), true);
                }

                @Override
                public void onUp(View v, int value) {

                }
            });

            sbBlurDecor.setOnSeekbarResult(new OnSeekbarResult() {
                @Override
                public void onDown(View v) {

                }

                @Override
                public void onMove(View v, int value) {
                    if (value == -50) blur = 1f;
                    else if (value == 50) blur = 10f;
                    else if (value == 0) blur = 5f;
                    else blur = 5 + (value * 5 / 50f);

                    tvBlurDecor.setText(String.valueOf(value));
                    drawableSticker.getDecorModel().getShadowModel().setBlur(blur);
                    vSticker.replace(drawableSticker.getDecorModel().shadow(EditActivity.this, drawableSticker), true);
                }

                @Override
                public void onUp(View v, int value) {

                }
            });

            ivColorBlurDecor.setOnClickListener(v -> DataColor.pickColor(this, (color) -> {
                drawableSticker.getDecorModel().getShadowModel().setColorBlur(color.getColorStart());
                vSticker.replace(drawableSticker.getDecorModel().shadow(EditActivity.this, drawableSticker), true);
            }));
        }
    }

    private void setShadowDecorCurrent(DrawableStickerCustom drawableSticker) {
        float xPos = 0f, yPos = 0f;
        int color = 0;
        ShadowModel shadowModel = drawableSticker.getDecorModel().getShadowModel();
        if (shadowModel != null) {
            xPos = (shadowModel.getXPos());
            yPos = (shadowModel.getYPos());
            blur = (shadowModel.getBlur());
            color = shadowModel.getColorBlur();
        } else
            drawableSticker.getDecorModel().setShadowModel(new ShadowModel(xPos, yPos, blur, color));

        ShadowModel shadowModelOld = new ShadowModel(xPos, yPos, blur, color);

        tvResetEditDecor.setOnClickListener(v -> resetDecor(1, drawableSticker, null,
                shadowModelOld, 0));

        tvXPosDecor.setText(String.valueOf((int) xPos));
        sbXPosDecor.setProgress((int) xPos);
        tvYPosDecor.setText(String.valueOf((int) yPos));
        sbYPosDecor.setProgress((int) yPos);
        if (blur == 0) tvBlurDecor.setText(String.valueOf(0));
        else tvBlurDecor.setText(String.valueOf((int) ((blur - 5) * 10f)));

        if (blur > 5) sbBlurDecor.setProgress((int) ((blur - 5) * 10f));
        else if (blur == 0f) {
            sbBlurDecor.setProgress(0);
            blur = 5f;
        } else sbBlurDecor.setProgress((int) ((5 - blur) * 10f));

        drawableSticker.getDecorModel().getShadowModel().setBlur(blur);
    }

    //Opacity Decor
    private void opacityDecor(DrawableStickerCustom drawableSticker) {
        seekAndHideViewDecor(2);

        int opacityOld = drawableSticker.getDecorModel().getOpacity() * 100 / 255;
        sbOpacityDecor.setProgress(opacityOld);
        tvResetEditDecor.setOnClickListener(v -> resetDecor(2, drawableSticker, null, null, opacityOld));

        sbOpacityDecor.setOnSeekbarResult(new OnSeekbarResult() {
            @Override
            public void onDown(View v) {

            }

            @Override
            public void onMove(View v, int value) {
                drawableSticker.getDecorModel().setOpacity(value * 255 / 100);
                vSticker.replace(drawableSticker.getDecorModel().opacity(EditActivity.this, drawableSticker), true);
            }

            @Override
            public void onUp(View v, int value) {

            }
        });
    }

    private void resetDecor(int position, DrawableStickerCustom drawableSticker,
                            ColorModel colorOld, ShadowModel shadowModel, int opacityOle) {
        switch (position) {
            case 0:
                if (colorOld == null)
                    drawableSticker.setColor(new ColorModel(Color.BLACK, Color.BLACK, 0, false));
                else drawableSticker.setColor(colorOld);
                vSticker.invalidate();
                break;
            case 1:
                float xPos = shadowModel.getXPos();
                float yPos = shadowModel.getYPos();
                float blurOld = shadowModel.getBlur();
                int color = shadowModel.getColorBlur();

                tvXPosDecor.setText(String.valueOf((int) xPos));
                sbXPosDecor.setProgress((int) xPos);
                tvYPosDecor.setText(String.valueOf((int) yPos));
                sbYPosDecor.setProgress((int) yPos);
                if (blurOld == 0f) tvBlurDecor.setText(String.valueOf(0));
                else tvBlurDecor.setText(String.valueOf((int) ((blurOld - 5) * 10f)));
                if (blurOld > 5) sbBlurDecor.setProgress((int) ((blurOld - 5) * 10f));
                else if (blurOld == 0f) sbBlurDecor.setProgress(0);
                else sbBlurDecor.setProgress((int) ((5 - blurOld) * 10f));

                drawableSticker.getDecorModel().getShadowModel().setXPos(xPos);
                drawableSticker.getDecorModel().getShadowModel().setYPos(yPos);
                drawableSticker.getDecorModel().getShadowModel().setBlur(blurOld);
                drawableSticker.getDecorModel().getShadowModel().setColorBlur(color);

                vSticker.replace(drawableSticker.getDecorModel().shadow(EditActivity.this, drawableSticker), true);
                break;
            case 2:
                drawableSticker.getDecorModel().setOpacity(opacityOle);
                drawableSticker.setAlpha((int) (opacityOle * 255 / 100f));
                sbOpacityDecor.setProgress(opacityOle);

                vSticker.invalidate();
                break;
        }
    }

    private void seekAndHideViewDecor(int position) {
        animation = AnimationUtils.loadAnimation(this, R.anim.slide_down_out);
        if (vOperation.getVisibility() == View.VISIBLE) {
            vOperation.setAnimation(animation);
            vOperation.setVisibility(View.GONE);
        }

        if (vEditDecor.getVisibility() == View.VISIBLE) {
            vEditDecor.setAnimation(animation);
            vEditDecor.setVisibility(View.GONE);
        }

        switch (position) {
            case 0:
                animation = AnimationUtils.loadAnimation(this, R.anim.slide_up_in);
                if (rlEditColorDecor.getVisibility() == View.GONE) {
                    rlEditColorDecor.setAnimation(animation);
                    rlEditColorDecor.setVisibility(View.VISIBLE);
                }

                tvTitleEditDecor.setText(R.string.color);

                if (tvResetEditDecor.getVisibility() == View.GONE)
                    tvResetEditDecor.setVisibility(View.VISIBLE);
                break;
            case 1:
                animation = AnimationUtils.loadAnimation(this, R.anim.slide_up_in);
                if (llEditShadowDecor.getVisibility() == View.GONE) {
                    llEditShadowDecor.setAnimation(animation);
                    llEditShadowDecor.setVisibility(View.VISIBLE);
                }

                tvTitleEditDecor.setText(R.string.shadow);
                tvXPosDecor.setText(String.valueOf(0));
                tvYPosDecor.setText(String.valueOf(0));
                tvBlurDecor.setText(String.valueOf(0));
                sbXPosDecor.setMax(100);
                sbYPosDecor.setMax(100);
                sbBlurDecor.setMax(100);
                blur = 0f;

                if (tvResetEditDecor.getVisibility() == View.GONE)
                    tvResetEditDecor.setVisibility(View.VISIBLE);
                break;
            case 2:
                animation = AnimationUtils.loadAnimation(this, R.anim.slide_up_in);
                if (rlEditOpacityDecor.getVisibility() == View.GONE) {
                    rlEditOpacityDecor.setAnimation(animation);
                    rlEditOpacityDecor.setVisibility(View.VISIBLE);
                }

                tvTitleEditDecor.setText(getResources().getString(R.string.opacity));
                sbOpacityDecor.setColorText(getResources().getColor(R.color.green));
                sbOpacityDecor.setSizeText(com.intuit.ssp.R.dimen._10ssp);
                sbOpacityDecor.setProgress(100);
                sbOpacityDecor.setMax(100);

                if (tvResetEditDecor.getVisibility() == View.GONE)
                    tvResetEditDecor.setVisibility(View.VISIBLE);
                break;
            default:
                animation = AnimationUtils.loadAnimation(this, R.anim.slide_up_in);
                if (rlExpandPickDecor.getVisibility() == View.GONE) {
                    rlExpandPickDecor.setAnimation(animation);
                    rlExpandPickDecor.setVisibility(View.VISIBLE);
                }

                if (rlExpandEditDecor.getVisibility() == View.VISIBLE)
                    rlExpandEditDecor.setVisibility(View.GONE);

                if (tvResetEditDecor.getVisibility() == View.VISIBLE)
                    tvResetEditDecor.setVisibility(View.GONE);

                tvTitleEditDecor.setText(R.string.decor);
                break;
        }
    }

    private final Handler handlerLoading = new Handler(Looper.getMainLooper(), new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            if (ivLoading.getVisibility() == View.VISIBLE)
                ivLoading.setVisibility(View.GONE);
            switch (msg.what) {
                case 0:
                    Bitmap bitmap;
                    if (backgroundModel.getOverlayModel() != null)
                        bitmap = BitmapFactory.decodeFile(backgroundModel.getUriOverlay());
                    else bitmap = BitmapFactory.decodeFile(backgroundModel.getUriCache());
                    vMain.setImageBitmap(bitmap);
                    break;
                case 1:
                    Utils.showToast(EditActivity.this, getResources().getString(R.string.done));
                    break;
            }

            return true;
        }
    });

    //Overlay
    private void pickOverlay() {
        rlPickOverlay.getLayoutParams().height = getResources().getDisplayMetrics().heightPixels * 90 / 100;
        seekAndHideViewOverlay(indexDefault);
        setUpDataOverlay();
    }

    //Add Overlay
    private void addOverlay(OverlayModel overlay) {
        new Thread(() -> {
            Bitmap bitmap = BitmapFactory.decodeFile(backgroundModel.getUriCache());

            Bitmap bm;
            if (backgroundModel.getOverlayModel() != null)
                bm = UtilsBitmap.setOpacityBitmap(UtilsBitmap.getBitmapFromAsset(EditActivity.this,
                        overlay.getNameFolder(), overlay.getNameOverlay(), false, false), backgroundModel.getOverlayModel().getOpacity());
            else
                bm = UtilsBitmap.getBitmapFromAsset(EditActivity.this, overlay.getNameFolder(), overlay.getNameOverlay(), false, false);

            backgroundModel.setUriOverlayRoot(UtilsBitmap.saveBitmapToApp(EditActivity.this, bm,
                    nameFolderBackground, Utils.OVERLAY_ROOT));

            Bitmap bmOverlay = CGENativeLibrary.cgeFilterImage_MultipleEffects(bitmap,
                    strOverlay.replace("image", backgroundModel.getUriOverlayRoot()), 0.8f);

            backgroundModel.setUriOverlay(UtilsBitmap.saveBitmapToApp(EditActivity.this, bmOverlay, nameFolderBackground, Utils.BACKGROUND_OVERLAY_CACHE));

            handlerLoading.sendEmptyMessage(0);
        }).start();
    }

    private void setUpDataOverlay() {
        ArrayList<OverlayModel> lstOverlay = new ArrayList<>(DataOverlay.getOverlay(this, "overlay"));

        OverlayAdapter overlayAdapter = new OverlayAdapter(this, (o, pos) -> {
            OverlayModel overlay = (OverlayModel) o;

            seekAndHideOperation(positionOverlay);
            if (ivLoading.getVisibility() == View.GONE) ivLoading.setVisibility(View.VISIBLE);
            if (backgroundModel.getOverlayModel() != null)
                overlay.setOpacity(backgroundModel.getOverlayModel().getOpacity());

            backgroundModel.setOverlayModel(overlay);
            addOverlay(overlay);
        });
        if (!lstOverlay.isEmpty()) overlayAdapter.setData(lstOverlay);

        GridLayoutManager manager = new GridLayoutManager(this, 3);
        rcvOverlay.setLayoutManager(manager);
        rcvOverlay.setAdapter(overlayAdapter);
    }

    //Delete Overlay
    private void delOverlay() {
        Bitmap bitmap = BitmapFactory.decodeFile(backgroundModel.getUriCache());
        vMain.setImageBitmap(bitmap);

        File fileOverlayRoot = new File(backgroundModel.getUriOverlayRoot());
        File fileOverlayCache = new File(backgroundModel.getUriOverlay());

        if (fileOverlayRoot.exists()) fileOverlayRoot.delete();
        if (fileOverlayCache.exists()) fileOverlayCache.delete();

        backgroundModel.setUriOverlayRoot("");
        backgroundModel.setUriOverlay("");
        backgroundModel.setOverlayModel(null);

        seekAndHideOperation(indexDefault);
    }

    //Opacity Overlay
    private void opacityOverlay() {
        seekAndHideViewOverlay(0);

        Bitmap bitmap = BitmapFactory.decodeFile(backgroundModel.getUriCache());
        Bitmap bmOverlay = BitmapFactory.decodeFile(backgroundModel.getUriOverlayRoot());

        int opacityOld = backgroundModel.getOverlayModel().getOpacity() * 100 / 255;
        sbOpacityOverlay.setProgress(opacityOld);
        tvResetEditOverlay.setOnClickListener(v -> {
            if (ivLoading.getVisibility() == View.GONE) ivLoading.setVisibility(View.VISIBLE);

            new Thread(() -> {
                sbOpacityOverlay.setProgress(opacityOld);
                backgroundModel.getOverlayModel().setOpacity(opacityOld * 255 / 100);

                Bitmap bmOverlayOpacity = UtilsBitmap.setOpacityBitmap(bmOverlay, backgroundModel.getOverlayModel().getOpacity());

                backgroundModel.setUriOverlayRoot(UtilsBitmap.saveBitmapToApp(EditActivity.this, bmOverlayOpacity,
                        nameFolderBackground, Utils.OVERLAY_ROOT));

                Bitmap overlay = CGENativeLibrary.cgeFilterImage_MultipleEffects(bitmap,
                        strOverlay.replace("image", backgroundModel.getUriOverlayRoot()), 0.8f);

                backgroundModel.setUriOverlay(UtilsBitmap.saveBitmapToApp(EditActivity.this, overlay,
                        nameFolderBackground, Utils.BACKGROUND_OVERLAY_CACHE));

                handlerLoading.sendEmptyMessage(0);
            }).start();
        });

        sbOpacityOverlay.setOnSeekbarResult(new OnSeekbarResult() {
            @Override
            public void onDown(View v) {

            }

            @Override
            public void onMove(View v, int value) {
            }

            @Override
            public void onUp(View v, int value) {
                if (ivLoading.getVisibility() == View.GONE) ivLoading.setVisibility(View.VISIBLE);

                new Thread(() -> {
                    backgroundModel.getOverlayModel().setOpacity(value * 255 / 100);

                    Bitmap bmOverlayOpacity = UtilsBitmap.setOpacityBitmap(bmOverlay, backgroundModel.getOverlayModel().getOpacity());
                    backgroundModel.setUriOverlayRoot(UtilsBitmap.saveBitmapToApp(EditActivity.this, bmOverlayOpacity,
                            nameFolderBackground, Utils.OVERLAY_ROOT));

                    Bitmap overlay = CGENativeLibrary.cgeFilterImage_MultipleEffects(bitmap,
                            strOverlay.replace("image", backgroundModel.getUriOverlayRoot()), 0.8f);

                    backgroundModel.setUriOverlay(UtilsBitmap.saveBitmapToApp(EditActivity.this, overlay,
                            nameFolderBackground, Utils.BACKGROUND_OVERLAY_CACHE));

                    handlerLoading.sendEmptyMessage(0);
                }).start();
            }
        });
    }

    private void flipOverlay(boolean flipX) {
        if (ivLoading.getVisibility() == View.GONE) ivLoading.setVisibility(View.VISIBLE);
        new Thread(() -> {
            Bitmap bitmap = BitmapFactory.decodeFile(backgroundModel.getUriCache());
            Bitmap bm = UtilsBitmap.createFlippedBitmap(BitmapFactory.decodeFile(backgroundModel.getUriOverlayRoot()), flipX, !flipX);

            backgroundModel.setUriOverlayRoot(UtilsBitmap.saveBitmapToApp(EditActivity.this, bm, nameFolderBackground, Utils.OVERLAY_ROOT));

            Bitmap bmOverlay = CGENativeLibrary.cgeFilterImage_MultipleEffects(bitmap,
                    strOverlay.replace("image", backgroundModel.getUriOverlayRoot()), 0.8f);

            backgroundModel.setUriOverlay(UtilsBitmap.saveBitmapToApp(EditActivity.this, bmOverlay, nameFolderBackground, Utils.BACKGROUND_OVERLAY_CACHE));

            handlerLoading.sendEmptyMessage(0);
        }).start();
    }

    private void seekAndHideViewOverlay(int position) {
        animation = AnimationUtils.loadAnimation(this, R.anim.slide_down_out);
        if (vOperation.getVisibility() == View.VISIBLE) {
            vOperation.setAnimation(animation);
            vOperation.setVisibility(View.GONE);
        }

        switch (position) {
            case 0:
                animation = AnimationUtils.loadAnimation(this, R.anim.slide_down_out);
                if (llEditOverlay.getVisibility() == View.VISIBLE) {
                    llEditOverlay.setAnimation(animation);
                    llEditOverlay.setVisibility(View.GONE);
                }

                animation = AnimationUtils.loadAnimation(this, R.anim.slide_up_in);
                if (rlEditOpacityOverlay.getVisibility() == View.GONE) {
                    rlEditOpacityOverlay.setAnimation(animation);
                    rlEditOpacityOverlay.setVisibility(View.VISIBLE);
                    sbOpacityOverlay.setColorText(getResources().getColor(R.color.green));
                    sbOpacityOverlay.setSizeText(com.intuit.ssp.R.dimen._10ssp);
                    sbOpacityOverlay.setProgress(100);
                    sbOpacityOverlay.setMax(100);
                }

                if (tvResetEditOverlay.getVisibility() == View.GONE)
                    tvResetEditOverlay.setVisibility(View.VISIBLE);

                tvTitleEditOverlay.setText(R.string.opacity);
                break;
            case 1:
                animation = AnimationUtils.loadAnimation(this, R.anim.slide_down_out);
                if (rlEditOpacityOverlay.getVisibility() == View.VISIBLE) {
                    rlEditOpacityOverlay.setAnimation(animation);
                    rlEditOpacityOverlay.setVisibility(View.GONE);
                }

                animation = AnimationUtils.loadAnimation(this, R.anim.slide_up_in);
                if (llEditOverlay.getVisibility() == View.GONE) {
                    llEditOverlay.setAnimation(animation);
                    llEditOverlay.setVisibility(View.VISIBLE);
                }

                if (tvResetEditOverlay.getVisibility() == View.VISIBLE)
                    tvResetEditOverlay.setVisibility(View.GONE);

                tvTitleEditOverlay.setText(R.string.overlay);
                break;
            default:
                animation = AnimationUtils.loadAnimation(this, R.anim.slide_up_in);
                if (rlPickOverlay.getVisibility() == View.GONE) {
                    rlPickOverlay.setAnimation(animation);
                    rlPickOverlay.setVisibility(View.VISIBLE);
                }

                if (rlExpandEditOverlay.getVisibility() == View.VISIBLE)
                    rlExpandEditOverlay.setVisibility(View.GONE);

                if (tvResetEditOverlay.getVisibility() == View.VISIBLE)
                    tvResetEditOverlay.setVisibility(View.GONE);

                tvTitleEditOverlay.setText(R.string.overlay);
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

    //Flip Background
    private void flipBackground(boolean flipX) {
        if (!isColor) {
            if (ivLoading.getVisibility() == View.GONE) ivLoading.setVisibility(View.VISIBLE);
            new Thread(() -> {
                Bitmap bitmap = UtilsBitmap.createFlippedBitmap(BitmapFactory.decodeFile(backgroundModel.getUriCache()),
                        flipX, !flipX);

                backgroundModel.setUriCache(UtilsBitmap.saveBitmapToApp(EditActivity.this, bitmap,
                        nameFolderBackground, Utils.BACKGROUND));

                if (backgroundModel.getOverlayModel() != null)
                    addOverlay(backgroundModel.getOverlayModel());
                else {
                    Message message = new Message();
                    message.what = 0;
                    message.obj = bitmap;
                    handlerLoading.sendMessage(message);
                }
            }).start();
        }
    }

    //Opacity Background
    private void opacityBackground() {
        seekAndHideViewBackground(2);

        int opacityOld = backgroundModel.getOpacity();
        sbOpacityBackground.setProgress(opacityOld);

        tvResetBackground.setOnClickListener(v -> {
            backgroundModel.setOpacity(opacityOld);
            sbOpacityBackground.setProgress(opacityOld);

            if (vMain.getVisibility() == View.VISIBLE) vMain.setAlpha(opacityOld / 100f);
            else if (vColor.getVisibility() == View.VISIBLE)
                vColor.setAlpha(opacityOld * 255 / 100);
        });

        sbOpacityBackground.setOnSeekbarResult(new OnSeekbarResult() {
            @Override
            public void onDown(View v) {

            }

            @Override
            public void onMove(View v, int value) {
                backgroundModel.setOpacity(value);
                if (vMain.getVisibility() == View.VISIBLE) vMain.setAlpha(value / 100f);
                else if (vColor.getVisibility() == View.VISIBLE) vColor.setAlpha(value * 255 / 100);
            }

            @Override
            public void onUp(View v, int value) {

            }
        });
    }

    //Filter Background
    private void filterBackground() {
        isBackground = true;
        seekAndHideViewBackground(1);

        Bitmap bitmap = BitmapFactory.decodeFile(backgroundModel.getUriCache());

        filterImageAdapter = new FilterImageAdapter(this, (o, pos) -> {
            FilterModel filter = (FilterModel) o;
            filter.setCheck(true);
            filterImageAdapter.setCurrent(pos);
            filterImageAdapter.changeNotify();

            if (ivLoading.getVisibility() == View.GONE) ivLoading.setVisibility(View.VISIBLE);
            new Thread(() -> {
                Bitmap bm = CGENativeLibrary.cgeFilterImage_MultipleEffects(bitmap, filter.getParameterFilter(), 0.8f);

                backgroundModel.setPositionFilterBackground(pos);

                backgroundModel.setUriCache(UtilsBitmap.saveBitmapToApp(EditActivity.this,
                        bm, nameFolderBackground, Utils.BACKGROUND));

                if (backgroundModel.getOverlayModel() != null)
                    addOverlay(backgroundModel.getOverlayModel());
                else {
                    handlerLoading.sendEmptyMessage(0);
                }
            }).start();
        });

        if (!lstFilter.isEmpty()) filterImageAdapter.setData(lstFilter);

        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rcvFilterBackground.setLayoutManager(manager);
        rcvFilterBackground.setAdapter(filterImageAdapter);

        rcvFilterBackground.smoothScrollToPosition(backgroundModel.getPositionFilterBackground());
        filterImageAdapter.setCurrent(backgroundModel.getPositionFilterBackground());
        filterImageAdapter.changeNotify();
    }

    //Adjust Background
    private void adjustBackground() {
        seekAndHideViewBackground(0);
        setUpOptionAdjustBackground(0);
        bmAdjust = BitmapFactory.decodeFile(backgroundModel.getUriCache());
        vMain.setImageBitmap(bmAdjust);
        tvResetBackground.setOnClickListener(v -> {
            @SuppressLint("InflateParams")
            View vDialog = LayoutInflater.from(this).inflate(R.layout.dialog_del_sticker, null, false);
            TextView tvNo = vDialog.findViewById(R.id.tvNo);
            TextView tvYes = vDialog.findViewById(R.id.tvYes);
            TextView tvDes = vDialog.findViewById(R.id.tvDes);

            AlertDialog dialog = new AlertDialog.Builder(this, R.style.SheetDialog).create();
            dialog.setView(vDialog);
            dialog.show();

            tvDes.setText(getResources().getString(R.string.des_reset_adjust));
            tvNo.setOnClickListener(vNo -> dialog.cancel());
            tvYes.setOnClickListener(vYes -> {
                adjust(backgroundModel.getAdjustModel(), true);
                sbAdjust.setProgress(0);
                tvAdjustBackground.setText(String.valueOf(0));
                dialog.cancel();
            });
        });

        rlBrightness.setOnClickListener(v -> {
            bitmap = null;
            setUpOptionAdjustBackground(0);
        });
        rlContrast.setOnClickListener(v -> {
            bitmap = null;
            setUpOptionAdjustBackground(1);
        });
        rlExposure.setOnClickListener(v -> {
            bitmap = null;
            setUpOptionAdjustBackground(2);
        });
        rlHighLight.setOnClickListener(v -> {
            bitmap = null;
            setUpOptionAdjustBackground(3);
        });
        rlShadows.setOnClickListener(v -> {
            bitmap = null;
            setUpOptionAdjustBackground(4);
        });
        rlBlacks.setOnClickListener(v -> {
            bitmap = null;
            setUpOptionAdjustBackground(5);
        });
        rlWhites.setOnClickListener(v -> {
            bitmap = null;
            setUpOptionAdjustBackground(6);
        });
        rlSaturation.setOnClickListener(v -> {
            bitmap = null;
            setUpOptionAdjustBackground(7);
        });
        rlHue.setOnClickListener(v -> {
            bitmap = null;
            setUpOptionAdjustBackground(8);
        });
        rlWarmth.setOnClickListener(v -> {
            bitmap = null;
            setUpOptionAdjustBackground(9);
        });
        rlVibrance.setOnClickListener(v -> {
            bitmap = null;
            setUpOptionAdjustBackground(10);
        });
        rlVignette.setOnClickListener(v -> {
            bitmap = null;
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
                adjust(backgroundModel.getAdjustModel(), false);
                break;
            case 1:
                backgroundModel.getAdjustModel().setContrast(value);
                adjust(backgroundModel.getAdjustModel(), false);
                break;
            case 2:
                backgroundModel.getAdjustModel().setExposure(value * 4f);
                adjust(backgroundModel.getAdjustModel(), false);
                break;
            case 3:
                if (value > 0) backgroundModel.getAdjustModel().setHighlight(value * 4f);
                else if (value < 0) backgroundModel.getAdjustModel().setHighlight(value * 2f);
                adjust(backgroundModel.getAdjustModel(), false);
                break;
            case 4:
                if (value > 0) backgroundModel.getAdjustModel().setShadows(value * 4f);
                else if (value < 0) backgroundModel.getAdjustModel().setShadows(value * 2f);
                adjust(backgroundModel.getAdjustModel(), false);
                break;
            case 5:
                backgroundModel.getAdjustModel().setBlacks(value * 2f);
                adjust(backgroundModel.getAdjustModel(), false);
                break;
            case 6:
                backgroundModel.getAdjustModel().setWhites(value * 2f);
                adjust(backgroundModel.getAdjustModel(), false);
                break;
            case 7:
                backgroundModel.getAdjustModel().setSaturation(value * 2f);
                adjust(backgroundModel.getAdjustModel(), false);
                break;
            case 8:
                backgroundModel.getAdjustModel().setHue(value * 2f);
                adjust(backgroundModel.getAdjustModel(), false);
                break;
            case 9:
                backgroundModel.getAdjustModel().setWarmth(value * 2f);
                adjust(backgroundModel.getAdjustModel(), false);
                break;
            case 10:
                backgroundModel.getAdjustModel().setVibrance(value * 2f);
                adjust(backgroundModel.getAdjustModel(), false);
                break;
            case 11:
                backgroundModel.getAdjustModel().setVignette(value * 2f);
                adjust(backgroundModel.getAdjustModel(), false);
                break;
        }
    }

    private void adjust(AdjustModel adjust, boolean fulReset) {
        if (!fulReset) {
            bitmap = UtilsAdjust.adjust(bmAdjust, adjust);

            if (bitmap != null) vMain.setImageBitmap(bitmap);
        } else {
            bitmap = null;
            backgroundModel.setAdjustModel(new AdjustModel(0f, 0f, 0f,
                    0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f));
            vMain.setImageBitmap(bmAdjust);
        }
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

                if (tvResetBackground.getVisibility() == View.GONE)
                    tvResetBackground.setVisibility(View.VISIBLE);

                tvTitleEditBackground.setText(R.string.adjust);
                break;
            case 1:
                animation = AnimationUtils.loadAnimation(this, R.anim.slide_up_in);
                if (rlEditFilterBackground.getVisibility() == View.GONE) {
                    rlEditFilterBackground.setAnimation(animation);
                    rlEditFilterBackground.setVisibility(View.VISIBLE);
                }
                if (backgroundModel.getOverlayModel() == null)
                    setUpDataFilter(BitmapFactory.decodeFile(backgroundModel.getUriCache()));
                else setUpDataFilter(BitmapFactory.decodeFile(backgroundModel.getUriOverlay()));

                tvTitleEditBackground.setText(R.string.filter);
                break;
            case 2:
                animation = AnimationUtils.loadAnimation(this, R.anim.slide_up_in);
                if (rlEditOpacityBackground.getVisibility() == View.GONE) {
                    rlEditOpacityBackground.setAnimation(animation);
                    rlEditOpacityBackground.setVisibility(View.VISIBLE);

                    sbOpacityBackground.setColorText(getResources().getColor(R.color.green));
                    sbOpacityBackground.setSizeText(com.intuit.ssp.R.dimen._10ssp);
                    sbOpacityBackground.setProgress(100);
                    sbOpacityBackground.setMax(100);
                }

                if (tvResetBackground.getVisibility() == View.GONE)
                    tvResetBackground.setVisibility(View.VISIBLE);

                tvTitleEditBackground.setText(R.string.opacity);
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
                        Bitmap bitmap = UtilsBitmap.getBitmapFromUri(EditActivity.this, uri);
                        if (bitmap != null) {
                            bitmap = Bitmap.createBitmap(UtilsBitmap.modifyOrientation(EditActivity.this,
                                    Bitmap.createScaledBitmap(bitmap, 512,
                                            512 * bitmap.getHeight() / bitmap.getWidth(), false), uri));

                            UtilsBitmap.saveBitmapToApp(EditActivity.this, bitmap,
                                    nameFolderImage, Utils.IMAGE_ROOT + "_" + picModel.getId());
                            String path = UtilsBitmap.saveBitmapToApp(EditActivity.this, bitmap,
                                    nameFolderImage, Utils.IMAGE + "_" + picModel.getId());

                            setUpDataFilter(bitmap);
                            setUpDataBlend(bitmap);

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
                    if (ivLoadingFilterBackground.getVisibility() == View.VISIBLE)
                        ivLoadingFilterBackground.setVisibility(View.GONE);
                    if (checkCurrentSticker(sticker)) {
                        if (sticker instanceof DrawableStickerCustom) {
                            DrawableStickerCustom drawableSticker = (DrawableStickerCustom) sticker;
                            if (filterImageAdapter != null) {
                                filterImageAdapter.setData(lstFilter);
                                rcvEditFilterImage.smoothScrollToPosition(drawableSticker.getImageModel().getPosFilter());
                                filterImageAdapter.setCurrent(drawableSticker.getImageModel().getPosFilter());
                                filterImageAdapter.changeNotify();
                            }
                        }
                    } else {
                        if (filterImageAdapter != null) {
                            filterImageAdapter.setData(lstFilter);
                            rcvFilterBackground.smoothScrollToPosition(backgroundModel.getPositionFilterBackground());
                            filterImageAdapter.setCurrent(backgroundModel.getPositionFilterBackground());
                            filterImageAdapter.changeNotify();
                        }
                    }
                    break;
                case 2:
                    if (checkCurrentSticker(sticker)) {
                        if (sticker instanceof DrawableStickerCustom) {
                            DrawableStickerCustom drawableSticker = (DrawableStickerCustom) sticker;
                            if (blendImageAdapter != null) {
                                blendImageAdapter.setData(lstBlend);
                                rcvEditFilterImage.smoothScrollToPosition(drawableSticker.getImageModel().getPosBlend());
                                blendImageAdapter.setCurrent(drawableSticker.getImageModel().getPosBlend());
                                blendImageAdapter.changeNotify();
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
        if (ivLoadingFilterBackground.getVisibility() == View.GONE)
            ivLoadingFilterBackground.setVisibility(View.VISIBLE);
        new Thread(() -> {
            lstFilter = FilterImage.getDataFilter(
                    Bitmap.createScaledBitmap(bitmap, 400, 400 * bitmap.getHeight() / bitmap.getWidth(), false));

            handler.sendEmptyMessage(1);
        }).start();
    }

    private void setUpDataBlend(Bitmap bitmap) {
        if (bitmap == null) return;
        lstBlend = new ArrayList<>();
        new Thread(() -> {
            lstBlend = BlendImage.getDataBlend(
                    Bitmap.createScaledBitmap(bitmap, 400, 400 * bitmap.getHeight() / bitmap.getWidth(), false));

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

                filterImageAdapter = new FilterImageAdapter(this, (o, pos) -> {
                    FilterModel filter = (FilterModel) o;
                    filter.setCheck(true);
                    filterImageAdapter.setCurrent(pos);
                    filterImageAdapter.changeNotify();

                    Bitmap bm = CGENativeLibrary.cgeFilterImage_MultipleEffects(bitmap, filter.getParameterFilter(), 0.8f);

                    drawableSticker.getImageModel().setPosFilter(pos);
                    drawableSticker.getImageModel().setUri(UtilsBitmap.saveBitmapToApp(EditActivity.this,
                            bm, nameFolderImage, Utils.IMAGE));
                    drawableSticker.replaceImage();
                    vSticker.invalidate();
                });

                if (!lstFilter.isEmpty()) filterImageAdapter.setData(lstFilter);

                LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
                rcvEditFilterImage.setLayoutManager(manager);
                rcvEditFilterImage.setAdapter(filterImageAdapter);

                rcvEditFilterImage.smoothScrollToPosition(drawableSticker.getImageModel().getPosFilter());
                filterImageAdapter.setCurrent(drawableSticker.getImageModel().getPosFilter());
                filterImageAdapter.changeNotify();
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

        tvResetImage.setOnClickListener(v -> resetImage(0, drawableSticker, shadowModelOld, indexDefault));

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

        int opacityOld = drawableSticker.getImageModel().getOpacity() * 100 / 255;
        sbOpacityImage.setProgress(opacityOld);
        tvResetImage.setOnClickListener(v -> resetImage(1, drawableSticker, null, opacityOld));

        sbOpacityImage.setOnSeekbarResult(new OnSeekbarResult() {
            @Override
            public void onDown(View v) {

            }

            @Override
            public void onMove(View v, int value) {
                drawableSticker.getImageModel().setOpacity(value);
                if (!drawableSticker.isShadow()) {
                    drawableSticker.getImageModel().setOpacity(value * 255 / 100);
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

                blendImageAdapter = new BlendImageAdapter(this, (o, pos) -> {
                    BlendModel blendModel = (BlendModel) o;
                    blendModel.setCheck(true);
                    blendImageAdapter.setCurrent(pos);
                    blendImageAdapter.changeNotify();

                    Bitmap bm = bitmap.copy(Bitmap.Config.ARGB_8888, true);
                    new Blend(1f, blendModel.getModeBlend()).adjustBitmap(bm);

                    drawableSticker.getImageModel().setPosBlend(pos);
                    drawableSticker.getImageModel().setUri(UtilsBitmap.saveBitmapToApp(EditActivity.this,
                            bm, nameFolderImage, Utils.IMAGE));
                    if (pos != 0) drawableSticker.getImageModel().setOpacity(134);
                    else drawableSticker.getImageModel().setOpacity(255);
                    drawableSticker.replaceImage();
                    vSticker.invalidate();
                });

                if (!lstBlend.isEmpty()) blendImageAdapter.setData(lstBlend);

                LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
                rcvEditBlendImage.setLayoutManager(manager);
                rcvEditBlendImage.setAdapter(blendImageAdapter);

                rcvEditBlendImage.smoothScrollToPosition(drawableSticker.getImageModel().getPosBlend());
                blendImageAdapter.setCurrent(drawableSticker.getImageModel().getPosBlend());
                blendImageAdapter.changeNotify();
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
                blur = 0f;

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
        seeAndHideViewEmoji(indexDefault);
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
        if (drawableSticker != null && drawableSticker.getTypeSticker().equals(Utils.EMOJI)) {
            drawableSticker.getEmojiModel().setNameEmoji(emoji.getNameEmoji());
            drawableSticker.replaceEmoji();
        }

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

    //Size Text
    private void fontSizeText(Sticker sticker) {
        if (!checkCurrentSticker(sticker)) return;
        seeAndHideViewText(0);

        if (sticker instanceof TextStickerCustom) {
            TextStickerCustom textSticker = (TextStickerCustom) sticker;
            int sizeOld = (int) textSticker.getTextSize();
            sbFontText.setProgress(sizeOld);
            tvFontText.setText(String.valueOf(sizeOld));
            tvResetText.setOnClickListener(v -> resetText(0, textSticker, sizeOld, null, null, null, indexDefault));

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

        if (sticker instanceof TextStickerCustom) {
            TextStickerCustom textSticker = (TextStickerCustom) sticker;

            ColorModel colorModelOld = textSticker.getTextModel().getColorModel();
            tvResetText.setOnClickListener(v -> resetText(1, textSticker, indexDefault,
                    colorModelOld, null, null, indexDefault));

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

        tvResetText.setOnClickListener(v -> resetText(2, textSticker, indexDefault,
                null, shearTextModelOld, null, indexDefault));

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

        tvResetText.setOnClickListener(v -> resetText(3, textSticker, indexDefault,
                null, null, shadowModelOld, indexDefault));

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

            int opacityOld = textSticker.getTextModel().getOpacity() * 100 / 255;
            sbOpacityText.setProgress(opacityOld);
            tvResetText.setOnClickListener(v -> resetText(4, textSticker, indexDefault, null,
                    null, null, opacityOld));

            sbOpacityText.setOnSeekbarResult(new OnSeekbarResult() {
                @Override
                public void onDown(View v) {

                }

                @Override
                public void onMove(View v, int value) {
                    textSticker.getTextModel().setOpacity(value * 255 / 100);
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
                int shearX = (int) (shearTextModel.getShearX() * 100);
                int shearY = (int) (shearTextModel.getShearY() * 100);
                int stretch = (int) (shearTextModel.getStretch() * 100);

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
                blur = 0f;

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

    public void getData(int position, String strPic, ColorModel color, TemplateModel template, boolean isNewData) {
        Bitmap bmRoot;
        switch (position) {
            case 0:
                try {
                    bmRoot = UtilsBitmap.modifyOrientation(this, UtilsBitmap.getBitmapFromUri(this, Uri.parse(strPic)), Uri.parse(strPic));
                    Bitmap bm = Bitmap.createScaledBitmap(bmRoot, 1080, 1080 * bmRoot.getHeight() / bmRoot.getWidth(), false);
                    backgroundModel.setUriRoot(UtilsBitmap.saveBitmapToApp(this, bm, nameFolderBackground, Utils.BACKGROUND_ROOT));
                    seekAndHideViewMain(positionCrop, bm, color, isNewData);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case 1:
                bmRoot = UtilsBitmap.getBitmapFromAsset(this, "offline_myapp", strPic, false, false);
                backgroundModel.setUriRoot(UtilsBitmap.saveBitmapToApp(this, bmRoot, nameFolderBackground, Utils.BACKGROUND_ROOT));
                seekAndHideViewMain(positionCrop, bmRoot, color, isNewData);
                break;
            case 2:
                bmRoot = UtilsBitmap.getBitmapFromAsset(this, "template/template_background", template.getBackground(), false, false);
                backgroundModel.setUriRoot(UtilsBitmap.saveBitmapToApp(this, bmRoot, nameFolderBackground, Utils.BACKGROUND_ROOT));
                seekAndHideViewMain(positionCrop, bmRoot, color, isNewData);
                break;
            case 3:
                backgroundModel.setColorModel(color);
                seekAndHideViewMain(positionColor, null, color, isNewData);
                break;
        }
    }

    private void seekAndHideOperation(int position) {
        if (vSticker.getStickerCount() == 0)
            ivLayer.setImageResource(R.drawable.ic_layer_uncheck);
        else ivLayer.setImageResource(R.drawable.ic_layer);
        DrawableStickerCustom drawableSticker = null;
        if (vSticker.getCurrentSticker() instanceof DrawableStickerCustom)
            drawableSticker = (DrawableStickerCustom) vSticker.getCurrentSticker();

        switch (position) {
            case positionLayer:
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

                if (rlExpandEditBackground.getVisibility() == View.VISIBLE) {
                    rlExpandEditBackground.setAnimation(animation);
                    rlExpandEditBackground.setVisibility(View.GONE);
                }

                if (rlExpandEditOverlay.getVisibility() == View.VISIBLE) {
                    rlExpandEditOverlay.setAnimation(animation);
                    rlExpandEditOverlay.setVisibility(View.GONE);
                }

                if (rlExpandEditDecor.getVisibility() == View.VISIBLE) {
                    rlExpandEditDecor.setAnimation(animation);
                    rlExpandEditDecor.setVisibility(View.GONE);
                }

                if (rlExpandEditTemp.getVisibility() == View.VISIBLE) {
                    rlExpandEditTemp.setAnimation(animation);
                    rlExpandEditTemp.setVisibility(View.GONE);
                }

                animation = AnimationUtils.loadAnimation(this, R.anim.slide_up_in);
                if (rlExpandLayer.getVisibility() == View.GONE) {
                    rlExpandLayer.setAnimation(animation);
                    rlExpandLayer.setVisibility(View.VISIBLE);
                }
                break;
            case positionTemp:
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

                if (rlExpandEditBackground.getVisibility() == View.VISIBLE) {
                    rlExpandEditBackground.setAnimation(animation);
                    rlExpandEditBackground.setVisibility(View.GONE);
                }

                if (rlExpandEditOverlay.getVisibility() == View.VISIBLE) {
                    rlExpandEditOverlay.setAnimation(animation);
                    rlExpandEditOverlay.setVisibility(View.GONE);
                }

                if (rlExpandEditDecor.getVisibility() == View.VISIBLE) {
                    rlExpandEditDecor.setAnimation(animation);
                    rlExpandEditDecor.setVisibility(View.GONE);
                }

                if (rlPickTemp.getVisibility() == View.VISIBLE) {
                    rlPickTemp.setAnimation(animation);
                    rlPickTemp.setVisibility(View.GONE);
                }

                if (rlEditColorTemp.getVisibility() == View.VISIBLE) {
                    rlEditColorTemp.setAnimation(animation);
                    rlEditColorTemp.setVisibility(View.GONE);
                }

                if (llEditShadowTemp.getVisibility() == View.VISIBLE) {
                    llEditShadowTemp.setAnimation(animation);
                    llEditShadowTemp.setVisibility(View.GONE);
                }

                if (rlEditOpacityTemp.getVisibility() == View.VISIBLE) {
                    rlEditOpacityTemp.setAnimation(animation);
                    rlEditOpacityTemp.setVisibility(View.GONE);
                }

                animation = AnimationUtils.loadAnimation(this, R.anim.slide_up_in);
                if (rlExpandEditTemp.getVisibility() == View.GONE) {
                    rlExpandEditTemp.setAnimation(animation);
                    rlExpandEditTemp.setVisibility(View.VISIBLE);
                }

                if (vEditTemp.getVisibility() == View.GONE) {
                    vEditTemp.setAnimation(animation);
                    vEditTemp.setVisibility(View.VISIBLE);
                }

                if (drawableSticker != null)
                    if (drawableSticker.getTemplateModel().getColorModel() != null) {
                        ivColorTemp.setBackground(null);
                        ivColorTemp.setImageDrawable(createGradientDrawable(drawableSticker.getTemplateModel().getColorModel()));
                    } else
                        ivColorTemp.setBackgroundResource(R.drawable.ic_color_decor);

                tvTitleEditTemp.setText(R.string.temp);

                if (tvResetEditTemp.getVisibility() == View.VISIBLE)
                    tvResetEditTemp.setVisibility(View.GONE);
                break;
            case positionDecor:
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

                if (rlExpandEditBackground.getVisibility() == View.VISIBLE) {
                    rlExpandEditBackground.setAnimation(animation);
                    rlExpandEditBackground.setVisibility(View.GONE);
                }

                if (rlExpandEditOverlay.getVisibility() == View.VISIBLE) {
                    rlExpandEditOverlay.setAnimation(animation);
                    rlExpandEditOverlay.setVisibility(View.GONE);
                }

                if (rlExpandEditTemp.getVisibility() == View.VISIBLE) {
                    rlExpandEditTemp.setAnimation(animation);
                    rlExpandEditTemp.setVisibility(View.GONE);
                }

                if (rlExpandPickDecor.getVisibility() == View.VISIBLE) {
                    rlExpandPickDecor.setAnimation(animation);
                    rlExpandPickDecor.setVisibility(View.GONE);
                }

                if (rlEditColorDecor.getVisibility() == View.VISIBLE) {
                    rlEditColorDecor.setAnimation(animation);
                    rlEditColorDecor.setVisibility(View.GONE);
                }

                if (llEditShadowDecor.getVisibility() == View.VISIBLE) {
                    llEditShadowDecor.setAnimation(animation);
                    llEditShadowDecor.setVisibility(View.GONE);
                }

                if (rlEditOpacityDecor.getVisibility() == View.VISIBLE) {
                    rlEditOpacityDecor.setAnimation(animation);
                    rlEditOpacityDecor.setVisibility(View.GONE);
                }

                if (tvResetEditDecor.getVisibility() == View.VISIBLE)
                    tvResetEditDecor.setVisibility(View.GONE);

                animation = AnimationUtils.loadAnimation(this, R.anim.slide_up_in);
                if (rlExpandEditDecor.getVisibility() == View.GONE) {
                    rlExpandEditDecor.setAnimation(animation);
                    rlExpandEditDecor.setVisibility(View.VISIBLE);
                }

                if (vEditDecor.getVisibility() == View.GONE) {
                    vEditDecor.setAnimation(animation);
                    vEditDecor.setVisibility(View.VISIBLE);
                }

                tvTitleEditDecor.setText(R.string.decor);

                if (drawableSticker != null)
                    if (drawableSticker.getDecorModel().getColorModel() != null)
                        ivColorDecor.setImageDrawable(createGradientDrawable(drawableSticker.getDecorModel().getColorModel()));
                    else
                        ivColorDecor.setImageDrawable(createGradientDrawable(new ColorModel(Color.BLACK, Color.BLACK, 0, false)));
                break;
            case positionOverlay:
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

                if (rlExpandEditBackground.getVisibility() == View.VISIBLE) {
                    rlExpandEditBackground.setAnimation(animation);
                    rlExpandEditBackground.setVisibility(View.GONE);
                }

                if (rlPickOverlay.getVisibility() == View.VISIBLE) {
                    rlPickOverlay.setAnimation(animation);
                    rlPickOverlay.setVisibility(View.GONE);
                }

                if (rlEditOpacityOverlay.getVisibility() == View.VISIBLE) {
                    rlEditOpacityOverlay.setAnimation(animation);
                    rlEditOpacityOverlay.setVisibility(View.GONE);
                }

                if (rlExpandEditDecor.getVisibility() == View.VISIBLE) {
                    rlExpandEditDecor.setAnimation(animation);
                    rlExpandEditDecor.setVisibility(View.GONE);
                }

                if (rlExpandEditTemp.getVisibility() == View.VISIBLE) {
                    rlExpandEditTemp.setAnimation(animation);
                    rlExpandEditTemp.setVisibility(View.GONE);
                }

                if (tvResetEditOverlay.getVisibility() == View.VISIBLE)
                    tvResetEditOverlay.setVisibility(View.GONE);

                animation = AnimationUtils.loadAnimation(this, R.anim.slide_up_in);
                if (rlExpandEditOverlay.getVisibility() == View.GONE) {
                    rlExpandEditOverlay.setAnimation(animation);
                    rlExpandEditOverlay.setVisibility(View.VISIBLE);
                }

                if (llEditOverlay.getVisibility() == View.GONE) {
                    llEditOverlay.setAnimation(animation);
                    llEditOverlay.setVisibility(View.VISIBLE);
                }

                tvTitleEditOverlay.setText(R.string.overlay);
                break;
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

                if (rlExpandEditOverlay.getVisibility() == View.VISIBLE) {
                    rlExpandEditOverlay.setAnimation(animation);
                    rlExpandEditOverlay.setVisibility(View.GONE);
                }

                if (rlExpandEditDecor.getVisibility() == View.VISIBLE) {
                    rlExpandEditDecor.setAnimation(animation);
                    rlExpandEditDecor.setVisibility(View.GONE);
                }

                if (rlExpandEditTemp.getVisibility() == View.VISIBLE) {
                    rlExpandEditTemp.setAnimation(animation);
                    rlExpandEditTemp.setVisibility(View.GONE);
                }

                if (rlEditAdjust.getVisibility() == View.VISIBLE) {
                    rlEditAdjust.setAnimation(animation);
                    rlEditAdjust.setVisibility(View.GONE);
                }

                if (rlEditFilterBackground.getVisibility() == View.VISIBLE) {
                    rlEditFilterBackground.setAnimation(animation);
                    rlEditFilterBackground.setVisibility(View.GONE);
                    isBackground = false;
                }

                if (rlEditOpacityBackground.getVisibility() == View.VISIBLE) {
                    rlEditOpacityBackground.setAnimation(animation);
                    rlEditOpacityBackground.setVisibility(View.GONE);
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

                if (isColor) {
                    rlFilterBackground.setVisibility(View.GONE);
                    rlAdjustBackground.setVisibility(View.GONE);
                    rlFlipYBackground.setVisibility(View.GONE);
                    rlFlipXBackground.setVisibility(View.GONE);
                } else {
                    rlFilterBackground.setVisibility(View.VISIBLE);
                    rlAdjustBackground.setVisibility(View.VISIBLE);
                    rlFlipYBackground.setVisibility(View.VISIBLE);
                    rlFlipXBackground.setVisibility(View.VISIBLE);
                }

                if (bitmap != null) {
                    if (ivLoading.getVisibility() == View.GONE)
                        ivLoading.setVisibility(View.VISIBLE);

                    new Thread(() -> {
                        backgroundModel.setUriCache(UtilsBitmap.saveBitmapToApp(this, bitmap,
                                nameFolderBackground, Utils.BACKGROUND));

                        if (backgroundModel.getOverlayModel() != null)
                            addOverlay(backgroundModel.getOverlayModel());
                        else {
                            Message message = new Message();
                            message.what = 0;
                            message.obj = bitmap;
                            handlerLoading.sendMessage(message);
                        }
                    }).start();
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

                if (rlExpandEditBackground.getVisibility() == View.VISIBLE) {
                    rlExpandEditBackground.setAnimation(animation);
                    rlExpandEditBackground.setVisibility(View.GONE);
                }

                if (rlExpandEditOverlay.getVisibility() == View.VISIBLE) {
                    rlExpandEditOverlay.setAnimation(animation);
                    rlExpandEditOverlay.setVisibility(View.GONE);
                }

                if (rlExpandEditDecor.getVisibility() == View.VISIBLE) {
                    rlExpandEditDecor.setAnimation(animation);
                    rlExpandEditDecor.setVisibility(View.GONE);
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

                if (rlExpandEditTemp.getVisibility() == View.VISIBLE) {
                    rlExpandEditTemp.setAnimation(animation);
                    rlExpandEditTemp.setVisibility(View.GONE);
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

                if (rlExpandEditBackground.getVisibility() == View.VISIBLE) {
                    rlExpandEditBackground.setAnimation(animation);
                    rlExpandEditBackground.setVisibility(View.GONE);
                }

                if (rlExpandEditOverlay.getVisibility() == View.VISIBLE) {
                    rlExpandEditOverlay.setAnimation(animation);
                    rlExpandEditOverlay.setVisibility(View.GONE);
                }

                if (rlExpandEditDecor.getVisibility() == View.VISIBLE) {
                    rlExpandEditDecor.setAnimation(animation);
                    rlExpandEditDecor.setVisibility(View.GONE);
                }

                if (rlExpandEditTemp.getVisibility() == View.VISIBLE) {
                    rlExpandEditTemp.setAnimation(animation);
                    rlExpandEditTemp.setVisibility(View.GONE);
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

                if (rlExpandEditBackground.getVisibility() == View.VISIBLE) {
                    rlExpandEditBackground.setAnimation(animation);
                    rlExpandEditBackground.setVisibility(View.GONE);
                }

                if (rlExpandEditOverlay.getVisibility() == View.VISIBLE) {
                    rlExpandEditOverlay.setAnimation(animation);
                    rlExpandEditOverlay.setVisibility(View.GONE);
                }

                if (rlExpandEditDecor.getVisibility() == View.VISIBLE) {
                    rlExpandEditDecor.setAnimation(animation);
                    rlExpandEditDecor.setVisibility(View.GONE);
                }

                if (rlExpandEditTemp.getVisibility() == View.VISIBLE) {
                    rlExpandEditTemp.setAnimation(animation);
                    rlExpandEditTemp.setVisibility(View.GONE);
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
                if (vSticker.getCurrentSticker() instanceof TextStickerCustom) {
                    TextStickerCustom textSticker = (TextStickerCustom) vSticker.getCurrentSticker();
                    if (textSticker != null)
                        if (textSticker.getTextModel().getColorModel() != null)
                            ivColorText.setImageDrawable(createGradientDrawable(textSticker.getTextModel().getColorModel()));
                        else
                            ivColorText.setImageDrawable(createGradientDrawable(new ColorModel(Color.BLACK, Color.BLACK, 0, false)));
                }
                break;
            case positionSize:
                Bitmap bitmap = BitmapFactory.decodeFile(backgroundModel.getUriRoot());
                if (!isColor)
                    seekAndHideViewMain(positionCrop, bitmap, colorModelOld, true);
                else seekAndHideViewMain(positionColor, bitmap, colorModelOld, true);
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

                if (rlPickOverlay.getVisibility() == View.VISIBLE) {
                    rlPickOverlay.setAnimation(animation);
                    rlPickOverlay.setVisibility(View.GONE);
                }

                if (rlExpandEditOverlay.getVisibility() == View.VISIBLE) {
                    rlExpandEditOverlay.setAnimation(animation);
                    rlExpandEditOverlay.setVisibility(View.GONE);
                }

                if (rlExpandPickDecor.getVisibility() == View.VISIBLE) {
                    rlExpandPickDecor.setAnimation(animation);
                    rlExpandPickDecor.setVisibility(View.GONE);
                }

                if (rlExpandEditDecor.getVisibility() == View.VISIBLE) {
                    rlExpandEditDecor.setAnimation(animation);
                    rlExpandEditDecor.setVisibility(View.GONE);
                }

                if (rlExpandEditTemp.getVisibility() == View.VISIBLE) {
                    rlExpandEditTemp.setAnimation(animation);
                    rlExpandEditTemp.setVisibility(View.GONE);
                }

                if (rlExpandLayer.getVisibility() == View.VISIBLE) {
                    rlExpandLayer.setAnimation(animation);
                    rlExpandLayer.setVisibility(View.GONE);
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

                rlPickOverlay.clearAnimation();
                rlExpandEditOverlay.clearAnimation();
                rlOpacityOverlay.clearAnimation();

                rlExpandPickDecor.clearAnimation();
                rlExpandEditDecor.clearAnimation();
                rlEditColorDecor.clearAnimation();
                llEditShadowDecor.clearAnimation();
                rlEditOpacityDecor.clearAnimation();

                rlPickTemp.clearAnimation();
                rlExpandEditTemp.clearAnimation();
                rlEditColorTemp.clearAnimation();
                llEditShadowTemp.clearAnimation();
                rlEditOpacityTemp.clearAnimation();
                rlExpandLayer.clearAnimation();
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

            if (rlExpandEditBackground.getVisibility() == View.VISIBLE)
                rlExpandEditBackground.setVisibility(View.GONE);

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
            case positionTemp:
                if (vMain.getVisibility() == View.GONE) vMain.setVisibility(View.VISIBLE);
                if (bitmap != null) vMain.setImageBitmap(bitmap);

                if (vCrop.getVisibility() == View.VISIBLE) vCrop.setVisibility(View.GONE);
                if (vColor.getVisibility() == View.VISIBLE) vColor.setVisibility(View.GONE);

                if (!isReplaceBackground) {
                    DrawableStickerCustom drawableSticker = new DrawableStickerCustom(this, templatelOld, getId(), Utils.TEMPLATE);
                    vSticker.addSticker(drawableSticker);
                    seekAndHideOperation(positionTemp);
                }
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
        Bitmap bitmap = BitmapFactory.decodeFile(backgroundModel.getUriRoot());
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
                    vCrop.setData(bitmap);
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
                    vCrop.setData(bitmap);
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
                    vCrop.setData(bitmap);
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
                    vCrop.setData(bitmap);
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
                    vCrop.setData(bitmap);
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
        ivLoading = findViewById(R.id.ivLoading);

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
        ivLoadingFilterBackground = findViewById(R.id.ivLoadingFilterBackground);
        rlExpandEditBackground = findViewById(R.id.rlExpandEditBackground);
        vEditBackground = findViewById(R.id.vEditBackground);
        rlCancelEditBackground = findViewById(R.id.rlCancelEditBackground);
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

        rlEditFilterBackground = findViewById(R.id.rlEditFilterBackground);
        rcvFilterBackground = findViewById(R.id.rcvEditFilterBackground);

        rlEditOpacityBackground = findViewById(R.id.rlEditOpacityBackground);
        sbOpacityBackground = findViewById(R.id.sbOpacityBackground);

        //Overlay
        rlPickOverlay = findViewById(R.id.rlPickOverlay);
        rlCancelPickOverlay = findViewById(R.id.rlCancelPickOverlay);
        rlExpandEditOverlay = findViewById(R.id.rlExpandEditOverlay);
        rlCancelEditOverlay = findViewById(R.id.rlCancelEditOverlay);
        tvTitleEditOverlay = findViewById(R.id.tvTitleEditOverlay);
        tvResetEditOverlay = findViewById(R.id.tvResetEditOverlay);
        llEditOverlay = findViewById(R.id.llEditOverlay);
        rcvOverlay = findViewById(R.id.rcvOverlay);

        rlDelOverlay = findViewById(R.id.rlDelOverlay);
        rlReplaceOverlay = findViewById(R.id.rlReplaceOverlay);
        rlOpacityOverlay = findViewById(R.id.rlOpacityOverlay);
        rlFlipXOverlay = findViewById(R.id.rlFlipXOverlay);
        rlFlipYOverlay = findViewById(R.id.rlFlipYOverlay);

        rlEditOpacityOverlay = findViewById(R.id.rlEditOpacityOverlay);
        sbOpacityOverlay = findViewById(R.id.sbOpacityOverlay);

        //Decor
        vEditDecor = findViewById(R.id.vEditDecor);
        rlExpandPickDecor = findViewById(R.id.rlExpandPickDecor);
        rlCancelPickDecor = findViewById(R.id.rlCancelPickDecor);
        rlCancelEditDecor = findViewById(R.id.rlCancelEditDecor);
        vpDecor = findViewById(R.id.vpDecor);
        rcvTypeDecor = findViewById(R.id.rcvTypeDecor);
        rlExpandEditDecor = findViewById(R.id.rlExpandEditDecor);
        tvResetEditDecor = findViewById(R.id.tvResetEditDecor);
        tvTitleEditDecor = findViewById(R.id.tvTitleEditDecor);

        rlDelDecor = findViewById(R.id.rlDelDecor);
        rlReplaceDecor = findViewById(R.id.rlReplaceDecor);
        rlDuplicateDecor = findViewById(R.id.rlDuplicateDecor);
        rlColorDecor = findViewById(R.id.rlColorDecor);
        rlShadowDecor = findViewById(R.id.rlShadowDecor);
        rlOpacityDecor = findViewById(R.id.rlOpacityDecor);
        rlFlipXDecor = findViewById(R.id.rlFlipXDecor);
        rlFlipYDecor = findViewById(R.id.rlFlipYDecor);

        rlEditColorDecor = findViewById(R.id.rlEditColorDecor);
        rcvEditColorDecor = findViewById(R.id.rcvEditColorDecor);
        ivColorDecor = findViewById(R.id.ivColorDecor);

        llEditShadowDecor = findViewById(R.id.llEditShadowDecor);
        tvXPosDecor = findViewById(R.id.tvXPosDecor);
        tvYPosDecor = findViewById(R.id.tvYPosDecor);
        tvBlurDecor = findViewById(R.id.tvBlurDecor);
        ivColorBlurDecor = findViewById(R.id.ivColorBlurDecor);
        sbXPosDecor = findViewById(R.id.sbXPosDecor);
        sbYPosDecor = findViewById(R.id.sbYPosDecor);
        sbBlurDecor = findViewById(R.id.sbBlurDecor);

        rlEditOpacityDecor = findViewById(R.id.rlEditOpacityDecor);
        sbOpacityDecor = findViewById(R.id.sbOpacityDecor);

        //Template
        rlPickTemp = findViewById(R.id.rlPickTemp);
        rlCancelPickTemp = findViewById(R.id.rlCancelPickTemp);
        rcvTextTemp = findViewById(R.id.rcvTextTemp);

        vEditTemp = findViewById(R.id.vEditTemp);
        rlExpandEditTemp = findViewById(R.id.rlExpandEditTemp);
        rlCancelEditTemp = findViewById(R.id.rlCancelEditTemp);
        tvTitleEditTemp = findViewById(R.id.tvTitleEditTemp);
        tvResetEditTemp = findViewById(R.id.tvResetEditTemp);

        rlDelTemp = findViewById(R.id.rlDelTemp);
        rlReplaceTemp = findViewById(R.id.rlReplaceTemp);
        rlDuplicateTemp = findViewById(R.id.rlDuplicateTemp);
        rlColorTemp = findViewById(R.id.rlColorTemp);
        rlBackgroundTemp = findViewById(R.id.rlBackgroundTemp);
        rlShadowTemp = findViewById(R.id.rlShadowTemp);
        rlOpacityTemp = findViewById(R.id.rlOpacityTemp);
        rlFlipXTemp = findViewById(R.id.rlFlipXTemp);
        rlFlipYTemp = findViewById(R.id.rlFlipYTemp);
        ivColorTemp = findViewById(R.id.ivColorTemp);

        rlEditColorTemp = findViewById(R.id.rlEditColorTemp);
        rcvEditColorTemp = findViewById(R.id.rcvEditColorTemp);

        llEditShadowTemp = findViewById(R.id.llEditShadowTemp);
        tvXPosTemp = findViewById(R.id.tvXPosTemp);
        tvYPosTemp = findViewById(R.id.tvYPosTemp);
        tvBlurTemp = findViewById(R.id.tvBlurTemp);
        ivColorBlurTemp = findViewById(R.id.ivColorBlurTemp);
        sbXPosTemp = findViewById(R.id.sbXPosTemp);
        sbYPosTemp = findViewById(R.id.sbYPosTemp);
        sbBlurTemp = findViewById(R.id.sbBlurTemp);

        rlEditOpacityTemp = findViewById(R.id.rlEditOpacityTemp);
        sbOpacityTemp = findViewById(R.id.sbOpacityTemp);

        //Layer
        rlExpandLayer = findViewById(R.id.rlExpandLayer);
        rlCancelLayer = findViewById(R.id.rlCancelLayer);
        rlDelLayer = findViewById(R.id.rlDelLayer);
        rlDuplicateLayer = findViewById(R.id.rlDuplicateLayer);
        rlLook = findViewById(R.id.rlLook);
        rlLock = findViewById(R.id.rlLock);
        ivLock = findViewById(R.id.ivLock);
        ivLook = findViewById(R.id.ivLook);
        rcvLayer = findViewById(R.id.rcvLayer);

        backgroundModel = new BackgroundModel();
        new Thread(() -> lstColor = DataColor.getListColor(this)).start();
        Glide.with(this)
                .asGif()
                .load(R.drawable.loading)
                .into(ivLoading);
        Glide.with(this)
                .asGif()
                .load(R.drawable.loading)
                .into(ivLoadingFilterBackground);
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

            if (!isDelLayer) seekAndHideOperation(indexDefault);
            else layerAdapter.setData(vSticker.getListLayer());
            isDelLayer = false;

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
            if (!isBackground)
                Utils.showToast(this, getResources().getString(R.string.choose_sticker_text));
            return false;
        }
        return true;
    }

    private void saveProject() {
        if (ivLoading.getVisibility() == View.GONE) ivLoading.setVisibility(View.VISIBLE);
        ArrayList<Sticker> lstSticker = vSticker.getListStickers();
        Project project = new Project();
        if (!isColor)
            project.setUriThumb(UtilsBitmap.saveBitmapToApp(this,
                    vSticker.getThumb(UtilsBitmap.loadBitmapFromView(vMain, false)), nameFolder, Utils.THUMB));
        else
            project.setUriThumb(UtilsBitmap.saveBitmapToApp(this,
                    vSticker.getThumb(UtilsBitmap.loadBitmapFromView(vColor, true)), nameFolder, Utils.THUMB));
        project.setBackgroundModel(backgroundModel);
        for (Sticker sticker : lstSticker) {
            if (sticker instanceof DrawableStickerCustom) {
                DrawableStickerCustom drawableSticker = (DrawableStickerCustom) sticker;
                switch (drawableSticker.getTypeSticker()) {
                    case Utils.EMOJI:
                        project.getLstEmojiModel().add(drawableSticker.getEmojiModel());
                        break;
                    case Utils.IMAGE:
                        project.getLstImageModel().add(drawableSticker.getImageModel());
                        break;
                    case Utils.DECOR:
                        project.getLstDecorModel().add(drawableSticker.getDecorModel());
                        break;
                    case Utils.TEMPLATE:
                        project.getLstTempModel().add(drawableSticker.getTemplateModel());
                        break;
                }
            } else {
                TextStickerCustom textSticker = (TextStickerCustom) sticker;
                project.getLstTextModel().add(textSticker.getTextModel());
            }
        }
        ArrayList<Project> lstProject = DataLocalManager.getListProject(this, Utils.LIST_PROJECT);
        if (indexProject == -1) lstProject.add(project);
        else lstProject.set(indexProject, project);
        DataLocalManager.setListProject(this, lstProject, Utils.LIST_PROJECT);
        handlerLoading.sendEmptyMessage(1);
    }

    private void addDataProject() {
        if (ivLoading.getVisibility() == View.GONE) ivLoading.setVisibility(View.VISIBLE);

        if (!project.getLstTextModel().isEmpty()
                || !project.getLstEmojiModel().isEmpty()
                || !project.getLstImageModel().isEmpty()
                || !project.getLstDecorModel().isEmpty()
                || !project.getLstTempModel().isEmpty())
            ivLayer.setImageResource(R.drawable.ic_layer);
        else ivLayer.setImageResource(R.drawable.ic_layer_uncheck);

        if (!project.getLstTextModel().isEmpty()) {
            indexMatrix = 0;
            setMatrixText(indexMatrix);
        } else if (!project.getLstEmojiModel().isEmpty()) {
            indexMatrix = 0;
            setMatrixEmoji(indexMatrix);
        } else if (!project.getLstImageModel().isEmpty()) {
            indexMatrix = 0;
            setMatrixImage(indexMatrix);
        } else if (!project.getLstDecorModel().isEmpty()) {
            indexMatrix = 0;
            setMatrixDecor(indexMatrix);
        } else if (!project.getLstTempModel().isEmpty()) {
            indexMatrix = 0;
            setMatrixTemp(indexMatrix);
        } else project = null;

        if (ivLoading.getVisibility() == View.VISIBLE) ivLoading.setVisibility(View.GONE);
    }

    private void setMatrixText(int index) {
        if (index >= project.getLstTextModel().size()) {
            if (!project.getLstEmojiModel().isEmpty()) {
                indexMatrix = 0;
                setMatrixEmoji(indexMatrix);
            } else if (!project.getLstImageModel().isEmpty()) {
                indexMatrix = 0;
                setMatrixImage(indexMatrix);
            } else if (!project.getLstDecorModel().isEmpty()) {
                indexMatrix = 0;
                setMatrixDecor(indexMatrix);
            } else if (!project.getLstTempModel().isEmpty()) {
                indexMatrix = 0;
                setMatrixTemp(indexMatrix);
            } else project = null;
            return;
        }
        TextModel textModel = project.getLstTextModel().get(index);
        TextStickerCustom textSticker = new TextStickerCustom(this, textModel, getId());
        vSticker.addSticker(textSticker);
    }

    private void setMatrixEmoji(int index) {
        if (index >= project.getLstEmojiModel().size()) {
            if (!project.getLstImageModel().isEmpty()) {
                indexMatrix = 0;
                setMatrixImage(indexMatrix);
            } else if (!project.getLstDecorModel().isEmpty()) {
                indexMatrix = 0;
                setMatrixDecor(indexMatrix);
            } else if (!project.getLstTempModel().isEmpty()) {
                indexMatrix = 0;
                setMatrixTemp(indexMatrix);
            } else project = null;
            return;
        }
        EmojiModel emoji = project.getLstEmojiModel().get(index);
        DrawableStickerCustom drawableSticker = new DrawableStickerCustom(this, emoji, getId(), Utils.EMOJI);
        vSticker.addSticker(drawableSticker);
    }

    private void setMatrixImage(int index) {
        if (index >= project.getLstImageModel().size()) {
            if (!project.getLstDecorModel().isEmpty()) {
                indexMatrix = 0;
                setMatrixDecor(indexMatrix);
            } else if (!project.getLstTempModel().isEmpty()) {
                indexMatrix = 0;
                setMatrixTemp(indexMatrix);
            } else project = null;
            return;
        }
        ImageModel image = project.getLstImageModel().get(index);
        DrawableStickerCustom drawableSticker = new DrawableStickerCustom(this, image, getId(), Utils.IMAGE);
        vSticker.addSticker(drawableSticker);
    }

    private void setMatrixDecor(int index) {
        if (index >= project.getLstDecorModel().size()) {
            if (!project.getLstTempModel().isEmpty()) {
                indexMatrix = 0;
                setMatrixTemp(indexMatrix);
            } else project = null;
            return;
        }
        DecorModel decor = project.getLstDecorModel().get(index);
        DrawableStickerCustom drawableSticker = new DrawableStickerCustom(this, decor, getId(), Utils.DECOR);
        vSticker.addSticker(drawableSticker);
    }

    private void setMatrixTemp(int index) {
        if (index == project.getLstTempModel().size()) {
            project = null;
            return;
        }
        TemplateModel temp = project.getLstTempModel().get(index);
        DrawableStickerCustom drawableSticker = new DrawableStickerCustom(this, temp, getId(), Utils.TEMPLATE);
        vSticker.addSticker(drawableSticker);
    }

    @Override
    public void onBackPressed() {
        if (vSize.getVisibility() == View.VISIBLE)
            if (vSticker.getListStickers().isEmpty())
                super.onBackPressed();
            else clickTick();
        else if (vOperation.getVisibility() == View.GONE) {
            if (vEditText.getVisibility() == View.GONE) {
                seekAndHideOperation(positionAddText);
            } else if (llEditEmoji.getVisibility() == View.GONE) {
                seekAndHideOperation(positionEmoji);
            } else if (vEditImage.getVisibility() == View.GONE) {
                seekAndHideOperation(positionImage);
            } else if (vEditBackground.getVisibility() == View.GONE) {
                seekAndHideOperation(positionBackground);
            } else if (llEditOverlay.getVisibility() == View.GONE) {
                seekAndHideOperation(positionOverlay);
            } else if (vEditDecor.getVisibility() == View.GONE) {
                seekAndHideOperation(positionDecor);
            } else {
                vSticker.setCurrentSticker(null);
                seekAndHideOperation(indexDefault);
            }
        } else {
            @SuppressLint("InflateParams")
            View v = LayoutInflater.from(this).inflate(R.layout.dialog_exit_edit, null);

            LinearLayout rlBack = v.findViewById(R.id.rlBack);
            rlBack.getLayoutParams().width = (int) (getResources().getDisplayMetrics().widthPixels
                    - getResources().getDimension(com.intuit.sdp.R.dimen._20sdp));

            TextView tvCancel = v.findViewById(R.id.tvCancel);
            TextView tvDiscard = v.findViewById(R.id.tvDiscard);
            TextView tvSave = v.findViewById(R.id.tvSave);

            AlertDialog dialog = new AlertDialog.Builder(this, R.style.SheetDialog).create();
            dialog.setView(v);
            dialog.setCancelable(false);
            dialog.show();

            tvDiscard.setOnClickListener(vDiscard -> {
                super.onBackPressed();
                Utils.setAnimExit(this);
                new Thread(() -> Utils.delFileInFolder(this, nameFolder, "")).start();
                dialog.cancel();
            });
            tvSave.setOnClickListener(vSave -> {
                saveProject();
                super.onBackPressed();
                Utils.setAnimExit(this);
                dialog.cancel();
            });
            tvCancel.setOnClickListener(vCancel -> dialog.cancel());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        project = DataLocalManager.getProject(Utils.PROJECT);
        if (project == null) {
            String strPicUser = DataLocalManager.getOption("bitmap");
            String strPicApp = DataLocalManager.getOption("bitmap_myapp");
            ColorModel colorModel = DataLocalManager.getColor("color");
            TemplateModel templateModel = DataLocalManager.getTemp("temp");

            if (!strPicUser.equals("") && !strPicUser.equals(strPicUserOld)) {
                getData(0, strPicUser, null, null, true);
            } else if (!strPicApp.equals("") && !strPicApp.equals(strPicAppOld)) {
                getData(1, strPicApp, null, null, true);
            } else if (templateModel != null && !templateModel.getBackground().equals(templatelOld.getBackground())) {
                getData(2, "", null, templateModel, true);
            } else if (colorModel != null && colorModel != colorModelOld) {
                getData(3, "", colorModel, null, true);
            }
            strPicUserOld = strPicUser;
            strPicAppOld = strPicApp;
            templatelOld = templateModel;
            colorModelOld = colorModel;
        } else {
            indexProject = DataLocalManager.getInt("indexProject");
            backgroundModel = project.getBackgroundModel();
            backgroundModel.setOverlayModel(project.getOverlayModel());

            if (backgroundModel.getColorModel() == null) {
                Bitmap bmMain;
                if (backgroundModel.getUriOverlay().equals(""))
                    bmMain = BitmapFactory.decodeFile(backgroundModel.getUriCache());
                else bmMain = BitmapFactory.decodeFile(backgroundModel.getUriOverlay());

                vSticker.getLayoutParams().width = bmMain.getWidth();
                vMain.getLayoutParams().width = bmMain.getWidth();
                vSticker.getLayoutParams().height = bmMain.getHeight();
                vMain.getLayoutParams().height = bmMain.getHeight();

                seekAndHideViewMain(positionMain, bmMain, null, false);
            } else {
                colorModelOld = backgroundModel.getColorModel();

                vColor.setSize(backgroundModel.getSizeViewColor());

                int w = (int) calculatorSizeColor(backgroundModel.getSizeViewColor())[0];
                int h = (int) calculatorSizeColor(backgroundModel.getSizeViewColor())[1];
                vSticker.getLayoutParams().height = h;
                vSticker.getLayoutParams().width = w;
                vColor.getLayoutParams().width = w;
                vColor.getLayoutParams().height = h;

                seekAndHideViewMain(positionColor, null, colorModelOld, false);
            }
            addDataProject();

            DataLocalManager.setProject(null, Utils.PROJECT);
        }
    }

    private float[] calculatorSizeColor(int position) {
        float w = getResources().getDisplayMetrics().widthPixels;
        float h = (getResources().getDisplayMetrics().heightPixels - getResources().getDimension(com.intuit.sdp.R.dimen._245sdp));

        float scale;
        switch (position) {
            case 1:
                scale = 1f;
                break;
            case 2:
                scale = 9 / 16f;
                break;
            case 3:
                scale = 4 / 5f;
                break;
            case 4:
                scale = 16 / 9f;
                break;
            default:
                scale = w / h;
                break;
        }

        if (w / h >= scale) return new float[]{scale * w, h};
        else return new float[]{w, w / scale};
    }
}