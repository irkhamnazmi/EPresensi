package com.rsudbrebes.epresensi.ui.home.check

import android.content.DialogInterface
import android.graphics.Color
import android.hardware.biometrics.BiometricPrompt
import com.rsudbrebes.epresensi.model.request.AbsensiRequest
import com.rsudbrebes.epresensi.network.HttpClient
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.math.RoundingMode
import java.text.DecimalFormat

class CheckPresenter (private val view: CheckContract.View) : CheckContract.Presenter{
    private val mCompositeDisposable : CompositeDisposable?
    init {
        this.mCompositeDisposable = CompositeDisposable()
    }

    override fun locationDistance(latitude : Double, longitude: Double, latitudeTo: Double, longitudeTo: Double) {
        val distance  = getDistance(latitudeTo,longitudeTo, latitude, longitude)
        val df = DecimalFormat("#.##")
        df.roundingMode = RoundingMode.CEILING
        val resultDistance : Double = distance!! * 1000
//        tvResult.text = "Jarak : ${resultDistance} meter"
        if(resultDistance <= 100 ){
            view.onShortDistance("${latitude.toString()},${longitude.toString()}","Sudah dekat ${resultDistance.toString()} meter")
        } else{
            view.onLongDistance("Masih jauh ${resultDistance.toString()} meter")
        }
    }

    override fun submitCheck(absensiRequest: AbsensiRequest) {
        val disposable = HttpClient.getInstance().getApi()!!.absensi(
            absensiRequest.nama_pegawai,
            absensiRequest.kode_pegawai,
            absensiRequest.keterangan_absen,
            absensiRequest.maps_absen,
            absensiRequest.status_setting
            )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {


                    if (it.meta?.status.equals("success",true)){
                        it.data?.let { it1 -> view.onCheckSuccess(it1) }
                    } else {
                        it.meta?.message?.let { it1 -> view.onCheckFailed(it1) }
                    }
                },
                {
                    view.onCheckFailed(it.message.toString())
                }
            )
        mCompositeDisposable!!.add(disposable)
    }


    override fun subscribe() {

    }

    override fun unSubscribe() {

    }


    private fun getDistance(
        latitudeTujuan: Double,
        longitudeTujuan: Double,
        latitudeUser: Double,
        longitudeUser: Double
    ): Double? {
        /* VARIABLE */

        val radian = 0.0174532925
        val R = 6371

        val latRad1 = latitudeUser * radian
        val longRad1 = longitudeUser * radian
        val latRad2 = latitudeTujuan * radian
        val longRad2 = longitudeTujuan * radian

        val deltaLat = (latRad2 - latRad1)
        val deltaLong = (longRad2 - longRad1)


        /* RUMUS HAVERSINE */

        val a = Math.pow(Math.sin(deltaLat / 2), 2.toDouble()) + Math.pow(Math.sin(deltaLong / 2), 2.toDouble()) * Math.cos(latRad1) * Math.cos(latRad2);
        val c = 2 * Math.asin(Math.sqrt(a));
        return R * c
    }


}
