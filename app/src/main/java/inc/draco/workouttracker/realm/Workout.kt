package inc.draco.workouttracker.realm

import inc.draco.workouttracker.navigation.Screens
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.RealmUUID
import io.realm.kotlin.types.annotations.Ignore
import io.realm.kotlin.types.annotations.PrimaryKey

class Workout() : RealmObject {
    @PrimaryKey
    var id = RealmUUID.random()
    var type: String? = null
    var weight: Double? = null
    var reps: Int = 0
    var sets: Int = 0
}