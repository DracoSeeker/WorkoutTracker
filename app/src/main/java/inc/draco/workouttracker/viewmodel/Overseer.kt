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
//                exerciseTypes.addAll(testNames)
                exerciseCategories.addAll(testCategories.distinct())
            })
            for (i in testNames.indices) {
                copyToRealm(Exercise().apply {
                    type = testNames[i]
                    category = testCategories[i]
                    if (testNames[i]=="Pushups") isBodyweight = true
                })
            }
        }
        .build()
    private val realm: Realm = Realm.open(exerciseConfig)

    fun getExercises(onResults: (List<Exercise>) -> Unit) {
        scope.launch {
            realm.query<Exercise>().asFlow().collect { resultsChange ->
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

//    fun getExerciseTypes(onTypesChanged: (newTypes: Set<String>) -> Unit) {
//        scope.launch {
//            val lists = realm.query<WorkoutLists>().first().find()!!
//            lists.exerciseTypes.asFlow().collect {
//                listChange ->
//                onTypesChanged(listChange.set)
//            }
//
//        }
//    }

    fun getExerciseCategories(onCategoriesChanged: (newCategories: Set<String>)->Unit) {
        scope.launch {
            val lists = realm.query<WorkoutLists>().find().first()
            lists?.exerciseCategories?.asFlow()?.collect {
                listChange ->
                onCategoriesChanged(listChange.set)
            }
        }
    }

    fun addExercise(exercise: Exercise, newType: String, newCategory: String, onException: ()->Unit) {
        scope.launch {
            realm.write {
                try {
                    copyToRealm(exercise)
                } catch (e: IllegalArgumentException ) {
                    onException()
                }
                val lists = realm.query<WorkoutLists>().find().first()
                findLatest(lists)?.exerciseCategories?.add(newCategory)
            }
        }
    }
}