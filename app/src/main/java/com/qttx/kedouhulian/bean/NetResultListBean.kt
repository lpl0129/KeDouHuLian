package com.qttx.kedouhulian.bean

/**
 * Created by huangyr
 * on 2018/3/8.
 */

class NetResultListBean<T> {

    var list: MutableList<T>? = null
    var pageBean: PageBean? = null

    var score: String = ""
    var all_score: String = ""
    var start_time: String = ""
    var end_time: String = ""
}
