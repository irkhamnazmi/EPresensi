package com.rsudbrebes.epresensi.ui.auth.signin

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import com.google.gson.Gson
import com.rsudbrebes.epresensi.EPresensi
import com.rsudbrebes.epresensi.R
import com.rsudbrebes.epresensi.databinding.FragmentSigninBinding
import com.rsudbrebes.epresensi.model.response.login.LoginResponse
import com.rsudbrebes.epresensi.model.response.login.User
import com.rsudbrebes.epresensi.ui.MainActivity
import com.rsudbrebes.epresensi.ui.auth.AuthActivity

class SigninFragment : Fragment(), SigninContract.View {

    private lateinit var binding: FragmentSigninBinding
    lateinit var presenter: SigninPresenter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSigninBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        presenter = SigninPresenter(this)

        if(!EPresensi.getApp().getUser().isNullOrEmpty()){
            val submit = Intent(activity, AuthActivity::class.java)
            submit.putExtra("page_request", 2)
            startActivity(submit)
        }

        initDummy()

        binding.tvRegister.setOnClickListener {
            view?.let { Navigation.findNavController(it).navigate(R.id.action_signup) }
        }

        binding.btnSignin.setOnClickListener {
            var username = binding.edtUsername
            var password = binding.edtPassword

            if (username.text.toString().isNullOrEmpty()) {
                username.error = "Silahkan masukkan username Anda"
                username.requestFocus()
            } else if (password.text.toString().isNullOrEmpty()) {
                password.error = "Silahkan masukkan password Anda"
                password.requestFocus()
            } else{
                presenter.submitLogin(username.text.toString() , password.text.toString())
            }


        }

    }

    override fun onLoginSuccess(loginResponse: LoginResponse) {

        val gson = Gson()
        val json = gson.toJson(loginResponse.user)
        EPresensi.getApp().setUser(json)
        var userResponse = Gson().fromJson(json, User::class.java)
        val submit = Intent(activity, AuthActivity::class.java)
        submit.putExtra("page_request", 0)
        submit.putExtra("full_name", userResponse.nama_lengkap)
        submit.putExtra("username", userResponse.username)
        startActivity(submit)
        activity?.finishAffinity()

    }

    override fun onLoginFailed(message: String) {

        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
    }

    private fun initDummy(){
        binding.edtUsername.setText("admin")
        binding.edtPassword.setText("12345678")
    }

    override fun showLoading() {

    }

    override fun dismissLoading() {

    }


}