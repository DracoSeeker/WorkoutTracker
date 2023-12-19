package inc.draco.workouttracker.viewmodel

import androidx.lifecycle.ViewModel
import inc.draco.workouttracker.realm.Exercise
import inc.draco.workouttracker.realm.Workout
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration

class Overseer() : ViewModel() {
    private val exerciseConfig: RealmConfiguration = RealmConfiguration
        .Builder(schema = setOf(Exercise::class))
        .inMemory() /*TODO*/
        .initialData {
            copyToRealm(Exercise().apply {
                type = "knee extensions"
            })
        }
        .build()
    val exerciseRealm: Realm = Realm.open(exerciseConfig)

    private val workoutsConfig: RealmConfiguration = RealmConfiguration
        .Builder(schema = setOf(Workout::class))
        .inMemory() /*TODO*/
        .build()
    val workoutRealm: Realm = Realm.open(workoutsConfig)
}