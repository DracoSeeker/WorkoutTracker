package inc.draco.workouttracker.realm

import inc.draco.workouttracker.navigation.Screens
import io.realm.kotlin.types.RealmInstant
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.RealmUUID
import io.realm.kotlin.types.annotations.Ignore
import io.realm.kotlin.types.annotations.PrimaryKey

class Workout() : RealmObject {
    @PrimaryKey
    var id = RealmUUID.random()
        private set
    var type: String = ""
    var weight: Double = 0.0
    var reps: Int = 0
    var sets: Int = 0
    var timestamp: RealmInstant = RealmInstant.MIN
}