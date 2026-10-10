package id.lifx.motionstudio.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import id.lifx.motionstudio.R
import id.lifx.motionstudio.ui.components.Ic
import id.lifx.motionstudio.ui.editor.EditorScreen
import id.lifx.motionstudio.ui.home.HomeScreen
import id.lifx.motionstudio.ui.settings.SettingsScreen
import id.lifx.motionstudio.ui.theme.Bg
import id.lifx.motionstudio.ui.theme.CardColor
import id.lifx.motionstudio.ui.theme.Cyan
import id.lifx.motionstudio.ui.theme.Muted
import id.lifx.motionstudio.ui.theme.Surface1

@Composable
fun LifxApp() {
    val nav = rememberNavController()
    val entry by nav.currentBackStackEntryAsState()
    val route = entry?.destination?.route
    val showBar = route == "home" || route == "settings"

    val itemColors = NavigationBarItemDefaults.colors(
        selectedIconColor = Cyan,
        selectedTextColor = Cyan,
        indicatorColor = CardColor,
        unselectedIconColor = Muted,
        unselectedTextColor = Muted
    )

    Scaffold(
        containerColor = Bg,
        bottomBar = {
            if (showBar) {
                NavigationBar(containerColor = Surface1) {
                    NavigationBarItem(
                        selected = route == "home",
                        onClick = {
                            nav.navigate("home") {
                                popUpTo("home") { inclusive = false }
                                launchSingleTop = true
                            }
                        },
                        icon = { Ic(R.drawable.ic_home, tint = if (route == "home") Cyan else Muted) },
                        label = { Text("Beranda") },
                        colors = itemColors
                    )
                    NavigationBarItem(
                        selected = route == "settings",
                        onClick = {
                            nav.navigate("settings") {
                                popUpTo("home") { inclusive = false }
                                launchSingleTop = true
                            }
                        },
                        icon = { Ic(R.drawable.ic_settings, tint = if (route == "settings") Cyan else Muted) },
                        label = { Text("Pengaturan") },
                        colors = itemColors
                    )
                }
            }
        }
    ) { inner ->
        NavHost(
            navController = nav,
            startDestination = "home",
            modifier = Modifier.padding(inner)
        ) {
            composable("home") {
                HomeScreen(
                    onOpen = { id -> nav.navigate("editor/$id") },
                    onSettings = {
                        nav.navigate("settings") {
                            popUpTo("home") { inclusive = false }
                            launchSingleTop = true
                        }
                    }
                )
            }
            composable("settings") { SettingsScreen() }
            composable(
                route = "editor/{projectId}",
                arguments = listOf(navArgument("projectId") { type = NavType.LongType })
            ) {
                EditorScreen(onBack = { nav.popBackStack() })
            }
        }
    }
}
