package com.rsudbrebes.epresensi.ui.auth.signup

import com.rsudbrebes.epresensi.network.HttpClient
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers


class SignupPresenter (private val view:SignupContract.View ) : SignupContract.Presenter{
    private val mCompositeDisposable : CompositeDisposable?
    init {
        this.mCompositeDisposable = CompositeDisposable()
    }

    override fun submitSignUp(username: String, password: String) {
        TODO("Not yet implemented")
    }

    override fun getRegister(email: String) {
        TODO("Not yet implemented")
    }


    override fun subscribe() {
        TODO("Not yet implemented")
    }

    override fun unSubscribe() {
        TODO("Not yet implemented")
    }


}