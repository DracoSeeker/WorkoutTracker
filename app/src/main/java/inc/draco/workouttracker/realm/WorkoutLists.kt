package inc.draco.workouttracker.realm

import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.types.RealmList
import io.realm.kotlin.types.RealmObject

class WorkoutLists: RealmObject {
    var exerciseTypes: RealmList<String> = realmListOf()
    var exerciseCategories: RealmList<String> = realmListOf()
}