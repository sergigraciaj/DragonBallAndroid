package com.keepcoding.dragonball.game.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.keepcoding.dragonball.databinding.FragmentListBinding
import com.keepcoding.dragonball.game.GameViewModel
import kotlinx.coroutines.launch
import androidx.fragment.app.activityViewModels
import com.keepcoding.dragonball.game.GameOptions
import kotlinx.coroutines.Job


class ListFragment: Fragment() {

    private val characterAdapter = CharacterAdapter(
        onCharacterClicked = { character ->
            viewModel.selectedCharacter(character)
        }
    )
    private val viewModel: GameViewModel by activityViewModels()

    private lateinit var binding: FragmentListBinding
    private var job: Job? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentListBinding.inflate(inflater, container, false)
        initViews()
        initObservers()
        return binding.root
    }

    private fun initViews() {
        binding.rvCharacters.layoutManager = LinearLayoutManager(this.context)
        binding.rvCharacters.adapter = characterAdapter
    }

    private fun initObservers() {
        job = lifecycleScope.launch {
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
                    is GameViewModel.State.SelectedCharacter -> {
                        (activity as? GameOptions)?.goToDetail()
                    }
                }
            }
        }
    }

    override fun onStop() {
        super.onStop()
        job?.cancel()
    }

}