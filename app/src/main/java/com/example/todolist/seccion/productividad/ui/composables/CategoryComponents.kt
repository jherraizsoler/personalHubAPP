package com.example.todolist.seccion.productividad.ui.composables

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

/**
 * Un composable para el diálogo de confirmación de eliminación de una categoría.
 *
 * @param categoryName El nombre de la categoría a eliminar.
 * @param onConfirm La acción a realizar si el usuario confirma la eliminación.
 * @param onDismiss La acción a realizar si el usuario cancela o cierra el diálogo.
 */
@Composable
fun DeleteCategoryDialog(
    categoryName: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(text = "Eliminar Categoría")
        },
        text = {
            Text(text = "¿Estás seguro de que quieres eliminar la categoría '$categoryName'?")
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text("Eliminar", color = Color.Red)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}

/**
 * Composable para mostrar las categorías como chips.
 * Añade soporte para la pulsación larga para eliminar una categoría.
 *
 * @param categories La lista de categorías a mostrar.
 * @param onCategoryLongPress La lambda a ejecutar cuando se mantiene pulsado un chip.
 */
@OptIn(ExperimentalLayoutApi::class, ExperimentalFoundationApi::class)
@Composable
fun CategoryChips(
    categories: List<String>,
    onCategoryLongPress: (String) -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }
    var selectedCategory by remember { mutableStateOf<String?>(null) }

    if (showDialog && selectedCategory != null) {
        DeleteCategoryDialog(
            categoryName = selectedCategory!!,
            onConfirm = {
                onCategoryLongPress(selectedCategory!!)
                showDialog = false
                selectedCategory = null
            },
            onDismiss = {
                showDialog = false
                selectedCategory = null
            }
        )
    }

    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        categories.forEach { category ->
            AssistChip(
                onClick = {},
                label = { Text(text = category) },
                colors = AssistChipDefaults.assistChipColors(
                    containerColor = Color(0xFFE0E0E0),
                    labelColor = Color.Black
                ),
                modifier = Modifier.combinedClickable(
                    onLongClick = {
                        selectedCategory = category
                        showDialog = true
                    },
                    onClick = {}
                )
            )
        }
    }
}

/**
 * Composable para mostrar una fila de chips filtrables para las categorías.
 *
 * @param allCategories Todas las categorías disponibles.
 * @param selectedCategories Las categorías seleccionadas para el filtrado.
 * @param onCategorySelectionChange La lambda a ejecutar cuando cambia la selección.
 */
@Composable
fun CategoryFilterRow(
    allCategories: List<String>,
    selectedCategories: List<String>,
    onCategorySelectionChange: (List<String>) -> Unit
) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 4.dp)
    ) {
        items(allCategories) { category ->
            val isSelected = selectedCategories.contains(category)
            FilterChip(
                selected = isSelected,
                onClick = {
                    val newSelectedCategories = if (isSelected) {
                        selectedCategories - category
                    } else {
                        selectedCategories + category
                    }
                    onCategorySelectionChange(newSelectedCategories)
                },
                label = { Text(category) }
            )
        }
    }
}
