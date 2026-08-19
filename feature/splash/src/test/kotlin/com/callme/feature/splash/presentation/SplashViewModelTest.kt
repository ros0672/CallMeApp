package com.callme.feature.splash.presentation

import app.cash.turbine.test
import com.callme.feature.splash.domain.model.SplashEffect
import com.callme.feature.splash.domain.usecase.ValidateTokenUseCase
import io.mockk.coEvery
import io.mockk.mockk
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
class SplashViewModelTest {
    private val testDispatcher = StandardTestDispatcher()

    private lateinit var validateTokenUseCase: ValidateTokenUseCase
    private lateinit var viewModel: SplashViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        validateTokenUseCase = mockk()
        viewModel = SplashViewModel(validateTokenUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `checkSession should emit Authorized when token is valid`() = runTest {
        coEvery { validateTokenUseCase() } returns true

        viewModel.checkSession()

        viewModel.effect.test {
            val effect = awaitItem()
            assertTrue(effect is SplashEffect.Authorized)
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `checkSession should emit Unauthorized when token is invalid`() = runTest {
        coEvery { validateTokenUseCase() } returns false

        viewModel.checkSession()

        viewModel.effect.test {
            val effect = awaitItem()
            assertTrue(effect is SplashEffect.Unauthorized)
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `checkSession should emit exactly one effect`() = runTest {
        coEvery { validateTokenUseCase() } returns true

        viewModel.checkSession()

        viewModel.effect.test {
            val effect = awaitItem()
            assertTrue(effect is SplashEffect.Authorized)
            expectNoEvents() // No more events expected
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `checkSession should handle multiple calls correctly`() = runTest {
        coEvery { validateTokenUseCase() } returns true

        viewModel.checkSession()
        viewModel.checkSession()

        viewModel.effect.test {
            val firstEffect = awaitItem()
            assertTrue(firstEffect is SplashEffect.Authorized)

            val secondEffect = awaitItem()
            assertTrue(secondEffect is SplashEffect.Authorized)

            cancelAndConsumeRemainingEvents()
        }
    }
}