package com.example.medagenda.domain.validation

import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class ValidateEmailTest {

    private lateinit var validateEmail: ValidateEmail

    @Before
    fun setUp() {
        validateEmail = ValidateEmail()
    }

    @Test
    fun `Email is valid`() {
        val result = validateEmail.execute("test@email.com")
        assertTrue(result.successful)
    }

    @Test
    fun `Email is blank`() {
        val result = validateEmail.execute("")
        assertFalse(result.successful)
        assertEquals("El email no puede estar vacío", result.errorMessage)
    }

    @Test
    fun `Email is invalid`() {
        val result = validateEmail.execute("testemail.com")
        assertFalse(result.successful)
        assertEquals("El formato del email no es válido", result.errorMessage)
    }
}