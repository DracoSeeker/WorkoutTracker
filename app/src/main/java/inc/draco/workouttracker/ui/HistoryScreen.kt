package inc.draco.workouttracker.ui

import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import inc.draco.workouttracker.realm.Workout
import inc.draco.workouttracker.titleCase
import inc.draco.workouttracker.viewmodel.HistoryViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(historyVM: HistoryViewModel) {
    Scaffold (
        topBar = {
            TopAppBar(title = {
                Text(text = "History of ${historyVM.exercise.type.titleCase()}")
            })
        }
    ) {paddingValues ->
        var surfaceHeight by remember { mutableStateOf(0.dp) }
        val density = LocalDensity.current
        Surface (
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .onGloballyPositioned {
                    surfaceHeight = with(density) {
                        it.size.height.toDp()
                    }
                }
        ) {
            Column(
                Modifier
                    .padding(10.dp)
                    .fillMaxSize()
            ) {
                Box(
                    Modifier
                        .height(surfaceHeight * 0.2f)
                        .border(2.dp, Color.Black)
                ) {
                    Canvas(Modifier.fillMaxSize()) {

                    }
                }
                Surface (
                    Modifier
                        .fillMaxSize()
                        .padding(10.dp),
                    shadowElevation = 5.dp
                ) {
                    Column (
                        Modifier
                            .fillMaxSize()
                    ) {
                        Surface (
                            shadowElevation = 5.dp
                        ){
                            Row(
                                Modifier
                                    .fillMaxWidth()
                                    .background(Color.LightGray),
                                horizontalArrangement = Arrangement.SpaceAround
                            ) {
                                WorkoutListTableBlock(value = "Reps")
                                WorkoutListTableBlock(value = "Sets")
                                if (!historyVM.exercise.isBodyweight) {
                                    WorkoutListTableBlock(value = "Weight")
                                }
                            }
                        }
                        LazyColumn(
                            Modifier
                                .fillMaxSize()
                        ) {
                            val workouts = historyVM.workouts[historyVM.exercise.type] ?: emptyList()
                            items(
                                items = workouts,
                                key = { item -> item.id.toString() }) {
                                /*Log.d(
                                    "TGT",
                                    "${it.type} Workout: ${it.reps}x${it.sets}@${it.weight}"
                                )*/
                                WorkoutDisplay(workout = it, isBodyweight = historyVM.exercise.isBodyweight)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun WorkoutListTableBlock(value: String) {
    Box(
        Modifier
            .horizontalScroll(rememberScrollState())
            .fillMaxWidth(),
        contentAlignment = Alignment.CenterStart
    ) {
        Text(text = value, fontSize = 5.em)
    }
}

@Composable
fun WorkoutDisplay(workout: Workout, isBodyweight: Boolean) {
    Row (
        Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround
    ){
        WorkoutListTableBlock(value = workout.reps.toString())
        WorkoutListTableBlock(value = workout.sets.toString())
        if (!isBodyweight) {
            WorkoutListTableBlock(value = workout.weight.toString())
        }
    }
}