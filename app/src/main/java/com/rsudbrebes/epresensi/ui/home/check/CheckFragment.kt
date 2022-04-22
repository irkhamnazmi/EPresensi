package com.rsudbrebes.epresensi.ui.home.check

import android.Manifest
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.KeyguardManager
import android.content.ContentValues.TAG
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.hardware.biometrics.BiometricPrompt
import android.os.Build
import android.os.Bundle
import android.os.CancellationSignal
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bumptech.glide.Glide
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.gson.Gson
import com.rsudbrebes.epresensi.BuildConfig.BASE_URL
import com.rsudbrebes.epresensi.EPresensi
import com.rsudbrebes.epresensi.R
import com.rsudbrebes.epresensi.databinding.FragmentCheckBinding
import com.rsudbrebes.epresensi.databinding.ShowdialogCheckSuccessBinding
import com.rsudbrebes.epresensi.model.request.AbsensiRequest
import com.rsudbrebes.epresensi.model.response.absensi.AbsensiResponse
import com.rsudbrebes.epresensi.model.response.user.User
import com.rsudbrebes.epresensi.ui.auth.AuthActivity
import java.text.SimpleDateFormat
import java.util.*


class CheckFragment : Fragment(), CheckContract.View {

    private lateinit var binding: FragmentCheckBinding
    lateinit var presenter: CheckPresenter
    private val LOCATION_PERMISSION_REQ_CODE = 1000;

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var mHandler: Handler
    lateinit var tvUsername: TextView
    lateinit var tvJabatan: TextView
    lateinit var tvNip: TextView

    private var cancellationSignal: CancellationSignal? = null
    val user = EPresensi.getApp().getUser()
    var userResponse = Gson().fromJson(user, User::class.java)

    var location: String = ""
    var keterangan: String = ""
    var shift: Int = 0
    var absenStatus = ""
    private lateinit var alertDialog: AlertDialog



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCheckBinding.inflate(inflater, container, false)
        initPresenter()

        return binding.root
    }

    private fun initPresenter() {
        presenter = CheckPresenter(this)
        presenter.spinner(requireContext())
    }


    @RequiresApi(Build.VERSION_CODES.P)
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initView()
        initLogout()

    }




    private fun initLogout() {
        binding.tvLogout.setOnClickListener {
            EPresensi.getApp().setUser("")
            EPresensi.getApp().setActive("")
            val logout = Intent(requireActivity(), AuthActivity::class.java)
            startActivity(logout)
            activity?.finish()
        }
    }


    @RequiresApi(Build.VERSION_CODES.P)
    fun initView() {
        tvUsername = binding.tvUsername
        tvJabatan = binding.tvJabatan
        tvNip = binding.tvNip

        Glide.with(this).load(BASE_URL + "storage/profile/${userResponse.image}")
            .into(binding.imageProfil)

        tvUsername.text = "Nama : ${userResponse.nama_lengkap}"
        tvNip.text = "NIP : ${userResponse.kode_pegawai}"
        tvJabatan.text = "Jabatan : ${userResponse.jabatan}"

        presenter.checkAbsen(userResponse.kode_pegawai)

        statusKet()
    }


    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        when (requestCode) {
            LOCATION_PERMISSION_REQ_CODE -> {
                if (grantResults.isNotEmpty() &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED
                ) {
                    // permission granted
                } else {
                    // permission denied
                    Toast.makeText(
                        context, "You need to grant permission to access location",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

//    private fun getCurrentLocation() {
//        // checking location permission
//        if (ActivityCompat.checkSelfPermission(
//                requireActivity(),
//                Manifest.permission.ACCESS_FINE_LOCATION
//            ) != PackageManager.PERMISSION_GRANTED
//        ) {
//
//            // request permission
//            ActivityCompat.requestPermissions(
//                requireActivity(),
//                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQ_CODE
//            );
//
//            return
//
//        }
//
//        fusedLocationClient.lastLocation
//            .addOnSuccessListener { location ->
//                val latitude = location.latitude
//                val longitude = location.longitude
//                val latitudeTo = -6.874072867659325
//                val longitudeTo = 109.04892526906285
//
////                presenter.locationDistance(latitude, longitude, latitudeTo, longitudeTo)
//
//            }
//            .addOnFailureListener {
//                Toast.makeText(
//                    context, "Failed on getting current location",
//                    Toast.LENGTH_SHORT
//                ).show()
//            }
//    }


    @RequiresApi(Build.VERSION_CODES.P)
    private fun checkBiometricSupport(): Boolean {

        val keyguardManager: KeyguardManager =
            requireContext().getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager

        if (!keyguardManager.isKeyguardSecure) {
            notifyUser("Fingerprint has not been enabled in settings.")
            return false
        }
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.USE_BIOMETRIC
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            notifyUser("Fingerprint has not been enabled in settings.")
            return false
        }
        return if (requireContext().packageManager.hasSystemFeature(PackageManager.FEATURE_FINGERPRINT)) {
            true
        } else true
    }

    private fun notifyUser(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    private fun getCancellationSignal(): CancellationSignal {
        cancellationSignal = CancellationSignal()
        cancellationSignal?.setOnCancelListener {
            notifyUser("Authentication was cancelled by the user")
        }
        return cancellationSignal as CancellationSignal
    }


    private val authenticationCallback: BiometricPrompt.AuthenticationCallback
        get() =
            @RequiresApi(Build.VERSION_CODES.P)
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(errorCode: Int, errString: CharSequence?) {
                    super.onAuthenticationError(errorCode, errString)
//                    notifyUser("Authentication error: $errString")
                    notifyUser("Sidik jari Anda kurang pas: $errString")
                }

                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult?) {
                    super.onAuthenticationSucceeded(result)
//                    notifyUser("Anda berhasil hadir hari ini")
                    postData()

                }
            }

//    @RequiresApi(Build.VERSION_CODES.M)
//    override fun onLongDistance(message: String) {
////        binding.tvResult.text = message
//        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
//    }
//
//    @RequiresApi(Build.VERSION_CODES.P)
//    override fun onShortDistance(maps: String, message: String) {
////        binding.tvResult.text = message
//        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
//        location = maps
//
//        val biometricPrompt: BiometricPrompt = BiometricPrompt.Builder(activity)
//            .setTitle("Presensi dulu")
//            .setSubtitle("Dibutuhkan bukti hadir")
//            .setDescription("Gunakan sidik jari milik Anda")
//            .setNegativeButton(
//                "Batal",
//                context!!.mainExecutor,
//                DialogInterface.OnClickListener { dialog, which ->
//
//                }).build()
//
//        biometricPrompt.authenticate(
//            getCancellationSignal(),
//            context!!.mainExecutor,
//            authenticationCallback
//        )
//    }


    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCheckAbsenSuccess(absensiResponse: AbsensiResponse) {
        binding.btnCheckIn.setBackgroundResource(R.drawable.btn_from_uncheck_style)
        binding.btnCheckOut.setBackgroundResource(R.drawable.btn_from_uncheck_style)
        binding.btnSubmit.setBackgroundResource(R.drawable.btn_from_uncheck_style)
        binding.btnCheckIn.setTextAppearance(R.style.selamat_datang)
    }

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCheckAbsenFailed(message: String) {
        Log.d(TAG, "onCheckAbsenFailed: ${message}")
        if (message == "Jam Pulang") {
//            binding.tvResult.text = message

            binding.btnCheckIn.setBackgroundResource(R.drawable.btn_from_uncheck_style)
            binding.btnCheckIn.setTextAppearance(R.style.selamat_datang)
            binding.btnCheckOut.setBackgroundResource(R.drawable.btn_from_checkout_style)
            binding.btnCheckOut.setTextColor(Color.parseColor("#FFFFFF"))
            binding.btnCheckOut.setOnClickListener {
//                fusedLocationClient =
//                    LocationServices.getFusedLocationProviderClient(requireActivity())
//                getCurrentLocation()
                absenStatus = "CHECK OUT"
//                checkBiometricSupport()
//                val biometricPrompt: BiometricPrompt = BiometricPrompt.Builder(activity)
//                    .setTitle("Presensi dulu")
//                    .setSubtitle("Dibutuhkan bukti hadir")
//                    .setDescription("Gunakan sidik jari milik Anda")
//                    .setNegativeButton(
//                        "Batal",
//                        requireContext().mainExecutor,
//                        DialogInterface.OnClickListener { dialog, which ->
//
//                        }).build()
//
//                biometricPrompt.authenticate(
//                    getCancellationSignal(),
//                    requireContext().mainExecutor,
//                    authenticationCallback
//                )

                postData()
            }
        } else if (message == "Belum Absen") {
//            binding.tvResult.text = message
            binding.btnCheckIn.setBackgroundResource(R.drawable.btn_from_checkin_style)
            binding.btnCheckIn.setTextAppearance(R.style.login)
            binding.btnCheckIn.setOnClickListener {
//                fusedLocationClient =
//                    LocationServices.getFusedLocationProviderClient(requireActivity())
//                getCurrentLocation()
                absenStatus = "CHECK IN"
//                checkBiometricSupport()
//                val biometricPrompt: BiometricPrompt = BiometricPrompt.Builder(activity)
//                    .setTitle("Presensi dulu")
//                    .setSubtitle("Dibutuhkan bukti hadir")
//                    .setDescription("Gunakan sidik jari milik Anda")
//                    .setNegativeButton(
//                        "Batal",
//                        requireContext().mainExecutor,
//                        DialogInterface.OnClickListener { dialog, which ->
//
//                        }).build()
//
//                biometricPrompt.authenticate(
//                    getCancellationSignal(),
//                    requireContext().mainExecutor,
//                    authenticationCallback
//                )

                postData()


            }
            binding.btnSubmit.setOnClickListener {
                absenStatus = "Konfirmasi ${keterangan}"
                postData()
            }
        }

    }

    override fun showKetAbsen(adapter: ArrayAdapter<CharSequence>) {
        binding.spKetAbsen.adapter = adapter
        binding.spKetAbsen.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                keterangan = ""
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val x = parent?.getItemAtPosition(position).toString()
                keterangan = if (x == "Ket.Absen") "" else x
                statusKet()
            }

        }

    }


    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCheckSuccess(absensiResponse: AbsensiResponse, status : String) {
//        view?.let { Navigation.findNavController(it).navigate(R.id.action_check_success) }
        showCustomDialog("Success", status)

    }

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCheckFailed(message: String, status: String) {
        showCustomDialog("Failed", status)
    }


    override fun showLoading() {

    }

    override fun dismissLoading() {

    }

    private fun postData() {
        val user = EPresensi.getApp().getUser()
        if (keterangan.isEmpty()) {
            Toast.makeText(context, "Harap dipilih keterangan Absen", Toast.LENGTH_SHORT).show()
        } else {
            var userResponse = Gson().fromJson(user, User::class.java)
            var data = AbsensiRequest(
                userResponse.nama_lengkap,
                userResponse.kode_pegawai,
                keterangan,
                binding.edtAlasan.text.toString(),
                location,
                1
            )

            presenter.submitCheck(data, absenStatus)
        }
    }

    private fun statusKet() {
        if (keterangan == "" || keterangan == "Hadir") {
            binding.lnCheck.visibility = View.VISIBLE
            binding.lnFormAlasan.visibility = View.GONE
            binding.edtAlasan.setText("")
        } else {
            binding.lnCheck.visibility = View.GONE
            binding.lnFormAlasan.visibility = View.VISIBLE
            binding.edtAlasan.setText("")

        }
    }

    @RequiresApi(Build.VERSION_CODES.P)
    fun showCustomDialog(condition: String, status: String) {
        val inflater: LayoutInflater = this.layoutInflater
        val bind: ShowdialogCheckSuccessBinding = ShowdialogCheckSuccessBinding.inflate(inflater)

        val dialogBuilder: AlertDialog.Builder =
            AlertDialog.Builder(context, R.style.fullscreenalert)
        dialogBuilder.setOnDismissListener { }
        dialogBuilder.setView(bind.root)
        if (condition == "Success") {
            bind.tvAlert.text = "Selamat Anda"
            bind.tvSuccess.text = "Berhasil"

        } else {
            bind.tvAlert.text = "Maaf Anda"
            bind.tvSuccess.text = "Gagal"
        }
        var color = ""
        color = if(status == "CHECK IN") "#00fc28" else "#f44336"
        bind.tvCheck.setTextColor(Color.parseColor(color))
        bind.tvCheck.text = status
        Handler().postDelayed({
            alertDialog.dismiss()
            initPresenter()
            initView()
        }, 2000)

        alertDialog = dialogBuilder.create();
//        alertDialog.window!!.getAttributes().windowAnimations = R.style.PauseDialogAnimation
        alertDialog.window!!.getAttributes().windowAnimations =
            R.style.Animation_Design_BottomSheetDialog
//        alertDialog.window!!.setBackgroundDrawableResource(android.R.color.transparent);
        alertDialog.show()
    }


}