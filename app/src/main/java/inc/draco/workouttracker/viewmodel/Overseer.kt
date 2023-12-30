package inc.draco.workouttracker.viewmodel

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import inc.draco.workouttracker.log
import inc.draco.workouttracker.realm.Exercise
import inc.draco.workouttracker.realm.Workout
import inc.draco.workouttracker.realm.WorkoutLists
import inc.draco.workouttracker.timer
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import io.realm.kotlin.ext.query
import io.realm.kotlin.notifications.InitialResults
import io.realm.kotlin.notifications.ResultsChange
import io.realm.kotlin.notifications.UpdatedResults
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class Overseer() : ViewModel() {
    private lateinit var scope: CoroutineScope

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
                "Arm Extensions",
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

    var exercises by mutableStateOf(emptyList<Exercise>())
        private set

    var workouts = mutableMapOf<String, MutableState<List<Workout>>>()

    var categories by mutableStateOf(emptyList<String>())

    fun init(_scope: CoroutineScope) {
        scope = _scope
        Log.d("TGT", "Setting up Overseer Query connections")
        scope.launch {
            // Link the exercises list to a Query
            realm.query<Exercise>().asFlow().collect { resultsChange ->
                exercises = resultsChange.list
                // Link the workout lists to queries
                when (resultsChange) {
                    is InitialResults -> {
                        log("Inital Exercises: ${resultsChange.list.size} ${resultsChange.list.map { it.type }}")
                        resultsChange.list.forEach { exercise ->
                            getWorkoutsOfExercise(exercise)
                        }
                    }
                    is UpdatedResults -> {
                        log("Insertion to Exercises: ${resultsChange.insertions}")
                        resultsChange.insertions.forEach { i ->
                            getWorkoutsOfExercise(exercises[i])
                        }
                    }
                }
            }
        }
        scope.launch {
            val lists = realm.query<WorkoutLists>().find()
            if (lists.size > 0) {
                lists.first().exerciseCategories.asFlow().collect { listChange ->
                    categories = listChange.set.toList()
                }
            }
        }
    }

//    fun getExercises(onResults: (List<Exercise>) -> Unit) {
//        scope.launch {
//            realm.query<Exercise>().asFlow().collect { resultsChange ->
//                onResults(resultsChange.list)
//            }
//        }
//    }

    private fun getWorkoutsOfExercise(exercise: Exercise) {
        scope.launch {
            realm.query<Workout>("type = $0", exercise.type).asFlow()
                .collect { resultsChange ->
                    if (workouts[exercise.type] == null) {
                        workouts[exercise.type] = mutableStateOf(emptyList<Workout>())
                    }
                    workouts[exercise.type]!!.value = resultsChange.list
                    log("${exercise.type} Workouts [${resultsChange.list.size}] after ${timer.elapsed().inWholeMilliseconds} ms")
                }
        }
    }

//    fun getWorkoutsOfExercise(exercise: Exercise, onResults: (List<Workout>)->Unit) {
//        scope.launch {
//            realm.query<Workout>("type = $0", exercise.type).asFlow().collect {
//                resultsChange ->
//                onResults(resultsChange.list)
//            }
//        }
//    }

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

//    fun getExerciseCategories(onCategoriesChanged: (newCategories: Set<String>)->Unit) {
//        scope.launch {
//            val lists = realm.query<WorkoutLists>().find()
//            if (lists.size > 0) {
//                lists.first().exerciseCategories.asFlow().collect { listChange ->
//                    onCategoriesChanged(listChange.set)
//                }
//            }
//        }
//    }

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

    fun addWorkout(workout: Workout) {
        scope.launch {
            realm.write {
                copyToRealm(workout)
            }
        }
    }
}