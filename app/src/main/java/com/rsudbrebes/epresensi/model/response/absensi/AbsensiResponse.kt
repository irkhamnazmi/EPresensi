package com.rsudbrebes.epresensi.model.response.absensi

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class AbsensiResponse(
    @Expose
    @SerializedName("absensi")
    val absensi: Absensi,
    @Expose
    @SerializedName("access_token")
    val access_token: String,
    @Expose
    @SerializedName("token_type")
    val token_type: String
)