package com.rsudbrebes.epresensi.ui.auth.signup

import com.rsudbrebes.epresensi.base.BasePresenter
import com.rsudbrebes.epresensi.base.BaseView
import com.rsudbrebes.epresensi.model.request.RegisterRequest
import com.rsudbrebes.epresensi.model.response.login.LoginResponse
import com.rsudbrebes.epresensi.model.response.register.RegisterResponse
import com.rsudbrebes.epresensi.ui.auth.signin.SigninContract

interface SignupContract {
    interface View: BaseView {
        fun onSignUpSuccess(registerResponse: RegisterResponse)
        fun onSignUpFailed(message:String)

    }

    interface Presenter : SigninContract, BasePresenter {
        fun submitSignUp(registerRequest: RegisterRequest)

    }
}