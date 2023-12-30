package inc.draco.workouttracker.ui

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Build
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.modifierLocalMapOf
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import inc.draco.workouttracker.log
import inc.draco.workouttracker.realm.Exercise
import inc.draco.workouttracker.realm.Workout
import inc.draco.workouttracker.titleCase
import inc.draco.workouttracker.viewmodel.ExerciseViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExerciseScreen(
    exerciseVM: ExerciseViewModel,
    navToHistory: (Exercise) -> Unit,
    navToWorkout: () -> Unit
) {

//    val exercises by exerciseVM.exercisesFlow.collectAsStateWithLifecycle(initialValue = null)

    if (exerciseVM.showAddExercise) {
        AddExerciseDialog(
            newExerciseType = exerciseVM.newExerciseType,
            onExerciseTypeTyping = { exerciseVM.newExerciseType = it },
            newExerciseIsBodyweight = exerciseVM.newExerciseIsBodyweight,
            onNewExerciseIsBodyweightChange = { exerciseVM.newExerciseIsBodyweight = it },
            onAddExercise = exerciseVM::onAddExercise,
            onDismiss = { exerciseVM.showAddExercise = false },
            categories = exerciseVM.overseer.categories,
            newExerciseCategory = exerciseVM.newExerciseCategory,
            onNewExerciseCategoryChanged = { exerciseVM.newExerciseCategory = it },
            newExerciseCustomCategory = exerciseVM.newExerciseCustomCategory,
            onNewExerciseCustomCategoryChanged = { exerciseVM.newExerciseCustomCategory = it },
            newExerciseIsCustomCategory = exerciseVM.newExerciseIsCustomCategory,
            onNewExerciseIsCustomCategoryChanged = { exerciseVM.newExerciseIsCustomCategory = it }
        ) 
    }

    Scaffold (
        Modifier,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Exercises"
                    )
                }
            )
        },
        bottomBar = {
            BottomAppBar (
            ) {
                Row (
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    IconButton(onClick = { exerciseVM.showAddExercise = true}) {
                        Icon(imageVector = Icons.Default.Add, contentDescription = "Add Exercise")
                    }
                    IconButton(onClick = { navToWorkout() }) {
                        Icon(imageVector = Icons.Default.Build, contentDescription = "Add Workouts")
                    }
                }
            }
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
                for (category in exerciseVM.overseer.categories) {
                    CategoryGrouping(
                        category = category,
                        exercises = exerciseVM.overseer.exercises,
                        workouts = exerciseVM.overseer.workouts.toMap(),
                        navToHistory = {navToHistory(it)}
                    )
                }
            }
        }
    }
}

@Composable
fun CategoryGrouping(
    category: String,
    exercises: List<Exercise>,
    workouts: Map<String, MutableState<List<Workout>>>,
    navToHistory: (Exercise) -> Unit
) {
    Column {
        Text(
            text = "$category",
            textAlign = TextAlign.Left
        )
        Box (
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 15.dp)
        ) {
            Column (
                Modifier
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                for (exercise in exercises.filter { it.category == category }) {
                    key(workouts[exercise.type]) {
                        ExerciseDisplay(
                            exercise = exercise,
                            workouts = workouts[exercise.type]?.value ?: emptyList(),
                            navToHistory = { navToHistory(it) }
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ExerciseDisplay(exercise: Exercise, workouts: List<Workout>, navToHistory: (Exercise)->Unit) {

    val lastWorkout = if (workouts.isEmpty()) Workout() else workouts.last()

    Card (
        Modifier
            .fillMaxWidth()
            .combinedClickable(
                onClick = { navToHistory(exercise) }
            ),
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)
    ) {
        Column (
            Modifier
                .fillMaxWidth()
                .padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(text = "${exercise.type.titleCase()} ${workouts.size}")
            log("Displaying ${exercise.type.titleCase()}[${workouts.size}]")
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
                    if (!exercise.isBodyweight) Text(text = "Weight: ${lastWorkout.weight}")
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddExerciseDialog(
    newExerciseType: String,
    onExerciseTypeTyping: (String) -> Unit,
    newExerciseIsBodyweight: Boolean,
    onNewExerciseIsBodyweightChange: (Boolean) -> Unit,
    onAddExercise: () -> Unit,
    onDismiss: () -> Unit,
    categories: List<String>,
    newExerciseCategory: String,
    onNewExerciseCategoryChanged: (category: String)->Unit,
    newExerciseCustomCategory: String,
    onNewExerciseCustomCategoryChanged: (String) -> Unit,
    newExerciseIsCustomCategory: Boolean,
    onNewExerciseIsCustomCategoryChanged: (Boolean)->Unit
) {
    Dialog(
        onDismissRequest = {
            onDismiss()
            onNewExerciseIsCustomCategoryChanged(false)
            onNewExerciseCategoryChanged("")
        }
    ) {
        Card(

        ) {
            Surface(
                Modifier
                    .padding(20.dp)
            ) {
                Column(
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
                    Row(
                        Modifier
                            .height(IntrinsicSize.Min),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Switch(
                            checked = newExerciseIsBodyweight,
                            onCheckedChange = { onNewExerciseIsBodyweightChange(it) }
                        )
                        Text(
                            text = "Bodyweight",
                            Modifier
                                .fillMaxSize()
                                .wrapContentHeight()
                            )
                    }
                    Row (
                        modifier = Modifier
                    ) {
                        Log.d("TGT", "Recomposing Cat row")
                        if (!newExerciseIsCustomCategory) {
                            var expanded by remember { mutableStateOf(false) }
                            val dropDownIntroText = "Select a Category"
                            var dropDownText by remember { mutableStateOf(dropDownIntroText) }
                            Box(
                            ) {
                                Card(
                                    onClick = {
                                        expanded = true
                                    }
                                ) {
                                    Row() {
                                        Text(text = dropDownText)
                                        Icon(Icons.Default.ArrowDropDown, null)
                                    }
                                }
                                DropdownMenu(
                                    expanded = expanded,
                                    onDismissRequest = { expanded = false }
                                ) {
                                    categories.forEachIndexed { index, category ->
                                        DropdownMenuItem(
                                            text = {
                                                    Text(text = category)
                                            },
                                            onClick = {
                                                onNewExerciseCategoryChanged(category)
                                                dropDownText = category
                                            }
                                        )
                                    }
                                }
                            }
                        } else {
                            OutlinedTextField(
                                value = newExerciseCustomCategory,
                                onValueChange = { onNewExerciseCustomCategoryChanged(it) },
                                modifier = Modifier
                                    .weight(1f, true),
                                label = {
                                    Text(text = "Custom Category")
                                }
                            )
                        }
                        Checkbox(
                            checked = newExerciseIsCustomCategory,
                            onCheckedChange = { onNewExerciseIsCustomCategoryChanged(it) },
                            modifier = Modifier
                        )
                    }
                    Button(onClick = {
                        onAddExercise()
                        onDismiss()
                    }) {
                        Text(text = "Add Exercise")
                    }
                }
            }
        }
    }
}

