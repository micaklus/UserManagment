package org.micaklus.user


import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.Navigator
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.micaklus.user.home.ui.HomeScreen


@Composable
@Preview
fun App() {
    MaterialTheme {
        Navigator(HomeScreen)
    }
}

