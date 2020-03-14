package com.stay.toolslibrary.utils;

import android.os.Environment;

import com.yalantis.ucrop.util.CropUtils;

import java.io.File;

public class PathUtils {
    public static String PATH_ROOT = Environment.getExternalStorageDirectory().getAbsolutePath() + "/sht/";
    public static String PATH_CACHE = PATH_ROOT + "cache/";
    public static String PATH_IMAGE = PATH_ROOT + "photo/";// 图片缓存目录
    public static String PATH_ERROR = PATH_ROOT + "error/";
    public static String PATH_CROP = PATH_ROOT + "cropimg/";//裁剪保存目录
    public static String PATH_FILE = PATH_ROOT + "file/";//音频保存目录
    public static String packname;
    private static  int IMAGE_ZIP_MAX_SIZE = 240;
    public static void setImageZipMaxSize(int imageZipMaxSize) {
        IMAGE_ZIP_MAX_SIZE = imageZipMaxSize;
    }

    /**
     * 初始化存储路径
     *
     * @param myPackname
     */
    public static void initPath(String myPackname) {
        packname = myPackname;
        PATH_ROOT = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + myPackname + "/";
        PATH_CACHE = PATH_ROOT + "cache/";
        PATH_IMAGE = PATH_ROOT + "photo/";
        PATH_ERROR = PATH_ROOT + "error/";
        PATH_CROP = PATH_ROOT + "cropimg/";
        PATH_FILE = PATH_ROOT + "file/";
        File path_root = new File(PATH_ROOT);
        if (!path_root.exists()) {
            path_root.mkdirs();
        }
        File path_cache = new File(PATH_CACHE);
        if (!path_cache.exists()) {
            path_cache.mkdir();
        }
        File path_image = new File(PATH_IMAGE);
        if (!path_image.exists()) {
            path_image.mkdir();
        }

        File path_error = new File(PATH_ERROR);
        if (!path_error.exists()) {
            path_error.mkdir();
        }
        File path_crop = new File(PATH_CROP);
        if (!path_crop.exists()) {
            path_crop.mkdir();
        }
        File path_audio = new File(PATH_FILE);
        if (!path_audio.exists()) {
            path_audio.mkdirs();
        }
        /**
         * 设置裁剪压缩图片的保存路径,
         * 默认压缩的限制大小
         */
        CropUtils.cropZipPath = PATH_CACHE;
        CropUtils.zipMaxSize = IMAGE_ZIP_MAX_SIZE;
    }


}
