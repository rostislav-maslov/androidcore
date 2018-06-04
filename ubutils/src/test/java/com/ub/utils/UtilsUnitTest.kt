package com.ub.utils

import org.junit.Test

import org.junit.Assert.*

class UtilsUnitTest {

    @Test
    fun testSameItems() {
        val first = arrayListOf(1, 2, 3, 4, 5, 6, 7, 8, 9)
        val second = arrayListOf(2, 5, 1, 7, 8, 9, 6, 4, 3)

        assertTrue(haveSameElements(first, second))
    }
}
