package com.rsudbrebes.epresensi.ui.auth.signup

import com.rsudbrebes.epresensi.model.request.AbsensiRequest
import com.rsudbrebes.epresensi.model.request.RegisterRequest
import com.rsudbrebes.epresensi.network.HttpClient
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers


class SignupPresenter (private val view:SignupContract.View ) : SignupContract.Presenter{
    private val mCompositeDisposable : CompositeDisposable?
    init {
        this.mCompositeDisposable = CompositeDisposable()
    }

    override fun submitSignUp(registerRequest: RegisterRequest) {
        val disposable = HttpClient.getInstance().getApi()!!.registerGet(registerRequest.email)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    if (it.meta?.status.equals("available",true)){
                        view.onSignUpFailed("Email Anda Telah Terdaftar")
                    } else if(it.meta?.status.equals("unavailable",true)) {
                      postData(registerRequest)
                    }
                },
                {
                    view.onSignUpFailed(it.message.toString())
                }
            )
        mCompositeDisposable!!.add(disposable)
    }

    private fun postData(registerRequest: RegisterRequest){
        val disposable = HttpClient.getInstance().getApi()!!.registerPost(
            registerRequest.nama_lengkap,
            registerRequest.username,
            registerRequest.password,
            registerRequest.email
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {


                    if (it.meta?.status.equals("success",true)){
                        it.data?.let { it1 -> view.onSignUpSuccess(it1) }
                    } else {
                        it.meta?.message?.let { it1 -> view.onSignUpFailed(it1) }
                    }
                },
                {
                    view.onSignUpFailed(it.message.toString())
                }
            )
        mCompositeDisposable!!.add(disposable)
    }



    override fun subscribe() {

    }

    override fun unSubscribe() {

    }


}