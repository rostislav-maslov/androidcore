package com.ub.utils

import android.graphics.Color
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import com.ub.ubutils.R
import okhttp3.MediaType
import okhttp3.ResponseBody

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Before
import retrofit2.HttpException
import retrofit2.Response

@RunWith(AndroidJUnit4::class)
class UtilsInstrumentationTest {

    @Before
    fun prepare() {
        LogUtils.init(true)
        UbUtils.init(InstrumentationRegistry.getTargetContext())
    }

    @Test
    fun testUbUtils() {
        assertEquals("UbUtils", UbUtils.getString(R.string.app_name))
    }

    @Test
    fun testLogUtils() {
        LogUtils.init(false)
        LogUtils.setThrowableLogger {
            assertTrue(it is HttpException)
        }
        LogUtils.e("TestLogUtils", "http", HttpException(Response.error<String>(401, ResponseBody.create(MediaType.parse("plain/text"), "test error"))))
    }

    @Test
    fun openMarket() {
        assertTrue(com.ub.utils.openMarket(InstrumentationRegistry.getTargetContext()))
    }

    @Test
    fun validatePhone() {
        assertTrue(isValidPhoneNumber("8 906 169 93 29"))
        assertFalse(isValidPhoneNumber("+ +906 169 93 29"))
    }

    @Test
    fun validateEmails() {
        assertTrue(isValidEmail("unitbean@gmail.com"))
        assertTrue(isValidEmail("unitbean@mail.ru"))
        assertFalse(isValidEmail("unitbean@gmail"))
        assertFalse(isValidEmail("unitbean@gmail."))
        assertFalse(isValidEmail("unitbean2gmail.com"))
        assertTrue(isValidEmail("u@g.com"))
    }

    @Test
    fun createNotification() {
        val notification = UbNotify.Builder(InstrumentationRegistry.getTargetContext())
            .fromLocal(android.R.drawable.ic_menu_add, "Title", "Message")
            .setParams {
                this.color = Color.WHITE
            }
            .build()

        assertNotNull(notification)
    }
}
