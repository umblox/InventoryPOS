package com.inventorypos.presentation.screens.inventory.product

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.inventorypos.presentation.theme.*

@Composable
fun ProductImagePicker(
    currentImage: String? = null,
    onImageSelected: (String?) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(PremiumDarkSurface),
        contentAlignment = Alignment.Center
    ) {
        if (currentImage != null) {
            AsyncImage(
                model = currentImage,
                contentDescription = "Product Image",
                modifier = Modifier.fillMaxSize()
            )
        } else {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    Icons.Default.AddAPhoto,
                    contentDescription = "Add Photo",
                    tint = PremiumGold.copy(alpha = 0.5f),
                    modifier = Modifier.size(48.dp)
                )
                Text(
                    text = "Tap to add image",
                    style = MaterialTheme.typography.bodySmall,
                    color = PremiumTextMuted
                )
            }
        }
        
        IconButton(
            onClick = { /* Open image picker */ },
            modifier = Modifier.align(Alignment.BottomEnd)
        ) {
            Icon(
                Icons.Default.AddAPhoto,
                contentDescription = "Change Image",
                tint = PremiumGold
            )
        }
    }
}
