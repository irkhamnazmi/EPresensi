package com.rsudbrebes.epresensi.ui.auth.signcomplete

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.rsudbrebes.epresensi.BuildConfig
import com.rsudbrebes.epresensi.EPresensi
import com.rsudbrebes.epresensi.R
import com.rsudbrebes.epresensi.databinding.FragmentSignCompleteBinding
import com.rsudbrebes.epresensi.databinding.LoadingItemBinding
import com.rsudbrebes.epresensi.databinding.ShowdialogCheckSuccessBinding
import com.rsudbrebes.epresensi.model.request.AbsensiRequest
import com.rsudbrebes.epresensi.model.request.CompleteRequest
import com.rsudbrebes.epresensi.model.request.RegisterRequest
import com.rsudbrebes.epresensi.model.response.register.RegisterResponse
import com.rsudbrebes.epresensi.model.response.user.User
import com.rsudbrebes.epresensi.ui.auth.AuthActivity
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


class SignCompleteFragment : Fragment(), SignCompleteContract.View {

    private lateinit var binding: FragmentSignCompleteBinding
    lateinit var presenter: SignCompletePresenter
    var progressDialog : Dialog? = null

    private val GALLERY_PERMISSION_CODE = 101

    var pick: Uri? = null


    private var myFormat = "dd-MM-yyyy"
    var sdf = SimpleDateFormat(myFormat, Locale.getDefault())
    var today = Calendar.getInstance()

    val CAMERA_REQUEST = 100
    val STORAGE_REQUEST = 101
    lateinit var cameraPermission: Array<String>
    lateinit var storagePermission: Array<String>

    private var latestTmpUri: Uri? = null
    var genre: RadioButton? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSignCompleteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        presenter = SignCompletePresenter(this)
        initView()

    }

    private fun initView() {
        initProgressDialog()

        view?.let { onBackPressed(it) }
        (activity as AuthActivity)?.dispatchAction("GONE")
        val user = EPresensi.getApp().getUser()
        var userResponse = Gson().fromJson(user, User::class.java)

        binding.edtEmail.setText(userResponse.email)
        binding.edtUsername.setText(userResponse.username)
        binding.edtPassword.setText(userResponse.password)
        val dob = Calendar.getInstance()

        val datePicker =
            DatePickerDialog.OnDateSetListener { view, year: Int, month: Int, dayOfMonth: Int ->

                dob.set(Calendar.YEAR, year)
                dob.set(Calendar.MONTH, month)
                dob.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                binding.edtTgl.setText(sdf.format(dob.time))
                val umur = age(
                    dob.get(Calendar.YEAR),
                    dob.get(Calendar.MONTH),
                    dob.get(Calendar.DAY_OF_MONTH)
                )
                binding.edtUmur.setText(umur)
            }

        binding.edtTgl.setOnClickListener {
            DatePickerDialog(
                requireContext(),
                datePicker,
                dob.get(Calendar.YEAR),
                dob.get(Calendar.MONTH),
                dob.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        binding.btnUpload.setOnClickListener {
            initImagePicker()
        }

        binding.rgGenre.setOnCheckedChangeListener { radioGroup, id ->
            genre = radioGroup.findViewById(id)
        }

        binding.btnBack.setOnClickListener {
            view?.let { it1 -> backAction(it1) }
        }

        binding.btnSave.setOnClickListener {
            var edtNamaPegawai = binding.edtNamaLengkap
            var edtNip = binding.edtNIP
            var edtNik = binding.edtNIK
            var rbJk = genre
            var edtTempat = binding.edtTempatLahir
            var edtTglLahir = binding.edtTgl
            var edtUmur = binding.edtUmur
            var edtAlamat = binding.edtAlamat
            var edtEmail = binding.edtEmail
            var edtJabatan = binding.edtJabatan
            var edtRuangan = binding.edtRuangan
            var edtInstansi = binding.edtInstansi
            var image = pick
            var edtUsername = binding.edtUsername
            var edtPassword = binding.edtPassword

            if (edtNamaPegawai.text.isNullOrEmpty()) {
                edtUsername.error = "Silahkan masukkan Nama lengkap Anda"
                edtUsername.requestFocus()
            } else if (edtNip.text.toString().isNullOrEmpty()) {
                edtNip.error = "Silahkan masukkan NIP Anda"
                edtNip.requestFocus()
            } else if (edtNik.text.toString().isNullOrEmpty()) {
                edtNik.error = "Silahkan masukkan NIK Anda"
                edtNik.requestFocus()
            } else if (rbJk?.text.toString().isNullOrEmpty()) {
                rbJk?.error = "Silahkan pilih jenis kelamin"
                rbJk?.requestFocus()
            } else if (edtTempat.text.toString().isNullOrEmpty()) {
                edtTempat.error = "Silahkan masukkan tempat lahir Anda"
                edtTempat.requestFocus()
            } else if (edtTglLahir.text.toString().isNullOrEmpty()) {
                edtTglLahir.error = "Silahkan masukkan tanggal lahir Anda"
                edtTglLahir.requestFocus()
            } else if (edtAlamat.text.toString().isNullOrEmpty()) {
                edtAlamat.error = "Silahkan masukkan Alamat Lengkap Anda"
                edtAlamat.requestFocus()
            } else if (edtJabatan.text.toString().isNullOrEmpty()) {
                edtJabatan.error = "Silahkan masukkan Jabatan Anda"
                edtJabatan.requestFocus()
            } else if (edtRuangan.text.toString().isNullOrEmpty()) {
                edtRuangan.error = "Silahkan masukkan Ruangan Anda"
                edtRuangan.requestFocus()
            } else if (image.toString().isNullOrEmpty()) {
                edtJabatan.error = "Silahkan masukkan Pas Foto Anda"
                binding.btnUpload.requestFocus()
            } else {
                var data = CompleteRequest(
                    edtNamaPegawai.text.toString(),
                    edtNip.text.toString(),
                    edtNik.text.toString(),
                    rbJk?.text.toString(),
                    edtTempat.text.toString(),
                    edtTglLahir.text.toString(),
                    edtUmur.text.toString(),
                    edtAlamat.text.toString(),
                    edtEmail.text.toString(),
                    edtJabatan.text.toString(),
                    edtRuangan.text.toString(),
                    edtInstansi.text.toString(),
                    edtUmur.text.toString(),
                    edtPassword.text.toString()
                )
                presenter.submitComplete(data)
            }
        }


    }

    private fun initProgressDialog() {
        progressDialog = context?.let { Dialog(it) }
        val inflater: LayoutInflater = this.layoutInflater
        val bind = LoadingItemBinding.inflate(inflater)

        progressDialog?.let {
            it.setContentView(bind.root)
            it.setCancelable(false)
            it.window?.setBackgroundDrawableResource(android.R.color.transparent)

        }
    }


    private fun initImagePicker() {
        cameraPermission =
            arrayOf(
                android.Manifest.permission.CAMERA,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
        storagePermission = arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)


        val options = arrayOf<CharSequence>(
            "Take photo", "Choose from Gallery",
            "Cancel"
        )
        val builder = AlertDialog.Builder(
            requireContext()
        )

        builder.setTitle("Select")

        builder.setItems(options) { dialog, which ->
            when {
                options[which] == "Take photo" -> {
                    requestPermissionsLauncher.launch(
                        arrayOf(
                            Manifest.permission.CAMERA,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                        )
                    )
                }
                options[which] == "Choose from Gallery" -> {

                    fileChooserContract.launch("image/*")

                }
                options[which] == "Cancel" -> {
                    dialog.dismiss()
                }
            }
        }
        val dialog = builder.create()
//        dialog.getWindow().getAttributes().windowAnimations = R.style.dialog_animation


        dialog.show()


    }


    private fun age(year: Int, month: Int, dayOfMonth: Int): String {
        var modCurrentDate = today.get(Calendar.DAY_OF_MONTH)
        var modCurrentMonth = today.get(Calendar.MONTH)
        var modCurrentYear = today.get(Calendar.YEAR)
        var ageDay: Int
        var ageMonth: Int
        var ageYear: Int

        if (dayOfMonth > modCurrentDate) {
            modCurrentDate += 30
            modCurrentMonth -= 1
            ageDay = modCurrentDate - dayOfMonth
        } else {
            ageDay = modCurrentDate - dayOfMonth

        }
        if (month > modCurrentMonth) {
            modCurrentMonth += 12
            modCurrentYear -= 1
            ageMonth = modCurrentMonth - month
        } else {
            ageMonth = modCurrentMonth - month

        }
        ageYear = modCurrentYear - year

        return ageYear.toString()


    }


    private val fileChooserContract =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            binding.image.visibility = View.VISIBLE
            Glide.with(this)
                .asBitmap()
                .load(uri)
                .into(binding.image)
            pick = uri

        }
//    private val filesChooserContract =
//        registerForActivityResult(ActivityResultContracts.GetMultipleContents()) { uriList ->
//            for (uri in uriList) {
//                Log.d(TAG, "onActivityResult: uri $uri")
//            }
//        }

    override fun onSubmitSuccess(registerResponse: RegisterResponse) {
//       presenter.imageUpload(pick!!)
        Toast.makeText(context, registerResponse.toString(), Toast.LENGTH_SHORT).show()
    }

    override fun onSubmitFailed(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    override fun onUploadCondition(message: String) {

      if(message == "Success")   view?.let { backAction(it) } else message
    }

    override fun showLoading() {
        progressDialog?.show()
    }

    override fun dismissLoading() {
        progressDialog?.dismiss()
    }


    private fun takeImage() {
        lifecycleScope.launchWhenStarted {
            getTmpFileUri().let { uri ->
                latestTmpUri = uri
                takeImageResult.launch(uri)
            }
        }
    }

    private fun getTmpFileUri(): Uri {
        val tmpFile = File.createTempFile("tmp_image_file", ".png").apply {
            createNewFile()
            deleteOnExit()
        }

        return FileProvider.getUriForFile(
            requireContext(),
            "${BuildConfig.APPLICATION_ID}.provider",
            tmpFile
        )
    }

    private val takeImageResult =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { isSuccess ->
            if (isSuccess) {
                latestTmpUri?.let { uri ->
                    binding.image.visibility = View.VISIBLE
                    Glide.with(this)
                        .asBitmap()
                        .load(uri)
                        .into(binding.image)
                }
            }
        }

    private val requestPermissionsLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            if (permissions.all { it.value }) {
                takeImage()
            } else
//            Toast.makeText(
//                context,
//                "You have not accepted all the permissions",
//                Toast.LENGTH_LONG
//            ).show()
                Toast.makeText(
                    context,
                    "Tidak dapat mengakses kamera",
                    Toast.LENGTH_LONG
                ).show()
        }

    private fun onBackPressed(view: View) {
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    backAction(view)
                }
            })
    }


    fun backAction(view: View) {
        Navigation.findNavController(view).popBackStack()
        (activity as AuthActivity)?.dispatchAction("VISIBLE")
    }

}






