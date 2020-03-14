package com.qttx.kedouhulian.net

import com.qttx.kedouhulian.bean.*
import io.reactivex.Observable
import retrofit2.http.*

/**
 * @author huangyr
 * @date 2019/2/12 0012
 */

interface Api {
    /**
     * 获取微信的 access_token
     *
     * @param path
     * @return
     */
    @GET
    fun getWeiXinAccessToken(@Url path: String): Observable<WeiXinAccessTokenBean>

    /**
     * 获取微信的 用户信息
     *
     * @param path
     * @return
     */
    @GET
    fun getWeiXinUserInfo(@Url path: String): Observable<WeiXinUserInfoBean>

    /**
     * 获取微博的 用户信息
     *
     * @param path
     * @return
     */
    @GET
    fun getWeiBoUserInfo(@Url path: String): Observable<WeiBoUserInfoBean>

    /**
     * 检测手机号是否存在
     *
     * @return
     */
    @GET("validate/check_mobile_exist")
    fun check_mobile_exist(@Query("mobile") phone: String): Observable<NetResultBean<CheckPhone>>

    /**
     * 获取验证码
     *
     * @return
     */
    @GET("sms/send")
    fun smsSend(@Query("mobile") phone: String, @Query("event") event: String): Observable<NetResultBean<Any>>

    /**
     * 注册
     *
     * @return
     */
    @FormUrlEncoded
    @POST("user/register")
    fun register(@FieldMap map: Map<String, String>): Observable<NetResultBean<Userinfo>>

    /**
     * 绑定
     *
     * @return
     */
    @FormUrlEncoded
    @POST("user/bind")
    fun bindThirdAcount(@FieldMap map: Map<String, String>): Observable<NetResultBean<Userinfo>>


    /**
     * 登录
     *
     * @return
     */
    @GET("user/login")
    fun login(@Query("account") account: String, @Query("password") password: String): Observable<NetResultBean<Userinfo>>

    /**
     * 三方登录
     *
     * @return
     */
    @GET("user/third")
    fun thirdLogin(@Query("platform") platform: String, @Query("code") code: String): Observable<NetResultBean<Userinfo>>

    /**
     * 重置密码
     *
     * @return
     */
    @FormUrlEncoded
    @POST("user/resetpwd")
    fun resetpwd(@FieldMap map: Map<String, String>): Observable<NetResultBean<Any>>


    /**
     * 获取年龄阶段
     *
     * @return
     */
    @GET("user/age_group")
    fun age_group(): Observable<NetResultBean<List<AgeLevelBean>>>


    /**
     * 获取年龄阶段
     *
     * @return
     */
    @FormUrlEncoded
    @POST("user/perfect_info")
    fun perfect_info(@FieldMap map: Map<String, String>): Observable<NetResultBean<Any>>


    /**
     * 抢任务列表
     *
     * @return
     */
    @FormUrlEncoded
    @POST("task/taskList")
    fun taskList(@FieldMap map: Map<String, String>): Observable<NetResultBean<NetResultListBean<TaskBean>>>

    /**
     *发布任务类型
     * @return
     */
    @GET("task/taskTypeList")
    fun taskTypeList(): Observable<NetResultBean<TaskConfig>>


    /**
     * 获取上传QiNiuToken
     *
     * @return
     */
    @GET("common/get_upload_token")
    fun get_upload_token(): Observable<NetResultBean<String>>

    /**
     *发布任务
     *
     * @return
     */
    @FormUrlEncoded
    @POST("task/NewProjects")
    fun publishTask(@FieldMap map: Map<String, String>): Observable<NetResultBean<PayBean>>


    /**
     *  创建订单并发起支付请求 /api/recharge/submit
     *
     * @return
     */
    @GET("recharge/submit")
    fun recharge_submit(@QueryMap map: Map<String, String>): Observable<NetResultBean<PayResultBean>>

    /**
     *
     *获取任务回复列表
     * @return
     */
    @FormUrlEncoded
    @POST("task/replyList")
    fun replyList(@FieldMap map: Map<String, String>): Observable<NetResultBean<NetResultListBean<TaskMsgBean>>>

    /**
     *
     *获取任务详情
     * @return
     */
    @GET("task/taskDetail")
    fun taskDetail(@Query("task_id") id: String): Observable<NetResultBean<TaskDetailBean>>

    /**
     *
     *任务详情(已领取） /api/task/taskOrderDetail
     * @return
     */
    @GET("task/taskOrderDetail")
    fun taskOrderDetail(@Query("task_id") id: String,@Query("order_id") order_id: String): Observable<NetResultBean<TaskOrderDetailBean>>



    /**
     *
     *发布留言
     * @return
     */
    @GET("task/addreply")
    fun addreply(@Query("task_id") id: String, @Query("content") content: String): Observable<NetResultBean<Any>>
    /**
     *
     *发布留言
     * @return
     */
    @FormUrlEncoded
    @POST("task/evaluate")
    fun evaluate(@FieldMap map: Map<String, String>): Observable<NetResultBean<Any>>
    /**
     *
     *发布留言
     * @return
     */
    @FormUrlEncoded
    @POST("bounty/evaluate")
    fun bountyevaluate(@FieldMap map: Map<String, String>): Observable<NetResultBean<Any>>
    /**
     *
     *领取任务
     * @return
     */
    @GET("task/getTask")
    fun getTask(@QueryMap map: Map<String, String>): Observable<NetResultBean<Any>>

    /**
     *
     *提交任务
     * @return
     */
    @GET("task/submitTask")
    fun submitTask(@QueryMap map: Map<String, String>): Observable<NetResultBean<Any>>

    /**
     * 我发布的任务
     *
     * @return
     */
    @FormUrlEncoded
    @POST("task/myTaskList")
    fun myTaskList(@FieldMap map: Map<String, String>): Observable<NetResultBean<NetResultListBean<TaskBean>>>

    /**
     * 待审核的任务
     *
     * @return
     */
    @FormUrlEncoded
    @POST("task/orderInfoList")
    fun orderInfoList(@FieldMap map: Map<String, String>): Observable<NetResultBean<NetResultListBean<TaskBean>>>

    /**
     * 我抢单的任务
     *
     * @return
     */
    @FormUrlEncoded
    @POST("task/myOrderList")
    fun myOrderList(@FieldMap map: Map<String, String>): Observable<NetResultBean<NetResultListBean<MyGrapTaskBean>>>

    /**
     *审核任务
     * @return
     */
    @GET("task/examine")
    fun examine(@Query("order_id") id: String, @Query("status") status: Int, @Query("false_info") content: String): Observable<NetResultBean<Any>>

    /**
     * 删除任务
     * @return
     */
    @GET("task/delectTask")
    fun delectTask(@Query("task_id") id: String): Observable<NetResultBean<RedPacketDeleteBean>>

    /**
     * 申诉
     * @return
     */
    @GET("task/appeal")
    fun taskAppeal(@Query("order_id") id: String, @Query("mobile") mobile: String, @Query("appeal") appeal: String): Observable<NetResultBean<Any>>

    /**
     * 获取app配置信息
     * @return
     */
    @GET("common/getAppsetList")
    fun getAppsetList(): Observable<NetResultBean<SystemConfig>>

    /**
     *  /api/platformred/get_setting
     * @return
     */
    @GET("platformred/get_setting")
    fun platformred_get_setting(): Observable<NetResultBean<SystemConfig>>

    /**
     *发布广告
     *
     * @return
     */
    @FormUrlEncoded
    @POST("platformred/platformRedAdd")
    fun platformRedAdd(@FieldMap map: Map<String, String>): Observable<NetResultBean<PayBean>>

    /**
     *广告列表
     *
     * @return
     */
    @FormUrlEncoded
    @POST("platformred/platformred_list")
    fun platformred_list(@FieldMap map: Map<String, String>): Observable<NetResultBean<NetResultListBean<RedPacketBean>>>

    /**
     *红包领取列表
     *
     * @return
     */
    @FormUrlEncoded
    @POST("platformred/isGetList")
    fun isGetList(@FieldMap map: Map<String, String>): Observable<NetResultBean<NetResultListBean<RedPacketHistory>>>

    /**
     *广告详情
     *
     * @return
     */
    @FormUrlEncoded
    @POST("platformred/platformredInfo")
    fun platformredInfo(@Field("id") id: String, @Field("is_clear") is_clear: String): Observable<NetResultBean<RedPacketBean>>

    /**
     *领取红包
     *
     * @return
     */
    @FormUrlEncoded
    @POST("platformred/getPlatformred")
    fun getPlatformred(@FieldMap map: Map<String, String>): Observable<NetResultBean<RedPacketResultBean>>


    /**
     *设置广告状态
     *
     * @return
     */
    @FormUrlEncoded
    @POST("platformred/platformred_cancel")
    fun platformred_cancel(@Field("rid") id: String, @Field("status") status: String): Observable<NetResultBean<RedPacketDeleteBean>>

    /**
     *设置广告状态
     *
     * @return
     */
    @POST
    fun versions(@Url url: String): Observable<NetResultBean<Any>>

    /**
     * 我发布的广告
     *
     * @return
     */
    @FormUrlEncoded
    @POST("platformred/my_platformred_list")
    fun my_platformred_list(@FieldMap map: Map<String, String>): Observable<NetResultBean<NetResultListBean<RedPacketBean>>>

    /**
     * 我抢到的红包
     *
     * @return
     */
    @FormUrlEncoded
    @POST("platformred/my_get_platformred_list")
    fun my_get_platformred_list(@FieldMap map: Map<String, String>): Observable<NetResultBean<NetResultListBean<RedPacketBean>>>

    /**
     *获取悬赏参数 /api/bounty/get_setting
     * @return
     */
    @GET("bounty/get_setting")
    fun bounty_get_setting(): Observable<NetResultBean<HunterConfig>>

    /**
     *悬赏发布 /api/bounty/bounty_add
     *
     * @return
     */
    @FormUrlEncoded
    @POST("bounty/bounty_add")
    fun bounty_add(@FieldMap map: Map<String, String>): Observable<NetResultBean<PayBean>>

    /**
     *获取悬赏列表 /api/bounty/get_bounty_list
     *
     * @return
     */
    @FormUrlEncoded
    @POST("bounty/get_bounty_list")
    fun get_bounty_list(@FieldMap map: Map<String, String>): Observable<NetResultBean<NetResultListBean<BountyBean>>>


    /**
     *
     *获取赏金详情
     * bounty/get_bounty_info
     * @return
     */
    @FormUrlEncoded
    @POST("bounty/get_bounty_info")
    fun get_bounty_info(@FieldMap map: Map<String, String>): Observable<NetResultBean<BountyBean>>


    /**
     *
     *悬赏报名 /api/bounty/bounty_join
     * @return
     */
    @GET("bounty/bounty_join")
    fun bounty_join(@QueryMap map: Map<String, String>): Observable<NetResultBean<Any>>


    /**
     * 赏金任务撤销和暂停 /api/bounty/bounty_cancel
     *
     * @return
     */
    @FormUrlEncoded
    @POST("bounty/bounty_cancel")
    fun bounty_cancel(@Field("bounty_id") id: String, @Field("status") status: String): Observable<NetResultBean<RedPacketDeleteBean>>


    /**
     *
     *  悬赏留言列表 /api/bounty/bounty_reply_list
     * @return
     */
    @FormUrlEncoded
    @POST("bounty/bounty_reply_list")
    fun bounty_reply_list(@FieldMap map: Map<String, String>): Observable<NetResultBean<NetResultListBean<TaskMsgBean>>>

    /**
     *
     *悬赏发布留言 /api/bounty/bounty_reply_add
     * @return
     */
    @GET("bounty/bounty_reply_add")
    fun bounty_reply_add(@Query("bounty_id") id: String, @Query("content") content: String): Observable<NetResultBean<Any>>

    /**
     *我的悬赏-发布的任务 /api/bounty/my_bounty_list
     *
     * @return
     */
    @FormUrlEncoded
    @POST("bounty/my_bounty_list")
    fun my_bounty_list(@FieldMap map: Map<String, String>): Observable<NetResultBean<NetResultListBean<BountyBean>>>

    /**
     *我的悬赏-抢到的任务 /api/bounty/my_bounty_order_list
     *
     * @return
     */
    @FormUrlEncoded
    @POST("bounty/my_bounty_order_list")
    fun my_bounty_order_list(@FieldMap map: Map<String, String>): Observable<NetResultBean<NetResultListBean<BountyBean>>>


    /**
     * 申请结算 /api/bounty/apply_finish
     * @return
     */
    @FormUrlEncoded
    @POST("bounty/apply_finish")
    fun apply_finish(@Field("bounty_id") id: String): Observable<NetResultBean<Any>>

    /**
     * 悬赏-被解雇通知处理 /api/bounty/bounty_fire_confirm
     * @return
     */
    @FormUrlEncoded
    @POST("bounty/bounty_fire_confirm")
    fun bounty_fire_confirm(@Field("bounty_order_id") id: String, @Field("is_agree") is_agree: String): Observable<NetResultBean<Any>>

    /**
     * 池塘列表 /api/pool/poolList
     * @return
     */
    @GET("pool/poolList")
    fun poolList(@QueryMap map: Map<String, String>): Observable<NetResultBean<NetResultListBean<PondBean>>>


    /**
     *占领池塘 /api/pool/buyPool
     *
     * @return
     */
    @FormUrlEncoded
    @POST("pool/buyPool")
    fun buyPool(@FieldMap map: Map<String, String>): Observable<NetResultBean<PayBean>>

    /**
     *池塘收益 /api/pool/rateAcc
     * @return
     */
    @GET("pool/rateAcc")
    fun rateAcc(): Observable<NetResultBean<PondIncomeBean>>


    /**
     * 我的池塘 /api/pool/myPool
     *
     * @return
     */
    @GET("pool/myPool")
    fun myPool(@QueryMap map: Map<String, String>): Observable<NetResultBean<NetResultListBean<PondBean>>>

    /**
     * 池塘详情 /api/pool/poolInfo
     *
     * @return
     */
    @GET("pool/poolInfo")
    fun poolInfo(@Query("id") id: String): Observable<NetResultBean<PondDetailBean>>

    /**
     * 池塘交易记录 /api/pool/changeLog
     *
     * @return
     */
    @GET("pool/changeLog")
    fun changeLog(@QueryMap map: Map<String, String>): Observable<NetResultBean<NetResultListBean<PondHistoryBean>>>

    /**
     * 池塘定价转让/取消定价转让 /api/pool/poolUpdateType
     *
     * @return
     */
    @FormUrlEncoded
    @POST("pool/poolUpdateType")
    fun poolUpdateType(@FieldMap map: Map<String, String>): Observable<NetResultBean<Any>>

    /**
     * 蝌蚪币交易市场 /api/trade/trade_sell_list
     *
     * @return
     */
    @FormUrlEncoded
    @POST("trade/trade_sell_list")
    fun trade_sell_list(@FieldMap map: Map<String, String>): Observable<NetResultBean<NetResultListBean<TradeMarketBean>>>


    /**
     * 蝌蚪币交易市场
     *挂单详情 /api/trade/sell_info
     * @return
     */
    @FormUrlEncoded
    @POST("trade/sell_info")
    fun sell_info(@Field("id") id: String): Observable<NetResultBean<TradeMarketBean>>

    /**
     * 蝌蚪币交易市场
     *下单 /api/trade/trade_order
     * @return
     */
    @FormUrlEncoded
    @POST("trade/trade_order")
    fun trade_order(@FieldMap map: Map<String, String>): Observable<NetResultBean<PayBean>>

    /**
     * 交易出售发布 /api/trade/sell_add
     */
    @FormUrlEncoded
    @POST("trade/sell_add")
    fun sell_add(@FieldMap map: Map<String, String>): Observable<NetResultBean<PayBean>>

    /**
     * 获取交易的基本参数 /api/trade/get_setting
     */
    @POST("trade/get_setting")
    fun trade_get_setting(): Observable<NetResultBean<TradeConfigBean>>

    /**
     * 我的发布列表-挂售中 /api/trade/my_sell_list
     *
     * @return
     */
    @FormUrlEncoded
    @POST("trade/my_sell_list")
    fun my_sell_list(@FieldMap map: Map<String, String>): Observable<NetResultBean<NetResultListBean<TradeMarketBean>>>

    /**
     * 蝌蚪币交易市场
     *挂单详情 /api/trade/sell_info
     * @return
     */
    @FormUrlEncoded
    @POST("trade/sell_cancel")
    fun sell_cancel(@Field("id") id: String): Observable<NetResultBean<Any>>
    /**
     * 蝌蚪币交易市场
     *挂单详情 /api/trade/sell_info
     * @return
     */
    @FormUrlEncoded
    @POST("trade/sell_cancel")
    fun sell_order_cancel(@Field("id") id: String): Observable<NetResultBean<Any>>


    /**
     * 蝌蚪币交易市场
     *挂单详情 /api/trade/trade_order_cancel
     * @return
     */
    @FormUrlEncoded
    @POST("trade/trade_order_cancel")
    fun trade_order_cancel(@Field("order_id") id: String): Observable<NetResultBean<Any>>
//    撤销交易订单 /api/trade/

    /**
     * 蝌蚪币交易市场
     *挂单详情 /api/trade/update_price
     * @return
     */
    @FormUrlEncoded
    @POST("trade/update_price")
    fun update_price(@Field("id") id: String, @Field("unit") unit: String): Observable<NetResultBean<Any>>

    /**
     * 我的发布-已交易列表 /api/trade/my_sellorder_list
     *
     * @return
     */
    @FormUrlEncoded
    @POST("trade/my_sellorder_list")
    fun my_sellorder_list(@FieldMap map: Map<String, String>): Observable<NetResultBean<NetResultListBean<TradeMarketBean>>>

    /**
     * 我的购买-待付款和已购买 /api/trade/my_buy_list
     * @return
     */
    @FormUrlEncoded
    @POST("trade/my_buy_list")
    fun trade_my_buy_list(@FieldMap map: Map<String, String>): Observable<NetResultBean<NetResultListBean<TradeMyBuy>>>

    /**
     * 获取用户信息 /api/user/getUserInfo
     * @return
     */
    @FormUrlEncoded
    @POST("user/getUserInfo")
    fun getUserInfoById(@Field("uid") id: String): Observable<NetResultBean<UserInfoById>>

    /**
     * 获取用户信息 会员中心 /api/user/index
     * @return
     */
    @GET("user/index")
    fun getUserInfo(): Observable<NetResultBean<UserInfoById>>

    /**
     * 群组搜索 /api/group/group_serach_list
     * @return
     */
    @FormUrlEncoded
    @POST("group/group_serach_list")
    fun friends_search_list(@Field("keyword") id: String): Observable<NetResultBean<List<SearchBean>>>

    /**
     *添加入群申请 /api/group/set_group_msg
     * @return
     */
    @FormUrlEncoded
    @POST("group/set_group_msg")
    fun set_group_msg(@FieldMap map: Map<String, String>): Observable<NetResultBean<Any>>

    /**
     * 直接加入群接口 /api/group/join_group
     */
    @FormUrlEncoded
    @POST("group/join_group")
    fun join_group(@FieldMap map: Map<String, String>): Observable<NetResultBean<Any>>

    /**
     *  创建群聊 /api/group/group_add
     */
    @FormUrlEncoded
    @POST("group/group_add")
    fun group_add(@FieldMap map: Map<String, String>): Observable<NetResultBean<PayBean>>

    /**
     * 添加 /api/friends/set_friends_msg
     * @return
     */
    @FormUrlEncoded
    @POST("friends/set_friends_msg")
    fun set_friends_msg(@FieldMap map: Map<String, String>): Observable<NetResultBean<Int>>

    /**
     *好友列表 /api/friends/friends_list
     * @return
     */
    @POST("friends/friends_list")
    fun friends_list(): Observable<NetResultBean<NetResultListBean<FriendBean>>>


    /**
     *好友添加请求数量 /api/friends/frined_msg_num
     * @return
     */
    @POST("friends/frined_msg_num")
    fun frined_msg_num(): Observable<NetResultBean<FrinedMsgNumBean>>


    /**
     *审批入群列表 /api/group/group_msg_list
     * @return
     */
    @FormUrlEncoded
    @POST("group/group_msg_list")
    fun group_msg_list(@FieldMap map: Map<String, String>): Observable<NetResultBean<NetResultListBean<ChatApplyBean>>>

    /**
     *好友请求列表 /api/friends/friends_msg_list
     * @return
     */
    @FormUrlEncoded
    @POST("friends/friends_msg_list")
    fun friends_msg_list(@FieldMap map: Map<String, String>): Observable<NetResultBean<NetResultListBean<ChatApplyBean>>>


    /**同意好友或拒绝操作 /api/friends/friends_operate
     * @return
     */
    @FormUrlEncoded
    @POST("friends/friends_operate")
    fun friends_operate(@Field("mid") mid: String, @Field("status") status: String): Observable<NetResultBean<Any>>

    /**入群审批同意和操作拒绝 /api/group/group_msg_operate
     * @return
     */
    @FormUrlEncoded
    @POST("group/group_msg_operate")
    fun group_msg_operate(@Field("mid") mid: String, @Field("status") status: String): Observable<NetResultBean<Any>>

    /**
     * 删除好友 /api/friends/del_friend
     * @return
     */
    @FormUrlEncoded
    @POST("friends/del_friend")
    fun del_friend(@Field("friend_id") mid: String): Observable<NetResultBean<Any>>

    /**
     * 设置好友备注 /api/friends/set_friend_remarks
     * @return
     */
    @FormUrlEncoded
    @POST("friends/set_friend_remarks")
    fun set_friend_remarks(@Field("friend_id") mid: String, @Field("remark") remark: String): Observable<NetResultBean<Any>>


    /**
     * 群组信息 /api/group/group_info
     * @return
     */
    @FormUrlEncoded
    @POST("group/group_info")
    fun group_info(@Field("group_id") group_id: String): Observable<NetResultBean<GroupInfoBean>>


    /**
     * 修改群昵称 /api/group/set_group_name
     * @return
     */
    @FormUrlEncoded
    @POST("group/set_group_name")
    fun set_group_name(@Field("group_id") mid: String, @Field("group_name") remark: String): Observable<NetResultBean<Any>>

    /**
     *   设置群组头像 /api/group/set_group_avatar
     * @return
     */
    @FormUrlEncoded
    @POST("group/set_group_avatar")
    fun set_group_avatar(@Field("group_id") mid: String, @Field("avatar_url") avatar_url: String): Observable<NetResultBean<Any>>

    /**
     * 退出群组 /api/group/quitGroup
     * @return
     */
    @FormUrlEncoded
    @POST("group/quitGroup")
    fun quitGroup(@Field("group_id") mid: String): Observable<NetResultBean<Any>>

    /**
     * T 踢出群组 /api/group/out_group
     * @return
     */
    @FormUrlEncoded
    @POST("group/out_group")
    fun out_group(@Field("group_id") mid: String, @Field("uid") uid: String): Observable<NetResultBean<Any>>


    /**
     * 群聊列表 /api/group/group_list
     * @return
     */
    @FormUrlEncoded
    @POST("group/group_list")
    fun group_list(@FieldMap map: Map<String, String>): Observable<NetResultBean<NetResultListBean<GroupListBean>>>


    /**
     * 修改会员个人信息 /api/user/profile
     * @return
     */
    @GET("user/profile")
    fun editProfile(@QueryMap map: Map<String, String>): Observable<NetResultBean<Any>>

    /**
     *  我的二维码 /api/user/myQcode
     * @return
     */
    @GET("user/myQcode")
    fun myQcode(): Observable<NetResultBean<QrCodeBean>>

    /**
     * 我的钱包 /api/wallet/myAccount
     * @return
     */
    @GET("wallet/myAccount")
    fun myAccount(): Observable<NetResultBean<WalletBean>>


    /**
     * 账户信息 /api/wallet/account
     * @return
     */
    @GET("wallet/account")
    fun account(): Observable<NetResultBean<WalletAcountBean>>


    /**
     *账户绑定 /api/wallet/accountBinding
     * @return
     */
    @GET("wallet/accountBinding")
    fun accountBinding(@QueryMap map: Map<String, String>): Observable<NetResultBean<Any>>

    /**
     *账户修改 /api/wallet/accountAmend
     * @return
     */
    @GET("wallet/accountAmend")
    fun accountAmend(@QueryMap map: Map<String, String>): Observable<NetResultBean<Any>>

    /**
     *提现申请 /api/wallet/apply_withdraw
     * @return
     */
    @GET("wallet/apply_withdraw")
    fun apply_withdraw(@QueryMap map: Map<String, String>): Observable<NetResultBean<Any>>


    /**
     *  资金明细 /api/wallet/myAccountList
     * @return
     */
    @FormUrlEncoded
    @POST("wallet/myAccountList")
    fun myAccountList(@FieldMap map: Map<String, String>): Observable<NetResultBean<NetResultListBean<WalletListBean>>>
    /**
     * wallet/userScoreList
     * @return
     */
    @FormUrlEncoded
    @POST("wallet/userScoreList")
    fun userScoreList(@FieldMap map: Map<String, String>): Observable<NetResultBean<NetResultListBean<WalletListBean>>>

    /**
     *  我的合伙人-列表 /api/user/partnerList
     * @return
     */
    @FormUrlEncoded
    @POST("user/partnerList")
    fun partnerList(@FieldMap map: Map<String, String>): Observable<NetResultBean<NetResultListBean<CopartnerBean>>>


    /**
     *   我的合伙人-信息 /api/user/partner
     * @return
     */
    @GET("user/partner")
    fun partner(): Observable<NetResultBean<CopartnerBean>>

    /**
     *   分红页面详情 /api/share/share_red_info
     * @return
     */
    @POST("share/share_red_info")
    fun share_red_info(): Observable<NetResultBean<BonusBean>>

    /**
     *   分红接口 /api/share/share_red
     * @return
     */
    @POST("share/share_red")
    fun share_red(): Observable<NetResultBean<BonusBean>>

    /**
     *用户自定义消息列表 /api/user/user_custom_list
     * @return
     */
    @FormUrlEncoded
    @POST("user/user_custom_list")
    fun user_custom_list(@FieldMap map: Map<String, String>): Observable<NetResultBean<NetResultListBean<ChatCustomMsgBean>>>

    /**
     * 用户自定义消息添加 /api/user/user_custom_add
     * @return
     */
    @FormUrlEncoded
    @POST("user/user_custom_add")
    fun user_custom_add(@Field("content") id: String): Observable<NetResultBean<Any>>

    /**
     *用户自定义消息删除 /api/user/user_custom_del
     * @return
     */
    @FormUrlEncoded
    @POST("user/user_custom_del")
    fun user_custom_del(@Field("gc_id") id: String): Observable<NetResultBean<Any>>


    /**
     *POST 赏金任务-订单列表 /api/bounty/bounty_order_list
     * @return
     */
    @FormUrlEncoded
    @POST("bounty/bounty_order_list")
    fun bounty_order_list(@Field("group_id") id: String): Observable<NetResultBean<NetResultListBean<BountyUserBean>>>

    /**
     *POST 赏金任务-订单列表 /api/bounty/bounty_order_list
     * @return
     */
    @FormUrlEncoded
    @POST("bounty/bounty_order_list")
    fun bounty_order_list1(@FieldMap map: Map<String, String>): Observable<NetResultBean<NetResultListBean<BountyUserBean>>>

    /**
     *POST 悬赏雇佣 /api/bounty/bounty_employ
     * @return
     */
    @FormUrlEncoded
    @POST("bounty/bounty_employ")
    fun bounty_employ(@Field("bounty_id") id: String, @Field("employ_uid") uid: String): Observable<NetResultBean<Any>>

    /**
     *悬赏解雇 /api/bounty/bounty_fire
     * @return
     */
    @FormUrlEncoded
    @POST("bounty/bounty_fire")
    fun bounty_fire(@Field("bounty_id") id: String, @Field("fire_uid") uid: String): Observable<NetResultBean<Any>>

    /**
     *悬赏-结算 /api/bounty/bounty_pay
     * @return
     */
    @FormUrlEncoded
    @POST("bounty/bounty_pay")
    fun bounty_pay(@Field("bounty_order_id") id: String, @Field("pay_uid") uid: String): Observable<NetResultBean<Any>>

    /**
     * 消息开关列表 /api/common/getMsgSwitch
     * @return
     */
    @POST("common/getMsgSwitch")
    fun getMsgSwitch(): Observable<NetResultBean<SettingNotifyBean>>

    /**
     * 消息开关设置 /api/common/setMsgSwitch
     * @return
     */
    @FormUrlEncoded
    @POST("common/setMsgSwitch")
    fun setMsgSwitch(@FieldMap map: Map<String, String>): Observable<NetResultBean<Any>>

    /**
     * 蝌蚪币待领列表 /api/score/score_list
     * @return
     */
    @POST("score/score_list")
    fun score_list(): Observable<NetResultBean<NetResultListBean<ScoreBean>>>

    /**
     * 一键收取蝌蚪币 /api/score/getScore
     * @return
     */
    @POST("score/getScore")
    fun getScore(): Observable<NetResultBean<Any>>


    /**
     *站内信列表 /api/index/msgList
     * @return
     */
    @FormUrlEncoded
    @POST("index/msgList")
    fun msgList(@FieldMap map: Map<String, String>): Observable<NetResultBean<NetResultListBean<HomeMsgBean>>>


    /**
     *蝌蚪币收集攻略 /api/index/kedoubi
     * @return
     */
    @GET("index/kedoubi")
    fun kedoubi(): Observable<NetResultBean<GongLvBean>>

    /**
     * 投诉建议 /api/user/advice
     * @return
     */
    @GET("user/advice")
    fun advice(@Query("mobile") mobile: String, @Query("content") content: String): Observable<NetResultBean<Any>>
    /**
     * 投诉建议 /api/user/advice
     * @return
     */
    @GET("bounty/set_bounty_appeal")
    fun set_bounty_appeal(@QueryMap map:Map<String,String>): Observable<NetResultBean<Any>>

    /**
     *客户端更新 /api/version/check_version
     * @return
     */
    @FormUrlEncoded
    @POST("version/check_version")
    fun check_version(@Field("version") version: String, @Field("type") type: String): Observable<NetResultBean<VersionBean>>


    /**
     *蝌蚪币交易市场文字说明 /api/trade/walletUse
     * @return
     */
    @GET("trade/walletUse")
    fun walletUse(): Observable<NetResultBean<DescriptionBean>>
    /**
     * 我的钱包使用说明 /api/wallet/walletUse
     * @return
     */
    @GET("wallet/walletUse")
    fun wallet_walletUse(): Observable<NetResultBean<DescriptionBean>>

    @POST("user/get_customer")
    fun get_customer(): Observable<NetResultBean<String>>

    @FormUrlEncoded
    @POST("wallet/transter_money")
    fun transter_money(@Field("money") money: String, @Field("to_uid") to_uid: String): Observable<NetResultBean<Any>>

}