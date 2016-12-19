package me.stefan.pickturelib.utils;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import me.stefan.pickturelib.domain.Pic;

/**
 * Created by Administrator on 2016/12/12.
 */
public class PhotoHelper {
    private Activity mActivity;
    private final String TAG = getClass().getSimpleName();

    private File mTempFile;
    private String TEMP_FILE_KEY = "TEMP_FILE_KEY";

    public PhotoHelper(Activity mActivity) {
        this.mActivity = mActivity;
    }

    public void goCamera(int requestCode) {
        if (mActivity == null) {
            Log.e(TAG, "mActivity cannot be null!");
            return;
        }
        // 利用系统自带的相机应用:拍照
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // create a file to save the image
        Uri fileUri = getOutputMediaFileUri();

        // 此处这句intent的值设置关系到后面的onActivityResult中会进入那个分支，即关系到data是否为null，如果此处指定，则后来的data为null
        // set the image file name
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

        mActivity.startActivityForResult(intent, requestCode);
    }

    private Uri getOutputMediaFileUri() {
        return Uri.fromFile(getOutputMediaFile());
    }

    /**
     * Create a File for saving an image or video
     */
    private File getOutputMediaFile() {
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

        File mediaStorageDir = null;
        try {
            // This location works best if you want the created images to be
            // shared
            // between applications and persist after your app has been
            // uninstalled.
            mediaStorageDir = new File(
                    Environment
                            .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                    "Picture");

            Log.d(TAG, "Successfully created mediaStorageDir: "
                    + mediaStorageDir);

        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "Error in Creating mediaStorageDir: "
                    + mediaStorageDir);
        }

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                // need sdcard permission：
                // <uses-permission
                // android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
                Log.d(TAG,
                        "failed to create directory, check if you have the WRITE_EXTERNAL_STORAGE permission");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
                .format(new Date());

        mTempFile = new File(mediaStorageDir.getPath() + File.separator
                + "IMG_" + timeStamp + ".jpg");
        Log.e(TAG,"create file in path:"+mTempFile.getAbsolutePath());
        return mTempFile;
    }

    public Pic onLoadPic() {
        if (mTempFile == null) {
            return null;
        }

        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri contentUri = Uri.fromFile(mTempFile);
        mediaScanIntent.setData(contentUri);
        mActivity.sendBroadcast(mediaScanIntent);

        Pic pic = new Pic(mTempFile.getAbsolutePath(), mTempFile.getName(), 0);
        return pic;
    }

    public void onSaveInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState != null && mTempFile != null) {
            savedInstanceState.putSerializable(TEMP_FILE_KEY, mTempFile);
        }
    }

    public void onRestoreInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState != null && savedInstanceState.containsKey(TEMP_FILE_KEY)) {
            mTempFile = (File) savedInstanceState.getSerializable(TEMP_FILE_KEY);
        }
    }


}
