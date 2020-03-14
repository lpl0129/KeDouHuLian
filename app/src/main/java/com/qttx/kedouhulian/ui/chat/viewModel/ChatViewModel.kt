package com.qttx.kedouhulian.ui.chat.viewModel


import com.qttx.kedouhulian.net.Api
import com.stay.toolslibrary.net.BaseViewModel
import android.arch.lifecycle.LifecycleOwner
import com.qttx.kedouhulian.bean.*
import com.qttx.kedouhulian.net.NetLiveData
import com.stay.toolslibrary.net.ViewErrorStatus
import com.stay.toolslibrary.net.ViewLoadingStatus
import com.stay.toolslibrary.utils.extension.bindSchedulerException
import com.stay.toolslibrary.utils.extension.bindSchedulerExceptionLife
import com.stay.toolslibrary.utils.showToast

class ChatViewModel constructor(val api: Api) : BaseViewModel() {


    var avatarLocal: String = ""


    val tokenLiveData = NetLiveData<String>()
    fun getToken(owner: LifecycleOwner) {
        api.get_upload_token()
                .bindSchedulerExceptionLife(owner)
                .subscribe(tokenLiveData)
    }


    /**
     * 搜索
     */
    val list = mutableListOf<SearchBean>()
    val searchLiveData = NetLiveData<List<SearchBean>>()
    fun searchUserOrGroup(owner: LifecycleOwner, id: String?) {
        if (id.isNullOrEmpty()) {
            "请输入搜索关键词".showToast()
        } else {
            api.friends_search_list(id)
                    .bindSchedulerExceptionLife(owner)
                    .subscribe(searchLiveData)
        }
    }

    /**
     * 申请入群
     */
    val applyAddGroupLiveData = NetLiveData<Any>()

    fun applyAddGroup(owner: LifecycleOwner, map: MutableMap<String, String>) {
        api.set_group_msg(map)
                .bindSchedulerExceptionLife(owner)
                .subscribe(applyAddGroupLiveData)
    }

    /**
     * 申请加好友
     */
    val applyAddFriendLiveData = NetLiveData<Int>()

    var userid: String? = null
    fun applyAddFriend(owner: LifecycleOwner, map: MutableMap<String, String>) {
        userid = map.get("uid")

        api.set_friends_msg(map)
                .bindSchedulerExceptionLife(owner)
                .subscribe(applyAddFriendLiveData)
    }

    /**
     * 好友列表
     */
    val friendList = mutableListOf<FriendBean>()
    val friendLiveData = NetLiveData<NetResultListBean<FriendBean>>(ViewLoadingStatus.LOADING_VIEW, ViewErrorStatus.ERROR_VIEW)
    fun getFirendList(owner: LifecycleOwner) {
        api.friends_list()
                .bindSchedulerExceptionLife(owner)
                .subscribe(friendLiveData)
    }

    /**
     * 加人入群
     */
    val joInToGroupLiveData = NetLiveData<Any>()

    fun joInToAddGroup(owner: LifecycleOwner, map: MutableMap<String, String>) {
        api.join_group(map)
                .bindSchedulerExceptionLife(owner)
                .subscribe(joInToGroupLiveData)
    }

    /**
     * 创建群
     */
    val createGroupLiveData = NetLiveData<PayBean>()

    fun createGroup(owner: LifecycleOwner, map: MutableMap<String, String>) {
        api.group_add(map)
                .bindSchedulerExceptionLife(owner)
                .subscribe(createGroupLiveData)
    }

    /**
     * 获取好友请求数量
     */
    val friendNumLiveData = NetLiveData<FrinedMsgNumBean>(ViewLoadingStatus.LOADING_NO)

    fun friendNumLiveData(owner: LifecycleOwner) {
        api.frined_msg_num()
                .bindSchedulerExceptionLife(owner)
                .subscribe(friendNumLiveData)
    }


    /**
     * 删除好友
     */
    val delFriendLiveData = NetLiveData<Any>()

    fun delFriend(owner: LifecycleOwner, id: String) {
        api.del_friend(id)
                .bindSchedulerExceptionLife(owner)
                .subscribe(delFriendLiveData)
    }

    /**
     * 设置备注
     */
    val setMarkLiveData = NetLiveData<Any>()

    fun setUserMark(owner: LifecycleOwner, id: String, mark: String) {
        api.set_friend_remarks(id, mark)
                .bindSchedulerExceptionLife(owner)
                .subscribe(setMarkLiveData)
    }

    /**
     * 获取群信息
     */
    val grioupInfoLiveData = NetLiveData<GroupInfoBean>()

    fun getGroupInfoById(own: LifecycleOwner, id: String, loadingStatus: ViewLoadingStatus = ViewLoadingStatus.LOADING_NO
                         , errorStatus: ViewErrorStatus = ViewErrorStatus.ERROR_TOAST
    ) {
        grioupInfoLiveData.loadingStatus = loadingStatus
        grioupInfoLiveData.errorStatus = errorStatus
        api.group_info(id)
                .bindSchedulerExceptionLife(own)
                .subscribe(grioupInfoLiveData)
    }

    val userinfoLiveData = NetLiveData<UserInfoById>(ViewLoadingStatus.LOADING_NO)

    fun getUserInfoById(own: LifecycleOwner, id: String) {
        api.getUserInfoById(id)
                .bindSchedulerExceptionLife(own)
                .subscribe(userinfoLiveData)
    }

    /**
     * 修改群昵称
     */
    val setGroupNameLiveData = NetLiveData<Any>()

    fun setGroupName(owner: LifecycleOwner, id: String, mark: String) {
        api.set_group_name(id, mark)
                .bindSchedulerExceptionLife(owner)
                .subscribe(setGroupNameLiveData)
    }

    /**
     * 修改群昵称
     */
    val setGroupAvatarLiveData = NetLiveData<Any>()

    fun setGroupAvatar(owner: LifecycleOwner, id: String, avatar: String) {
        api.set_group_avatar(id, avatar)
                .bindSchedulerExceptionLife(owner)
                .subscribe(setGroupAvatarLiveData)
    }


    /**
     * 退出群组
     */
    val quitGroupLiveData = NetLiveData<Any>()

    fun quitGroup(owner: LifecycleOwner, id: String) {
        api.quitGroup(id)
                .bindSchedulerExceptionLife(owner)
                .subscribe(quitGroupLiveData)
    }

    /**
     *  踢出群组 /api/group/out_group
     */
    val delMemberFromGroupLiveData = NetLiveData<Any>()

    fun delMemberFromGroup(owner: LifecycleOwner, id: String, uid: String) {
        api.out_group(id, uid)
                .bindSchedulerExceptionLife(owner)
                .subscribe(delMemberFromGroupLiveData)
    }

    /**
     * 获取悬赏群成员
     */
    val bountyMemberFromGroupLiveData = NetLiveData<NetResultListBean<BountyUserBean>>()

    val bountyList = mutableListOf<BountyUserBean>()

    fun bountyFromGroup(owner: LifecycleOwner, id: String) {
        api.bounty_order_list(id)
                .bindSchedulerExceptionLife(owner)
                .subscribe(bountyMemberFromGroupLiveData)
    }

    /**
     * 获取悬赏群成员
     */
    val bountyChangeLiveData = NetLiveData<Any>()
    var pos = 0

    val bountyjieguLiveData = NetLiveData<Any>()
    fun bountyChange(owner: LifecycleOwner, bounty_order_id: String, uid: String, position: Int, state: Int) {

        pos = position
        when (state) {
            0 -> {
                api.bounty_employ(bounty_order_id, uid)
                        .bindSchedulerExceptionLife(owner)
                        .subscribe(bountyjieguLiveData)
            }
            1 -> {
                api.bounty_fire(bounty_order_id, uid)
                        .bindSchedulerExceptionLife(owner)
                        .subscribe(bountyChangeLiveData)
            }
            2 -> {
                api.bounty_pay(bounty_order_id, uid)
                        .bindSchedulerExceptionLife(owner)
                        .subscribe(bountyChangeLiveData)
            }
        }

    }


}
