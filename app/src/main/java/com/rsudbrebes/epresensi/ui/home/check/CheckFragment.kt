package com.rsudbrebes.epresensi.ui.home.check

import android.Manifest
import android.app.KeyguardManager
import android.content.ContentValues.TAG
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.biometrics.BiometricPrompt
import android.os.Build
import android.os.Bundle
import android.os.CancellationSignal
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.navigation.Navigation
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.gson.Gson
import com.rsudbrebes.epresensi.EPresensi
import com.rsudbrebes.epresensi.R

import com.rsudbrebes.epresensi.databinding.FragmentCheckBinding
import com.rsudbrebes.epresensi.model.request.AbsensiRequest
import com.rsudbrebes.epresensi.model.response.absensi.AbsensiResponse
import com.rsudbrebes.epresensi.model.response.login.User
import com.rsudbrebes.epresensi.ui.auth.AuthActivity


class CheckFragment : Fragment(), CheckContract.View {

    private lateinit var binding: FragmentCheckBinding
    lateinit var presenter: CheckPresenter
    private val LOCATION_PERMISSION_REQ_CODE = 1000;

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var mHandler: Handler

    private var cancellationSignal: CancellationSignal? = null

    var location: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCheckBinding.inflate(inflater, container, false)
        return binding.root
    }


    @RequiresApi(Build.VERSION_CODES.P)
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        presenter = CheckPresenter(this)
        initView()

        binding.tvLogout.setOnClickListener {
            EPresensi.getApp().setUser("")
            EPresensi.getApp().setActive("")
            val logout = Intent(activity!!,AuthActivity::class.java)
            startActivity(logout)
            activity?.finish()
        }


    }


    @RequiresApi(Build.VERSION_CODES.P)
    fun initView() {

        val user = EPresensi.getApp().getUser()
        var userResponse = Gson().fromJson(user, User::class.java)
        binding.tvUsername.text = userResponse.nama_lengkap
        presenter.checkAbsen(userResponse.kode_pegawai)
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

    private fun getCurrentLocation() {
        // checking location permission
        if (ActivityCompat.checkSelfPermission(
                activity!!,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            // request permission
            ActivityCompat.requestPermissions(
                activity!!,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQ_CODE
            );

            return

        }

        fusedLocationClient.lastLocation
            .addOnSuccessListener { location ->
                val latitude = location.latitude
                val longitude = location.longitude
                val latitudeTo = -6.874072867659325
                val longitudeTo = 109.04892526906285

                presenter.locationDistance(latitude, longitude, latitudeTo, longitudeTo)

            }
            .addOnFailureListener {
                Toast.makeText(
                    context, "Failed on getting current location",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }


    @RequiresApi(Build.VERSION_CODES.P)
    private fun checkBiometricSupport(): Boolean {

        val keyguardManager: KeyguardManager =
            context!!.getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager

        if (!keyguardManager.isKeyguardSecure) {
            notifyUser("Fingerprint has not been enabled in settings.")
            return false
        }
        if (ActivityCompat.checkSelfPermission(
                context!!,
                android.Manifest.permission.USE_BIOMETRIC
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            notifyUser("Fingerprint has not been enabled in settings.")
            return false
        }
        return if (context!!.packageManager.hasSystemFeature(PackageManager.FEATURE_FINGERPRINT)) {
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
                    val user = EPresensi.getApp().getUser()
                    var userResponse = Gson().fromJson(user, User::class.java)
                    var data = AbsensiRequest(
                        userResponse.nama_lengkap,
                        userResponse.kode_pegawai,
                        "Bekerja di Kantor",
                        location,
                        1
                    )

                    presenter.submitCheck(data)


                }
            }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onLongDistance(message: String) {
        binding.tvResult.text = message
    }

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onShortDistance(maps: String, message: String) {
        binding.tvResult.text = message
        location = maps

        val biometricPrompt: BiometricPrompt = BiometricPrompt.Builder(activity)
            .setTitle("Presensi dulu")
            .setSubtitle("Dibutuhkan bukti hadir")
            .setDescription("Gunakan sidik jari milik Anda")
            .setNegativeButton(
                "Batal",
                context!!.mainExecutor,
                DialogInterface.OnClickListener { dialog, which ->

                }).build()

        biometricPrompt.authenticate(
            getCancellationSignal(),
            context!!.mainExecutor,
            authenticationCallback
        )
    }


    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCheckAbsenSuccess(absensiResponse: AbsensiResponse) {
        binding.btnCheckIn.setBackgroundResource(R.drawable.btn_from_uncheck_style)
        binding.btnCheckIn.setTextAppearance(R.style.selamat_datang)
    }

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCheckAbsenFailed(message: String) {
        Log.d(TAG, "onCheckAbsenFailed: ${message}")
        if (message == "Jam Pulang") {
            binding.tvResult.text = message
            binding.btnCheckIn.setBackgroundResource(R.drawable.btn_from_uncheck_style)
            binding.btnCheckIn.setTextAppearance(R.style.selamat_datang)
            binding.btnCheckOut.setBackgroundResource(R.drawable.btn_from_checkout_style)
            binding.btnCheckOut.setTextAppearance(R.style.login)
            binding.btnCheckOut.setOnClickListener {
                fusedLocationClient =
                    LocationServices.getFusedLocationProviderClient(requireActivity())
                getCurrentLocation()
                checkBiometricSupport()
            }
        } else if (message == "Belum Absen") {
            binding.tvResult.text = message
            binding.btnCheckIn.setBackgroundResource(R.drawable.btn_from_checkin_style)
            binding.btnCheckIn.setTextAppearance(R.style.login)
            binding.btnCheckIn.setOnClickListener {
                fusedLocationClient =
                    LocationServices.getFusedLocationProviderClient(requireActivity())
                getCurrentLocation()
                checkBiometricSupport()
            }
        }

    }

    override fun onCheckSuccess(absensiResponse: AbsensiResponse) {
        view?.let { Navigation.findNavController(it).navigate(R.id.action_check_success) }
        Toast.makeText(context, "Anda Berhasi Absen Hari ini", Toast.LENGTH_SHORT).show()
    }

    override fun onCheckFailed(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }


    override fun showLoading() {

    }

    override fun dismissLoading() {

    }

}