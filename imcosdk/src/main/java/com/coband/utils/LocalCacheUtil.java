package com.coband.utils;

import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by mai on 17-7-11.
 */

public class LocalCacheUtil {
    public static void save(InputStream inputStream, String localPath) {
        String path = Environment.getExternalStorageDirectory().getPath();
        File file = new File(localPath);
        FileOutputStream outputStream = null;

        try {
            outputStream = new FileOutputStream(file);
            int available = inputStream.available();
            byte[] bytes = new byte[1024];
            int len;
            while ((len = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, len);
            }
        } catch (IOException e) {
            if (file.exists()) {
                file.delete();
            }
        } finally {
            try {
                if (inputStream != null) inputStream.close();
                if (outputStream != null) outputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
