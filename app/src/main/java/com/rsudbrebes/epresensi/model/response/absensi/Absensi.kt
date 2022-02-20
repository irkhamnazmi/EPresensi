package com.rsudbrebes.epresensi.model.response.absensi

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Absensi(
    @Expose
    @SerializedName("jam_masuk")
    val jam_masuk: String,
    @Expose
    @SerializedName("keterangan_absen")
    val keterangan_absen: String,
    @Expose
    @SerializedName("kode_absen")
    val kode_absen: String,
    @Expose
    @SerializedName("kode_pegawai")
    val kode_pegawai: String,
    @Expose
    @SerializedName("maps_absen")
    val maps_absen: String,
    @Expose
    @SerializedName("nama_pegawai")
    val nama_pegawai: String,
    @Expose
    @SerializedName("status_pegawai")
    val status_pegawai: Int,
    @Expose
    @SerializedName("tgl_absen")
    val tgl_absen: String
)