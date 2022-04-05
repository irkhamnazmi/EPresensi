package com.rsudbrebes.epresensi.ui.auth.signup

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.gson.Gson
import com.rsudbrebes.epresensi.EPresensi
import com.rsudbrebes.epresensi.R
import com.rsudbrebes.epresensi.databinding.FragmentSignupBinding
import com.rsudbrebes.epresensi.model.request.AbsensiRequest
import com.rsudbrebes.epresensi.model.request.RegisterRequest
import com.rsudbrebes.epresensi.model.response.login.LoginResponse
import com.rsudbrebes.epresensi.model.response.register.RegisterResponse
import com.rsudbrebes.epresensi.model.response.user.User
import com.rsudbrebes.epresensi.ui.MainActivity
import com.rsudbrebes.epresensi.ui.auth.AuthActivity
import com.rsudbrebes.epresensi.ui.auth.signin.SigninPresenter
import java.util.regex.Pattern

class SignupFragment : Fragment(), SignupContract.View {

    private lateinit var binding: FragmentSignupBinding
    lateinit var presenter: SignupPresenter
    private val emailPattern = Pattern.compile(
        "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                "\\@" +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                "(" +
                "\\." +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                ")+"
    )


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSignupBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        presenter = SignupPresenter(this)
        (activity as AuthActivity)?.dispatchInformations(activity?.getString(R.string.version))
        binding.tvLogin.setOnClickListener {
            val signin = Intent(activity, AuthActivity::class.java)
            signin.putExtra("page_request", 2)
            startActivity(signin)
        }

        binding.btnSignup.setOnClickListener {
//            var fullName = binding.edtFullname
            var username = binding.edtUsername
            var password = binding.edtPassword
            var email = binding.edtEmail

            if (username.text.toString().isNullOrEmpty()) {
                username.error = "Silahkan masukkan Username Anda"
                username.requestFocus()
            } else if (password.text.toString().isNullOrEmpty()) {
                password.error = "Silahkan masukkan password Anda"
                password.requestFocus()
            } else if (email.text.toString().isNullOrEmpty()) {
                email.error = "Silahkan masukkan Email Anda"
                email.requestFocus()
            } else if (!isEmailValid(email.text.toString())) {
                email.error = "Email Anda Salah"
                email.requestFocus()
            } else {
                var data = RegisterRequest(
                    "",
                    username.text.toString(),
                    password.text.toString(),
                    email.text.toString()
                )
                presenter.submitSignUp(data)
            }
        }

    }

    private fun isEmailValid(email: String): Boolean {
        return emailPattern.toRegex().matches(email);
    }


    override fun onSignUpSuccess(registerResponse: RegisterResponse) {
        val gson = Gson()
        val json = gson.toJson(registerResponse.register)
        EPresensi.getApp().setUser(json)
        startActivity(Intent(activity, AuthActivity::class.java))
        activity?.finish()
    }

    override fun onSignUpFailed(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    override fun showLoading() {

    }

    override fun dismissLoading() {

    }


}