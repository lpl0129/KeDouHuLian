package com.qttx.kedouhulian.ui.reward

import android.os.Bundle
import android.text.Html
import android.view.View
import android.widget.TextView
import com.qttx.kedouhulian.R
import com.stay.toolslibrary.base.ModuleViewHolder
import com.stay.toolslibrary.library.nicedialog.BaseNiceDialog

/**
 * @author huangyr
 * @date 2019/5/11 0011
 */
class GuiZeDilog : BaseNiceDialog() {

    internal var url: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        url = arguments!!.getString("url")
    }

    override fun intLayoutId(): Int {
        return R.layout.guizel_dialog
    }


    override fun convertView(holder: ModuleViewHolder, dialog: BaseNiceDialog) {
        val textView: TextView = holder.findViewById(R.id.webview) as TextView
        holder.setOnClickListener(R.id.iv, View.OnClickListener {
            dismiss()
        }
        )
//        textView.text = Html.fromHtml(url)
        //        WebView webview=holder.getView(R.id.webview);
        //        //声明WebSettings子类
        //        WebSettings webSettings = webview.getSettings();
        ////如果访问的页面中要与Javascript交互，则webview必须设置支持Javascript
        //        webSettings.setJavaScriptEnabled(true);
        //// 若加载的 html 里有JS 在执行动画等操作，会造成资源浪费（CPU、电量）
        //// 在 onStop 和 onResume 里分别把 setJavaScriptEnabled() 给设置成 false 和 true 即可
        ////设置自适应屏幕，两者合用
        //        webSettings.setUseWideViewPort(true); //将图片调整到适合webview的大小
        //        webSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小
        //
        ////缩放操作
        //        webSettings.setSupportZoom(true); //支持缩放，默认为true。是下面那个的前提。
        //        webSettings.setBuiltInZoomControls(true); //设置内置的缩放控件。若为false，则该WebView不可缩放
        //        webSettings.setDisplayZoomControls(false); //隐藏原生的缩放控件
        //
        ////其他细节操作
        //        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK); //关闭webview中缓存
        //        webSettings.setAllowFileAccess(true); //设置可以访问文件
        //        webSettings.setJavaScriptCanOpenWindowsAutomatically(true); //支持通过JS打开新窗口
        //        webSettings.setLoadsImagesAutomatically(true); //支持自动加载图片
        //        webSettings.setDefaultTextEncodingName("utf-8");//设置编码格式
        //        webview.setWebChromeClient(new WebChromeClient() {
        //                                       @Override
        //                                       public void onReceivedTitle(WebView view, String str) {
        //                                           super.onReceivedTitle(view, str);
        //                                       }
        //                                   }
        //
        //        );
        //        webview.setWebViewClient(new WebViewClient());
        //
        //        webview.loadDataWithBaseURL(null, url, "text/html", "utf-8", null);
    }
}
