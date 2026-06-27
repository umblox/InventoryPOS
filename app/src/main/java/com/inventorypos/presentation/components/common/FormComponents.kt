package com.inventorypos.presentation.components.common

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.inventorypos.presentation.theme.PremiumGold
import com.inventorypos.presentation.theme.PremiumTextPrimary

@Composable
fun SectionHeader(icon: androidx.compose.ui.graphics.vector.ImageVector, title: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
    ) {
        Icon(icon, contentDescription = null, tint = PremiumGold, modifier = Modifier.size(24.dp))
        Spacer(modifier = Modifier.width(8.dp))
        Text(title, style = MaterialTheme.typography.titleMedium, color = PremiumTextPrimary)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryDropdown(
    selectedCategoryId: Long?,
    onCategorySelected: (Long) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            value = if (selectedCategoryId != null && selectedCategoryId > 0) "Category $selectedCategoryId" else "Select Category",
            onValueChange = {},
            readOnly = true,
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier.fillMaxWidth().menuAnchor()
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(text = { Text("Food") }, onClick = { onCategorySelected(1L); expanded = false })
            DropdownMenuItem(text = { Text("Drink") }, onClick = { onCategorySelected(2L); expanded = false })
            DropdownMenuItem(text = { Text("Snack") }, onClick = { onCategorySelected(3L); expanded = false })
        }
    }
}
