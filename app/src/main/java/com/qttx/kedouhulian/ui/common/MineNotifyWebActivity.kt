package com.qttx.kedouhulian.ui.common

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.webkit.WebSettings
import android.webkit.WebView

import com.qttx.kedouhulian.R
import com.stay.toolslibrary.base.BaseActivity
import kotlinx.android.synthetic.main.mine_notify_web_activity.*

/**
 * @author huangyr
 * @date 2019/2/25 0025
 */
class MineNotifyWebActivity : BaseActivity() {



    override fun getLayoutId(): Int {
        return R.layout.mine_notify_web_activity
    }


    public override fun onPause() {
        super.onPause()
        webview!!.onPause()
    }

    public override fun onResume() {
        super.onResume()
        webview!!.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()
        webview!!.destroy()
    }

    override fun processLogic(savedInstanceState: Bundle?) {
//        setTopTitle("")

        val title = intent.getStringExtra("title")
        if (!TextUtils.isEmpty(title)) {
            setTopTitle(title)
        }
        val content = intent.getStringExtra("content")
        //声明WebSettings子类
        val webSettings = webview!!.settings
        //如果访问的页面中要与Javascript交互，则webview必须设置支持Javascript
        webSettings.javaScriptEnabled = true
        // 若加载的 html 里有JS 在执行动画等操作，会造成资源浪费（CPU、电量）
        // 在 onStop 和 onResume 里分别把 setJavaScriptEnabled() 给设置成 false 和 true 即可
        //设置自适应屏幕，两者合用
        webSettings.useWideViewPort = true //将图片调整到适合webview的大小
        webSettings.loadWithOverviewMode = true // 缩放至屏幕的大小

        //缩放操作
        webSettings.setSupportZoom(true) //支持缩放，默认为true。是下面那个的前提。
        webSettings.builtInZoomControls = true //设置内置的缩放控件。若为false，则该WebView不可缩放
        webSettings.displayZoomControls = false //隐藏原生的缩放控件

        //其他细节操作
        webSettings.cacheMode = WebSettings.LOAD_CACHE_ELSE_NETWORK //关闭webview中缓存
        webSettings.allowFileAccess = true //设置可以访问文件
        webSettings.javaScriptCanOpenWindowsAutomatically = true //支持通过JS打开新窗口
        webSettings.loadsImagesAutomatically = true //支持自动加载图片
        webSettings.defaultTextEncodingName = "utf-8"//设置编码格式
        webview.loadUrl(content)
    }

    override fun liveDataListener() {

    }

    companion object {
        fun show(context: Context, title: String, content: String) {
            val intent = Intent(context, MineNotifyWebActivity::class.java)
            intent.putExtra("content", content)
            intent.putExtra("title", title)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        }
    }
}
