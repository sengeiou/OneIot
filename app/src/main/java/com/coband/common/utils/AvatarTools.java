package com.coband.common.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.widget.ImageView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVUser;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by imco on 2/1/16.
 */
public class AvatarTools {

    private static final String PIC_PATH = Environment.getExternalStorageDirectory().toString() + "/WatchManager/";
    private ImageView mImageView;
    private String url;

    public AvatarTools(ImageView mImageView, String url) {
        this.mImageView = mImageView;
        this.url = url;
    }

    public void setAvatar() {
        Bitmap localAvatar = getLocalAvatar();
        if (localAvatar != null) {
            mImageView.setImageBitmap(localAvatar);
        } else {
            new AvatDownloadTask().execute(url);
        }
    }

    public static Bitmap getLocalAvatar() {
        File file = new File(PIC_PATH + AVUser.getCurrentUser().getUsername() + ".png");
        if (file.exists()) {
            return BitmapFactory.decodeFile(file.getPath());
        }

        return null;
    }

    class AvatDownloadTask extends AsyncTask<String, Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(String... params) {
            AVFile file = new AVFile("avat", url, null);
            try {
                byte[] data = file.getData();
                saveAvatar(data);
                Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                return bitmap;
            } catch (AVException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            mImageView.setImageBitmap(bitmap);
        }
    }

    private void saveAvatar(byte[] data) {
        File dir = new File(PIC_PATH);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        File file = new File(PIC_PATH + AVUser.getCurrentUser().getUsername() + ".png");
        if (file.exists()) {
            file.delete();
        }

        try {
            file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(data);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void deleteLocalExistAvatar() {
        File file = new File(PIC_PATH + AVUser.getCurrentUser().getUsername() + ".png");
        if (file.exists()) {
            file.delete();
        }
    }
}
