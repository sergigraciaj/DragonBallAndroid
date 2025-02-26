package com.keepcoding.dragonball.repository

import android.content.SharedPreferences
import com.google.gson.Gson
import com.keepcoding.model.Character
import com.keepcoding.model.CharacterDto
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request

class CharactersRepository {
    private val BASE_URL = "https://dragonball.keepcoding.education/api/"
    private var characterList = listOf<Character>()

    sealed class CharactersResponse {
        data class Success(val characters: List<Character>) : CharactersResponse()
        data class Error(val message: String) : CharactersResponse()
    }

    fun fetchCharacters(token: String, sharedPreferences: SharedPreferences? = null): CharactersResponse {
        if (characterList.isNotEmpty())
            return CharactersResponse.Success(characterList)

        sharedPreferences?.let {
            val characterListJson = it.getString("characterList", "")
            val characters: Array<Character>? =
                Gson().fromJson(characterListJson, Array<Character>::class.java)
            if(!characters.isNullOrEmpty()) return CharactersResponse.Success(characters.toList())
        }

        val client = OkHttpClient()
        val url = "${BASE_URL}heros/all"

        val formBody = FormBody.Builder()
            .add("name", "")
            .build()

        val request = Request.Builder()
            .url(url)
            .post(formBody)
            .addHeader("Authorization", "Bearer $token")
            .build()

        val call = client.newCall(request)
        val response = call.execute()

        return if (response.isSuccessful) {
            val characterDto: Array<CharacterDto> =
                Gson().fromJson(response.body?.string(), Array<CharacterDto>::class.java)

            characterList = characterDto.map {
                Character(
                    id = it.id,
                    name = it.name,
                    imageUrl = it.photo,
                    currentLife = 100,
                    totalLife = 100,
                )
            }
            sharedPreferences?.edit()?.apply {
                putString("CharacterList", Gson().toJson(characterList))
                apply()
            }
            CharactersResponse.Success(characterList)
        } else {
            CharactersResponse.Error("Error fetching characters. ${response.message}")
        }
    }
}