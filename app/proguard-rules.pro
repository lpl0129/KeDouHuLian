#############################################
#
# 基本指令
#
#############################################
#代码混淆压缩比，在0~7之间，默认为5，一般不做修改
-optimizationpasses 5

#不忽略非公共库的类成员
-dontskipnonpubliclibraryclassmembers

#保留注解和内部类
-keepattributes *Annotation*,InnerClasses

#保留泛型
-keepattributes Signature

#抛出异常时保留代码行号
-renamesourcefileattribute SourceFile
-keepattributes SourceFile,LineNumberTable

#指定混淆采用的算法，后面的参数是一个过滤器，谷歌推荐的算法，一般不做更改
-optimizations !code/simplification/cast,!field/*,!class/merging/*

#############################################
#
# 需要保留的公共部分
#
#############################################
#保留四大组件和Application等
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Appliction
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class * extends android.view.View
-keepnames class android.arch.lifecycle.ViewModel
#保留support下的所有类及其内部类
-keep class android.support.** {*;}

#保留R下面的资源
-keep class **.R$* {*;}

#保留继承自View的自定义控件
-keep public class * extends android.view.View{
    *** get*();
    void set*(***);
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

#保留自定义控件
-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

#保留Parcelable序列化类
-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}

#保留Serializable序列化类
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

#保留带有回调函数的
-keepclassmembers class * {
    void *(**On*Event);
    void *(**On*Listener);
}

#############################################
#
# webView处理
#
#############################################
-keepclassmembers class fqcn.of.javascript.interface.for.webview {
   public *;
}
-keepclassmembers class * extends android.webkit.webViewClient {
    public void *(android.webkit.WebView, java.lang.String, android.graphics.Bitmap);
    public boolean *(android.webkit.WebView, java.lang.String);
}
-keepclassmembers class * extends android.webkit.webViewClient {
    public void *(android.webkit.webView, jav.lang.String);
}

#############################################
#
# 特殊处理
#
#############################################
#实体类
-keep class com.qttx.kedouhulian.bean.** {*;}

#js交互

#反射类

#############################################
#
# 第三方依赖库，包括jar模式和库引用模式
#
#############################################


#友盟统计
-keep class com.umeng.commonsdk.** {*;}


-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}
#Apache Http
-keep class org.apache.http.** { *; }
-dontwarn org.apache.http.**
-dontwarn android.net.**

#zxing
-keep class com.google.zxing.** { *; }
-dontwarn com.google.zxing.**

# Glide规则
-keep public class com.bumptech.glide.**{*;}
-dontwarn com.bumptech.glide.**
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public class * extends com.bumptech.glide.module.AppGlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}
#EventBus规则
-keepclassmembers class ** {
    @org.greenrobot.eventbus.Subscribe <methods>;
}
-keep enum org.greenrobot.eventbus.ThreadMode { *; }
# Only required if you use AsyncExecutor
-keepclassmembers class * extends org.greenrobot.eventbus.util.ThrowableFailureEvent {
    <init>(java.lang.Throwable);
}
#com.nineoldandroids:library规则
-dontwarn com.nineoldandroids.**
-keep class com.nineoldandroids.** { *; }

-dontwarn javax.annotation.**
-dontwarn javax.inject.**
# OkHttp3
#-dontwarn com.squareup.okhttp3.**
#-keep class com.squareup.okhttp3.** { *;}
-dontwarn okio.**
## Retrofit
#-dontwarn retrofit2.**
#-keep class retrofit2.** { *; }
#-keepattributes Signature
#-keepattributes Exceptions

# Retrofit
-dontnote retrofit2.Platform
-dontnote retrofit2.Platform$IOS$MainThreadExecutor
-dontwarn retrofit2.Platform$Java8
-keepattributes Signature
-keepattributes Exceptions

# rxjava
-dontwarn sun.misc.**
-keepclassmembers class rx.internal.util.unsafe.*ArrayQueue*Field* {
    long producerIndex;
    long consumerIndex;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode producerNode;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueConsumerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode consumerNode;
}
# rxlifecycle
-keep class com.trello.rxlifecycle2.**{*;}
# Gson
-dontwarn com.google.**
-keep class com.google.gson.** {*;}
-keep class com.google.protobuf.** {*;}
-keepattributes EnclosingMethod
#banner
-keep class com.youth.banner.** {
    *;
 }

#jsbridge
-keep  class com.github.lzyzsd.jsbridge.** { *; }
#uCrop
-dontwarn com.yalantis.ucrop**
-keep class com.yalantis.ucrop** { *; }
-keep interface com.yalantis.ucrop** { *; }

#toolslibrary
-keep class  com.stay.toolslibrary.net.** {*;}

-keep class net.bither.util.** {*;}
#photoview
-keep class com.github.chrisbanes.photoview.** { *; }
#butterknife
-keep class butterknife.** { *; }
-dontwarn butterknife.internal.**
-keep class **$$ViewBinder { *; }
-keepclasseswithmembernames class * {
    @butterknife.* <fields>;
}
-keepclasseswithmembernames class * {
    @butterknife.* <methods>;
}
-keep class com.autonavi.** { *; }
-keep class com.amap.** { *; }

#微博sdk
-keep class com.sina.weibo.sdk.** { *; }
#weixinJar
-keep class com.tencent.mm.opensdk.** { *; }
#qqJar
-keep class com.tencent.connect.** { *; }
-keep class com.tencent.open.** { *; }
-keep class com.tencent.tauth.** { *; }

 # com.wang.avi:library:2.1.3
-keep class com.wang.avi.** { *; }
-keep class com.wang.avi.indicators.** { *; }
 # easypermissions
-keep class pub.devrel.easypermissions.** { *; }
 # pickerview
-keep class com.contrarywind.** { *; }
-keep class com.bigkoo.pickerview.** { *; }
 # autosize
-keep class me.jessyan.autosize.** { *; }
-keep interface me.jessyan.autosize.** { *; }



#RongIm RongCloud SDK
-keep class io.rong.** {*;}
-keep class * implements io.rong.imlib.model.MessageContent {*;}
-dontwarn io.rong.push.**
-dontnote com.xiaomi.**
-dontnote com.google.android.gms.gcm.**
-dontnote io.rong.**

#RongIm VoIP
-keep class io.agora.rtc.** {*;}
-keep class com.qttx.kedouhulian.receiver.RongImNotificationReceiver {*;}

-keep class cn.rongcloud.rtc.core.**  { *; }
-keep class cn.rongcloud.rtc.engine.binstack.json.**  { *; }

#极光推送 Location
-dontoptimize
-dontpreverify
-dontwarn cn.jpush.**
-keep class cn.jpush.** { *; }
-keep class * extends cn.jpush.android.helpers.JPushMessageReceiver { *; }
-dontwarn cn.jiguang.**
-keep class cn.jiguang.** { *; }


#极光推送 alipay
-keep class com.alipay.android.app.IAlixPay{*;}
-keep class com.alipay.android.app.IAlixPay$Stub{*;}
-keep class com.alipay.android.app.IRemoteServiceCallback{*;}
-keep class com.alipay.android.app.IRemoteServiceCallback$Stub{*;}
-keep class com.alipay.sdk.app.PayTask{ public *;}
-keep class com.alipay.sdk.app.AuthTask{ public *;}
-keep class com.alipay.sdk.app.H5PayCallback {
    <fields>;
    <methods>;
}
-keep class com.alipay.android.phone.mrpc.core.** { *; }
-keep class com.alipay.apmobilesecuritysdk.** { *; }
-keep class com.alipay.mobile.framework.service.annotation.** { *; }
-keep class com.alipay.mobilesecuritysdk.face.** { *; }
-keep class com.alipay.tscenter.biz.rpc.** { *; }
-keep class org.json.alipay.** { *; }
-keep class com.alipay.tscenter.** { *; }
-keep class com.ta.utdid2.** { *;}
-keep class com.ut.device.** { *;}

-keepclasseswithmembernames class * {
    native <methods>;
}
# adding this in to preserve line numbers so that the stack traces
# can be remapped
#ROOM
-dontwarn android.arch.util.paging.CountedDataSource
-dontwarn android.arch.persistence.room.paging.LimitOffsetDataSource
#七牛
-keep class com.qiniu.**{*;}
-keep class com.qiniu.**{public <init>();}
-ignorewarnings

-dontwarn com.jeremyliao.liveeventbus.**
-keep class com.jeremyliao.liveeventbus.LiveEventBus { *; }
-keep class android.arch.lifecycle.ExternalLiveData { *; }

-keep public class cn.jzvd.JZMediaSystem {*; }
-keep public class cn.jzvd.demo.CustomMedia.CustomMedia {*; }
-keep public class cn.jzvd.demo.CustomMedia.JZMediaIjk {*; }
-keep public class cn.jzvd.demo.CustomMedia.JZMediaSystemAssertFolder {*; }

-keep class tv.danmaku.ijk.media.player.** {*; }
-dontwarn tv.danmaku.ijk.media.player.*
-keep interface tv.danmaku.ijk.media.player.** { *;}