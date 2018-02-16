#include <jni.h>
#include <android/bitmap.h>
#include <android/log.h>
#include <malloc.h>
extern "C"{
#include "jpeglib.h"
}
#define LOGE(...)  __android_log_print(ANDROID_LOG_ERROR, "JPEG", __VA_ARGS__);
void writeJPEG(uint8_t *data, AndroidBitmapInfo info,const char *path) {
    struct jpeg_compress_struct jpeg_struct;
    jpeg_error_mgr err;
//    设置错误处理信息
    jpeg_struct.err = jpeg_std_error(&err);
//    给结构体分配内存
    jpeg_create_compress(&jpeg_struct);
    FILE *file = fopen(path, "wb");

    jpeg_stdio_dest(&jpeg_struct, file);

    jpeg_struct.image_width=info.width;
    jpeg_struct.image_height=info.height;
    //采取哈夫曼编码   在skia 源码中    jpeg_struct.arith_code=TRUE
    jpeg_struct.arith_code=FALSE;
    //优化编码
    jpeg_struct.optimize_coding=TRUE;
    jpeg_struct.in_color_space=JCS_RGB;

    jpeg_struct.input_components=3;
    jpeg_set_defaults(&jpeg_struct);
    jpeg_set_quality(&jpeg_struct,20, true);

    jpeg_start_compress(&jpeg_struct, true);

    JSAMPROW row_point[1];
    int rowRGB=info.width*3;
    while (jpeg_struct.next_scanline <info.height) {
        row_point[0]=&data[jpeg_struct.next_scanline*rowRGB];
        jpeg_write_scanlines(&jpeg_struct, row_point, 1);
    }
    jpeg_finish_compress(&jpeg_struct);
    jpeg_destroy_compress(&jpeg_struct);
    fclose(file);
}
extern "C"
JNIEXPORT void JNICALL
Java_lzy_com_jpegcompresslibrary_JpegCompressUtil_compress(JNIEnv *env, jobject instance,
                                                        jobject bitmap, jstring path_) {

    const char *path = env->GetStringUTFChars(path_, 0);
    uint8_t  *pixels;
    AndroidBitmapInfo info;
    AndroidBitmap_getInfo(env, bitmap, &info);
    AndroidBitmap_lockPixels(env, bitmap, (void **) &pixels);
    int w=info.width;
    int h=info.height;
    uint8_t *data = (uint8_t *) malloc(w * h * 3);
    uint8_t  *tmpData=data;
    int color;
    char r,g,b;
    for (int y = 0; y < h; y++) {
        for (int x = 0; x < w; x++) {
            color=(*(int *)(pixels));
            r = (color >> 16) & 0xFF;
            g = (color >> 8) & 0xFF;
            b = color & 0xFF;
            //给jpeg库使用的顺序是bgr
            //从前bgr是主流。opencv都是bgr
            *data = b;
            *(data + 1) = g;
            *(data + 2) = r;
            data += 3;
            pixels += 4;
        }
    }

    writeJPEG(tmpData, info, path);
    env->ReleaseStringUTFChars(path_, path);
}


