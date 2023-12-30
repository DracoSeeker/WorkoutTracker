package inc.draco.workouttracker.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.lifecycle.ViewModel
import inc.draco.workouttracker.realm.Exercise
import inc.draco.workouttracker.realm.Workout
import java.lang.NumberFormatException

class HistoryViewModel(
    private val overseer: Overseer
): ViewModel() {

    var exercise by mutableStateOf(Exercise())
    var workoutsState: MutableState<List<Workout>>? = null

    fun init(_exercise: Exercise) {
        exercise = _exercise
        workoutsState = overseer.workouts[exercise.type]
    }

    var showAddWorkout by mutableStateOf(false)
    var newWorkoutReps by mutableStateOf("")
    var newWorkoutSets by mutableStateOf("")
    var newWorkoutWeight by mutableStateOf("")

    fun addWorkout() {
        var newReps = 0
        var newSets = 0
        var newWeight = 0.0
        try {
            newReps = newWorkoutReps.toInt()
            newSets = newWorkoutSets.toInt()
            newWeight = newWorkoutWeight.toDouble()
        } catch (e: NumberFormatException) {
            return
        }

        overseer.addWorkout(
            Workout().apply {
                type = exercise.type
                reps = newReps
                sets = newSets
                weight = newWeight
            }
        )

        showAddWorkout = false
        newWorkoutReps = ""
        newWorkoutSets = ""
        newWorkoutWeight = ""
    }

}