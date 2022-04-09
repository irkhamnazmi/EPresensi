package com.rsudbrebes.epresensi.ui.auth.signsuccess

import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.ValueCallback
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.google.gson.Gson
import com.rsudbrebes.epresensi.BuildConfig.BASE_URL
import com.rsudbrebes.epresensi.EPresensi
import com.rsudbrebes.epresensi.R
import com.rsudbrebes.epresensi.databinding.FragmentSignSuccessBinding
import com.rsudbrebes.epresensi.model.response.register.RegisterResponse
import com.rsudbrebes.epresensi.model.response.user.User
import com.rsudbrebes.epresensi.ui.MainActivity
import com.rsudbrebes.epresensi.ui.auth.AuthActivity


class SignSuccessFragment : Fragment(), SignSuccessContract.View {

    private lateinit var binding: FragmentSignSuccessBinding
    lateinit var presenter: SignSuccessPresenter
    private lateinit var alertDialog: AlertDialog

    private var mUploadMessage: ValueCallback<Uri>? = null

    var uploadMessage:ValueCallback<Array<Uri>>? = null

    val REQUEST_SELECT_FILE = 100
    private val FILECHOOSER_RESULTCODE = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSignSuccessBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        presenter = SignSuccessPresenter(this)
        (activity as AuthActivity)?.dispatchInformations("Jl. Jendral Sudirman No.181, Brebes, Jawa Tengah 52272")

        if (EPresensi.getApp().getUser().isNullOrEmpty()) {
            view?.let { Navigation.findNavController(it).navigate(R.id.action_signin) }
        } else {
            val user = EPresensi.getApp().getUser()
            var userResponse = Gson().fromJson(user, User::class.java)
            val email = userResponse.email
            if (EPresensi.getApp().getActive().isNullOrEmpty()) {
                binding.tvUsername.text = userResponse.nama_lengkap
                presenter.getUser(email)
            } else {
                startActivity(Intent(activity, MainActivity::class.java))
                activity?.finish()
            }


        }


    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCheckSuccess(registerResponse: RegisterResponse) {

        if (registerResponse.register.kode_pegawai.isEmpty()) {
            binding.btnNext.text = "Lengkapi Profil Anda "
            binding.btnNext.setOnClickListener {
//                val url = BASE_URL + "register/${registerResponse.register.email}"
//                val i = Intent(Intent.ACTION_VIEW)
//                i.data = Uri.parse(url)
//                startActivity(i)

//                val i = Intent(activity, AuthActivity::class.java)
//                i.putExtra("url",BASE_URL + "register")
////                i.data = Uri.parse(url)
//                i.putExtra("page_request","webview")
//                startActivity(i)

                showCustomDialog(BASE_URL + "register")

            }

        } else {
            binding.btnNext.text = "Lanjutkan sebagai ${registerResponse.register.username} "
            binding.btnNext.setOnClickListener {
                EPresensi.getApp().setActive("1")
                val gson = Gson()
                val json = gson.toJson(registerResponse.register)
                EPresensi.getApp().setUser(json)
                startActivity(Intent(activity, MainActivity::class.java))
                activity?.finish()
            }

        }

    }

    override fun onCheckFailed(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    override fun showLoading() {

    }

    override fun dismissLoading() {

    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun showCustomDialog(url : String) {
        val inflater: LayoutInflater = this.getLayoutInflater()
        val dialogView: View = inflater.inflate(R.layout.showdialog_webview, null)
        val web = dialogView.findViewById<WebView>(R.id.webview1)
        val inputField = dialogView.findViewById<EditText>(R.id.inputField)
        inputField.requestFocus()
        inputField.isFocusable = true
        web.loadUrl(url)
        val webSettings = web.settings

        webSettings.javaScriptEnabled = true
        webSettings.builtInZoomControls = true
        webSettings.displayZoomControls = true
        webSettings.domStorageEnabled = true
        webSettings.allowContentAccess = true
        webSettings.setAppCacheEnabled(false)
        webSettings.cacheMode = WebSettings.LOAD_NO_CACHE
        webSettings.setGeolocationEnabled(true)      // life saver, do not remove

        web?.webChromeClient = object: WebChromeClient() {

            protected fun openFileChooser(uploadMsg: ValueCallback<Uri>?, acceptType: String?) {
                mUploadMessage = uploadMsg
                val i = Intent(Intent.ACTION_GET_CONTENT)
                i.addCategory(Intent.CATEGORY_OPENABLE)
                i.type = "image/*"
                startActivityForResult(
                    Intent.createChooser(i, "File Browser"),
                    FILECHOOSER_RESULTCODE
                )
            }


            // For Lollipop 5.0+ Devices
            override fun onShowFileChooser(
                mWebView: WebView?,
                filePathCallback: ValueCallback<Array<Uri>>?,
                fileChooserParams: FileChooserParams
            ): Boolean {
                if (uploadMessage != null) {
                    uploadMessage!!.onReceiveValue(null)
                    uploadMessage = null
                }
                uploadMessage = filePathCallback
                val intent = fileChooserParams.createIntent()
                try {
                    startActivityForResult(intent, REQUEST_SELECT_FILE)
                } catch (e: ActivityNotFoundException) {
                    uploadMessage = null
                    Toast.makeText(
                        activity!!.applicationContext,
                        "Cannot Open File Chooser",
                        Toast.LENGTH_LONG
                    ).show()
                    return false
                }
                return true
            }

            //For Android 4.1 only
            protected fun openFileChooser(
                uploadMsg: ValueCallback<Uri>?,
                acceptType: String?,
                capture: String?
            ) {
                mUploadMessage = uploadMsg
                val intent = Intent(Intent.ACTION_GET_CONTENT)
                intent.addCategory(Intent.CATEGORY_OPENABLE)
                intent.type = "image/*"
                startActivityForResult(
                    Intent.createChooser(intent, "File Browser"),
                    FILECHOOSER_RESULTCODE
                )
            }

            protected fun openFileChooser(uploadMsg: ValueCallback<Uri>?) {
                mUploadMessage = uploadMsg
                val i = Intent(Intent.ACTION_GET_CONTENT)
                i.addCategory(Intent.CATEGORY_OPENABLE)
                i.type = "image/*"
                startActivityForResult(
                    Intent.createChooser(i, "File Chooser"),
                    FILECHOOSER_RESULTCODE
                )
            }
            }











        val dialogBuilder: AlertDialog.Builder = AlertDialog.Builder(context)
        dialogBuilder.setOnDismissListener(object : DialogInterface.OnDismissListener {
            override fun onDismiss(arg0: DialogInterface) {

            }
        })
        dialogBuilder.setView(dialogView)

        alertDialog = dialogBuilder.create();
//        alertDialog.window!!.getAttributes().windowAnimations = R.style.PauseDialogAnimation
        alertDialog.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            if(requestCode == REQUEST_SELECT_FILE){
                if(uploadMessage != null){
                    uploadMessage?.onReceiveValue(WebChromeClient.FileChooserParams.parseResult(resultCode,data))
                    uploadMessage = null
                }
            }
        }else if(requestCode == FILECHOOSER_RESULTCODE){
            if(mUploadMessage!=null){
                var result = data?.data
                mUploadMessage?.onReceiveValue(result)
                mUploadMessage = null
            }
        }else{
            Toast.makeText(context,"Failed to open file uploader, please check app permissions.",Toast.LENGTH_LONG).show()
            super.onActivityResult(requestCode, resultCode, data)
        }


    }

}

