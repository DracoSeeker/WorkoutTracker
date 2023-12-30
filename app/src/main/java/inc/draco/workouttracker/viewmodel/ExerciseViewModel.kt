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

class ExerciseViewModel(val overseer: Overseer) : ViewModel() {
//    var exercises by mutableStateOf(emptyList<Exercise>())
//        private set
//
//    var workouts = mutableStateMapOf<String,List<Workout>>()
//        private set
//
//    var categories by mutableStateOf(emptyList<String>())

    init{
        Log.d("TGT", "Initializing Exercise View Model")
    }

//    fun init() {
//        overseer.getExercises {
//            exerciseList ->
//            exercises = exerciseList.sortedBy { exercise -> exercise.type }
//            Log.d("TGT", "Updating Exercises[${exercises.size}]")
//        }
//        Log.d("TGT", "At Init Exercises[${exercises.size}]")
//        for (exercise in exercises) {
//            overseer.getWorkoutsOfExercise(exercise) {
//                workoutsOfExercise -> workouts[exercise.type] = workoutsOfExercise
//                Log.d("TGT", "${exercise.type}[${workoutsOfExercise.size}] updated")
//            }
//        }
//        overseer.getExerciseCategories { categories = it.toList() }
//    }

    var showAddExercise by mutableStateOf(false)
    var newExerciseType by mutableStateOf("")
    var newExerciseIsBodyweight by mutableStateOf(false)
    var newExerciseCategory by mutableStateOf("")
    var newExerciseCustomCategory by mutableStateOf("")
    var newExerciseIsCustomCategory by mutableStateOf(false)

    fun onAddExercise() {
        val newCategory = if (newExerciseIsCustomCategory) {
            newExerciseCustomCategory
        } else {
            newExerciseCategory
        }
        val exercise = Exercise().apply {
            type = newExerciseType
            isBodyweight = newExerciseIsBodyweight
            category = newCategory
        }
        overseer.addExercise(
            exercise = exercise,
            newType = newExerciseType,
            newCategory = newCategory,
            onException = {}
        )
//        overseer.getWorkoutsOfExercise(exercise) {
//            workouts[exercise.type] = it
//        }

        newExerciseType = ""
        newExerciseIsBodyweight = false
        newExerciseCategory = ""
        newExerciseCustomCategory = ""
        newExerciseIsCustomCategory = false
    }
}