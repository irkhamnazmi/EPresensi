package com.rsudbrebes.epresensi.ui.auth.signsuccess

import com.rsudbrebes.epresensi.network.HttpClient
import com.rsudbrebes.epresensi.ui.auth.signin.SigninContract
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class SignSuccessPresenter (private val view: SignSuccessContract.View ) : SignSuccessContract.Presenter {
    private val mCompositeDisposable: CompositeDisposable?

    init {
        this.mCompositeDisposable = CompositeDisposable()
    }

    override fun getUser(email: String) {
        val disposable = HttpClient.getInstance().getApi()!!.registerGet(email)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {

                    if (it.meta?.status.equals("available",true)){
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


}