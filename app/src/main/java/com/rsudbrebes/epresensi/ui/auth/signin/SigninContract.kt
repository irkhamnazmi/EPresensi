package com.rsudbrebes.epresensi.ui.auth.signin

import com.rsudbrebes.epresensi.base.BasePresenter
import com.rsudbrebes.epresensi.base.BaseView
import com.rsudbrebes.epresensi.model.response.login.LoginResponse

interface SigninContract {
    interface View: BaseView {
        fun onLoginSuccess(loginResponse: LoginResponse)
        fun onLoginFailed(message:String)
    }

    interface Presenter : SigninContract, BasePresenter {
        fun submitLogin(username : String, password : String)
    }
}