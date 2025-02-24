package com.keepcoding.dragonball

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.keepcoding.dragonball.databinding.ActivityLoginBinding
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {
    private val viewModel : LoginViewModel by viewModels()
    private lateinit var binding : ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        setObservers()
        viewModel.saveUser(
            preferences = getSharedPreferences("LoginPreferences", MODE_PRIVATE),
            user = "pepe",
            password = "1234"
        )

        binding.bLogin.setOnClickListener {
            viewModel.startLogin(
                user = binding.etUser.text.toString(),
                password = binding.etPassword.text.toString()
            )
        }
    }

    private fun setObservers() {
        lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                when(state){
                    is LoginViewModel.State.Idle -> {}
                    is LoginViewModel.State.Loading -> {
                        binding.pbLoading.visibility = View.VISIBLE
                    }
                    is LoginViewModel.State.Success -> {
                        binding.pbLoading.visibility = View.INVISIBLE
                        Toast.makeText(this@LoginActivity, "El token es. ${state.token}", Toast.LENGTH_LONG).show()
                        startGameActivity(state.token)
                    }
                    is LoginViewModel.State.Error -> {
                        binding.pbLoading.visibility = View.INVISIBLE
                        Toast.makeText(this@LoginActivity, "Ha ocurrido un error. ${state.message} ${state.errorCode}", Toast.LENGTH_LONG).show()
                    }
                }

            }
        }
    }

    private fun startGameActivity(token: String) {
        GameActivity.startGameActivity(this, token)
    }

}