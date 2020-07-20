package com.dkatalis.utils

import java.util.*

object Utils {


    fun capitalize(s: String): String? {
        return s.split(' ').joinToString(" ") { it.capitalize() }
    }

    fun getShortDate(ts:Long?):String{
        if(ts == null) return ""
        val calendar = Calendar.getInstance(Locale.getDefault())
        calendar.timeInMillis = ts
        return android.text.format.DateFormat.format("E, dd MMM yyyy", calendar).toString()
    }
}