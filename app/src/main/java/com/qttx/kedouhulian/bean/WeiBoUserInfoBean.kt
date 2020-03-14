package com.qttx.kedouhulian.bean

/**
 * Created by huangyr
 * on 2017/12/30.
 */

class WeiBoUserInfoBean {

    /**
     * id : 1404376560
     * screen_name : zaku
     * name : zaku
     * province : 11
     * city : 5
     * location : 北京 朝阳区
     * description : 人生五十年，乃如梦如幻；有生斯有死，壮士复何憾。
     * url : http://blog.sina.com.cn/zaku
     * profile_image_url : http://tp1.sinaimg.cn/1404376560/50/0/1
     * domain : zaku
     * gender : m
     * followers_count : 1204
     * friends_count : 447
     * statuses_count : 2908
     * favourites_count : 0
     * created_at : Fri Aug 28 00:00:00 +0800 2009
     * following : false
     * allow_all_act_msg : false
     * geo_enabled : true
     * verified : false
     * status : {"created_at":"Tue May 24 18:04:53 +0800 2011","id":11142488790,"text":"我的相机到了。","source":"","favorited":false,"truncated":false,"in_reply_to_status_id":"","in_reply_to_user_id":"","in_reply_to_screen_name":"","geo":null,"mid":"5610221544300749636","annotations":[],"reposts_count":5,"comments_count":8}
     * allow_all_comment : true
     * avatar_large : http://tp1.sinaimg.cn/1404376560/180/0/1
     * verified_reason :
     * follow_me : false
     * online_status : 0
     * bi_followers_count : 215
     */

    var id: String? = null
    var screen_name: String? = null
    var name: String? = null
    var province: String? = null
    var city: String? = null
    var location: String? = null
    var description: String? = null
    var url: String? = null
    var profile_image_url: String? = null
    var domain: String? = null
    var gender: String? = null
    var followers_count: Int = 0
    var friends_count: Int = 0
    var statuses_count: Int = 0
    var favourites_count: Int = 0
    var created_at: String? = null
    var isFollowing: Boolean = false
    var isAllow_all_act_msg: Boolean = false
    var isGeo_enabled: Boolean = false
    var isVerified: Boolean = false
    var status: StatusBean? = null
    var isAllow_all_comment: Boolean = false
    var avatar_large: String? = null
    var verified_reason: String? = null
    var isFollow_me: Boolean = false
    var online_status: Int = 0
    var bi_followers_count: Int = 0

    class StatusBean {
        /**
         * created_at : Tue May 24 18:04:53 +0800 2011
         * id : 11142488790
         * text : 我的相机到了。
         * source :
         * favorited : false
         * truncated : false
         * in_reply_to_status_id :
         * in_reply_to_user_id :
         * in_reply_to_screen_name :
         * geo : null
         * mid : 5610221544300749636
         * annotations : []
         * reposts_count : 5
         * comments_count : 8
         */

        var created_at: String? = null
        var id: Long = 0
        var text: String? = null
        var source: String? = null
        var isFavorited: Boolean = false
        var isTruncated: Boolean = false
        var in_reply_to_status_id: String? = null
        var in_reply_to_user_id: String? = null
        var in_reply_to_screen_name: String? = null
        var geo: Any? = null
        var mid: String? = null
        var reposts_count: Int = 0
        var comments_count: Int = 0
        var annotations: List<*>? = null
    }
}
