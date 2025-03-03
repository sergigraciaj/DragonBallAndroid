package com.keepcoding.dragonball.game.detail

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.keepcoding.dragonball.R
import com.keepcoding.dragonball.databinding.FragmentDetailBinding
import com.keepcoding.dragonball.game.GameViewModel
import com.keepcoding.model.Character
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class DetailFragment: Fragment() {

    private lateinit var binding: FragmentDetailBinding
    private val viewModel: GameViewModel by activityViewModels()
    private var job: Job? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailBinding.inflate(inflater, container, false)
        initObservers()
        return binding.root
    }

    private fun initViews(character: Character) {
        with(binding) {
            Glide
                .with(binding.root)
                .load(character.imageUrl)
                .centerInside()
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(binding.ivPhoto)
            tvName.text = character.name
            pbLife.progress = character.currentLife
            bDamage.setOnClickListener {
                viewModel.damageCharacter(character)
                pbLife.progress = character.currentLife

                if(character.isDead()) {
                    Toast.makeText(binding.root.context, "Fight is over", Toast.LENGTH_LONG).show()
                    parentFragmentManager.popBackStack()
                }
            }
            bHeal.setOnClickListener {
                viewModel.healCharacter(character)
                pbLife.progress = character.currentLife
            }

            bTimesSelected.setOnClickListener {
                Toast.makeText(binding.root.context, "Selected ${character.timesSelected} times", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun initObservers() {
        job = lifecycleScope.launch {
            viewModel.uiState.collect{ state ->
                when(state){
                    is GameViewModel.State.SelectedCharacter -> {
                        initViews(state.character)
                    }
                    else -> Unit
                }
            }
        }
    }

    override fun onStop() {
        super.onStop()
        job?.cancel()
        viewModel.unselectedCharacter()
    }
}