package inc.draco.workouttracker.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import inc.draco.workouttracker.realm.Exercise
import inc.draco.workouttracker.realm.Workout
import inc.draco.workouttracker.realm.WorkoutLists
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import io.realm.kotlin.ext.query
import io.realm.kotlin.notifications.ResultsChange
import io.realm.kotlin.notifications.UpdatedResults
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class Overseer() : ViewModel() {
    lateinit var scope: CoroutineScope

    private val exerciseConfig: RealmConfiguration = RealmConfiguration
        .Builder(schema = setOf(Exercise::class, Workout::class, WorkoutLists::class))
        .inMemory() /*TODO*/
        .initialData {
            copyToRealm(Workout().apply {
                type = "Knee Extensions"
                weight = 70.0
                reps = 10
                sets = 5
            })
            copyToRealm(Workout().apply {
                type = "Arm Extensions"
                weight = 30.0
                reps = 10
                sets = 3
            })
            val testNames = listOf(
                "Knee Extensions",
                "Abs",
                "Arm Curls",
                "Arm Extension",
                "Knee Curl",
                "Chest Press",
                "Rope Pulls",
                "Pushups"
            )
            val testCategories = listOf(
                "Legs",
                "Torso",
                "Arms",
                "Arms",
                "Legs",
                "Chest",
                "Arms",
                "Chest"

            )
            copyToRealm(WorkoutLists().apply {
                exerciseTypes.addAll(testNames)
                exerciseCategories.addAll(testCategories)
            })
            for (i in 1..testNames.size) {
                copyToRealm(Exercise().apply {
                    type = testNames[i]
                    category = testCategories[i]
                })
            }
        }
        .build()
    private val realm: Realm = Realm.open(exerciseConfig)

    fun getExercises(onResults: (List<Exercise>) -> Unit) {
        scope.launch {
            realm.query<Exercise>().asFlow().collect {
                resultsChange ->
//                var msg = "["
//                for (item in resultsChange.list) {
//                    msg += "${item.type}, "
//                }
//                msg += "]"
//                Log.d("TGT", msg)
                if (resultsChange is UpdatedResults) {
                    Log.d("TGT",
                        "Changes:" +
                                "\n\t${resultsChange.changes}"
                    )
                }
                onResults(resultsChange.list)
            }
        }
    }

    fun getWorkoutsOfExercise(exercise: Exercise, onResults: (List<Workout>)->Unit) {
        scope.launch {
            realm.query<Workout>("type = $0", exercise.type).asFlow().collect {
                resultsChange ->
                onResults(resultsChange.list)
            }
        }
    }

    fun addExercise(exercise: Exercise, onException: ()->Unit) {
        scope.launch {
            realm.write {
                try {
                    copyToRealm(exercise)
                } catch (e: IllegalArgumentException ) {
                    onException()
                }
            }
        }
    }
}