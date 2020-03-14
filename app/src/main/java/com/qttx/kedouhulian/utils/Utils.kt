package com.qttx.kedouhulian.utils

import android.Manifest.permission_group.LOCATION
import android.arch.persistence.room.Database
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.text.TextUtils
import com.amap.api.mapcore.util.it
import com.google.gson.Gson
import com.qttx.kedouhulian.ui.user.LoginActivity
import com.stay.toolslibrary.utils.PreferenceUtil
import io.rong.imkit.RongIM
import io.rong.imlib.model.Conversation
import io.rong.imlib.model.UserInfo
import java.io.BufferedReader
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStreamReader

/**
 * @author huangyr
 * @date 2019/4/4 0004
 */
class Utils {
    companion object {
        val PHONE = "phone"

        val TOKEN = "token"

        val RongToken = "rongtoken"
        val UserID = "userid"
        fun getJson(mContext: Context, fileName: String): String {
            val stringBuilder = StringBuilder()
            BufferedReader(InputStreamReader(mContext.assets.open(fileName), "UTF-8")).run {
                var line: String? = ""
                do {
                    line = readLine()
                    if (line != null) {
                        stringBuilder.append(line)
                    } else {
                        close()
                        break
                    }
                } while (true)
                return stringBuilder.toString().trim()
            }
        }

        /**
         *
         * @param mContext
         * @return
         */
        /**
         * 毫秒转化
         */
        fun formatmillTime(ms: Long): String {

            val ss = 1000
            val mi = ss * 60
            val hh = mi * 60
            val dd = hh * 24

//            val day = ms / dd
            val hour = ms / hh
            val minute = (ms - hour * hh) / mi
            val second = (ms - hour * hh - minute * mi) / ss
            val milliSecond = ms - hour * hh - minute * mi - second * ss

            val strHour = if (hour < 10) "0$hour" else "" + hour// 小时
            val strMinute = if (minute < 10) "0$minute" else "" + minute// 分钟
            val strSecond = if (second < 10) "0$second" else "" + second// 秒


            return "$strHour:$strMinute:$strSecond"
        }
    }


}

/**
 * 获取用户名
 */
fun getUserPhone(): String {
    return PreferenceUtil.getString(Utils.PHONE)
}

/**
 * 保存用户名
 */
fun setUserPhone(phone: String?) {
    PreferenceUtil.putString(Utils.PHONE, phone)
}

/**
 * 获取用户名
 */
fun getUserId(): String {
    return PreferenceUtil.getString(Utils.UserID)
}

/**
 * 保存用户名
 */
fun setUserId(phone: String?) {
    PreferenceUtil.putString(Utils.UserID, phone)
}

/**
 * 获取TOKEN
 */
fun getToken(): String {
    return PreferenceUtil.getString(Utils.TOKEN)
}

/**
 * 保存TOKEN
 */
fun setToken(token: String?) {
    PreferenceUtil.putString(Utils.TOKEN, token)
}

/**
 * 获取TOKEN
 */
fun getRongToken(): String {
    return PreferenceUtil.getString(Utils.RongToken)
}

/**
 * 保存TOKEN
 */
fun setRongToken(token: String?) {
    PreferenceUtil.putString(Utils.RongToken, token)
}


fun checkLogin(): Boolean {
    val token = getUserId()
    return !TextUtils.isEmpty(token)
}

fun checkLogin(context: Context): Boolean {
    val token = getUserId()
    return if (TextUtils.isEmpty(token)) {
        val intent = Intent(context, LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
        false
    } else {
        true
    }
}

class MsgType {

    companion object {
        val REGISGTER = "register"
        val bind = "bind"

        val CHANGE_MOBILE = "changemobile"

        val RESETPWD = "resetpwd"
    }

}

fun saveUserLocation(locationBean: LocationHelper.LocationBean?) {
    if (locationBean == null) {
        PreferenceUtil.putString("userlocation", "")
    } else {
        val gson = Gson()
        val location = gson.toJson(locationBean)
        PreferenceUtil.putString("userlocation", location)
    }

}

fun getUserLocation(): LocationHelper.LocationBean? {
    val location = PreferenceUtil.getString("userlocation")
    if (TextUtils.isEmpty(location)) {
        return null
    } else {
        val gson = Gson()
        return gson.fromJson(location, LocationHelper.LocationBean::class.java)
    }
}

fun clearUserData() {
    setRongToken("")
    setToken("")
    setUserId("")
}

fun jumpToChat(context: Context, user_id: String, username: String, avatar: String="") {
    RongIM.getInstance().startConversation(context, Conversation.ConversationType.PRIVATE, user_id, username)
}