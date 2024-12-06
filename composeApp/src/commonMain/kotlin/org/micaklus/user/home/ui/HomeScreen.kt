package org.micaklus.user.home.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabDisposable
import cafe.adriel.voyager.navigator.tab.TabNavigator
import org.micaklus.user.home.tab.OfflineTab
import org.micaklus.user.home.tab.OnlineTab

object HomeScreen : Screen {

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        TabNavigator(
            OfflineTab,
            tabDisposable = {
                TabDisposable(
                    navigator = it,
                    tabs = listOf(OfflineTab)
                )
            }
        ) { tabNavigator ->
            Scaffold(
                modifier = Modifier.fillMaxSize().safeDrawingPadding(),
                bottomBar = {
                    BottomAppBar() {
                        TabNavigationItem(OfflineTab)
                        TabNavigationItem(OnlineTab)
                        Spacer(modifier = Modifier.weight(1f))
                    }
                },
                topBar = {

                    TopAppBar(title = {
                        Text(text = tabNavigator.current.options.title)
                    })
                },
            ) { innerPadding ->
                Column(Modifier.padding(innerPadding)) {
                    tabNavigator.current.Content()
                }
            }
        }
    }
}

@Composable
private fun RowScope.TabNavigationItem(tab: Tab) {
    val tabNavigator = LocalTabNavigator.current

    NavigationBarItem(
        selected = tabNavigator.current == tab,
        onClick = { tabNavigator.current = tab },
        label = { Text(tab.options.title) },
        icon = {
            tab.options.icon?.let {
                Icon(
                    painter = it,
                    contentDescription = tab.options.title
                )
            }
        }
    )

}