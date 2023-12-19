package inc.draco.workouttracker.realm

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey

class Exercise() : RealmObject {
    @PrimaryKey
    var type: String? = null
}