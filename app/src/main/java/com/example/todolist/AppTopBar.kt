package com.example.todolist

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(
    navController: NavController,
    currentScreen: AppScreen,
    onNavigate: (AppScreen) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    CenterAlignedTopAppBar(
        title = { Text(text = currentScreen.title) },
        actions = {
            Box(
                modifier = Modifier.wrapContentSize(Alignment.TopEnd)
            ) {
                IconButton(onClick = { expanded = true }) {
                    Icon(
                        imageVector = Icons.Default.Menu,
                        contentDescription = "Menú de Secciones"
                    )
                }
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    listOf(
                        AppScreen.Productivity,
                        AppScreen.Finanzas,
                        AppScreen.Salud,
                        AppScreen.Estudio
                    ).forEach { screen ->
                        DropdownMenuItem(
                            text = { Text(text = screen.title) },
                            leadingIcon = {
                                Icon(
                                    imageVector = screen.icon,
                                    contentDescription = null
                                )
                            },
                            onClick = {
                                onNavigate(screen)
                                expanded = false
                            }
                        )
                    }
                }
            }
        }
    )
}