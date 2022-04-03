package com.rsudbrebes.epresensi.model.response.register

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.rsudbrebes.epresensi.model.response.user.User

data class RegisterResponse(
    @Expose
    @SerializedName("access_token")
    val access_token: String,
    @Expose
    @SerializedName("token_type")
    val token_type: String,
    @Expose
    @SerializedName("register")
    val register: User
)