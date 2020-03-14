package com.qttx.kedouhulian

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.text.TextUtils
import android.view.View
import cn.jpush.android.api.JPushInterface
import com.qttx.kedouhulian.bean.FriendBean
import com.qttx.kedouhulian.bean.GroupInfoBean
import com.qttx.kedouhulian.bean.NetResultListBean
import com.qttx.kedouhulian.bean.UserInfoById
import com.qttx.kedouhulian.di.moduleList
import com.qttx.kedouhulian.ui.chat.extensionModule.ExtensionModule
import com.qttx.kedouhulian.net.Api
import com.qttx.kedouhulian.net.NetObserver
import com.qttx.kedouhulian.ui.chat.ChatApplyActivity
import com.qttx.kedouhulian.ui.chat.message.RedMessage
import com.qttx.kedouhulian.ui.chat.message.RedPackageItemProvider
import com.qttx.kedouhulian.ui.common.AMapPreviewActivity
import com.qttx.kedouhulian.ui.user.LoginActivity
import com.qttx.kedouhulian.ui.user.UserDataActivity
import com.qttx.kedouhulian.utils.RongGenerate
import com.qttx.kedouhulian.utils.clearUserData
import com.qttx.kedouhulian.utils.getToken
import com.stay.toolslibrary.base.BaseApplication
import com.stay.toolslibrary.net.NetManager
import com.stay.toolslibrary.net.NetMsgBean
import com.stay.toolslibrary.net.NetRequestConfig
import com.stay.toolslibrary.net.ViewLoadingStatus
import com.stay.toolslibrary.utils.ActivityManagerUtils
import com.stay.toolslibrary.utils.LogUtils
import com.stay.toolslibrary.utils.extension.bindSchedulerException
import io.rong.contactcard.ContactCardExtensionModule
import io.rong.contactcard.IContactCardClickListener
import io.rong.contactcard.IContactCardInfoProvider
import io.rong.imkit.DefaultExtensionModule
import io.rong.imkit.IExtensionModule
import io.rong.imkit.RongExtensionManager
import io.rong.imkit.RongIM
import io.rong.imkit.model.UIConversation
import io.rong.imlib.RongIMClient
import io.rong.imlib.model.Conversation
import io.rong.imlib.model.Group
import io.rong.imlib.model.Message
import io.rong.imlib.model.UserInfo
import io.rong.message.GroupNotificationMessage
import io.rong.message.LocationMessage
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.android.inject
import org.koin.dsl.module.Module
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * @author huangyr
 * @date 2019/1/31 0031
 */
class App : BaseApplication() {

    private val api: Api by inject()

    override fun onCreate() {
        super.onCreate()
        instance = this
//        setupLeakCanary()

        initJPush()
        if (applicationInfo.packageName.equals(getProcessName(applicationContext))) {

            RongIM.init(this)

            RongIM.registerMessageType(RedMessage::class.java)
            RongIM.registerMessageTemplate(RedPackageItemProvider())

            initRongImListener()
        }
    }

    override fun configInjectModule(): List<Module> {
        return moduleList
    }

    override fun requestConfig(): NetRequestConfig {
        val builder = OkHttpClient.Builder()
        //设置超时时间
        builder.connectTimeout(NetManager.DEFAULT_CONNECT_TIMEOUT, TimeUnit.SECONDS)
        builder.writeTimeout(NetManager.DEFAULT_WRITE_TIMEOUT, TimeUnit.SECONDS)
        builder.readTimeout(NetManager.DEFAULT_READ_TIMEOUT, TimeUnit.SECONDS)
        if (BuildConfig.DEBUG) {
            val logInterceptor = HttpLoggingInterceptor()
            logInterceptor.level = HttpLoggingInterceptor.Level.BODY
            //设置拦截器
            //设置拦截器
            builder.addInterceptor(logInterceptor)
        }
        builder.addInterceptor(object : Interceptor {
            override fun intercept(chain: Interceptor.Chain): Response {
                val request = chain.request()
                        .newBuilder()
                        .addHeader("token", getToken())
                        .build()
                return chain.proceed(request)
            }
        })

        return NetRequestConfig(successCode = listOf(1), okHttpBuilder = builder)
    }

    override fun handleNetException(netMsgBean: NetMsgBean): NetMsgBean {
        return netMsgBean
    }


    /**
     * 初始化极光推送
     */
    private fun initJPush() {
        JPushInterface.setDebugMode(BuildConfig.DEBUG)    // 设置开启日志,发布时请关闭日志
        JPushInterface.init(this)            // 初始化 JPush
    }

    private fun initRongImListener() {
        RongIM.setConnectionStatusListener { connectionStatus ->
            when (connectionStatus) {
                RongIMClient.ConnectionStatusListener.ConnectionStatus.CONNECTED//连接成功。
                -> {
                }
                RongIMClient.ConnectionStatusListener.ConnectionStatus.DISCONNECTED//断开连接。
                -> {
                }
                RongIMClient.ConnectionStatusListener.ConnectionStatus.CONNECTING//连接中。
                -> {
                }
                RongIMClient.ConnectionStatusListener.ConnectionStatus.NETWORK_UNAVAILABLE//网络不可用。
                -> {
                }
                RongIMClient.ConnectionStatusListener.ConnectionStatus.KICKED_OFFLINE_BY_OTHER_CLIENT//用户账户在其他设备登录，本机会被踢掉线
                -> loginOut(true)
            }
        }
        RongIM.setConversationClickListener(object : RongIM.ConversationClickListener {
            override fun onUserPortraitClick(
                    context: Context,
                    conversationType: Conversation.ConversationType,
                    userInfo: UserInfo?,
                    s: String
            ): Boolean {
                if (userInfo != null && !TextUtils.isEmpty(userInfo.userId)) {
                    UserDataActivity.show(context, userInfo.userId)
                    return true
                }
                return false
            }

            override fun onUserPortraitLongClick(
                    context: Context,
                    conversationType: Conversation.ConversationType,
                    userInfo: UserInfo,
                    s: String
            ): Boolean {
                return false
            }

            override fun onMessageClick(context: Context, view: View, message: Message): Boolean {
                if (message.content is LocationMessage)
                {
                    val intent=Intent(context, AMapPreviewActivity::class.java)
                    intent.putExtra("location",message.content)
                    startActivity(intent)
                    return true
                }
                return false
            }

            override fun onMessageLinkClick(context: Context, s: String, message: Message): Boolean {
                return false
            }

            override fun onMessageLongClick(context: Context, view: View, message: Message): Boolean {
                return false
            }
        })//设置会话界面操作的监听器。

        RongIM.setConversationListBehaviorListener(
                object : RongIM.ConversationListBehaviorListener {
                    override fun onConversationPortraitClick(p0: Context?, uiConversation: Conversation.ConversationType?, p2: String?): Boolean {
                        uiConversation?.let {
                            val type = uiConversation.getName()
                            if (type == "SYSTEM") {
                                RongIM.getInstance().clearMessagesUnreadStatus(uiConversation, p2, null)
                                val intent = Intent(p0, ChatApplyActivity::class.java)
                                p0?.startActivity(intent)
                                return true
                            }
                        }
                        return false
                    }

                    override fun onConversationPortraitLongClick(p0: Context?, p1: Conversation.ConversationType?, p2: String?): Boolean {
                        return false
                    }

                    override fun onConversationLongClick(p0: Context?, p1: View?, p2: UIConversation?): Boolean {
                        return false
                    }

                    override fun onConversationClick(p0: Context?, p1: View?, uiConversation: UIConversation?): Boolean {
                        uiConversation?.let {
                            val type = uiConversation.conversationTargetId
                            val conversationType = uiConversation.conversationType.getName()
                            val id = type.substring(type.lastIndexOf("_") + 1, type.length)
                            if (type == "SYSTEM") {
                                RongIM.getInstance().clearMessagesUnreadStatus(it.conversationType, id, null)
                                val intent = Intent(p0, ChatApplyActivity::class.java)
                                p0?.startActivity(intent)
                                return true
                            }
                        }

                        return false
                    }
                }
        )

        RongIM.setUserInfoProvider({

            if (it == "SYSTEM") {
                UserInfo(it, "系统消息", Uri.parse(RongGenerate.generateDefaultAvatar("系统消息", it)))
            } else {
                getUserInfo(it)
                null
            }

        }, true)

        RongIM.setGroupInfoProvider({
            getGroupInfo(it)
            null
        }, true)

        RongIM.getInstance().setGroupMembersProvider { s, iGroupMemberCallback ->

            api.group_info(s)
                    .bindSchedulerException()
                    .subscribe(object : NetObserver<GroupInfoBean>(ViewLoadingStatus.LOADING_DIALOG,
                            block = {
                                data?.let {
                                    val list = ArrayList<UserInfo>()
                                    it.list.forEach {
                                        list.add(UserInfo(it.uid, it.mark, Uri.parse(it.avatarUrl)))
                                    }
                                    iGroupMemberCallback.onGetGroupMembersResult(list)
                                }
                            }
                    ) {
                        override fun onError(e: Throwable) {
                            iGroupMemberCallback.onGetGroupMembersResult(null)
                            super.onError(e)
                        }
                    })
        }
//        RongIM.setGroupUserInfoProvider({ s, s1 -> null
//
//
//        }, true)
        setInputProvider()

        RongIM
                .setOnReceiveMessageListener { message, i ->
                    if (message.content is GroupNotificationMessage) {
                        val groupNotificationMessage = message.content as GroupNotificationMessage
                        val groupID = message.targetId
                        LogUtils.e("onReceived:" + groupNotificationMessage.message)
                        try {
                            if (groupNotificationMessage.operation == "Dismiss") run {
                                //解散群组
                                RongIM.getInstance().getConversation(Conversation.ConversationType.GROUP, groupID, object : RongIMClient.ResultCallback<Conversation>() {
                                    override fun onSuccess(conversation: Conversation) {
                                        RongIM.getInstance().clearMessages(Conversation.ConversationType.GROUP, groupID, object : RongIMClient.ResultCallback<Boolean>() {
                                            override fun onSuccess(aBoolean: Boolean?) {
                                                RongIM.getInstance().removeConversation(Conversation.ConversationType.GROUP, groupID, null)
                                            }

                                            override fun onError(e: RongIMClient.ErrorCode) {

                                            }
                                        })
                                    }

                                    override fun onError(e: RongIMClient.ErrorCode) {

                                    }
                                })
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }


                    return@setOnReceiveMessageListener false
                }

    }

    private fun setInputProvider() {


        val moduleList = RongExtensionManager.getInstance().extensionModules
        var defaultModule: IExtensionModule? = null
        if (moduleList != null) {
            for (module in moduleList) {
                if (module is DefaultExtensionModule) {
                    defaultModule = module
                    break
                }
            }
            if (defaultModule != null) {
                RongExtensionManager.getInstance().unregisterExtensionModule(defaultModule)
                RongExtensionManager.getInstance().registerExtensionModule(ExtensionModule(this))
            }
        }

        RongExtensionManager.getInstance().registerExtensionModule(ContactCardExtensionModule(object : IContactCardInfoProvider {
            override fun getContactAllInfoProvider(contactInfoCallback: IContactCardInfoProvider.IContactCardInfoCallback) {

                api.friends_list()
                        .bindSchedulerException()
                        .subscribe(object : NetObserver<NetResultListBean<FriendBean>>(ViewLoadingStatus.LOADING_DIALOG,
                                block = {
                                    data?.list?.let {
                                        val list = ArrayList<UserInfo>()
                                        it.forEach {
                                            list.add(UserInfo(it.uid, it.mark, Uri.parse(it.avatarUrl)))
                                        }
                                        contactInfoCallback.getContactCardInfoCallback(list)
                                    }

                                }
                        ) {
                            override fun onError(e: Throwable) {
                                contactInfoCallback.getContactCardInfoCallback(null)
                                super.onError(e)
                            }
                        })

            }

            override fun getContactAppointedInfoProvider(userId: String, name: String, portrait: String, contactInfoCallback: IContactCardInfoProvider.IContactCardInfoCallback) {

                api.getUserInfoById(userId)
                        .bindSchedulerException()
                        .subscribe(object : NetObserver<UserInfoById>(ViewLoadingStatus.LOADING_DIALOG,
                                block = {
                                    data?.let {
                                        val list = ArrayList<UserInfo>()
                                        list.add(UserInfo(it.id, it.mark, Uri.parse(it.avatarUrl)))
                                        contactInfoCallback.getContactCardInfoCallback(list)
                                    }
                                }
                        ) {
                            override fun onError(e: Throwable) {
                                contactInfoCallback.getContactCardInfoCallback(null)
                                super.onError(e)
                            }
                        })
            }

        }, IContactCardClickListener { view, content ->

            UserDataActivity.show(view.context, content.id)
            //            val intent = Intent(view.context, FriendDetailActivity::class.java)
//            intent.putExtra("user_id", content.id)
//            intent.putExtra("nick_name", content.name)
//            intent.putExtra("user_avatar", content.imgUrl)
//            view.context.startActivity(intent)
        }))
    }

    companion object {

        fun loginOut(kickedByOtherClient: Boolean) {
            val intent = Intent(getApplicationContext(), LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            intent.putExtra("logout", kickedByOtherClient)
            getApplicationContext().startActivity(intent)
            JPushInterface.deleteAlias(getApplicationContext(), 1)
            RongIM.getInstance().disconnect()
            RongIM.getInstance().logout()
            clearUserData()
            ActivityManagerUtils.getActivityManager().finishAllActivity()
        }

        fun getProcessName(cxt: Context): String? {
            val am = cxt.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            val runningApps = am.runningAppProcesses
            if (runningApps != null) {
                for (procInfo in runningApps) {
                    if (procInfo.pid == android.os.Process.myPid()) {
                        return procInfo.processName
                    }
                }
            }
            return null
        }

        private var instance: App? = null
        fun getInstance(): Context {
            return instance!!
        }
    }

//    private fun setupLeakCanary(): RefWatcher {
//
//        val excludedRefs = AndroidExcludedRefs.createAppDefaults()
//                .instanceField("android.view.inputmethod.InputMethodManager", "sInstance")
//                .instanceField("android.view.inputmethod.InputMethodManager", "mLastSrvView")
//                .instanceField("com.android.internal.policy.PhoneWindow$" + "DecorView", "mContext")
//                .instanceField("android.support.v7.widget.SearchView$" + "SearchAutoComplete", "mContext")
//                .build()
//
//        return if (LeakCanary.isInAnalyzerProcess(this)) {
//            RefWatcher.DISABLED
//        } else LeakCanary.refWatcher(this)
//                .listenerServiceClass(DisplayLeakService::class.java)
//                .excludedRefs(excludedRefs)
//                .buildAndInstall()
//    }

    private fun getUserInfo(id: String) {
        api.getUserInfoById(id)
                .bindSchedulerException()
                .subscribe(NetObserver {
                    data?.let {

                        RongIM.getInstance().refreshUserInfoCache(UserInfo(it.id, it.mark, Uri.parse(it.avatarUrl)))
                    }
                })
    }

    private fun getGroupInfo(id: String) {
        api.group_info(id)
                .bindSchedulerException()
                .subscribe(NetObserver {
                    data?.let {
                        RongIM.getInstance().refreshGroupInfoCache(Group(it.group_id, it.group_name, Uri.parse(it.group_avatar)))
                    }
                })
    }
}