package inc.draco.workouttracker.realm

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.RealmUUID
import io.realm.kotlin.types.annotations.PrimaryKey

class Workout() : RealmObject {
    @PrimaryKey
    var id = RealmUUID.random()
    var type: String? = null
}