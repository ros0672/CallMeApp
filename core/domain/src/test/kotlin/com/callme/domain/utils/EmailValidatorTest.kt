package com.callme.domain.utils

import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import org.junit.Test

class EmailValidatorTest {

    @Test
    fun `EmailValidator should return true for valid email address`() {
        val validEmails = listOf(
            "example@example.com",
            "john.doe@example.com",
            "john_doe@example.com",
            "john.doe1@example.co.uk"
        )

        validEmails.forEach { email ->
            assertTrue(EmailValidator.isValidEmail(email))
        }
    }

    @Test
    fun `EmailValidator should return false for invalid email address`() {
        val validEmails = listOf(
            "example",
            "example@gmail",
            "examp!e@gmail.com",
            "example@gmail.com.",
            "example@gmail.c",
            "@example.com"
        )

        validEmails.forEach { email ->
            assertFalse(EmailValidator.isValidEmail(email))
        }
    }
}