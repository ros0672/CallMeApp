package com.callme.network.model

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertTrue
import org.junit.Test

class SerializationTest {
    @Test
    fun `should serialize LoginRequest to JSON`() {
        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        val adapter = moshi.adapter(LoginRequest::class.java)
        val json = adapter.toJson(LoginRequest("username", "password"))

        assertTrue(json.contains("username"))
        assertTrue(json.contains("password"))
    }

    @Test
    fun `should deserialize LoginRequest from JSON`() {
        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        val adapter = moshi.adapter(LoginRequest::class.java)
        val json = """{"email": "vasya_p@gmail.com", "password": "password123"}"""
        val loginRequest = adapter.fromJson(json)

        assertNotNull(loginRequest)
        assertEquals("vasya_p@gmail.com", loginRequest?.email)
        assertEquals("password123", loginRequest?.password)
    }
}