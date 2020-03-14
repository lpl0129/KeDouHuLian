package com.qttx.kedouhulian.ui.common;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import com.qttx.kedouhulian.R;
import com.qttx.kedouhulian.utils.Constants;
import com.qttx.kedouhulian.utils.EventStatus;
import com.qttx.kedouhulian.utils.EventUtils;
import com.stay.toolslibrary.utils.ToastUtils;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;


/**
 * @description 微信第三方登录，分享demo,更多移动开发内容请关注： http://blog.csdn.net/xiong_it
 * @charset UTF-8
 * @date 2015-9-9下午2:50:14
 */
/*
 * 微信登录，分享应用中必须有这个名字叫WXEntryActivity，并且必须在wxapi包名下，腾讯官方文档中有要求
 */
public class WXEntryActivity extends Activity implements IWXAPIEventHandler {
    private static final String TAG = "WXEntryActivity";
    private static final String WEIXIN_SCOPE = "snsapi_userinfo";// 用于请求用户信息的作用域
    private static final String WEIXIN_STATE = "login_state"; // 自定义
    private WXEntryActivity context;
    private IWXAPI api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        context = WXEntryActivity.this;
        api = WXAPIFactory.createWXAPI(this, Constants.WXAPP_ID, false);
        api.registerApp(Constants.WXAPP_ID);
        try {
            boolean success = api.handleIntent(getIntent(), this);
            if (!success) {
                finish();
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.i(TAG, "onNewIntent");
        setIntent(intent);
        try {
            boolean success = api.handleIntent(getIntent(), this);
            if (!success) {
                finish();
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    /**
     * 请求回调接口
     */
    @Override
    public void onReq(BaseReq req) {
        Log.i(TAG, "onReq");
    }

    /**
     * 请求响应回调接口
     */
    @Override
    public void onResp(BaseResp resp) {
        int result = 0;
        switch (resp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                result = R.string.errcode_success;
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                result = R.string.errcode_cancel;
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                result = R.string.errcode_deny;
                break;
            case BaseResp.ErrCode.ERR_UNSUPPORT:
                result = R.string.errcode_unsupported;
                break;
            default:
                ToastUtils.showShort(resp.errCode);
                result = R.string.errcode_unknown;
                break;
        }
        if (result == R.string.errcode_success) {
            try {
                SendAuth.Resp sendAuthResp = (SendAuth.Resp) resp;// 用于分享时不能强转
                if (sendAuthResp.state.equals(WEIXIN_STATE)) {
                    String code = sendAuthResp.code;
                    EventUtils.postEvent(EventStatus.WEIXIN_LOGIN, code);
                }
            } catch (Exception e) {
                EventUtils.postEvent(EventStatus.WEIXIN_SHARE, resp.errCode);
            }
        } else {
            ToastUtils.showShort(result);
        }
        finish();
    }


}
