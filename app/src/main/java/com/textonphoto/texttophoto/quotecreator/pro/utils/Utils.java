package com.textonphoto.texttophoto.quotecreator.pro.utils;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.textonphoto.texttophoto.quotecreator.pro.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;

public class Utils {

    public static final String PROJECT = "project";
    public static final String LIST_PROJECT = "lstProject";
    public static final String NUMB_PROJECT = "numbProject";
    public static final String THUMB = "thumb";

    //Model Check
    public static final String STICKER_ICON = "sticker_icon";
    public static final String EMOJI = "emoji";
    public static final String IMAGE = "image";
    public static final String OVERLAY = "overlay";
    public static final String DECOR = "decor";
    public static final String TEMPLATE = "template";

    //cache save
    public static final String BACKGROUND_ROOT = "background_root";
    public static final String BACKGROUND = "background";
    public static final String BACKGROUND_OVERLAY_CACHE = "background_overlay_cache";
    public static final String OVERLAY_ROOT = "overlay_root";
    public static final String IMAGE_ROOT = "image_root";
    public static final String INSTAGRAM_PACKAGE_NAME = "com.instagram.android";
    public static final String FACEBOOK_PACKAGE_NAME = "com.facebook.katana";
    public static final String TELEGRAM_PACKAGE_NAME = "org.telegram.messenger";

    //animation
    public static final int res = android.R.id.content;
    public static final int enter = R.anim.slide_in_right;
    public static final int exit = R.anim.slide_out_left;
    public static final int popEnter = R.anim.slide_in_left_small;
    public static final int popExit = R.anim.slide_out_right;

    public static void setStatusBarTransparent(Activity activity) {
        View decorView = activity.getWindow().getDecorView();
        activity.getWindow().setStatusBarColor(Color.TRANSPARENT);
        activity.getWindow().setNavigationBarColor(Color.TRANSPARENT);

        int flags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            flags = flags | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            flags = flags | View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR;

        decorView.setSystemUiVisibility(flags);
    }

    public static void hideKeyboard(Context context, View view) {
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
            view.clearFocus();
        }
    }

    public static void showSoftKeyboard(Context context, View view) {
        if (view.requestFocus()) {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    public static void setIntent(Activity activity, String nameActivity) {
        Intent intent = new Intent();
        intent.setComponent(new ComponentName(activity.getPackageName(), nameActivity));
        activity.startActivity(intent, ActivityOptions.makeCustomAnimation(activity, R.anim.slide_in_right, R.anim.slide_out_left).toBundle());
    }

    public static void replaceFragment(final FragmentManager manager, final Fragment fragment, boolean isAdd,
                                       final boolean addBackStack) {
        try {
            FragmentTransaction fragmentTransaction = manager.beginTransaction();
            if (enter != 0 && exit != 0 && popEnter != 0 && popExit != 0)
                fragmentTransaction.setCustomAnimations(enter, exit, popEnter, popExit);
            if (addBackStack)
                fragmentTransaction.addToBackStack(fragment.getClass().getSimpleName());
            if (isAdd) {
                fragmentTransaction.add(res, fragment, fragment.getClass().getSimpleName());
            } else {
                fragmentTransaction.replace(res, fragment, fragment.getClass().getSimpleName());
            }
            fragmentTransaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void clearBackStack(FragmentManager manager) {
        int count = manager.getBackStackEntryCount();
        for (int i = 0; i < count; ++i) {
            manager.popBackStack();
        }
    }

    public static void setAnimExit(Activity activity) {
        activity.overridePendingTransition(R.anim.slide_in_left_small, R.anim.slide_out_right);
    }

    public static void showToast(Context context, String msg) {
        Toast toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    public static void delFileInFolder(Context context, String nameFolder, String nameFile) {
        File dir = new File(getStore(context) + "/" + nameFolder + "/" + nameFile);
        if (dir.isDirectory()) {
            File[] children = dir.listFiles();
            if (children != null)
                for (File file : children) {
                    if (file.isFile()) new File(dir, file.getName()).delete();
                    else if (file.isDirectory())
                        delFileInFolder(context, nameFolder, file.getName());
                }
        }
    }

    public static void writeToFileText(Context context, String data, String name) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput(name, Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String readFromFile(Context context, String name) {
        String data = "";
        try {
            InputStream inputStream = context.openFileInput(name);

            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ((receiveString = bufferedReader.readLine()) != null) {
                    stringBuilder.append("\n").append(receiveString);
                }
                inputStream.close();
                data = stringBuilder.toString();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }

    public static SpannableString underLine(String strUnder) {
        SpannableString underLine = new SpannableString(strUnder);
        underLine.setSpan(new UnderlineSpan(), 0, underLine.length(), 0);

        return underLine;
    }

    public static Typeface getTypeFace(String font, String style, Context context) {
        return Typeface.createFromAsset(context.getAssets(), "fonts/"
                + font.toLowerCase() + "/"
                + font.toLowerCase() + "_"
                + style.toLowerCase().trim().replaceAll(" ", "_") + ".ttf");
    }

    public static Drawable getDrawableTransparent(Context context) {
        return ContextCompat.getDrawable(context, R.drawable.sticker_transparent_background);
    }

    public static GradientDrawable.Orientation setDirection(int pos) {
        switch (pos) {
            case 0:
                return GradientDrawable.Orientation.TOP_BOTTOM;
            case 1:
                return GradientDrawable.Orientation.TL_BR;
            case 2:
                return GradientDrawable.Orientation.LEFT_RIGHT;
            case 3:
                return GradientDrawable.Orientation.BL_TR;
            case 4:
                return GradientDrawable.Orientation.BOTTOM_TOP;
            case 5:
                return GradientDrawable.Orientation.RIGHT_LEFT;
        }
        return GradientDrawable.Orientation.TOP_BOTTOM;
    }

    public static void saveImage(Context context, Bitmap bitmap, String namePic) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            if (bitmap != null) {

                String fileName = makeFilename(context, namePic);

                File outFile = new File(fileName);

                ContentValues values = new ContentValues();
                values.put(MediaStore.MediaColumns.DISPLAY_NAME, outFile.getName());
                values.put(MediaStore.MediaColumns.MIME_TYPE, getMIMEType(outFile.getPath()));
                values.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES);

                ContentResolver contentResolver = context.getApplicationContext().getContentResolver();

                Uri contentUri = MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL);

                Uri newUri = null;

                OutputStream output;
                try {
                    newUri = contentResolver.insert(contentUri, values);

                    output = contentResolver.openOutputStream(newUri);

                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, output);

                } catch (IOException e) {
                    contentResolver.delete(newUri, null, null);
                }
            }
        } else {
            if (bitmap != null) {
                try {
                    String fileName = makeFilename(context, namePic);

                    File outFile = new File(fileName);

                    ContentValues values = new ContentValues();
                    values.put(MediaStore.Images.Media.DISPLAY_NAME, outFile.getName());
                    values.put(MediaStore.Images.Media.MIME_TYPE, getMIMEType(outFile.getPath()));

                    values.put(MediaStore.MediaColumns.DATA, outFile.getPath());

                    ContentResolver contentResolver = context.getContentResolver();

                    Uri uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

                    OutputStream output;
                    try {
                        output = contentResolver.openOutputStream(uri);
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, output);

                    } catch (IOException e) {
                        if (uri != null) {
                            contentResolver.delete(uri, null, null);
                        }
                    }
                } catch (Exception e) {
                    Log.d("onBtnSavePng", e.toString()); // java.io.IOException: Operation not permitted
                }
            }
        }

    }

    public static String makeFolder(Context c, String nameFolder) {
        String path = getStore(c) + "/" + nameFolder;
        File f = new File(path);
        if (!f.exists()) f.mkdirs();
        return path;
    }

    public static String makeFilename(Context activity, String namePic) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
            return getStore(activity) + namePic + ".png";

        String subdir;
        String externalRootDir = Environment.getExternalStorageDirectory().getPath();
        if (!externalRootDir.endsWith("/")) externalRootDir += "/";

        subdir = "media/image/";
        String parentDir = externalRootDir + subdir;

        // Create the parent directory
        File parentDirFile = new File(parentDir);
        parentDirFile.mkdirs();

        // If we can't write to that special path, try just writing directly to the sdcard
        if (!parentDirFile.isDirectory()) parentDir = externalRootDir;

        // Turn the title into a filename
        StringBuilder filename = new StringBuilder();
        for (int i = 0; i < "remi-text-art".length(); i++) {
            if (Character.isLetterOrDigit("remi-text-art".charAt(i)))
                filename.append("remi-text-art".charAt(i));
        }

        // Try to make the filename unique
        String path = null;
        for (int i = 0; i < 100; i++) {
            String testPath;
            if (i > 0) testPath = parentDir + filename + i + ".png";
            else testPath = parentDir + filename + ".png";

            try {
                RandomAccessFile f = new RandomAccessFile(new File(testPath), "r");
                f.close();
            } catch (Exception e) {
                // Good, the file didn't exist
                path = testPath;
                break;
            }
        }
        return path;
    }

    public static String getStore(Context c) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            File f = c.getExternalFilesDir(null);
            if (f != null) return f.getAbsolutePath();
            else return "/storage/emulated/0/Android/data/" + c.getPackageName();
        } else
            return Environment.getExternalStorageDirectory() + "/Android/data/" + c.getPackageName();
    }

    public static String getMIMEType(String url) {
        String mType = null;
        String mExtension = MimeTypeMap.getFileExtensionFromUrl(url);
        if (mExtension != null) {
            mType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(mExtension);
        }
        return mType;
    }

    public static void rateApp(Context context) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("https://play.google.com/store/apps/details?id=" + context.getPackageName()));
        context.startActivity(intent);
    }

    public static void shareApp(Context context) {
        try {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Wallpaper Maker");
            String shareMessage = "Let me recommend you this application\nDownload now:\n\n";
            shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + context.getPackageName();
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
            context.startActivity(Intent.createChooser(shareIntent, "choose one"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void sendFeedback(Context context) {
        Intent selectorIntent = new Intent(Intent.ACTION_SENDTO);
        selectorIntent.setData(Uri.parse("mailto:"));

        final Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"tatcachilathuthach92@gmail.com"});
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Remi TextArt feedback");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "The email body...");
        emailIntent.setSelector(selectorIntent);
        try {
            context.startActivity(Intent.createChooser(emailIntent, "Send email..."));
        } catch (ActivityNotFoundException ex) {
            Toast.makeText(context, "No email clients installed.", Toast.LENGTH_SHORT).show();
        }
    }

    public static void moreApps(Context context) {
        final String devName = "REMI Studio";
        try {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://search?q=pub:" + devName)));
        } catch (ActivityNotFoundException anfe) {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/search?q=pub:" + devName)));
        }
    }

    public static void privacyApp(Context context) {
        final String linkPrivacy = "https://myweatherforecastandwidget.blogspot.com/";
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(linkPrivacy));
        context.startActivity(browserIntent);
    }

    public static void shareFile(Context context, Bitmap bitmap, String application) {
        Uri uri = saveImageExternal(context, bitmap);

        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.setPackage(application);
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        shareIntent.setDataAndType(uri, "image/*");

        context.startActivity(Intent.createChooser(shareIntent, "Remi TextArt"));
    }

    public static Uri saveImageExternal(Context context, Bitmap image) {
        //TODO - Should be processed in another thread
        Uri uri = null;
        try {
            File file = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "remi-textArt.png");
            FileOutputStream stream = new FileOutputStream(file);
            image.compress(Bitmap.CompressFormat.PNG, 100, stream);
            stream.close();
            uri = FileProvider.getUriForFile(context, "com.remi.datnt.remitextart", file);
        } catch (IOException e) {
            Log.d("TAG", "IOException while trying to write file for sharing: " + e.getMessage());
        }
        return uri;
    }
}
