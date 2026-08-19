package com.callme.feature.auth.presentation

import app.cash.turbine.test
import com.callme.domain.utils.Result
import com.callme.feature.auth.domain.model.AuthEffect
import com.callme.feature.auth.domain.model.AuthUiState
import com.callme.feature.auth.domain.usecase.LoginUseCase
import com.callme.feature.auth.domain.usecase.RegisterUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AuthViewModelTest {
    private val testDispatcher = StandardTestDispatcher()

    private lateinit var viewModel: AuthViewModel
    private lateinit var loginUseCase: LoginUseCase
    private lateinit var registerUseCase: RegisterUseCase

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        loginUseCase = mockk()
        registerUseCase = mockk()
        viewModel = AuthViewModel(loginUseCase, registerUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `login should emit NavigateToMain event on success`() = runTest {
        coEvery { loginUseCase(any(), any()) } returns Result.Success(Unit)

        viewModel.login("example@gmail.com", "qwerty123")

        viewModel.effect.test {
            val effect = awaitItem()
            assertTrue(effect is AuthEffect.NavigateToMain)
            cancelAndConsumeRemainingEvents()
        }

        assertEquals(AuthUiState.Idle, viewModel.state.value)
        coVerify(exactly = 1) { loginUseCase("example@gmail.com", "qwerty123") }
    }

    @Test
    fun `login should emit ShowError event on failure`() = runTest {
        val errorMessage = "Please check your email and password"
        coEvery { loginUseCase(any(), any()) } returns Result.Error(errorMessage)

        viewModel.login("example@gmail.com", "qwerty123")

        viewModel.effect.test {
            val effect = awaitItem()
            assertTrue(effect is AuthEffect.ShowError)
            assertEquals(errorMessage, (effect as AuthEffect.ShowError).message)
            cancelAndConsumeRemainingEvents()
        }

        assertEquals(AuthUiState.Idle, viewModel.state.value)
        coVerify(exactly = 1) { loginUseCase("example@gmail.com", "qwerty123") }
    }

    @Test
    fun `login should set Error state when blank email is provided`() = runTest {
        viewModel.login("", "password")

        val state = viewModel.state.value
        assertTrue(state is AuthUiState.Error)
        assertEquals("Email cannot be empty", (state as AuthUiState.Error).message)
    }

    @Test
    fun `login should set Error state when password length is less than PASSWORD_MIN_LENGTH`() =
        runTest {
            viewModel.login("example@gmail.com", "123")

            val state = viewModel.state.value
            assertTrue(state is AuthUiState.Error)
            assertEquals(
                "Password must be at least $PASSWORD_MIN_LENGTH characters",
                (state as AuthUiState.Error).message
            )
        }

    @Test
    fun `login should set Error state when email format is invalid`() =
        runTest {
            val emailsList = listOf(
                "example",
                "example@gmail",
                "examp!e@gmail.com",
                "example@gmail.c"
            )

            emailsList.forEach { email ->
                viewModel.login(email, "qwerty123")

                val state = viewModel.state.value
                assertTrue(state is AuthUiState.Error)
                assertEquals(
                    "Invalid email format",
                    (state as AuthUiState.Error).message
                )
            }
        }

    @Test
    fun `register should emit NavigateToMain event on success`() = runTest {
        coEvery { registerUseCase(any(), any()) } returns Result.Success(Unit)

        viewModel.register("example@gmail.com", "qwerty123", "qwerty123")

        viewModel.effect.test {
            val effect = awaitItem()
            assertTrue(effect is AuthEffect.NavigateToMain)
            cancelAndConsumeRemainingEvents()
        }

        assertEquals(AuthUiState.Idle, viewModel.state.value)
        coVerify(exactly = 1) { registerUseCase("example@gmail.com", "qwerty123") }
    }

    @Test
    fun `register should emit ShowError event on failure`() = runTest {
        val errorMessage = "The server is currently unavailable. Please try again later"
        coEvery { registerUseCase(any(), any()) } returns Result.Error(errorMessage)

        viewModel.register("example@gmail.com", "qwerty123", "qwerty123")

        viewModel.effect.test {
            val effect = awaitItem()
            assertTrue(effect is AuthEffect.ShowError)
            assertEquals(errorMessage, (effect as AuthEffect.ShowError).message)
            cancelAndConsumeRemainingEvents()
        }

        assertEquals(AuthUiState.Idle, viewModel.state.value)
        coVerify(exactly = 1) { registerUseCase("example@gmail.com", "qwerty123") }
    }

    @Test
    fun `register should set Error state when password length is less than PASSWORD_MIN_LENGTH`() =
        runTest {
            viewModel.register("example@gmail.com", "123", "123")

            val state = viewModel.state.value
            assertTrue(state is AuthUiState.Error)
            assertEquals(
                "Password must be at least $PASSWORD_MIN_LENGTH characters",
                (state as AuthUiState.Error).message
            )
        }

    @Test
    fun `register should set Error state when email format is invalid`() =
        runTest {
            val emailsList = listOf(
                "example",
                "example@gmail",
                "examp!e@gmail.com",
                "example@gmail.c"
            )

            emailsList.forEach { email ->
                viewModel.register(email, "qwerty123", "qwerty123")

                val state = viewModel.state.value
                assertTrue(state is AuthUiState.Error)
                assertEquals(
                    "Invalid email format",
                    (state as AuthUiState.Error).message
                )
            }
        }

    @Test
    fun `register should set Error state when passwords do not match`() = runTest {
        viewModel.register("example@gmail.com", "qwerty123", "qwerty321")

        val state = viewModel.state.value
        assertTrue(state is AuthUiState.Error)
        assertEquals("Passwords do not match", (state as AuthUiState.Error).message)
    }

    @Test
    fun `clearError should reset Error state to Idle`() = runTest {
        coEvery { loginUseCase(any(), any()) } returns Result.Success(Unit)
        viewModel.login("invalid", "qwerty123")

        viewModel.clearError()

        assertEquals(AuthUiState.Idle, viewModel.state.value)
    }

    @Test
    fun `clearError should not change Idle state`() = runTest {
        assertEquals(AuthUiState.Idle, viewModel.state.value)

        viewModel.clearError()

        assertEquals(AuthUiState.Idle, viewModel.state.value)
    }

    companion object {
        private const val PASSWORD_MIN_LENGTH = 8
    }
}