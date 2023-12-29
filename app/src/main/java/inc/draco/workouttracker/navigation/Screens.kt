package inc.draco.workouttracker.navigation

enum class Screens(val route: String) {
    Exercises("exercises"), // Shows all Exercises
    History("history"), // Shows the workouts of that exercise
    Workout("workout") // Lets the User add a new workout of each Exercise
}