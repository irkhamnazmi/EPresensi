package com.rsudbrebes.epresensi.ui.auth.signcomplete

import android.net.Uri
import android.view.View
import com.rsudbrebes.epresensi.model.request.CompleteRequest
import com.rsudbrebes.epresensi.model.response.register.RegisterResponse
import com.rsudbrebes.epresensi.network.HttpClient
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

class SignCompletePresenter(private val view: SignCompleteContract.View) :
    SignCompleteContract.Presenter {
    private val mCompositeDisposable: CompositeDisposable?

    init {
        this.mCompositeDisposable = CompositeDisposable()
    }

    override fun submitComplete(completeRequest: CompleteRequest) {
        val disposable = HttpClient.getInstance().getApi()!!.registerPut(
            completeRequest.nama_lengkap,
            completeRequest.nip,
            completeRequest.nik,
            completeRequest.jenis_kelamin,
            completeRequest.tempat_lahir,
            completeRequest.tgl_lahir,
            completeRequest.umur,
            completeRequest.alamat,
            completeRequest.email,
            completeRequest.jabatan,
            completeRequest.ruangan,
            completeRequest.instansi,
            completeRequest.username,
            completeRequest.password

        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {


                    if (it.meta?.status.equals("success", true)) {
                        it.data?.let { it1 -> view.onSubmitSuccess(it1) }
                    } else {
                        it.meta?.message?.let { it1 -> view.onSubmitFailed(it1) }
                    }
                },
                {
                    view.onSubmitFailed(it.message.toString())
                }
            )
        mCompositeDisposable!!.add(disposable)
    }

    override fun imageUpload(image: Uri) {
        var imgFotoFile = File(image.path)
        var imgFotoRequestBody =
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), imgFotoFile)
        var imgFotoParms =
            MultipartBody.Part.createFormData("file", imgFotoFile.name, imgFotoRequestBody)
        val disposable = HttpClient.getInstance().getApi()!!.registerImage(
            imgFotoParms
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {


                    if (it.meta?.status.equals("success", true)) {
                        view.onSubmitFailed("Sukses")
                    } else {
                        view.onSubmitFailed("Gagal")
                    }
                },
                {
                    view.onSubmitFailed(it.message.toString())
                }
            )
        mCompositeDisposable!!.add(disposable)
    }

    override fun subscribe() {

    }

    override fun unSubscribe() {
    }

}




