package com.rsudbrebes.epresensi.model.request

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
class AbsensiRequest (
    @Expose
    @SerializedName("nama_pegawai")
    var nama_pegawai : String,
    @Expose
    @SerializedName("kode_pegawai")
    var kode_pegawai : String,
    @Expose
    @SerializedName("keterangan_absen")
    var keterangan_absen : String,
    @Expose
    @SerializedName("maps_absen")
    var maps_absen : String,
    @Expose
    @SerializedName("status_setting")
    var status_setting : Int

): Parcelable