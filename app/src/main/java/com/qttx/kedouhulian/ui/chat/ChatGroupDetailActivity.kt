package com.qttx.kedouhulian.ui.chat

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.amap.api.mapcore.util.it
import com.qiniu.android.storage.UploadManager
import com.qttx.kedouhulian.R
import com.qttx.kedouhulian.bean.FriendBean
import com.qttx.kedouhulian.ui.chat.viewModel.ChatViewModel
import com.qttx.kedouhulian.utils.EventStatus
import com.qttx.kedouhulian.utils.EventUtils
import com.qttx.kedouhulian.utils.OperationRong
import com.qttx.kedouhulian.utils.RongGenerate.generateDefaultAvatar
import com.qttx.kedouhulian.utils.getUserId
import com.stay.toolslibrary.base.BaseActivity
import com.stay.toolslibrary.library.nestfulllistview.NestFullViewAdapter
import com.stay.toolslibrary.library.nestfulllistview.NestFullViewHolder
import com.stay.toolslibrary.library.picture.PictureHelper
import com.stay.toolslibrary.net.NetMsgBean
import com.stay.toolslibrary.net.ViewErrorStatus
import com.stay.toolslibrary.net.ViewLoadingStatus
import com.stay.toolslibrary.utils.DateUtils
import com.stay.toolslibrary.utils.extension.loadCircleAvatar
import com.stay.toolslibrary.utils.extension.loadCircleImage
import com.stay.toolslibrary.utils.extension.toArrayList
import com.stay.toolslibrary.widget.dialog.TipDialog
import io.rong.imkit.RongIM
import io.rong.imkit.model.GroupUserInfo
import io.rong.imlib.RongIMClient
import io.rong.imlib.model.Conversation
import kotlinx.android.synthetic.main.chat_activity_group_info.*
import kotlinx.android.synthetic.main.chat_activity_group_info.avatarArrowIv
import kotlinx.android.synthetic.main.chat_activity_group_info.avatarIv
import kotlinx.android.synthetic.main.chat_activity_group_info.modifyAvatarLl
import kotlinx.android.synthetic.main.user_activity_data.*
import org.koin.android.viewmodel.ext.android.viewModel
import pub.devrel.easypermissions.AfterPermissionGranted
import java.io.File
import java.util.*

/**
 * @author huangyr
 * @date 2019/5/15 0015
 */
class ChatGroupDetailActivity : BaseActivity() {

    private val viewModel: ChatViewModel by viewModel()
    private var groupid = ""
    private var isOpen = false

    private val list = mutableListOf<FriendBean>()

    private val listid = mutableListOf<String>()

    override fun getLayoutId(): Int {
        return R.layout.chat_activity_group_info
    }

    override fun processLogic(savedInstanceState: Bundle?) {
        setTopTitle("群聊信息")
        groupid = intent.getStringExtra("id")
        memberGridView.setAdapter(object : NestFullViewAdapter<FriendBean>(R.layout.chat_gird_item_group_info, list) {
            override fun onBind(pos: Int, t: FriendBean?, holder: NestFullViewHolder?) {
                val imageIv = holder!!.getView<ImageView>(R.id.imageIv)
                val textTv = holder.getView<TextView>(R.id.textTv)
                with(t!!)
                {
                    if (t.uid.isEmpty()) {
                        textTv.visibility = View.INVISIBLE
                        imageIv.setImageResource(R.drawable.yanqing_btn)
                        imageIv.setOnClickListener {
                            //邀请人入群
                            viewModel.grioupInfoLiveData
                                    .value?.data?.let {
                                val intent = Intent(this@ChatGroupDetailActivity, ChatInviteToGroupActivity::class.java)
                                intent.putExtra("owner", it.is_grouper == 1)
                                intent.putExtra("list", listid.toArrayList())
                                intent.putExtra("id", groupid)
                                startActivityForResult(intent, 200)
                            }

                        }
                    } else {
                        imageIv.loadCircleAvatar(avatarUrl)
                        textTv.visibility = View.VISIBLE
                        textTv.text = mark
                        imageIv.setOnClickListener {
                            //个人信息
                        }
                    }
                }
            }
        }
        )

        RongIM.getInstance().getConversationNotificationStatus(Conversation.ConversationType.GROUP, groupid, object : RongIMClient.ResultCallback<Conversation.ConversationNotificationStatus>() {
            override fun onSuccess(status: Conversation.ConversationNotificationStatus) {
                isOpen = status.value == 0
                setImageViewState()
            }

            override fun onError(errorCode: RongIMClient.ErrorCode) {}
        })
        viewModel.getGroupInfoById(this, groupid, ViewLoadingStatus.LOADING_VIEW, ViewErrorStatus.ERROR_VIEW)


    }

    override fun onRetryLoad(errorMsgBean: NetMsgBean) {
        super.onRetryLoad(errorMsgBean)
        viewModel.getGroupInfoById(this, groupid, ViewLoadingStatus.LOADING_VIEW, ViewErrorStatus.ERROR_VIEW)
    }

    override fun liveDataListener() {
        viewModel.grioupInfoLiveData.toObservable(this)
        {
            it.data?.let {

                list.clear()
                listid.clear()
                it.list.forEachIndexed { index, groupUserListBean ->
                    if (index < 49) {
                        list.add(groupUserListBean)
                    }
                    listid.add(groupUserListBean.uid)
                }
                if (listid.size > 49) {
                    allGroupTv.visibility = View.VISIBLE
                } else {
                    allGroupTv.visibility = View.GONE
                }
                if (it.group_type != 2) {
                    list.add(FriendBean())
                }

                memberGridView.updateUI()
                avatarIv.loadCircleImage(it.group_avatar)
//                if(it.group_avatar.isEmpty())
//                {
//                    it.group_avatar= generateDefaultAvatar(it.group_name, it.group_id)
//                }
//
//                if(it.group_avatar.isEmpty())
//                {
//                    it.group_avatar= generateDefaultAvatar(it.group_name, it.group_id)
//                }
                if (it.group_type==1)
                {
                    group_id_ll.visibility=View.VISIBLE
                }else
                {
                    group_id_ll.visibility=View.GONE
                }
                group_id_tv.text=it.group_no
                markTv.text = it.group_name
                exitGroupTv.visibility=View.VISIBLE
                if (it.is_grouper == 1) {

                    delGroupMemberLl.visibility = View.VISIBLE
                    if (it.group_type == 2) {
                        //悬赏群不准群主退出
                        set_remarks.isEnabled = false
                        nameArrowIv.visibility = View.GONE
                        applyGroupLl.visibility = View.GONE
                    } else {
                        applyGroupLl.visibility = View.VISIBLE
                        set_remarks.isEnabled = true
                        nameArrowIv.visibility = View.VISIBLE
                    }
                    modifyAvatarLl.isEnabled = true
                    avatarArrowIv.visibility = View.VISIBLE
                } else {

                    if(it.group_type==0)
                    {
                        //免费群
                        set_remarks.isEnabled = true
                        nameArrowIv.visibility = View.VISIBLE
                    }else
                    {
                        set_remarks.isEnabled = false
                        nameArrowIv.visibility = View.GONE
                    }

                    modifyAvatarLl.isEnabled = false
                    delGroupMemberLl.visibility = View.GONE
                    applyGroupLl.visibility = View.GONE
                    avatarArrowIv.visibility = View.GONE

                }

            }
        }
        viewModel.quitGroupLiveData
                .toObservable(this)
                {
                    RongIM
                            .getInstance()
                            .quitGroup(groupid, object : RongIMClient.OperationCallback() {
                                override fun onSuccess() {
                                    showToast("退出成功")
                                    setResult(600)
                                    EventUtils.postEvent(EventStatus.GROUP_NOTIFY)
                                    finish()
                                }

                                override fun onError(p0: RongIMClient.ErrorCode?) {
                                    showToast("退出失败")
                                }

                            }
                            )
                }

        viewModel.tokenLiveData.toObservable(this)
        {
            //上传
            it.data?.let {
                upLoadImage(viewModel.avatarLocal, it)
            }
        }

        viewModel.setGroupAvatarLiveData.toObservable(this)
        {
            setResult(400)
            showToast("修改成功")
        }
    }

    override fun onClick(v: View?) {
        super.onClick(v)
        when (v) {
            allGroupTv -> {
                //查看全部群成员
                viewModel.grioupInfoLiveData
                        .value
                        ?.data?.let {
                    val intent = Intent(this, ChatGroupMemberActivity::class.java)
                    intent.putExtra("id", it.group_id)
                    intent.putExtra("isOwner", it.is_grouper == 1)
                    startActivityForResult(intent, 300)
                }

            }
            modifyAvatarLl -> {
                //修改群头像
                val per = listOf(android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.READ_EXTERNAL_STORAGE)
                requsetPerMission(10001, per)
            }
            set_remarks -> {
                //修改群昵称
                val intent = Intent(this, ChatSetGroupNameActivity::class.java)
                intent.putExtra("id", groupid)
                intent.putExtra("name", markTv.text.toString())
                startActivityForResult(intent, 400)
            }
            notifyLl -> {
                isOpen = !isOpen
                setImageViewState()
                OperationRong.setConversationNotification(this, Conversation.ConversationType.GROUP, groupid, isOpen)
            }
            delGroupMemberLl -> {
                viewModel.grioupInfoLiveData
                        .value
                        ?.data?.let {
                    val intent = Intent(this, ChatGroupMemberActivity::class.java)
                    intent.putExtra("id", it.group_id)
                    intent.putExtra("isOwner", it.is_grouper == 1)
                    startActivityForResult(intent, 300)
                }
            }
            applyGroupLl -> {
                val intent = Intent(this, ChatApplyActivity::class.java)
                intent.putExtra("id", groupid)
                startActivityForResult(intent, 200)
            }
            clearChatTv -> {
                RongIM.getInstance().clearMessages(Conversation.ConversationType.GROUP,
                        groupid, object : RongIMClient.ResultCallback<Boolean>() {
                    override fun onSuccess(aBoolean: Boolean?) {
                        showToast("已清空聊天记录")
                    }

                    override fun onError(errorCode: RongIMClient.ErrorCode) {

                    }
                })
                RongIMClient.getInstance().cleanRemoteHistoryMessages(Conversation.ConversationType.GROUP,
                        groupid, System.currentTimeMillis(), null)
            }
            exitGroupTv -> {
                TipDialog.newInstance("是否退出该群组")
                        .setListener {
                            onSureClick {
                                viewModel.quitGroup(this@ChatGroupDetailActivity, groupid)
                            }
                        }.show(supportFragmentManager)
            }
        }
    }

    @AfterPermissionGranted(10001)
    private fun hasPermission() {
        getPictureHelper().setHasCrop(true)
                .setHasCamera(true)
                .setHasZip(true)
                .setMaxSize(1)
                .setHasZipDialog(true)
                .takePhoto()
    }

    override fun initViewClickListeners() {
        allGroupTv.setOnClickListener(this)
        modifyAvatarLl.setOnClickListener(this)
        set_remarks.setOnClickListener(this)
        notifyLl.setOnClickListener(this)
        delGroupMemberLl.setOnClickListener(this)
        applyGroupLl.setOnClickListener(this)
        clearChatTv.setOnClickListener(this)
        exitGroupTv.setOnClickListener(this)
    }

    fun setImageViewState() {
        if (isOpen) {
            notifyIv.setImageResource(R.drawable.dakai_btn)
        } else {
            notifyIv.setImageResource(R.drawable.butongzhi_btn)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == 400||resultCode==300) {
            setResult(400)
            viewModel.getGroupInfoById(this, groupid, ViewLoadingStatus.LOADING_VIEW, ViewErrorStatus.ERROR_VIEW)
        } else {
            getPictureHelper().getPhotoList(requestCode, resultCode, data, object : PictureHelper.PictureResultListener {
                override fun error() {
                }

                override fun complete(list: List<String>, idtag: String?) {
                    if (!list.isEmpty()) {
                        viewModel.avatarLocal = list[0]
                        avatarIv.loadCircleAvatar(viewModel.avatarLocal)
                        val token = viewModel.tokenLiveData.value?.data
                        if (token.isNullOrEmpty()) {
                            viewModel.getToken(this@ChatGroupDetailActivity)
                        } else {
                            upLoadImage(viewModel.avatarLocal, token)
                        }
                    }
                }
            })
        }
    }

    private var uploadManager: UploadManager? = null

    private fun upLoadImage(localUrl: String, token: String) {
        if (loadingDialog == null || loadingDialog?.dialog == null) {
            showLoadingDilog("正在上传...")
        }
        val maxfile = File(localUrl)
        if (!maxfile.exists() || !maxfile.isFile()) {
            showToast("文件不存在")
            return
        }
        // 文件后缀
        val fileSuffix = maxfile.getName().substring(maxfile.getName().lastIndexOf("."))
        val name = UUID.randomUUID().toString() + fileSuffix

        val objectKey = "uploads/img/" + DateUtils.millis2String(System.currentTimeMillis(), DateUtils.getShortFormat()) + "/" + getUserId() + "/" + name
        if (uploadManager == null) {
            uploadManager = UploadManager()
        }
        uploadManager?.put(maxfile, objectKey, token,
                { key, info, _ ->
                    if (info.isOK) {
                        viewModel.setGroupAvatar(this, groupid, key)
                    } else {
                        showToast("上传失败")
                    }
                }, null)


    }
}