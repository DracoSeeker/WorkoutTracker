package inc.draco.workouttracker.ui

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import inc.draco.workouttracker.realm.Exercise
import inc.draco.workouttracker.realm.Workout
import inc.draco.workouttracker.viewmodel.ExerciseViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExerciseScreen(exerciseVM: ExerciseViewModel) {

//    val exercises by exerciseVM.exercisesFlow.collectAsStateWithLifecycle(initialValue = null)

    if (exerciseVM.showAddExercise) {
        AddExerciseDialog(
            newExerciseType = exerciseVM.newExerciseType,
            onExerciseTypeTyping = { exerciseVM.newExerciseType = it },
            newExerciseIsBodyweight = exerciseVM.newExerciseIsBodyweight,
            onNewExerciseIsBodyweightChange = { exerciseVM.newExerciseIsBodyweight = it },
            onAddExercise = exerciseVM::onAddExercise
        ) { exerciseVM.showAddExercise = false }
    }

    Scaffold (
        Modifier,
        topBar = { TopAppBar(
            title = {
                Text(
                    text = "Exercises"
                )
            },
            actions = {
                IconButton(onClick = { exerciseVM.showAddExercise = true}) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "Add Exercise")
                }
            }
        )
        }
    ) {paddingValues ->
        Surface (
            Modifier.padding(paddingValues)
        ) {
            Column (
                Modifier
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                for (exercise in exerciseVM.exercises) {
                    ExerciseDisplay(exercise = exercise, exerciseVM.workouts[exercise.type] ?: emptyList())
                }
            }
        }
    }
}

@Composable
fun ExerciseDisplay(exercise: Exercise, workouts: List<Workout>) {

    val lastWorkout = if (workouts.isEmpty()) Workout() else workouts.last()

    Card (
        Modifier
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)
    ) {
        Column (
            Modifier
                .fillMaxWidth()
                .padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(text = exercise.type?.titleCase() ?: "Null")
            Row (
                Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min),
//                horizontalArrangement = Arrangement.
            ) {
                Column (
                    Modifier
                        .padding(10.dp)
                ) {
                    if (lastWorkout.weight != null) Text(text = "Weight: ${lastWorkout.weight}")
                    Text(text = "Reps: ${lastWorkout.reps}")
                    Text(text = "Sets: ${lastWorkout.sets}")
                }
                Box (
                    Modifier
                        .fillMaxSize()
                        .border(1.dp, Color.Black),
                    contentAlignment = Alignment.Center
                ){
                    Text(text = "Graph Goes Here") /*TODO*/
                }
            }
        }
    }
}

@Composable
fun AddExerciseDialog(
    newExerciseType: String,
    onExerciseTypeTyping: (String) -> Unit,
    newExerciseIsBodyweight: Boolean,
    onNewExerciseIsBodyweightChange: (Boolean) -> Unit,
    onAddExercise: () -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = { onDismiss() }) {
        Card(
        ) {
            Column (
                Modifier
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(text = "Add Exercise")
                OutlinedTextField(
                    value = newExerciseType,
                    onValueChange = { onExerciseTypeTyping(it) },
                    label = {
                        Text(text = "Type")
                    }
                )
                Row (
                    Modifier
                        .height(IntrinsicSize.Min)
                ) {
                    Switch(
                        checked = newExerciseIsBodyweight,
                        onCheckedChange = { onNewExerciseIsBodyweightChange(it) }
                    )
                    Text(
                        text = "Bodyweight",
                        Modifier
                            .fillMaxSize(),

                    )
                }
                Button(onClick = { onAddExercise() }) {
                    Text(text = "Add Exercise")
                }
            }
        }
    }
}

fun String.titleCase(delimeter: String = " "): String {
    return split(delimeter).joinToString(delimeter) {
        it.lowercase().replaceFirstChar(Char::titlecase)
    }
}