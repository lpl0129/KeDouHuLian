package com.stay.toolslibrary.net

/**
 * @author huangyr
 * @date 2019/1/21 0021
 */

enum class ViewStatus {
    LOADING_NO,
    LOADING_DIALOG,
    LOADING_VIEW,

    ERROR_NO,
    ERROR_TOAST,
    ERROR_VIEW,

    EMPTY,
    SUCCESS;

    public fun isLoadingStatus(): Boolean {
        return this == LOADING_NO || this == LOADING_DIALOG || this == LOADING_VIEW
    }

    public fun isErrorStatus(): Boolean {
        return this == ERROR_NO || this == ERROR_TOAST || this == ERROR_VIEW
    }

    public fun isSuccessStatus(): Boolean {
        return this == EMPTY || this == SUCCESS
    }
}

enum class ViewLoadingStatus(i: ViewStatus) {
    LOADING_NO(ViewStatus.LOADING_NO),
    LOADING_DIALOG(ViewStatus.LOADING_DIALOG),
    LOADING_VIEW(ViewStatus.LOADING_VIEW);

    var viewStatus: ViewStatus = i
}

enum class ViewErrorStatus(i: ViewStatus) {
    ERROR_NO(ViewStatus.ERROR_NO),
    ERROR_TOAST(ViewStatus.ERROR_TOAST),
    ERROR_VIEW(ViewStatus.ERROR_VIEW);

    var viewStatus: ViewStatus = i
}

enum class NetListListenerStatus {
    NORMAL,

    REQUEST_BEGIN,
    REQUEST_SUCCESS,
    REQUEST_FAILED,

}


data class NetListManager(var status: NetListListenerStatus = NetListListenerStatus.NORMAL, var netMsgBean: NetMsgBean,var isRefresh: Boolean, var hasMore: Boolean, var isEmpty: Boolean)



