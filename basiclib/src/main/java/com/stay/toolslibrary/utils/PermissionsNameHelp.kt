package com.stay.toolslibrary.utils

import android.Manifest.permission

import java.util.ArrayList

/**
 * Created by huang on 2017/9/11.
 */

object PermissionsNameHelp {

    fun getPermissionsMulti(list: List<String>): String {
        var spiteChart = ""
        if (list.size <= 2) {
            spiteChart = "与"
        } else {
            spiteChart = "、"
        }
        val buffer = StringBuffer("请设置允许获取")
        for (nameitem in list) {
            val name = getPermissionSign(nameitem)
            if (!buffer.toString().contains(name)) {
                buffer.append(name)
                        .append(spiteChart)
            }
        }
        return buffer.deleteCharAt(buffer.length - 1).toString()
    }

    fun getPermissionsMulti(vararg strings: String): String {
        val lists = ArrayList<String>()
        for (s in strings) {
            lists.add(s)
        }
        return getPermissionsMulti(lists)
    }


    fun getPermissionSign(name: String): String {

        if (name == permission.CAMERA)
            return "相机权限"
        if (name == permission.READ_PHONE_STATE)
            return "读取手机状态权限"
        if (name == permission.RECORD_AUDIO)
            return "录音权限"
        if (name == permission.READ_EXTERNAL_STORAGE || name == permission.WRITE_EXTERNAL_STORAGE)
            return "SD卡权限"
        if (name == permission.READ_CALENDAR)
            return "日历权限"
        if (name == permission.ACCESS_FINE_LOCATION)
            return "定位权限"
        if (name == permission.BODY_SENSORS)
            return "传感器权限"
        return if (name == permission.SEND_SMS) "发短信权限" else name
    }

    fun getPermissionsStringM(vararg strings: String): String {
        val spiteChart = "_"
        val buffer = StringBuffer()
        for (nameitem in strings) {
            val name = getPermissionsStringS(nameitem)
            if (!buffer.toString().contains(name)) {
                buffer.append(name)
                        .append(spiteChart)
            }
        }
        return buffer.deleteCharAt(buffer.length - 1).toString()
    }

    fun getPermissionsStringS(name: String): String {

        if (name == permission.CAMERA)
            return "camera"
        if (name == permission.READ_PHONE_STATE)
            return "read_phone_state"
        if (name == permission.RECORD_AUDIO)
            return "reaord_audio"
        if (name == permission.READ_EXTERNAL_STORAGE)
            return "read_external_storage"
        if (name == permission.WRITE_EXTERNAL_STORAGE)
            return "write_external_storage"
        if (name == permission.READ_CALENDAR)
            return "read_calendra"
        if (name == permission.ACCESS_FINE_LOCATION)
            return "access_fine_location"
        if (name == permission.BODY_SENSORS)
            return "body_sensors"
        return if (name == permission.SEND_SMS) "send_sms" else name
    }


}
