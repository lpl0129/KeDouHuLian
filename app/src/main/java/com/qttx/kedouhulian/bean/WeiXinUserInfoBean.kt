package com.qttx.kedouhulian.bean

/**
 * Created by huangyr
 * on 2017/11/10.
 */

class WeiXinUserInfoBean {


    /**
     * openid : OPENID
     * nickname : NICKNAME
     * sex : 1
     * province : PROVINCE
     * city : CITY
     * country : COUNTRY
     * headimgurl : http://wx.qlogo.cn/mmopen/g3MonUZtNHkdmzicIlibx6iaFqAc56vxLSUfpb6n5WKSYVY0ChQKkiaJSgQ1dZuTOgvLLrhJbERQQ4eMsv84eavHiaiceqxibJxCfHe/0
     * privilege : ["PRIVILEGE1","PRIVILEGE2"]
     * unionid :  o6_bmasdasdsad6_2sgVt7hMZOPfL
     */

    var openid: String? = null
    var nickname: String? = null
    var sex: Int = 0
    var province: String? = null
    var city: String? = null
    var country: String? = null
    var headimgurl: String? = null
    var unionid: String? = null
    var privilege: List<String>? = null

    var accessToken: String? = null
}
