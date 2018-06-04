package com.ub.utils

import android.graphics.Color
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import com.ub.ubutils.R

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Before

@RunWith(AndroidJUnit4::class)
class UtilsInstrumentationTest {

    @Before
    fun prepare() {
        LogUtils.init(true)
        UbUtils.init(InstrumentationRegistry.getTargetContext())
    }

    @Test
    fun testLogUtils() {
        assertEquals("UbUtils", UbUtils.getString(R.string.app_name))
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
