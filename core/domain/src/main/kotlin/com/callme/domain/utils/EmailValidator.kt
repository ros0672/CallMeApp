package com.callme.domain.utils

object EmailValidator {
    // TODO CallMeApp #20: RFC 5322 standard must be used
    private val EMAIL_PATTERN = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")

    fun isValidEmail(email: String) = email.isNotBlank() && EMAIL_PATTERN.matches(email)
}