package inc.draco.workouttracker.realm

import inc.draco.workouttracker.navigation.Screens
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey

class Exercise() : RealmObject {
    @PrimaryKey
    var type: String = ""
    var category: String = ""
    var isBodyweight: Boolean = false
}