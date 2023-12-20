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
import inc.draco.workouttracker.ui.theme.WorkoutTrackerTheme
import inc.draco.workouttracker.viewmodel.ExerciseViewModel
import inc.draco.workouttracker.viewmodel.Overseer

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WorkoutTrackerTheme {
                NavHolder()
            }
        }
    }
}

@Composable
fun NavHolder(
    navController: NavHostController = rememberNavController(),
    overseer: Overseer = Overseer(),
    exerciseVM: ExerciseViewModel = ExerciseViewModel(overseer = overseer)
) {

    overseer.scope = rememberCoroutineScope()
    exerciseVM.init()

    NavHost(
        navController = navController,
        startDestination = Screens.Exercises.route
    ) {
        composable(
            route = Screens.Exercises.route
        ) {
            Log.d("TGT", "Recomposing NavHost Exercise Screen")
            ExerciseScreen(exerciseVM = exerciseVM)
        }
    }
}