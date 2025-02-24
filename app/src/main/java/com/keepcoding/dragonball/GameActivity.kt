package com.keepcoding.dragonball

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.keepcoding.dragonball.databinding.ActivityGameBinding
import kotlinx.coroutines.launch

class GameActivity : AppCompatActivity() {


    private val viewModel: GameViewModel by viewModels()
    private lateinit var binding: ActivityGameBinding
    private val characterAdapter = CharacterAdapter(
        onCharacterClicked = { character ->
            Toast.makeText(this, character.name, Toast.LENGTH_SHORT).show()
        }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initViews()
        setObservers()

        viewModel.downloadCharacters()
    }

    private fun initViews() {
        binding.rvCharacters.layoutManager = LinearLayoutManager(this)
        binding.rvCharacters.adapter = characterAdapter
    }

    private fun setObservers() {
        lifecycleScope.launch {
            viewModel.uiState.collect{ state ->
                when(state){
                    is GameViewModel.State.Loading -> {
                        binding.pbLoading.visibility = View.VISIBLE
                    }
                    is GameViewModel.State.Success -> {
                        binding.pbLoading.visibility = View.GONE
                        characterAdapter.updateCharacters(state.characters)
                    }
                    is GameViewModel.State.Error -> {
                        binding.pbLoading.visibility = View.GONE
                    }
                }
            }
        }
    }
}