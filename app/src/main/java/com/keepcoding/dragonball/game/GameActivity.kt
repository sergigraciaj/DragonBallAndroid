package com.keepcoding.dragonball.game

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.keepcoding.dragonball.databinding.ActivityGameBinding
import com.keepcoding.dragonball.game.detail.DetailFragment
import com.keepcoding.dragonball.game.list.ListFragment
import kotlin.random.Random

interface GameOptions{
    fun goToList()
    fun goToDetail()
}

class GameActivity : AppCompatActivity(), GameOptions {
    companion object{
        fun startGameActivity(context: Context) {
            val intent = Intent(context, GameActivity::class.java)
            context.startActivity(intent)
        }
    }

    private val viewModel: GameViewModel by viewModels()
    private lateinit var binding: ActivityGameBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.downloadCharacters(getSharedPreferences("my_preferences", Context.MODE_PRIVATE))
        initFragments()
    }

    private fun initFragments() {
        goToList()
    }

    override fun goToList() {
        supportFragmentManager.beginTransaction().apply {
            replace(binding.flHome.id, ListFragment())
            commit()
        }
    }

    override fun goToDetail() {
        supportFragmentManager.beginTransaction().apply {
            replace(binding.flHome.id, DetailFragment())
            addToBackStack(Random.nextInt().toString())
            commit()
        }
    }
}