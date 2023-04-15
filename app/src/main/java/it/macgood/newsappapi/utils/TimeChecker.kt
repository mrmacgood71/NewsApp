package it.macgood.newsappapi.utils

import java.text.SimpleDateFormat
import java.util.*

enum class TimeOfDay {
    DAY,
    EVENING,
    NIGHT
}

class TimeChecker {
    companion object {
        @JvmStatic
        fun getTimeOfDay(): TimeOfDay {
            val time = SimpleDateFormat("H", Locale.getDefault()).format(Date()).toInt()

            return if (time in 0..6) {
                TimeOfDay.NIGHT
            } else if (time in 7..18) {
                TimeOfDay.DAY
            } else {
                TimeOfDay.EVENING
            }
        }
    }
}