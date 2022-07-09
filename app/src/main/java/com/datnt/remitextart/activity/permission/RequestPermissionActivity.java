package com.datnt.remitextart.activity.permission;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.datnt.remitextart.R;
import com.datnt.remitextart.activity.MainActivity;
import com.datnt.remitextart.activity.base.BaseActivity;
import com.datnt.remitextart.data.DataPic;
import com.datnt.remitextart.sharepref.DataLocalManager;
import com.datnt.remitextart.utils.Utils;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.List;

public class RequestPermissionActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.request_permission_layout);
    }

    private void initView() {
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_check_permission, null);
        ImageView ivExit = view.findViewById(R.id.ivExitPer);
        TextView tvAllow = view.findViewById(R.id.tvAllow);

        AlertDialog dialog = new AlertDialog.Builder(this, R.style.SheetDialog).create();
        dialog.setView(view);
        dialog.setCancelable(false);
        dialog.show();

        ivExit.setOnClickListener(v -> {
            Utils.showToast(this, getString(R.string.rp_3));
            dialog.cancel();
            finish();
        });
        tvAllow.setOnClickListener(v -> {
            checkPermission();
            dialog.cancel();
        });
    }

    private void checkPermission() {
        Intent intent = new Intent(RequestPermissionActivity.this, MainActivity.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            Dexter.withContext(this)
                    .withPermissions(Manifest.permission.ACCESS_MEDIA_LOCATION, Manifest.permission.READ_EXTERNAL_STORAGE)
                    .withListener(new MultiplePermissionsListener() {
                        @Override
                        public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                            if (multiplePermissionsReport.areAllPermissionsGranted()) {
                                DataLocalManager.setCheck("per", true);
                                runOnUiThread(() -> DataPic.getBucketPictureList(RequestPermissionActivity.this));
                                startActivity(intent);
                                finish();
                            } else {
                                Utils.showToast(RequestPermissionActivity.this, getString(R.string.rp_3));
                                openSettingPermission();
                            }
                        }

                        @Override
                        public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                            permissionToken.continuePermissionRequest();
                        }
                    }).check();
        } else {
            Dexter.withContext(this)
                    .withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .withListener(new MultiplePermissionsListener() {
                        @Override
                        public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                            if (multiplePermissionsReport.areAllPermissionsGranted()) {
                                DataLocalManager.setCheck("per", true);
                                runOnUiThread(() -> DataPic.getBucketPictureList(RequestPermissionActivity.this));
                                startActivity(intent);
                                finish();
                            } else {
                                Utils.showToast(RequestPermissionActivity.this, getString(R.string.rp_3));
                                openSettingPermission();
                            }
                        }

                        @Override
                        public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                            permissionToken.continuePermissionRequest();
                        }
                    }).check();
        }
    }

    private void openSettingPermission() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!DataLocalManager.getCheck("per")) {
            initView();
        } else {
            Utils.setIntent(this, MainActivity.class.getName());
            finish();
        }
    }
}
