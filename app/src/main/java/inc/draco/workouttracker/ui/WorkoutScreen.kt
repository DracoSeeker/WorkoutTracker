package inc.draco.workouttracker.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import inc.draco.workouttracker.realm.Exercise
import inc.draco.workouttracker.viewmodel.WorkoutViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkoutScreen(workoutVM: WorkoutViewModel) {
    Scaffold (
        topBar = {
            TopAppBar(
                title = { Text(text = "New Workout") }
            )
        }
    ) { paddingValues ->
        Surface (
            Modifier
                .padding(paddingValues)
        ) {

        }
    }
}

@Composable
fun addWorkoutForExercise(workoutVM: WorkoutViewModel) {

}
