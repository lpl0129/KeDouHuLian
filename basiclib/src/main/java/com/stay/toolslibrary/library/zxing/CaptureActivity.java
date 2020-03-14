package com.stay.toolslibrary.library.zxing;

import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.stay.basiclib.R;
import com.stay.toolslibrary.base.BaseActivity;
import com.stay.toolslibrary.library.picture.PictureHelper;
import com.stay.toolslibrary.utils.EmptyUtils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import cn.bingoogolapple.qrcode.core.QRCodeView;
import cn.bingoogolapple.qrcode.zxing.ZXingView;

/**
 * @author huangyr
 * @date 2018/10/24
 */
public class CaptureActivity extends BaseActivity implements QRCodeView.Delegate {

    private ZXingView mZXingView;
    private ImageView left_tv;
    private TextView title_tv;
    private Button open_imagebt;
    private Button open_light_bt;

    private boolean isOpenLight;

    @Override
    protected int getLayoutId() {
        return R.layout.common_activity_capture;


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        getPictureHelper().getPhotoList(requestCode, resultCode, data, new PictureHelper.PictureResultListener() {
            @Override
            public void complete(@NotNull List<String> list, @Nullable String idtag) {
                if (list.size() > 0) {
                    mZXingView.decodeQRCode(list.get(0));
                }
            }

            @Override
            public void error() {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        mZXingView.startCamera(); // 打开后置摄像头开始预览，但是并未开始识别
//        mZXingView.startCamera(Camera.CameraInfo.CAMERA_FACING_FRONT); // 打开前置摄像头开始预览，但是并未开始识别

        mZXingView.startSpotAndShowRect(); // 显示扫描框，并且延迟0.1秒后开始识别
    }

    @Override
    protected void onStop() {
        mZXingView.stopCamera(); // 关闭摄像头预览，并且隐藏扫描框
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        mZXingView.onDestroy(); // 销毁二维码扫描控件
        super.onDestroy();
    }

    @Override
    public void onScanQRCodeSuccess(String result) {
        if (result == null) {
            showToast("未识别到二维码");
            return;
        }
        vibrate();
        Intent resultIntent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString("result", result);
        resultIntent.putExtras(bundle);
        this.setResult(RESULT_OK, resultIntent);
        finish();
    }

    @Override
    public void onCameraAmbientBrightnessChanged(boolean isDark) {
        String tipText = mZXingView.getScanBoxView().getTipText();
        String ambientBrightnessTip = "\n环境过暗，请打开闪光灯";
        if (isDark) {
            if (!tipText.contains(ambientBrightnessTip)) {
                mZXingView.getScanBoxView().setTipText(tipText + ambientBrightnessTip);
            }
        } else {
            if (tipText.contains(ambientBrightnessTip)) {
                tipText = tipText.substring(0, tipText.indexOf(ambientBrightnessTip));
                mZXingView.getScanBoxView().setTipText(tipText);
            }
        }
    }

    @Override
    public void onScanQRCodeOpenCameraError() {
        showToast("打开相机出错");
    }


    /**
     * 震动
     */
    private void vibrate() {
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(200);
    }

    @Override
    protected void processLogic(@Nullable Bundle savedInstanceState) {
        mZXingView = findViewById(R.id.zxingview);
        left_tv = findViewById(R.id.left_tv);
        title_tv = findViewById(R.id.title_tv);
        open_imagebt = findViewById(R.id.open_imagebt);
        open_light_bt = findViewById(R.id.open_light_bt);
        mZXingView.setDelegate(this);

        left_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        open_imagebt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getPictureHelper().setHasCamera(false)
                        .setMaxSize(1)
                        .setHasZipDialog(false)
                        .setHasZip(false)
                        .setHasCrop(false)
                        .takePhoto();
            }
        });
        open_light_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isOpenLight = !isOpenLight;
                open_light_bt.setText(isOpenLight ? "关闭" : "开启");
                if (isOpenLight) {
                    mZXingView.openFlashlight();
                } else {
                    mZXingView.closeFlashlight();
                }
            }
        });
    }

    @Override
    protected void liveDataListener() {

    }
}
