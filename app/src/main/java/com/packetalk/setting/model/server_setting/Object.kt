package com.packetalk.setting.model.server_setting


import com.google.gson.annotations.SerializedName

data class Object(
    @SerializedName("Cameraname")
    val cameraname: Any,
    @SerializedName("IsDeleted")
    val isDeleted: Int,
    @SerializedName("Port")
    val port: String,
    @SerializedName("RouterStatus")
    var routerStatus: Boolean,
    @SerializedName("ServerID")
    val serverID: Int,
    @SerializedName("TrailerID")
    val trailerID: Int,
    @SerializedName("Uri")
    val uri: String
)