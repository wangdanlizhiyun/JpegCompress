package lzy.com.jpegcompress;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import java.io.File;

import lzy.com.jpegcompresslibrary.JpegCompressUtil;
import lzy.com.life_library.entity.PermissionType;
import lzy.com.life_library.utils.LifeUtil;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LifeUtil.init(this);
        JpegCompressUtil.log(true);

        int g = 'G';
        int i = 'I';
        int f = 'F';
        Log.e("test","g="+g+"i="+i+"f="+f);

    }

    public void image(View view) {

        LifeUtil.permission(PermissionType.WRITE_EXTERNAL_STORAGE, PermissionType.READ_EXTERNAL_STORAGE).run(new Runnable() {
            @Override
            public void run() {
                File file = new File(Environment.getExternalStorageDirectory(), "down.png");
//              File out=new File(Environment.getExternalStorageDirectory(),"jpegeded"+".jpg0");
//              JpegCompressUtil.compress(file,out.getAbsolutePath());
                JpegCompressUtil.compressAndCorverOldFile(file);
            }
        });
    }
}
