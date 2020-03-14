package com.stay.toolslibrary.library.picture;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.TextView;

import com.stay.basiclib.R;
import com.stay.toolslibrary.base.BaseActivity;
import com.stay.toolslibrary.utils.EncryptUtils;
import com.stay.toolslibrary.utils.FileUtils;
import com.stay.toolslibrary.utils.PathUtils;
import com.stay.toolslibrary.utils.ToastUtils;
import com.stay.toolslibrary.widget.MyViewPager;
import com.stay.toolslibrary.widget.dialog.LoadingDialog;

import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import pub.devrel.easypermissions.AfterPermissionGranted;

public class ImageBrowserActivity extends BaseActivity implements
        OnPageChangeListener, OnClickListener {

    private MyViewPager mSvpPager;
    private TextView show_tv;
    private ImageBrowserAdapter mAdapter;
    private int mPosition;
    private int mTotal;
    private List<String> photoBeans;
    private Context context;
    private TextView save_tv;
    private String baseUrl;

    public static void showActivity(Context context, ArrayList<String> path, int postion) {
        showActivity(context, path.toArray(new String[]{}), postion);
    }

    public static void showActivity(Context context, List<String> path, int postion) {
        showActivity(context, path.toArray(new String[]{}), postion);
    }

    public static void showActivity(Context context, String[] path, int postion) {
        Intent intent = new Intent(context, ImageBrowserActivity.class);
        intent.putExtra("position", postion);
        intent.putExtra("data", path);
        context.startActivity(intent);
//        ((Activity) context).overridePendingTransition(0, R.anim.zoom_enter);
    }

    public static void showActivity(Context context, List<String> path, String baseUrl, int postion) {
        Intent intent = new Intent(context, ImageBrowserActivity.class);
        intent.putExtra("position", postion);
        intent.putExtra("data", path.toArray(new String[]{}));
        intent.putExtra("baseUrl", baseUrl);
        context.startActivity(intent);
//        ((Activity) context).overridePendingTransition(0, R.anim.zoom_enter);
    }
    @Override
    protected int getLayoutId() {
        return R.layout.activity_imagebrowser;
    }
    @Override
    protected void processLogic(@Nullable Bundle savedInstanceState) {
        mSvpPager = (MyViewPager) findViewById(R.id.imagebrowser_svp_pager);
        show_tv = (TextView) findViewById(R.id.show_tv);
        save_tv = (TextView) findViewById(R.id.save_tv);
        context = ImageBrowserActivity.this;

        setListener();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setSwipeBackEnable(false);
        baseUrl=getIntent().getStringExtra("baseUrl");
        mPosition = getIntent().getIntExtra("position", 0);
        // photoBeans =
        // (ArrayList<PhotosBean>)getIntent().getSerializableExtra("entity");
        path = getIntent().getStringArrayExtra("data");
        photoBeans = Arrays.asList(path);
        mTotal = photoBeans.size();
        if (mPosition > mTotal) {
            mPosition = mTotal - 1;
        }
        // if (mTotal > 1) {
        show_tv.setText((mPosition + 1) + "/" + mTotal);
        mAdapter = new ImageBrowserAdapter(photoBeans, ImageBrowserActivity.this);
        mAdapter.setBaseUrl(baseUrl);
        mSvpPager.setAdapter(mAdapter);
        mSvpPager.setCurrentItem(mPosition, false);
        // }
        save_tv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                requsetPerMission(10010, Collections.singletonList(Manifest.permission.WRITE_EXTERNAL_STORAGE));
            }
        });
    }

    @Override
    protected void liveDataListener() {

    }




    protected void setListener() {
        mSvpPager.setOnPageChangeListener(this);
    }

    private String[] path;

    @AfterPermissionGranted(10010)
    private void onhavePer() {
        List<String> list = new ArrayList<String>();
        list.add("保存图片");
        final String urlPath = path[mPosition].replaceAll(" ", "");
        String md5 = EncryptUtils.encryptMD5ToString(urlPath);
        final String fileName = md5 + FileUtils.getFileName(urlPath);
        final String savePath = PathUtils.PATH_IMAGE + fileName;
        final File file = new File(savePath);
        if (file.exists()) {
            ToastUtils.showShort("图片已保存至" + PathUtils.PATH_IMAGE + "下");
        } else {
            try {
                Observable.create(new ObservableOnSubscribe<Integer>() {
                    @Override
                    public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                        URL url = new URL(urlPath);
                        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                        connection.setRequestMethod("GET");
                        connection.setConnectTimeout(5000);
                        connection.setRequestProperty("Connection", "Keep-Alive");
                        connection.setRequestProperty("Charset", "UTF-8");
                        connection.setDoOutput(true);
                        connection.setDoInput(true);
                        connection.setUseCaches(false);
                        //打开连接
                        connection.connect();
                        //获取内容长度
                        int contentLength = connection.getContentLength();
                        InputStream inputStream = connection.getInputStream();
                        //输出流
                        FileOutputStream fileOutputStream = new FileOutputStream(file);
                        byte[] bytes = new byte[1024];
                        long totalReaded = 0;
                        int temp_Len;
                        while ((temp_Len = inputStream.read(bytes)) != -1) {
                            totalReaded += temp_Len;
                            int progressSize = (int) (totalReaded / (double) contentLength * 100);
                            e.onNext(progressSize);
                            fileOutputStream.write(bytes, 0, temp_Len);
                        }
                        if (fileOutputStream != null) {
                            fileOutputStream.close();
                        }
                        if (inputStream != null) {
                            inputStream.close();
                        }
                        if (connection != null) {
                            connection.disconnect();
                        }
                        e.onComplete();
                    }
                }).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .compose(this.<Integer>bindToLifecycle())
                        .doOnSubscribe(new Consumer<Disposable>() {
                            @Override
                            public void accept(Disposable disposable) throws Exception {
                                showLoadingDilog("下载中");
                            }
                        })
                        .subscribe(new Observer<Integer>() {
                            @Override
                            public void onSubscribe(Disposable d) {

                            }

                            @Override
                            public void onNext(Integer s) {
                                setLoadingDilogText("下载中" + s + "%");
                            }

                            @Override
                            public void onError(Throwable e) {
                                ToastUtils.showShort("图片保存失败!");
                            }

                            @Override
                            public void onComplete() {
                                dimissLoadingDialog();
                                try {
                                    MediaStore.Images.Media.insertImage(context.getContentResolver(),
                                            file.getAbsolutePath(), fileName, null);
                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                }
                                // 最后通知图库更新
                                context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + savePath)));
                                ToastUtils.showShort("图片已保存至" + PathUtils.PATH_IMAGE + "下");

                            }
                        });
            }catch (Exception e)
            {

            }

        }
    }

    @Override
    public void onPageScrollStateChanged(int arg0) {

    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {

    }

    @Override
    public void onPageSelected(int arg0) {
        mPosition = arg0;
        show_tv.setText((mPosition + 1) + "/" + mTotal);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, R.anim.zoom_exit);
    }

    @Override
    public void onClick(View v) {

    }


}
