package inc.draco.workouttracker.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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
        Surface (
            modifier = Modifier.padding(paddingValues)
        ) {

        }
    }
}