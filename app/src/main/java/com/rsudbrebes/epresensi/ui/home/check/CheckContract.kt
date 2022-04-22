package com.rsudbrebes.epresensi.ui.home.check

import android.content.Context
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.rsudbrebes.epresensi.base.BasePresenter
import com.rsudbrebes.epresensi.base.BaseView
import com.rsudbrebes.epresensi.databinding.FragmentCheckBinding
import com.rsudbrebes.epresensi.model.request.AbsensiRequest
import com.rsudbrebes.epresensi.model.response.absensi.AbsensiResponse
import java.sql.Struct

interface CheckContract {
    interface View: BaseView {
//        fun onLongDistance(message : String)
//        fun onShortDistance(maps: String, message : String)
        fun onCheckSuccess(absensiResponse: AbsensiResponse, status: String)
        fun onCheckFailed(message: String, status: String)
        fun onCheckAbsenSuccess(absensiResponse: AbsensiResponse)
        fun onCheckAbsenFailed(message: String)
        fun showKetAbsen(adapter: ArrayAdapter<CharSequence>)


    }

    interface Presenter : CheckContract, BasePresenter {
//        fun locationDistance(latitude : Double, longitude: Double, latitudeTo: Double, longitudeTo: Double)
        fun submitCheck(absensiRequest: AbsensiRequest, status: String)
        fun checkAbsen(id : String)
        fun spinner(context: Context)



    }
}