package com.keepcoding.dragonball

import app.cash.turbine.test
import com.keepcoding.dragonball.game.GameViewModel
import com.keepcoding.dragonball.game.GameViewModel.State
import com.keepcoding.model.Character
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class GameViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    private val viewModel = GameViewModel()

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }


    val initialCharacter = Character(
        id = "1",
        name = "Nombre",
        imageUrl = "----",
        totalLife = 100,
        currentLife = 100,
        timesSelected = 0,
    )

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `damageCharacter substracts life from character`() {
        viewModel.damageCharacter(initialCharacter)
        assertTrue( initialCharacter.currentLife < 100)
    }

    @Test
    fun `when character is selected, state is updated with that character`() = runTest {
        viewModel.uiState.test {
            assertEquals(State.Loading, awaitItem())
            viewModel.selectedCharacter(initialCharacter)
            assertEquals(State.SelectedCharacter(initialCharacter), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `when character is unselected, state is updated to success`() = runTest {
        viewModel.uiState.test {
            viewModel.userRepository.setToken("eyJraWQiOiJwcml2YXRlIiwidHlwIjoiSldUIiwiYWxnIjoiSFMyNTYifQ.eyJleHBpcmF0aW9uIjo2NDA5MjIxMTIwMCwiZW1haWwiOiJjZHRsQHBydWVibWFpbC5lcyIsImlkZW50aWZ5IjoiRDIwRTAwQTktODY0NC00MUYyLUE0OUYtN0ZDRUY2MTVFMTQ3In0.wMqJfh5qcs5tU6hu2VxT4OV9Svd7BGBA7HsVpKhx5-8")
            assertEquals(State.Loading, awaitItem())
            viewModel.unselectedCharacter()
            assertTrue(awaitItem() is State.Success)
            cancelAndIgnoreRemainingEvents()
        }
    }

}