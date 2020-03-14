package com.qttx.kedouhulian.bean

data class VersionBean(
        var downloadurl: String = "",
        var enforce: Int = 0,
        var newversion: String = "",
        var packagesize: String = "",
        var upgradetext: String = "",
        var version: String = ""
)