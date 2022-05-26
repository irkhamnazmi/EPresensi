package com.rsudbrebes.epresensi.ui.auth.signcomplete

import android.net.Uri
import android.view.View
import com.rsudbrebes.epresensi.base.BasePresenter
import com.rsudbrebes.epresensi.base.BaseView
import com.rsudbrebes.epresensi.model.request.CompleteRequest
import com.rsudbrebes.epresensi.model.response.register.RegisterResponse

interface SignCompleteContract {
    interface View: BaseView {
        fun onSubmitSuccess(registerResponse: RegisterResponse)
        fun onSubmitFailed(message:String)
        fun onUploadCondition(message: String)
    }

    interface Presenter : SignCompleteContract, BasePresenter {
        fun submitComplete(completeRequest: CompleteRequest)
        fun imageUpload(filePath : Uri)
    }
}