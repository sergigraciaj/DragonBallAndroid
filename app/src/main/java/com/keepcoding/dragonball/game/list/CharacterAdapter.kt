package com.keepcoding.dragonball.game.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.keepcoding.dragonball.R
import com.keepcoding.dragonball.databinding.CharacterItemBinding
import com.keepcoding.model.Character

class CharacterAdapter(
    private var onCharacterClicked: (Character) -> Unit,
): RecyclerView.Adapter<CharacterAdapter.CharacterViewHolder>() {

    private var characters = listOf<Character>()

    fun updateCharacters(characters: List<Character>) {
        this.characters = characters
        notifyDataSetChanged()
    }

    class CharacterViewHolder(
        private val binding: CharacterItemBinding,
        private var onCharacterClicked: (Character) -> Unit,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(character: Character) {
            binding.tvName.text = character.name
            Glide
                .with(binding.root)
                .load(character.imageUrl)
                .centerInside()
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(binding.ivPhoto)
            binding.tvLife.text = "${character.currentLife} / ${character.totalLife}"
            binding.root.setOnClickListener {
                onCharacterClicked(character)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterViewHolder {
        return CharacterViewHolder(
            binding = CharacterItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            onCharacterClicked = onCharacterClicked,
        )
    }

    override fun getItemCount(): Int {
        return characters.size
    }

    override fun onBindViewHolder(holder: CharacterViewHolder, position: Int) {
        holder.bind(characters[position])
    }

}
