package com.textonphoto.texttophoto.quotecreator.pro.data;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;

import com.textonphoto.texttophoto.quotecreator.pro.R;
import com.textonphoto.texttophoto.quotecreator.pro.model.picture.BucketPicModel;
import com.textonphoto.texttophoto.quotecreator.pro.model.picture.PicModel;
import com.textonphoto.texttophoto.quotecreator.pro.sharepref.DataLocalManager;

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
            "M27.24 32.11l-10.4-7.54-10.61 7.26 4.04-12.11L0 11.99l12.9 0.05L17.17 0l3.93 12.15L34 12.44l-10.47 7.45 3.7 12.22Z",
            "M30.99 2.9c-0.94-0.92-2.06-1.65-3.3-2.14C26.48 0.26 25.16 0 23.82 0 21.3 0 18.86 0.92 17 2.58 15.14 0.92 12.71 0 10.19 0c-1.34 0-2.66 0.26-3.9 0.76-1.22 0.5-2.34 1.23-3.28 2.15C-1 6.82-1 12.94 3 16.84L17 30.42l13.99-13.58C35 12.94 35 6.82 30.99 2.9Z",
            "M17.04 0c-0.32 0-0.64 0.1-0.9 0.29L0.6 11.76c-0.25 0.18-0.44 0.44-0.54 0.75-0.1 0.3-0.1 0.62 0 0.92L6 32c0.1 0.3 0.29 0.57 0.54 0.75 0.25 0.19 0.55 0.29 0.87 0.29h19.18c0.32 0 0.62-0.1 0.87-0.29C27.71 32.56 27.9 32.3 28 32l5.93-18.56c0.1-0.3 0.1-0.62 0-0.92-0.1-0.3-0.29-0.57-0.54-0.75L17.87 0.29C17.63 0.1 17.34 0 17.04 0Z",
            "M28.8 10.51C28.06 4.6 23.05 0 17 0 12.31 0 8.24 2.77 6.39 7.14 2.74 8.24 0 11.74 0 15.48c0 4.74 3.81 8.6 8.5 8.6h18.7c3.75 0 6.8-3.08 6.8-6.88 0-1.54-0.52-3.04-1.46-4.25s-2.26-2.07-3.74-2.44Z",
            "M30.89 19.47c-0.63-0.34-1.3-0.62-1.99-0.82 0.7-0.2 1.36-0.49 1.99-0.83 1.43-0.83 2.47-2.19 2.9-3.78 0.43-1.6 0.2-3.29-0.62-4.72-0.83-1.43-2.19-2.47-3.78-2.9-1.6-0.42-3.3-0.2-4.72 0.62-0.62 0.37-1.19 0.8-1.71 1.3 0.16-0.7 0.25-1.41 0.26-2.12 0-1.65-0.66-3.23-1.82-4.4C20.23 0.65 18.65 0 17 0c-1.65 0-3.23 0.65-4.4 1.82-1.16 1.17-1.82 2.75-1.82 4.4 0.01 0.71 0.1 1.42 0.26 2.12-0.52-0.5-1.1-0.93-1.7-1.3C7.9 6.22 6.2 6 4.6 6.42 3 6.85 1.66 7.9 0.83 9.32c-0.82 1.43-1.05 3.13-0.62 4.72 0.43 1.6 1.47 2.95 2.9 3.78 0.63 0.34 1.3 0.62 1.99 0.83-0.7 0.2-1.36 0.48-1.99 0.82-1.43 0.83-2.47 2.19-2.9 3.78-0.43 1.6-0.2 3.29 0.62 4.72 0.83 1.43 2.19 2.47 3.78 2.9 1.6 0.42 3.3 0.2 4.72-0.62 0.62-0.37 1.19-0.8 1.71-1.3-0.16 0.7-0.25 1.41-0.26 2.13 0 1.64 0.66 3.22 1.82 4.39 1.17 1.17 2.75 1.82 4.4 1.82 1.65 0 3.23-0.65 4.4-1.82 1.16-1.17 1.82-2.75 1.82-4.4-0.01-0.71-0.1-1.42-0.26-2.12 0.52 0.5 1.1 0.93 1.7 1.3 1.44 0.82 3.14 1.04 4.73 0.62 1.6-0.43 2.95-1.47 3.78-2.9 0.82-1.43 1.05-3.13 0.62-4.72-0.43-1.6-1.47-2.95-2.9-3.78ZM17 22.8c-0.82 0-1.62-0.24-2.3-0.7-0.68-0.45-1.22-1.1-1.53-1.86-0.31-0.76-0.4-1.59-0.24-2.4 0.16-0.8 0.56-1.54 1.14-2.11 0.58-0.58 1.32-0.98 2.12-1.14 0.8-0.16 1.64-0.08 2.4 0.24 0.75 0.31 1.4 0.84 1.86 1.52 0.45 0.68 0.7 1.49 0.7 2.3 0 1.1-0.45 2.15-1.22 2.93-0.78 0.78-1.83 1.21-2.93 1.22Z",
            "M6.8 0L0 10.2 17 34l17-23.8L27.2 0H6.8Z",
            "M34 22.23L17 44.46 0 22.23 17 0",
            "M29.47 23.59l-2.63-1.53 2.28-0.62c0.57-0.15 0.9-0.73 0.75-1.3l-0.28-1.03c-0.14-0.56-0.73-0.9-1.29-0.75l-5.34 1.44-4.8-2.8 4.81-2.8 5.34 1.44c0.56 0.15 1.14-0.19 1.29-0.75l0.27-1.03c0.15-0.57-0.18-1.15-0.74-1.3l-2.28-0.62 2.62-1.53c0.5-0.29 0.68-0.94 0.4-1.45l-0.54-0.92c-0.29-0.52-0.94-0.69-1.44-0.4l-2.62 1.53 0.6-2.3c0.15-0.57-0.17-1.15-0.74-1.3l-1.01-0.28c-0.57-0.15-1.14 0.19-1.3 0.75l-1.42 5.38-4.82 2.8v-5.6l3.9-3.94c0.42-0.42 0.42-1.1 0-1.5l-0.74-0.76c-0.4-0.4-1.07-0.4-1.48 0l-1.67 1.7V1.05C16.6 0.48 16.12 0 15.54 0h-1.05c-0.58 0-1.05 0.48-1.05 1.06v3.06l-1.67-1.68c-0.4-0.41-1.08-0.41-1.49 0L9.51 3.19c-0.4 0.4-0.4 1.09 0 1.5l3.9 3.94v5.6l-4.8-2.8-1.43-5.37c-0.15-0.57-0.73-0.9-1.3-0.75L4.87 5.58c-0.57 0.15-0.9 0.74-0.75 1.3l0.61 2.3-2.62-1.52c-0.5-0.3-1.15-0.12-1.44 0.39L0.14 8.97c-0.29 0.51-0.12 1.16 0.39 1.46l2.62 1.52-2.28 0.6c-0.56 0.16-0.9 0.74-0.74 1.3L0.4 14.9c0.15 0.56 0.73 0.9 1.3 0.75l5.33-1.44 4.81 2.8-4.81 2.8-5.34-1.44c-0.56-0.15-1.13 0.19-1.29 0.75l-0.27 1.03c-0.15 0.57 0.18 1.15 0.74 1.3l2.28 0.62-2.62 1.53c-0.5 0.29-0.68 0.94-0.39 1.45l0.53 0.92c0.29 0.52 0.94 0.69 1.44 0.4l2.62-1.53-0.6 2.3c-0.15 0.57 0.17 1.15 0.74 1.3l1.01 0.28c0.57 0.15 1.14-0.19 1.3-0.75l1.42-5.38 4.82-2.8v5.6l-3.9 3.95c-0.41 0.4-0.41 1.08 0 1.5l0.74 0.75c0.4 0.4 1.08 0.4 1.48 0l1.67-1.69v3.05c0 0.58 0.47 1.06 1.05 1.06h1.05c0.59 0 1.05-0.48 1.05-1.06v-3.06l1.67 1.68c0.41 0.41 1.08 0.41 1.49 0l0.74-0.75c0.4-0.4 0.4-1.09 0-1.5l-3.9-3.94v-5.6l4.81 2.8 1.43 5.37c0.14 0.57 0.73 0.9 1.29 0.75l1.02-0.27c0.56-0.15 0.9-0.74 0.74-1.3l-0.6-2.3 2.62 1.52c0.5 0.3 1.14 0.12 1.44-0.39l0.52-0.92c0.31-0.5 0.14-1.15-0.36-1.44Z",
            "M18 31.88c9.94 0 18-7.14 18-15.94S27.94 0 18 0 0 7.14 0 15.94c0 4 1.67 7.67 4.43 10.47-0.22 2.32-0.94 4.85-1.73 6.76-0.18 0.42 0.16 0.9 0.61 0.82 5.08-0.84 8.1-2.13 9.4-2.8 1.73 0.46 3.5 0.7 5.29 0.69Z"
    };
}
