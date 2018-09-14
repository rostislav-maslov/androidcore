package com.ub.utils

import java.util.*

var Calendar.dayOfWeek
    inline get() = this.get(Calendar.DAY_OF_WEEK)
    inline set(value) = this.set(Calendar.DAY_OF_WEEK, value)

var Calendar.day
    inline get() = this.get(Calendar.DATE)
    inline set(value) = this.set(Calendar.DATE, value)

var Calendar.hours
    inline get() = this.get(Calendar.HOUR_OF_DAY)
    inline set(value) = this.set(Calendar.HOUR_OF_DAY, value)

var Calendar.minutes
    inline get() = this.get(Calendar.MINUTE)
    inline set(value) = this.set(Calendar.MINUTE, value)

var Calendar.seconds
    inline get() = this.get(Calendar.SECOND)
    inline set(value) = this.set(Calendar.SECOND, value)

var Calendar.milliseconds
    inline get() = this.get(Calendar.MILLISECOND)
    inline set(value) = this.set(Calendar.MILLISECOND, value)

infix fun Calendar.addHours(hours: Int) {
    this.add(Calendar.HOUR_OF_DAY, hours)
}

infix fun Calendar.addMinutes(minutes: Int) {
    this.add(Calendar.MINUTE, minutes)
}

infix fun Calendar.dayRoll(amount: Int) {
    this.roll(Calendar.DATE, amount)
}