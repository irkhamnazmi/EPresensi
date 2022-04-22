package com.rsudbrebes.epresensi.ui.auth.signsuccess

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.ContentValues.TAG
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.rsudbrebes.epresensi.EPresensi
import com.rsudbrebes.epresensi.R
import com.rsudbrebes.epresensi.databinding.FragmentSignSuccessBinding
import com.rsudbrebes.epresensi.model.response.register.RegisterResponse
import com.rsudbrebes.epresensi.model.response.user.User
import com.rsudbrebes.epresensi.ui.MainActivity
import com.rsudbrebes.epresensi.ui.auth.AuthActivity
import java.io.*
import java.text.SimpleDateFormat
import java.util.*


class SignSuccessFragment : Fragment(), SignSuccessContract.View {

    private lateinit var binding: FragmentSignSuccessBinding
    lateinit var presenter: SignSuccessPresenter
    private lateinit var alertDialog: AlertDialog

    private val GALLERY_PERMISSION_CODE = 101

    var pick : Uri ?= null

    var email: String = ""
    var username: String = ""
    var pass: String = ""
    private var myFormat = "dd-MM-yyyy"
    var sdf = SimpleDateFormat(myFormat, Locale.getDefault())
    var today = Calendar.getInstance()

    val CAMERA_REQUEST = 100
    val STORAGE_REQUEST = 101
    lateinit var cameraPermission: Array<String>
    lateinit var storagePermission: Array<String>

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
            email = userResponse.email
            if (email.isEmpty()) {
                EPresensi.getApp().setUser("")
                view?.let { Navigation.findNavController(it).navigate(R.id.action_signin) }
                Toast.makeText(context, "Email Anda tidak terdaftar", Toast.LENGTH_SHORT).show()
            } else {
                if (EPresensi.getApp().getActive().isNullOrEmpty()) {
                    presenter.getUser(email)
                } else {
                    startActivity(Intent(activity, MainActivity::class.java))
                    activity?.finish()
                }
            }


        }


    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCheckSuccess(registerResponse: RegisterResponse) {

        if (registerResponse.register.kode_pegawai.isEmpty()) {
            binding.tvUsername.text = registerResponse.register.email
            username = registerResponse.register.username
            pass = registerResponse.register.password
            binding.tvKetKonfirmasi.text = requireContext().resources.getString(R.string.sebelum_mel)
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

                view?.let { Navigation.findNavController(it).navigate(R.id.action_sign_complete) }
//                showCustomDialog("https://www.google.com")

            }
            binding.btnLogout.text = "Ganti email lain"
            binding.btnLogout.setOnClickListener {
                EPresensi.getApp().setUser("")
                view?.let { Navigation.findNavController(it).navigate(R.id.action_signin) }
            }

        } else {
            binding.tvUsername.text = registerResponse.register.nama_lengkap
            binding.tvKetKonfirmasi.text = requireContext().resources.getString(R.string.sesudah_mel)
            binding.btnNext.text = "Lanjutkan sebagai ${registerResponse.register.username} "
            binding.btnNext.setOnClickListener {
                EPresensi.getApp().setActive("1")
                val gson = Gson()
                val json = gson.toJson(registerResponse.register)
                EPresensi.getApp().setUser(json)
                startActivity(Intent(activity, MainActivity::class.java))
                activity?.finish()
            }
            binding.btnLogout.text = "Ganti akun lain"
            binding.btnLogout.setOnClickListener {
                EPresensi.getApp().setUser("")
                view?.let { Navigation.findNavController(it).navigate(R.id.action_signin) }
            }

        }

    }

    override fun onCheckFailed(message: String) {
        EPresensi.getApp().setUser("")
        view?.let { Navigation.findNavController(it).navigate(R.id.action_signin) }
        Toast.makeText(context, "Email Anda tidak terdaftar", Toast.LENGTH_SHORT).show()
        activity?.finishAffinity()
    }

    override fun postUri(uri: Uri) {
       pick = uri
    }

    override fun showLoading() {

    }

    override fun dismissLoading() {

    }





}






