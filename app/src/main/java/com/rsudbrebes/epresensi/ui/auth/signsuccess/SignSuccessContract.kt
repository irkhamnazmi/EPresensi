package com.rsudbrebes.epresensi.ui.auth.signsuccess

import android.graphics.Bitmap
import android.net.Uri
import com.rsudbrebes.epresensi.base.BasePresenter
import com.rsudbrebes.epresensi.base.BaseView
import com.rsudbrebes.epresensi.model.response.register.RegisterResponse

interface SignSuccessContract {
    interface View: BaseView {
        fun onCheckSuccess(registerResponse: RegisterResponse)
        fun onCheckFailed(message:String)
        fun postUri(uri: Uri)
    }

    interface Presenter : SignSuccessContract, BasePresenter {
        fun getUser(email : String)
        fun getUri(uri: Uri)
    }
}