package com.rsudbrebes.epresensi.ui.home.check

import android.annotation.SuppressLint
import android.content.Context
import android.widget.ArrayAdapter
import com.rsudbrebes.epresensi.R
import com.rsudbrebes.epresensi.model.request.AbsensiRequest
import com.rsudbrebes.epresensi.network.HttpClient
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class CheckPresenter (private val view: CheckContract.View) : CheckContract.Presenter{
    private val mCompositeDisposable : CompositeDisposable?
    init {
        this.mCompositeDisposable = CompositeDisposable()
    }

//    override fun locationDistance(latitude : Double, longitude: Double, latitudeTo: Double, longitudeTo: Double) {
//        val distance  = getDistance(latitudeTo,longitudeTo, latitude, longitude)
//        val df = DecimalFormat("#.##")
//        df.roundingMode = RoundingMode.CEILING
//        val resultDistance : Double = distance!! * 1000
////        tvResult.text = "Jarak : ${resultDistance} meter"
//        if(resultDistance <= 100 ){
//            view.onShortDistance("${latitude.toString()},${longitude.toString()}","Sudah dekat ${resultDistance.toString()} meter")
//        } else{
//            view.onLongDistance("Masih jauh ${resultDistance.toString()} meter")
//        }
//    }

    override fun submitCheck(absensiRequest: AbsensiRequest, status : String) {
        val disposable = HttpClient.getInstance().getApi()!!.absenPost(
            absensiRequest.nama_pegawai,
            absensiRequest.kode_pegawai,
            absensiRequest.ket_absen,
            absensiRequest.alasan,
            absensiRequest.maps_absen,
            absensiRequest.bagian_shift,
            absensiRequest.status_setting
            )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {


                    if (it.meta?.status.equals("success",true)){
                        it.data?.let { it1 -> view.onCheckSuccess(it1, status) }
                    } else {
                        it.meta?.message?.let { it1 -> view.onCheckFailed(it1, status) }
                    }
                },
                {
                    view.onCheckFailed(it.message.toString(), status)
                }
            )
        mCompositeDisposable!!.add(disposable)
    }

    override fun checkAbsen(id: String) {
        val disposable = HttpClient.getInstance().getApi()!!.absenGet(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    if (it.meta?.status.equals("done",true)){
                        it.data?.let { it1 -> view.onCheckAbsenSuccess(it1) }
                    } else if(it.meta?.status.equals("ready",true)) {
                        it.meta?.message?.let { it1 -> view.onCheckAbsenFailed(it1) }
                    }
                },
                {
                    view.onCheckAbsenFailed(it.message.toString())
                }
            )
        mCompositeDisposable!!.add(disposable)
    }

    @SuppressLint("ResourceType")
    override fun spinner(context: Context) {

        ArrayAdapter.createFromResource(
            context,
            R.array.ket_absen,
            R.layout.spinner_list
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(R.layout.spinner_list)
            // Apply the adapter to the spinner
            view.showKetAbsen(adapter)
        }


    }




    override fun subscribe() {

    }

    override fun unSubscribe() {

    }


//    private fun getDistance(
//        latitudeTujuan: Double,
//        longitudeTujuan: Double,
//        latitudeUser: Double,
//        longitudeUser: Double
//    ): Double? {
//        /* VARIABLE */
//
//        val radian = 0.0174532925
//        val R = 6371
//
//        val latRad1 = latitudeUser * radian
//        val longRad1 = longitudeUser * radian
//        val latRad2 = latitudeTujuan * radian
//        val longRad2 = longitudeTujuan * radian
//
//        val deltaLat = (latRad2 - latRad1)
//        val deltaLong = (longRad2 - longRad1)
//
//
//        /* RUMUS HAVERSINE */
//
//        val a = Math.pow(Math.sin(deltaLat / 2), 2.toDouble()) + Math.pow(Math.sin(deltaLong / 2), 2.toDouble()) * Math.cos(latRad1) * Math.cos(latRad2);
//        val c = 2 * Math.asin(Math.sqrt(a));
//        return R * c
//    }


}
