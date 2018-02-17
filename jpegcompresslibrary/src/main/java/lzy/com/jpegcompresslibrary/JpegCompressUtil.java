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
        Bitmap bitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
        if (bitmap == null){
            return;
        }
        compress(bitmap, path);
        if (mIsLog.get()) {
            float p = imageFile.length() * 1.0f / old;
            if (p >= 0.3){
                Log.e("JpegCompressUtil", "报警了啊，全尺寸压缩，压缩率未小于0.3 "+p );
            }
        }
    }

    public static void compressAndCorverOldFile(File imageFile) {
        if (imageFile.canWrite() && !getImageType(imageFile.getAbsolutePath()).equals("GIF")){
            compress(imageFile, imageFile.getAbsolutePath());
        }
    }

    static AtomicBoolean mIsLog = new AtomicBoolean(false);

    public static void log(Boolean isLog) {
        mIsLog.compareAndSet(!isLog, isLog);
    }

    private static String getImageType(String filePath) {
        FileInputStream is = null;
        String fileType = null;
        try {
            is = new FileInputStream(filePath);
            byte[] buffer = new byte[8];
            is.read(buffer, 0, buffer.length);
            fileType = bytesToHexString(buffer);
            if(buffer[0] == 'G' && buffer[1] == 'I' && buffer[2] == 'F' && buffer[3] == '8' &&
                    (buffer[4] == '7' || buffer[4] == '9') && buffer[5] == 'a'){
                fileType ="GIF";
            }else if((buffer[0] == 0x42) && (buffer[1] == 0x4d)){
                fileType="BMP";
            }else if((buffer[0] == (byte)137 &&
                    buffer[1] == (byte)80 &&
                    buffer[2] == (byte)78 &&
                    buffer[3] == (byte)71 &&
                    buffer[4] == (byte)13 &&
                    buffer[5] == (byte)10 &&
                    buffer[6] == (byte)26 &&
                    buffer[7] == (byte)10)){
                fileType ="PNG";
            }else{
                fileType="JPG";
            }
        } catch (Exception e) {
        } finally {
            if(null != is) {
                try {
                    is.close();
                } catch (IOException e) {}
            }
        }
        return fileType;
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
