package com.devapp.cookfriends.domain.usecase

import com.devapp.cookfriends.R
import com.devapp.cookfriends.domain.model.UiText
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class ValidatePasswordInputUseCaseTest {

    private lateinit var validatePasswordInputUseCase: ValidatePasswordInputUseCase

    @Before
    fun setUp() {
        validatePasswordInputUseCase = ValidatePasswordInputUseCase()
    }

    @Test
    fun `invoke with valid passwords returns no errors and isValid true`() {
        val result = validatePasswordInputUseCase("password123", "password123")

        assertNull(result.newPasswordError)
        assertNull(result.repeatPasswordError)
        assertTrue(result.isValid)
    }

    @Test
    fun `invoke with blank new password returns mandatory error`() {
        val result = validatePasswordInputUseCase("", "password123")

        assertEquals(UiText.StringResource(R.string.password_mandatory), result.newPasswordError)
        assertFalse(result.isValid)
    }

    @Test
    fun `invoke with new password too short returns invalid error`() {
        val result = validatePasswordInputUseCase("pass", "pass")

        assertEquals(UiText.StringResource(R.string.password_invalid), result.newPasswordError)
        assertFalse(result.isValid)
    }

    @Test
    fun `invoke with blank repeat password returns mandatory error`() {
        val result = validatePasswordInputUseCase("password123", "")

        assertEquals(UiText.StringResource(R.string.repeat_password_mandatory), result.repeatPasswordError)
        assertFalse(result.isValid)
    }

    @Test
    fun `invoke with passwords not matching returns mismatch error`() {
        val result = validatePasswordInputUseCase("password123", "password456")

        assertEquals(UiText.StringResource(R.string.passwords_does_not_match), result.repeatPasswordError)
        assertFalse(result.isValid)
    }

    @Test
    fun `invoke with blank new password and mismatching repeat password returns both errors`() {
        val result = validatePasswordInputUseCase("", "password456")

        assertEquals(UiText.StringResource(R.string.password_mandatory), result.newPasswordError)
        assertEquals(UiText.StringResource(R.string.passwords_does_not_match), result.repeatPasswordError)
        assertFalse(result.isValid)
    }


    @Test
    fun `invoke with valid new password and blank repeat password returns repeat password error`() {
        val result = validatePasswordInputUseCase("password123", "")

        assertNull(result.newPasswordError)
        assertEquals(UiText.StringResource(R.string.repeat_password_mandatory), result.repeatPasswordError)
        assertFalse(result.isValid)
    }
}
