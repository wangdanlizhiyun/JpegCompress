# 该library用于压缩图片文件，暂不支持gif(压缩前会判断是否是gif)，压缩率基本在0.3以内，异步和权限自行处理
 
 
 
#使用
maven { url 'https://jitpack.io' }

 
 
 开启log
    ```
        JpegCompressUtil.log(true);
    ```
  
 压缩为新文件
 ```compress(File imageFile, String path)```
 
 压缩并覆盖原始文件
 ```compressAndCorverOldFile(File imageFile)```
 
 示例代码：
 ```LifeUtil.permission(PermissionType.WRITE_EXTERNAL_STORAGE, PermissionType.READ_EXTERNAL_STORAGE).run(new Runnable() {
                @Override
                public void run() {
                    File file = new File(Environment.getExternalStorageDirectory(), "danhao.jpg");
    //              File out=new File(Environment.getExternalStorageDirectory(),"jpegeded"+".jpg0");
    //              JpegCompressUtil.compress(file,out.getAbsolutePath());
                    JpegCompressUtil.compressAndCorverOldFile(file);
                }
            });
 ```