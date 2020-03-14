package com.qttx.kedouhulian.bean

data class HunterConfig(
        var bounty_score_fee: String = "",
        var bounty_user_fee: String = "",
        var catelist: List<Catelist> = listOf(),
        var top_price: Double = 0.0,
        var unitlist: List<String> = listOf()
)

data class Catelist(
        var id: Int = 0,
        var sort: Int = 0,
        var type_name: String = ""
)