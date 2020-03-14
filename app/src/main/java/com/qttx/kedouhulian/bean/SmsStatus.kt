package com.qttx.kedouhulian.bean

/**
 * @author huangyr
 * @date 2019/4/3 0003
 */
data class SmsBean(
    var status: SmsSendStatus = SmsSendStatus.Normal,
    val time: Long = 60
)

enum class SmsSendStatus {
    onSendCodeBegin,

    onTimeCountChange,

    onTimeComplete,
    onSendFailed,

    Normal

}