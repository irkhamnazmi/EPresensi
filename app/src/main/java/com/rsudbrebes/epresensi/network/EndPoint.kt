package com.rsudbrebes.epresensi.network

import com.rsudbrebes.epresensi.model.response.Wrapper
import com.rsudbrebes.epresensi.model.response.absensi.AbsensiResponse
import com.rsudbrebes.epresensi.model.response.login.LoginResponse
import io.reactivex.Observable
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST



interface EndPoint {
    @FormUrlEncoded
    @POST("login")
    fun login (@Field("username") username:String,
               @Field("password") password:String) : Observable<Wrapper<LoginResponse>>

    @FormUrlEncoded
    @POST("absensi")
    fun absensi (@Field("nama_pegawai") nama_pegawai:String,
                 @Field("kode_pegawai") kode_pegawai:String,
                 @Field("keterangan_absen") keterangan_absen:String,
                 @Field("maps_absen") maps_absen:String,
                 @Field("status_setting") status_setting:Int): Observable<Wrapper<AbsensiResponse>>
}