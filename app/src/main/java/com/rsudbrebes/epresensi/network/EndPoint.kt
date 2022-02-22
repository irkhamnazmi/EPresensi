package com.rsudbrebes.epresensi.network

import com.rsudbrebes.epresensi.model.response.Wrapper
import com.rsudbrebes.epresensi.model.response.absensi.AbsensiResponse
import com.rsudbrebes.epresensi.model.response.login.LoginResponse
import io.reactivex.Observable
import retrofit2.http.*


interface EndPoint {
    @FormUrlEncoded
    @POST("login")
    fun login (@Field("username") username:String,
               @Field("password") password:String) : Observable<Wrapper<LoginResponse>>

    @FormUrlEncoded
    @POST("absen")
    fun absenPost (@Field("nama_pegawai") nama_pegawai:String,
                 @Field("kode_pegawai") kode_pegawai:String,
                 @Field("keterangan_absen") keterangan_absen:String,
                 @Field("maps_absen") maps_absen:String,
                 @Field("status_setting") status_setting:Int): Observable<Wrapper<AbsensiResponse>>


    @GET("absen")
    fun absenGet (@Query("kode_pegawai", encoded = true) kode_pegawai: String) : Observable<Wrapper<AbsensiResponse>>
}