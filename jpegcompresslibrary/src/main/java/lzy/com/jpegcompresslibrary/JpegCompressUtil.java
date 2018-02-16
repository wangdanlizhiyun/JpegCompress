package lzy.com.jpegcompresslibrary;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by lizhiyun on 2018/2/16.
 */

public class JpegCompressUtil {
    static {
        System.loadLibrary("native-lib");
    }

    public static native void compress(Bitmap bitmap, String path);

    public static void compress(File imageFile, String path) {
        long old = imageFile.length();
        compress(BitmapFactory.decodeFile(imageFile.getAbsolutePath()), path);
        if (mIsLog.get()) {
            float p = imageFile.length() * 1.0f / old;
            if (p >= 0.2){
                Log.e("JpegCompressUtil", "报警了啊，全尺寸压缩，压缩率未小于0.2" );
            }
        }
    }

    public static void compressAndCorverOldFile(File imageFile) {
        if (imageFile.canWrite() && !getImageType(imageFile.getAbsolutePath()).equals("gif")){
            compress(imageFile, imageFile.getAbsolutePath());
        }
    }

    static AtomicBoolean mIsLog = new AtomicBoolean(false);

    public static void log(Boolean isLog) {
        mIsLog.compareAndSet(!isLog, isLog);
    }

    private static String getImageType(String filePath) {
        FileInputStream is = null;
        String value = null;
        try {
            is = new FileInputStream(filePath);
            byte[] b = new byte[3];
            is.read(b, 0, b.length);
            value = bytesToHexString(b);
        } catch (Exception e) {
        } finally {
            if(null != is) {
                try {
                    is.close();
                } catch (IOException e) {}
            }
        }
        if("FFD8FF".equals(value)){
            return "jpg";
        } else if("FFD8FF".equals(value)){
            return "jpg";
        } else if("47494638".equals(value)){
            return "gif";
        } else if("424D".equals(value)){
            return "bmp";
        }
        return value;
    }
    private static String bytesToHexString(byte[] src){
        StringBuilder builder = new StringBuilder();
        if (src == null || src.length <= 0) {
            return null;
        }
        String hv;
        for (int i = 0; i < src.length; i++) {
            hv = Integer.toHexString(src[i] & 0xFF).toUpperCase();
            if (hv.length() < 2) {
                builder.append(0);
            }
            builder.append(hv);
        }
        return builder.toString();
    }
}
