package com.rsudbrebes.epresensi.ui.home.check

import com.rsudbrebes.epresensi.base.BasePresenter
import com.rsudbrebes.epresensi.base.BaseView
import com.rsudbrebes.epresensi.model.request.AbsensiRequest
import com.rsudbrebes.epresensi.model.response.absensi.AbsensiResponse

interface CheckContract {
    interface View: BaseView {
//        fun onLongDistance(message : String)
//        fun onShortDistance(maps: String, message : String)
        fun onCheckSuccess(absensiResponse: AbsensiResponse)
        fun onCheckFailed(message: String)
        fun onCheckAbsenSuccess(absensiResponse: AbsensiResponse)
        fun onCheckAbsenFailed(message: String)

    }

    interface Presenter : CheckContract, BasePresenter {
//        fun locationDistance(latitude : Double, longitude: Double, latitudeTo: Double, longitudeTo: Double)
        fun submitCheck(absensiRequest: AbsensiRequest)
        fun checkAbsen(id : String)

    }
}