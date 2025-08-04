package com.devapp.cookfriends.presentation.profile

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.devapp.cookfriends.R
import com.devapp.cookfriends.domain.model.UiText
import com.devapp.cookfriends.domain.usecase.ChangePasswordUseCase
import com.devapp.cookfriends.domain.usecase.LogoutUseCase
import com.devapp.cookfriends.domain.usecase.PasswordValidationResult
import com.devapp.cookfriends.domain.usecase.ValidatePasswordInputUseCase
import com.devapp.cookfriends.util.ConnectivityObserver
import com.devapp.cookfriends.util.NetworkStatus
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.unmockkAll
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class ProfileViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher(TestCoroutineScheduler())

    @MockK
    private lateinit var mockChangePasswordUseCase: ChangePasswordUseCase
    @MockK
    private lateinit var mockLogoutUseCase: LogoutUseCase
    @MockK
    private lateinit var mockValidatePasswordInputUseCase: ValidatePasswordInputUseCase
    @MockK
    private lateinit var mockConnectivityObserver: ConnectivityObserver

    // Class under test
    private lateinit var viewModel: ProfileViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        MockKAnnotations.init(this)

        viewModel = ProfileViewModel(
            changePasswordUseCase = mockChangePasswordUseCase,
            logoutUseCase = mockLogoutUseCase,
            connectivityObserver = mockConnectivityObserver,
            validatePasswordInputUseCase = mockValidatePasswordInputUseCase
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        unmockkAll()
    }

    @Test
    fun `onNewPasswordChange updates newPassword state`() = runTest(testDispatcher) {
        val newPass = "newPassword123"
        viewModel.onNewPasswordChange(newPass)
        assertEquals(newPass, viewModel.newPassword.first())
    }

    @Test
    fun `onRepeatPasswordChange updates repeatPassword state`() = runTest(testDispatcher) {
        val repeatPass = "repeatPassword123"
        viewModel.onRepeatPasswordChange(repeatPass)
        assertEquals(repeatPass, viewModel.repeatPassword.first())
    }

    @Test
    fun `updatePassword with invalid input updates state with errors and does not call changePasswordUseCase`() = runTest(testDispatcher) {
        val newPass = "short"
        val repeatPass = "short"
        val validationError = UiText.StringResource(R.string.password_invalid)
        val validationResult = PasswordValidationResult(newPasswordError = validationError)

        viewModel.onNewPasswordChange(newPass)
        viewModel.onRepeatPasswordChange(repeatPass)

        every { mockValidatePasswordInputUseCase.invoke(newPass, repeatPass) } returns validationResult

        viewModel.updatePassword()
        testDispatcher.scheduler.advanceUntilIdle()


        val state = viewModel.profileState.first()
        assertEquals(validationError, state.passwordErrorMessage)
        assertFalse(state.isChangingPassword)
        coVerify(exactly = 0) { mockChangePasswordUseCase.invoke(any()) }
    }

    @Test
    fun `updatePassword with valid input and no network shows no internet message`() = runTest(testDispatcher) {
        val newPass = "validPassword123"
        val repeatPass = "validPassword123"
        val validationResult = PasswordValidationResult()

        viewModel.onNewPasswordChange(newPass)
        viewModel.onRepeatPasswordChange(repeatPass)

        every { mockValidatePasswordInputUseCase.invoke(newPass, repeatPass) } returns validationResult
        every { mockConnectivityObserver.getCurrentNetworkStatus() } returns NetworkStatus.Unavailable

        viewModel.updatePassword()
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.profileState.first()
        assertEquals(UiText.StringResource(R.string.no_internet_connection), state.message?.uiText)
        assertFalse(state.isChangingPassword)
        coVerify(exactly = 0) { mockChangePasswordUseCase.invoke(any()) }
    }

    @Test
    fun `updatePassword with valid input and network success calls changePasswordUseCase and updates state`() = runTest(testDispatcher) {
        val newPass = "validPassword123"
        val repeatPass = "validPassword123"
        val validationResult = PasswordValidationResult()

        viewModel.onNewPasswordChange(newPass)
        viewModel.onRepeatPasswordChange(repeatPass)

        every { mockValidatePasswordInputUseCase.invoke(newPass, repeatPass) } returns validationResult
        every { mockConnectivityObserver.getCurrentNetworkStatus() } returns NetworkStatus.Available
        coEvery { mockChangePasswordUseCase.invoke(newPass) } returns Unit // For suspending functions returning Unit

        viewModel.updatePassword()
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.profileState.first()
        assertFalse(state.isChangingPassword)
        assertEquals(UiText.StringResource(R.string.password_was_updated), state.message?.uiText)
        assertEquals("", viewModel.newPassword.first())
        assertEquals("", viewModel.repeatPassword.first())

        coVerify { mockChangePasswordUseCase.invoke(newPass) }
    }

    @Test
    fun `updatePassword with valid input and network failure calls changePasswordUseCase and updates state with error`() = runTest(testDispatcher) {
        val newPass = "validPassword123"
        val repeatPass = "validPassword123"
        val errorMessage = "Update failed"
        val validationResult = PasswordValidationResult()
        val exception = RuntimeException(errorMessage)

        viewModel.onNewPasswordChange(newPass)
        viewModel.onRepeatPasswordChange(repeatPass)

        every { mockValidatePasswordInputUseCase.invoke(newPass, repeatPass) } returns validationResult
        every { mockConnectivityObserver.getCurrentNetworkStatus() } returns NetworkStatus.Available
        coEvery { mockChangePasswordUseCase.invoke(newPass) } throws exception

        viewModel.updatePassword()
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.profileState.first()
        assertFalse(state.isChangingPassword)
        assertEquals(UiText.DynamicString(errorMessage), state.message?.uiText)
        assertEquals(newPass, viewModel.newPassword.first())
        assertEquals(repeatPass, viewModel.repeatPassword.first())

        coVerify { mockChangePasswordUseCase.invoke(newPass) }
    }

    @Test
    fun `logout success emits navigation event`() = runTest(testDispatcher) {
        val collectedEvents = mutableListOf<ProfileNavigationEvent>()
        val job = launch(UnconfinedTestDispatcher(testDispatcher.scheduler)) {
            viewModel.navigationEvent.collect { collectedEvents.add(it) }
        }

        coEvery { mockLogoutUseCase.invoke() } returns Unit

        viewModel.logout()
        testDispatcher.scheduler.advanceUntilIdle()

        assertTrue(viewModel.profileState.first().isLoggingOut)
        assertTrue(collectedEvents.contains(ProfileNavigationEvent.NavigateToLogin))
        coVerify { mockLogoutUseCase.invoke() }

        job.cancel()
    }

    @Test
    fun `logout failure updates state and does not emit navigation event`() = runTest(testDispatcher) {
        val errorMessage = "Logout failed"
        val exception = RuntimeException(errorMessage)
        coEvery { mockLogoutUseCase.invoke() } throws exception

        val collectedEvents = mutableListOf<ProfileNavigationEvent>()
        val job = launch(UnconfinedTestDispatcher(testDispatcher.scheduler)) {
            viewModel.navigationEvent.collect { collectedEvents.add(it) }
        }

        viewModel.logout()
        testDispatcher.scheduler.advanceUntilIdle()

        assertFalse(viewModel.profileState.first().isLoggingOut)
        assertTrue(collectedEvents.isEmpty())
        coVerify { mockLogoutUseCase.invoke() }

        job.cancel()
    }

    @Test
    fun `onClearMessage clears message in state`() = runTest(testDispatcher) {
        val newPass = "validPassword123"
        val repeatPass = "validPassword123"
        val validationResult = PasswordValidationResult()
        viewModel.onNewPasswordChange(newPass)
        viewModel.onRepeatPasswordChange(repeatPass)
        every { mockValidatePasswordInputUseCase.invoke(newPass, repeatPass) } returns validationResult
        every { mockConnectivityObserver.getCurrentNetworkStatus() } returns NetworkStatus.Unavailable

        viewModel.updatePassword()
        testDispatcher.scheduler.advanceUntilIdle()
        assertNotNull(viewModel.profileState.first().message)

        viewModel.onClearMessage()
        testDispatcher.scheduler.advanceUntilIdle()

        assertNull(viewModel.profileState.first().message)
    }
}
