package com.rsudbrebes.epresensi.model.response.user

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class User(
    @Expose
    @SerializedName("bagian_shift")
    val bagian_shift: Int,
    @Expose
    @SerializedName("date_created")
    val date_created: String,
    @Expose
    @SerializedName("email")
    val email: String,
    @Expose
    @SerializedName("id_pegawai")
    val id_pegawai: String,
    @Expose
    @SerializedName("image")
    val image: String,
    @Expose
    @SerializedName("instansi")
    val instansi: String,
    @Expose
    @SerializedName("is_active")
    val is_active: String,
    @Expose
    @SerializedName("jabatan")
    val jabatan: String,
    @Expose
    @SerializedName("ruangan")
    val ruangan: String,
    @Expose
    @SerializedName("jenis_kelamin")
    val jenis_kelamin: String,
    @Expose
    @SerializedName("kode_pegawai")
    val kode_pegawai: String,
    @Expose
    @SerializedName("last_login")
    val last_login: String,
    @Expose
    @SerializedName("nama_lengkap")
    val nama_lengkap: String,
    @Expose
    @SerializedName("nip")
    val nip: String,
    @Expose
    @SerializedName("password")
    val password: String,
    @Expose
    @SerializedName("qr_code_image")
    val qr_code_image: String,
    @Expose
    @SerializedName("qr_code_user")
    val qr_code_use: String,
    @Expose
    @SerializedName("role_id")
    val role_id: String,
    @Expose
    @SerializedName("tempat_lahir")
    val tempat_lahir: String,
    @Expose
    @SerializedName("tgl_lahir")
    val tgl_lahir: String,
    @Expose
    @SerializedName("umur")
    val umur: String,
    @Expose
    @SerializedName("username")
    val username: String


)