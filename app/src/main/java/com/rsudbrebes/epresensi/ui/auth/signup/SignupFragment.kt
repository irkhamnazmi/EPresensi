package com.rsudbrebes.epresensi.ui.auth.signup

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.rsudbrebes.epresensi.databinding.FragmentSignupBinding
import com.rsudbrebes.epresensi.model.response.login.LoginResponse
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
        binding.tvLogin.setOnClickListener {
            val signin = Intent(activity, AuthActivity::class.java)
            signin.putExtra("page_request",2)
            startActivity(signin)
        }

        binding.btnSignup.setOnClickListener {
            var fullName = binding.edtFullname
            var username = binding.edtUsername
            var password = binding.edtPassword
            var email = binding.edtEmail

            if (fullName.text.toString().isNullOrEmpty()) {
                fullName.error = "Silahkan masukkan Nama Lengkap Anda"
                fullName.requestFocus()
            }else if (username.text.toString().isNullOrEmpty()) {
                username.error = "Silahkan masukkan Username Anda"
                username.requestFocus()
            }else if (password.text.toString().isNullOrEmpty()) {
                password.error = "Silahkan masukkan password Anda"
                password.requestFocus()
            }else if (email.text.toString().isNullOrEmpty()) {
                email.error = "Silahkan masukkan Email Anda"
                email.requestFocus()
            }else if (!isEmailValid(email.text.toString())) {
                email.error = "Email Anda Salah"
                email.requestFocus()
            } else{
                presenter.getRegister(email.toString())
            }
        }

    }
   private fun isEmailValid(email: String): Boolean {
        return emailPattern.toRegex().matches(email);
    }

    override fun onSignUpSuccess(loginResponse: LoginResponse) {

    }

    override fun onSignUpFailed(message: String) {

    }

    override fun showLoading() {

    }

    override fun dismissLoading() {

    }


}