package inc.draco.workouttracker

import android.util.Log
import kotlin.time.Duration
import kotlin.time.TimeSource

fun String.titleCase(delimeter: String = " "): String {
    return split(delimeter).joinToString(delimeter) {
        it.lowercase().replaceFirstChar(Char::titlecase)
    }
}

fun log(value: String) {
    Log.d("TGT", value)
}

object timer {
    private val timeSource = TimeSource.Monotonic
    private var first = timeSource.markNow()

    fun start() {
        first = timeSource.markNow()
    }
    fun elapsed(): Duration {
        return timeSource.markNow() - first
    }
}