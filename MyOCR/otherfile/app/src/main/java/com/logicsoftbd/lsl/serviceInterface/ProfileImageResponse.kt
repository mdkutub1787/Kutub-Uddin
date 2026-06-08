package com.logicsoftbd.lsl.serviceInterface

import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

class ProfileImageResponse {
    @SerializedName("status")
    @Expose
    var status: Boolean? = null

    @SerializedName("resultset")
    @Expose
    var resultset: Resultset? = null

    inner class Resultset {
        @SerializedName("message")
        @Expose
        var message: String? = null

        @SerializedName("status")
        @Expose
        var status: Boolean? = null
    }
}