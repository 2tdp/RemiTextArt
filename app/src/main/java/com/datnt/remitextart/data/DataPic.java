package com.datnt.remitextart.data;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;

import com.datnt.remitextart.R;
import com.datnt.remitextart.model.picture.BucketPicModel;
import com.datnt.remitextart.model.picture.PicModel;
import com.datnt.remitextart.sharepref.DataLocalManager;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class DataPic {

    private static final String[] EXTERNAL_COLUMNS_PIC = new String[]{
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
            MediaStore.Images.Media.DATA,
            "\"" + MediaStore.Images.Media.EXTERNAL_CONTENT_URI + "\""
    };

    private static final String[] EXTERNAL_COLUMNS_PIC_API_Q = new String[]{
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
            MediaStore.Images.Media.DATA
    };

    public static void getBucketPictureList(Context context) {

        ArrayList<BucketPicModel> lstBucket = new ArrayList<>();
        ArrayList<PicModel> lstPic, lstAll = new ArrayList<>();
        Uri CONTENT_URI;
        String[] COLUMNS;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            CONTENT_URI = MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL);
            COLUMNS = EXTERNAL_COLUMNS_PIC_API_Q;
        } else {
            CONTENT_URI = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            COLUMNS = EXTERNAL_COLUMNS_PIC;
        }

        try (Cursor cursor = context.getContentResolver().query(
                CONTENT_URI,
                COLUMNS,
                null,
                null,
                MediaStore.Images.Media.DEFAULT_SORT_ORDER)) {
            int idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID);
            int bucketColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
            int dataColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

            while (cursor.moveToNext()) {
                String id = cursor.getString(idColumn);
                String bucket = cursor.getString(bucketColumn);
                String data = cursor.getString(dataColumn);

                Uri contentUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, Long.parseLong(id));

                File file = new File(data);
                if (file.canRead()) {
                    lstAll.add(new PicModel(id, bucket, contentUri.toString(), false));
                    boolean check = false;
                    if (lstBucket.isEmpty()) {
                        lstPic = new ArrayList<>();
                        if (bucket != null) {
                            lstBucket.add(new BucketPicModel(lstPic, bucket));
                            lstPic.add(new PicModel(id, bucket, contentUri.toString(), false));
                        } else {
                            lstBucket.add(new BucketPicModel(lstPic, ""));
                            lstPic.add(new PicModel(id, "", contentUri.toString(), false));
                        }
                    } else {
                        for (int i = 0; i < lstBucket.size(); i++) {
                            if (bucket == null) {
                                lstBucket.get(i).getLstPic().add(new PicModel(id, "", contentUri.toString(), false));
                                check = true;
                                break;
                            }
                            if (bucket.equals(lstBucket.get(i).getBucket())) {
                                lstBucket.get(i).getLstPic().add(new PicModel(id, bucket, contentUri.toString(), false));
                                check = true;
                                break;
                            }
                        }
                        if (!check) {
                            lstPic = new ArrayList<>();
                            lstPic.add(new PicModel(id, bucket, contentUri.toString(), false));
                            lstBucket.add(new BucketPicModel(lstPic, bucket));
                        }
                    }
                }
            }

            lstBucket.add(0, new BucketPicModel(lstAll, context.getString(R.string.all)));
        }
        DataLocalManager.setListBucket(lstBucket, "bucket");
    }

    public static ArrayList<PicModel> getAllPictureList(Context context) {

        ArrayList<PicModel> lstPic = new ArrayList<>();
        Uri CONTENT_URI;
        String[] COLUMNS;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            CONTENT_URI = MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL);
            COLUMNS = EXTERNAL_COLUMNS_PIC_API_Q;
        } else {
            CONTENT_URI = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            COLUMNS = EXTERNAL_COLUMNS_PIC;
        }

        try (Cursor cursor = context.getContentResolver().query(
                CONTENT_URI,
                COLUMNS,
                null,
                null,
                MediaStore.Images.Media.DEFAULT_SORT_ORDER)) {
            int idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID);
            int bucketColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
            int dataColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

            while (cursor.moveToNext()) {
                String id = cursor.getString(idColumn);
                String bucket = cursor.getString(bucketColumn);
                String data = cursor.getString(dataColumn);

                Uri contentUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, Long.parseLong(id));

                File file = new File(data);
                if (file.canRead())
                    lstPic.add(new PicModel(id, bucket, contentUri.toString(), false));
            }
        }
        return lstPic;
    }

    public static ArrayList<String> getPicAssets(Context context, String name) {
        ArrayList<String> lstPicAsset = new ArrayList<>();
        try {
            String[] f = context.getAssets().list(name);
            lstPicAsset.addAll(Arrays.asList(f));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return lstPicAsset;
    }

    public static String getPathDataCrop(int pos) {
        return lstPathData[pos];
    }

    public static String[] lstPathData = new String[]{
            "M0 0H34V34H0z",
            "M17 0A17 17 0 1 0 17 34 17 17 0 1 0 17 0z",
            "M28.84 34l-11.02-7.99-11.23 7.7 4.28-12.83L0 12.69l13.66 0.06L18.18 0l4.16 12.87L36 13.17l-11.09 7.9L28.84 34Z",
            "M34.63 3.25c-1.05-1.03-2.3-1.85-3.67-2.4C29.58 0.29 28.1 0 26.6 0 23.8 0 21.08 1.03 19 2.88 16.92 1.03 14.2 0 11.39 0c-1.5 0-2.97 0.29-4.35 0.85-1.38 0.56-2.63 1.37-3.68 2.4-4.48 4.37-4.48 11.21 0 15.57L19 34l15.64-15.18c4.48-4.36 4.48-11.2 0-15.57Z",
            "M17.54 0c-0.33 0-0.66 0.1-0.93 0.3L0.63 12.1c-0.26 0.2-0.46 0.46-0.56 0.77-0.1 0.31-0.1 0.65 0 0.96l6.1 19.1c0.1 0.31 0.3 0.58 0.56 0.77 0.26 0.2 0.57 0.3 0.9 0.3h19.75c0.32 0 0.63-0.1 0.89-0.3 0.26-0.19 0.45-0.46 0.55-0.77l6.1-19.1c0.1-0.31 0.1-0.65 0-0.96-0.1-0.3-0.29-0.58-0.55-0.77L18.4 0.3C18.15 0.1 17.85 0 17.54 0Z",
            "M40.67 14.84C39.62 6.48 32.54 0 24 0 17.39 0 11.64 3.91 9.02 10.08 3.86 11.64 0 16.56 0 21.86 0 28.56 5.38 34 12 34h26.4c5.3 0 9.6-4.36 9.6-9.71 0-2.18-0.73-4.3-2.06-6-1.33-1.72-3.18-2.93-5.27-3.45Z",
            "M28.16 17.76c-0.57-0.32-1.18-0.58-1.8-0.76 0.62-0.18 1.23-0.44 1.8-0.76 1.3-0.75 2.26-1.99 2.65-3.44 0.39-1.45 0.18-3-0.57-4.3-0.75-1.3-1.99-2.25-3.44-2.64-1.46-0.4-3-0.19-4.3 0.56-0.57 0.34-1.09 0.74-1.57 1.18 0.15-0.63 0.23-1.28 0.24-1.93 0-1.5-0.6-2.95-1.66-4.01C18.45 0.6 17 0 15.5 0c-1.5 0-2.95 0.6-4 1.66-1.07 1.06-1.67 2.5-1.67 4 0.01 0.66 0.09 1.3 0.24 1.94-0.48-0.44-1-0.84-1.56-1.18-1.3-0.75-2.85-0.95-4.3-0.56C2.74 6.25 1.5 7.2 0.75 8.5s-0.96 2.85-0.57 4.3c0.4 1.45 1.34 2.7 2.65 3.44 0.57 0.32 1.18 0.58 1.8 0.76-0.62 0.18-1.23 0.44-1.8 0.76-1.3 0.75-2.26 1.99-2.65 3.44-0.39 1.45-0.18 3 0.57 4.3 0.75 1.3 1.99 2.25 3.44 2.64 1.46 0.4 3 0.19 4.3-0.56 0.57-0.34 1.09-0.74 1.57-1.18-0.15 0.63-0.23 1.28-0.24 1.93 0 1.5 0.6 2.95 1.66 4.01C12.55 33.4 14 34 15.5 34c1.5 0 2.95-0.6 4-1.66 1.07-1.06 1.67-2.5 1.67-4-0.01-0.66-0.09-1.3-0.24-1.94 0.48 0.44 1 0.84 1.56 1.18 1.3 0.75 2.85 0.95 4.3 0.56 1.46-0.39 2.7-1.34 3.45-2.64s0.96-2.85 0.57-4.3c-0.4-1.45-1.34-2.7-2.65-3.44ZM15.5 20.78c-0.75 0-1.48-0.22-2.1-0.64-0.62-0.41-1.1-1-1.4-1.7-0.28-0.68-0.35-1.44-0.2-2.18 0.14-0.73 0.5-1.4 1.03-1.93s1.2-0.89 1.93-1.04c0.74-0.14 1.5-0.07 2.19 0.22 0.69 0.29 1.28 0.77 1.7 1.4 0.4 0.61 0.63 1.34 0.63 2.09 0 1-0.4 1.96-1.11 2.67-0.71 0.7-1.67 1.1-2.67 1.1Z",
            "M6.8 0L0 10.2 17 34l17-23.8L27.2 0H6.8Z",
            "M26 17L13 34 0 17 13 0",
            "M29.47 23.59l-2.63-1.53 2.28-0.62c0.57-0.15 0.9-0.73 0.75-1.3l-0.28-1.03c-0.14-0.56-0.73-0.9-1.29-0.75l-5.34 1.44-4.8-2.8 4.81-2.8 5.34 1.44c0.56 0.15 1.14-0.19 1.29-0.75l0.27-1.03c0.15-0.57-0.18-1.15-0.74-1.3l-2.28-0.62 2.62-1.53c0.5-0.29 0.68-0.94 0.4-1.45l-0.54-0.92c-0.29-0.52-0.94-0.69-1.44-0.4l-2.62 1.53 0.6-2.3c0.15-0.57-0.17-1.15-0.74-1.3l-1.01-0.28c-0.57-0.15-1.14 0.19-1.3 0.75l-1.42 5.38-4.82 2.8v-5.6l3.9-3.94c0.42-0.42 0.42-1.1 0-1.5l-0.74-0.76c-0.4-0.4-1.07-0.4-1.48 0l-1.67 1.7V1.05C16.6 0.48 16.12 0 15.54 0h-1.05c-0.58 0-1.05 0.48-1.05 1.06v3.06l-1.67-1.68c-0.4-0.41-1.08-0.41-1.49 0L9.51 3.19c-0.4 0.4-0.4 1.09 0 1.5l3.9 3.94v5.6l-4.8-2.8-1.43-5.37c-0.15-0.57-0.73-0.9-1.3-0.75L4.87 5.58c-0.57 0.15-0.9 0.74-0.75 1.3l0.61 2.3-2.62-1.52c-0.5-0.3-1.15-0.12-1.44 0.39L0.14 8.97c-0.29 0.51-0.12 1.16 0.39 1.46l2.62 1.52-2.28 0.6c-0.56 0.16-0.9 0.74-0.74 1.3L0.4 14.9c0.15 0.56 0.73 0.9 1.3 0.75l5.33-1.44 4.81 2.8-4.81 2.8-5.34-1.44c-0.56-0.15-1.13 0.19-1.29 0.75l-0.27 1.03c-0.15 0.57 0.18 1.15 0.74 1.3l2.28 0.62-2.62 1.53c-0.5 0.29-0.68 0.94-0.39 1.45l0.53 0.92c0.29 0.52 0.94 0.69 1.44 0.4l2.62-1.53-0.6 2.3c-0.15 0.57 0.17 1.15 0.74 1.3l1.01 0.28c0.57 0.15 1.14-0.19 1.3-0.75l1.42-5.38 4.82-2.8v5.6l-3.9 3.95c-0.41 0.4-0.41 1.08 0 1.5l0.74 0.75c0.4 0.4 1.08 0.4 1.48 0l1.67-1.69v3.05c0 0.58 0.47 1.06 1.05 1.06h1.05c0.59 0 1.05-0.48 1.05-1.06v-3.06l1.67 1.68c0.41 0.41 1.08 0.41 1.49 0l0.74-0.75c0.4-0.4 0.4-1.09 0-1.5l-3.9-3.94v-5.6l4.81 2.8 1.43 5.37c0.14 0.57 0.73 0.9 1.29 0.75l1.02-0.27c0.56-0.15 0.9-0.74 0.74-1.3l-0.6-2.3 2.62 1.52c0.5 0.3 1.14 0.12 1.44-0.39l0.52-0.92c0.31-0.5 0.14-1.15-0.36-1.44Z",
            "M18 31.88c9.94 0 18-7.14 18-15.94S27.94 0 18 0 0 7.14 0 15.94c0 4 1.67 7.67 4.43 10.47-0.22 2.32-0.94 4.85-1.73 6.76-0.18 0.42 0.16 0.9 0.61 0.82 5.08-0.84 8.1-2.13 9.4-2.8 1.73 0.46 3.5 0.7 5.29 0.69Z"
    };
}
