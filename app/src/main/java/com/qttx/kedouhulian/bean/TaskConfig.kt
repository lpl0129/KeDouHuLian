package com.qttx.kedouhulian.bean

data class TaskConfig(
        var info: TaskConfigInfo = TaskConfigInfo(),
        var list: List<TaskConfigItem> = listOf(),
        var broadcastlist: List<Broadcast> = listOf()
)
data class TaskConfigInfo(
        var task_top: Double = 0.0,
        var taskservice_fee: Double =0.0
)
data class TaskConfigItem(
        var id: Int = 0,
        var sort: Int = 0,
        var image: String = "",
        var type_name: String = ""
)
data class Broadcast(
        var title: String = ""
)
