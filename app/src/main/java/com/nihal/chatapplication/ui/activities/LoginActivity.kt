package com.nihal.chatapplication.ui.activities

import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.nihal.chatapplication.R
import com.nihal.chatapplication.databinding.ActivityLoginBinding
import com.nihal.chatapplication.utils.LoadingDialog
import com.nihal.chatapplication.utils.Status
import com.nihal.chatapplication.viewmodel.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var loginViewModel: LoginViewModel
    private lateinit var loadingDialog: LoadingDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        loginViewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
        loadingDialog = LoadingDialog(this)
        setListeners()
    }

    private fun setListeners(){
        binding.loginButton.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()
            loginViewModel.signInUser(email, password).observe(this, {
                when (it.status) {
                    Status.SUCCESS -> {
                        loadingDialog.dismissLoadingDialog()
                        login()
                    }
                    Status.LOADING -> {
                        loadingDialog.showLoadingDialog()
                    }
                    Status.ERROR -> {
                        loadingDialog.dismissLoadingDialog()
                        it.message?.let { errorMessage ->
                            Snackbar.make(
                                binding.constraintLayout,
                                errorMessage,
                                Snackbar.LENGTH_LONG
                            ).show()
                        }
                    }
                }
            })
        }

        binding.constraintLayout.setOnClickListener {
            hideKeyboard()
        }

        binding.passwordEditText.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                hideKeyboard()
                binding.loginButton.isFocusable = true
            }
            false
        }

        binding.createAccountTextView.setOnClickListener {
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
        }
    }

    private fun login() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun hideKeyboard() {
        val view = this.currentFocus
        if (view != null) {
            val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
}