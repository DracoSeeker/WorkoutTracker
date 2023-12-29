package inc.draco.workouttracker.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.lifecycle.ViewModel
import inc.draco.workouttracker.realm.Exercise
import inc.draco.workouttracker.realm.Workout

class HistoryViewModel(
    private val overseer: Overseer,
    var workouts: SnapshotStateMap<String, List<Workout>>
): ViewModel() {

    var exercise by mutableStateOf(Exercise())

}