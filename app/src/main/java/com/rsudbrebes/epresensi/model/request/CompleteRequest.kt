package com.rsudbrebes.epresensi.model.request

import android.net.Uri
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
class CompleteRequest(
    @Expose
    @SerializedName("nama_lengkap")
    var nama_lengkap: String,
    @Expose
    @SerializedName("nip")
    var nip: String,
    @Expose
    @SerializedName("nik")
    var nik: String,
    @Expose
    @SerializedName("jenis_kelamin")
    var jenis_kelamin: String,
    @Expose
    @SerializedName("tempat_lahir")
    var tempat_lahir: String,
    @Expose
    @SerializedName("tgl_lahir")
    var tgl_lahir: String,
    @Expose
    @SerializedName("umur")
    var umur: String,
    @Expose
    @SerializedName("alamat")
    var alamat: String,
    @Expose
    @SerializedName("email")
    var email: String,
    @Expose
    @SerializedName("jabatan")
    var jabatan: String,
    @Expose
    @SerializedName("ruangan")
    var ruangan: String,
    @Expose
    @SerializedName("instansi")
    var instansi: String,
    @Expose
    @SerializedName("username")
    var username: String,
    @Expose
    @SerializedName("password")
    var password: String


) : Parcelable