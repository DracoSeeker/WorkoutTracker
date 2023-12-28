package inc.draco.workouttracker.realm

import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.ext.realmSetOf
import io.realm.kotlin.types.RealmList
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.RealmSet

class WorkoutLists: RealmObject {
//    var exerciseTypes: RealmSet<Exercise> = realmSetOf()
    var exerciseCategories: RealmSet<String> = realmSetOf()
}