package com.keepcoding.dragonball.login

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.keepcoding.dragonball.game.GameActivity
import com.keepcoding.dragonball.databinding.ActivityLoginBinding
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {
    private val viewModel : LoginViewModel by viewModels()
    private lateinit var binding : ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setObservers()
        viewModel.saveUser(
            preferences = getSharedPreferences("LoginPreferences", MODE_PRIVATE),
            user = "test",
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
                        startGameActivity()
                    }
                    is LoginViewModel.State.Error -> {
                        binding.pbLoading.visibility = View.INVISIBLE
                        Toast.makeText(this@LoginActivity, "Error: ${state.message} ${state.errorCode}", Toast.LENGTH_LONG).show()
                    }
                }

            }
        }
    }

    private fun startGameActivity() {
        GameActivity.startGameActivity(this)
        finish()
    }

}