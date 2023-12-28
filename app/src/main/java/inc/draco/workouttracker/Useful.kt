package inc.draco.workouttracker

fun String.titleCase(delimeter: String = " "): String {
    return split(delimeter).joinToString(delimeter) {
        it.lowercase().replaceFirstChar(Char::titlecase)
    }
}