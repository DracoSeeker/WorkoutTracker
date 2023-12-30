package inc.draco.workouttracker

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import inc.draco.workouttracker.navigation.Screens
import inc.draco.workouttracker.ui.ExerciseScreen
import inc.draco.workouttracker.ui.HistoryScreen
import inc.draco.workouttracker.ui.WorkoutScreen
import inc.draco.workouttracker.ui.theme.WorkoutTrackerTheme
import inc.draco.workouttracker.viewmodel.ExerciseViewModel
import inc.draco.workouttracker.viewmodel.HistoryViewModel
import inc.draco.workouttracker.viewmodel.Overseer
import inc.draco.workouttracker.viewmodel.WorkoutViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val overseer = Overseer()
        val exerciseVM = ExerciseViewModel(overseer = overseer)
        val historyVM = HistoryViewModel(overseer)
        val workoutVM = WorkoutViewModel(overseer)

        timer.start()

        setContent {
            WorkoutTrackerTheme {
                NavHolder(
                    navController = rememberNavController(),
                    overseer = overseer,
                    exerciseVM = exerciseVM,
                    historyVM = historyVM,
                    workoutVM = workoutVM
                )
            }
        }
    }
}

@Composable
fun NavHolder(
    navController: NavHostController = rememberNavController(),
    overseer: Overseer,
    exerciseVM: ExerciseViewModel,
    historyVM: HistoryViewModel,
    workoutVM: WorkoutViewModel
) {

    overseer.init(rememberCoroutineScope())

    NavHost(
        navController = navController,
        startDestination = Screens.Exercises.route
    ) {
        composable(
            route = Screens.Exercises.route
        ) {
            ExerciseScreen(
                exerciseVM = exerciseVM,
                navToHistory = { exercise ->
                    navController.navigate(Screens.History.route)
                    historyVM.init(exercise)
                },
                navToWorkout = {
                    navController.navigate(Screens.Workout.route)
                }
            )
        }
        composable(
            route = Screens.History.route
        ) {
            HistoryScreen(historyVM = historyVM)
        }
        composable(
            route = Screens.Workout.route
        ) {
            WorkoutScreen(workoutVM = workoutVM)
        }
    }
}