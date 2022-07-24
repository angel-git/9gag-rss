package com.ags.transformer

import java.text.SimpleDateFormat
import java.util.*

abstract class Function<A, B> {

    abstract fun apply(input: A): B

    protected fun parseCreationTsToRF(dateTs: Long): String {
        val dateFormat = SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz")
        dateFormat.timeZone = TimeZone.getTimeZone("GMT")
        return dateFormat.format(Date(dateTs.times(1000)))
    }

}