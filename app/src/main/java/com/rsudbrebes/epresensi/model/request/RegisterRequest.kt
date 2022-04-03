package com.rsudbrebes.epresensi.model.request

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
class RegisterRequest (
    @Expose
    @SerializedName("nama_lengkap")
    var nama_lengkap : String,
    @Expose
    @SerializedName("username")
    var username : String,
    @Expose
    @SerializedName("password")
    var password : String,
    @Expose
    @SerializedName("email")
    var email : String


): Parcelable