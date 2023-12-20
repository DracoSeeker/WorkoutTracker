package inc.draco.workouttracker.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import inc.draco.workouttracker.realm.Exercise
import inc.draco.workouttracker.realm.Workout

class ExerciseViewModel(private val overseer: Overseer) : ViewModel() {
    var exercises by mutableStateOf(emptyList<Exercise>())
        private set

    var workouts = mutableStateMapOf<String?,List<Workout>>()
        private set
    fun init() {
        overseer.getExercises {
            exerciseList ->
            exercises = exerciseList
            Log.d("TGT", "Updating Exercises[${exercises.size}]")
        }
        Log.d("TGT", "At Init Exercises[${exercises.size}]")
        for (exercise in exercises) {
            overseer.getWorkoutsOfExercise(exercise) {
                workoutsOfExercise -> workouts[exercise.type] = workoutsOfExercise
                Log.d("TGT", "${exercise.type}[${workoutsOfExercise.size}] updated")
            }
        }
    }

    var showAddExercise by mutableStateOf(false)
    var newExerciseType by mutableStateOf("")
    var newExerciseIsBodyweight by mutableStateOf(false)
    var newExerciseWeight by mutableStateOf("")

    fun onAddExercise() {
        overseer.addExercise(
            Exercise().apply {
                type = newExerciseType
                isBodyweight = newExerciseIsBodyweight
            },
            onException = {}
        )
    }

}