package com.rsudbrebes.epresensi.network

import android.net.Uri
import com.rsudbrebes.epresensi.model.response.Wrapper
import com.rsudbrebes.epresensi.model.response.absensi.AbsensiResponse
import com.rsudbrebes.epresensi.model.response.login.LoginResponse
import com.rsudbrebes.epresensi.model.response.register.RegisterResponse
import io.reactivex.Observable
import okhttp3.MultipartBody
import retrofit2.http.*


interface EndPoint {
    @FormUrlEncoded
    @POST("login")
    fun login(
        @Field("username") username: String,
        @Field("password") password: String
    ): Observable<Wrapper<LoginResponse>>

    @FormUrlEncoded
    @POST("absen")
    @Headers("X-Requested-With:XMLHttpRequest")
    fun absenPost(
        @Field("nama_pegawai") nama_pegawai: String,
        @Field("kode_pegawai") kode_pegawai: String,
        @Field("ket_absen") ket_absen: String,
        @Field("alasan") alasan: String,
        @Field("maps_absen") maps_absen: String,
        @Field("bagian_shift") bagian_shift: Int,
        @Field("status_setting") status_setting: Int
    ): Observable<Wrapper<AbsensiResponse>>

    @GET("absen")
    fun absenGet(
        @Query(
            "kode_pegawai",
            encoded = true
        ) kode_pegawai: String
    ): Observable<Wrapper<AbsensiResponse>>

    @GET("register")
    fun registerGet(
        @Query(
            "email",
            encoded = true
        ) email: String
    ): Observable<Wrapper<RegisterResponse>>

    @FormUrlEncoded
    @POST("register")
    @Headers("X-Requested-With:XMLHttpRequest")
    fun registerPost(
        @Field("nama_lengkap") nama_lengkap: String,
        @Field("username") username: String,
        @Field("password") password: String,
        @Field("email") email: String


    ): Observable<Wrapper<RegisterResponse>>

    @FormUrlEncoded
    @PUT("register")
    @Multipart
    @Headers("X-Requested-With:XMLHttpRequest")
    fun registerPut(
        @Field("nama_lengkap") nama_lengkap: String,
        @Field("nip") nip: String,
        @Field("nik") nik: String,
        @Field("jenis_kelamin") jenis_kelamin: String,
        @Field("tempat_lahir") tempat_lahir: String,
        @Field("tgl_lahir") tgl_lahir: String,
        @Field("umur") umur: String,
        @Field("alamat") alamat: String,
        @Field("email") email: String,
        @Field("jabatan") jabatan: String,
        @Field("ruangan") ruangan: String,
        @Field("instansi") instansi: String,
        @Field("username") username: String,
        @Field("password") password: String


    ): Observable<Wrapper<RegisterResponse>>


    @POST("image")
    @Multipart
    fun registerImage(
        @Part image: MultipartBody.Part,


    ): Observable<Wrapper<Any>>



    @PATCH("alerts/{alert_id}/accept")
    fun accept_invited_alerts(
        @Header("X-Api-Token") api_token: String,
        @Path("alert_id") alert_id: Int
    ): retrofit2.Call<Unit>
}