package com.inventorypos.presentation.components.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.inventorypos.presentation.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String? = null,
    placeholder: String? = null,
    leadingIcon: ImageVector? = null,
    trailingIcon: ImageVector? = null,
    onTrailingClick: (() -> Unit)? = null,
    isPassword: Boolean = false,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    singleLine: Boolean = true,
    maxLines: Int = 1,
    readOnly: Boolean = false,
    enabled: Boolean = true,
    errorMessage: String? = null
) {
    var passwordVisible by remember { mutableStateOf(false) }
    
    Column(modifier = modifier) {
        if (label != null) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                color = if (errorMessage != null) PremiumError else PremiumTextSecondary,
                modifier = Modifier.padding(bottom = 6.dp, start = 4.dp)
            )
        }
        
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .background(PremiumDarkSurface),
            placeholder = {
                if (placeholder != null) {
                    Text(
                        text = placeholder,
                        style = MaterialTheme.typography.bodyMedium,
                        color = PremiumTextMuted.copy(alpha = 0.5f)
                    )
                }
            },
            leadingIcon = if (leadingIcon != null) {
                {
                    Icon(
                        imageVector = leadingIcon,
                        contentDescription = null,
                        tint = PremiumGold.copy(alpha = 0.8f),
                        modifier = Modifier.size(22.dp)
                    )
                }
            } else null,
            trailingIcon = when {
                isPassword -> {
                    {
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(
                                imageVector = if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                contentDescription = if (passwordVisible) "Hide password" else "Show password",
                                tint = PremiumTextMuted
                            )
                        }
                    }
                }
                trailingIcon != null -> {
                    {
                        IconButton(onClick = { onTrailingClick?.invoke() }) {
                            Icon(
                                imageVector = trailingIcon,
                                contentDescription = null,
                                tint = PremiumGold.copy(alpha = 0.8f),
                                modifier = Modifier.size(22.dp)
                            )
                        }
                    }
                }
                else -> null
            },
            visualTransformation = if (isPassword && !passwordVisible) {
                PasswordVisualTransformation()
            } else {
                VisualTransformation.None
            },
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            singleLine = singleLine,
            maxLines = maxLines,
            readOnly = readOnly,
            enabled = enabled,
            isError = errorMessage != null,
            shape = RoundedCornerShape(14.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = PremiumTextPrimary,
                unfocusedTextColor = PremiumTextPrimary,
                focusedContainerColor = PremiumDarkSurface,
                unfocusedContainerColor = PremiumDarkSurface,
                focusedBorderColor = PremiumGold.copy(alpha = 0.6f),
                unfocusedBorderColor = Color(0xFF2A3050),
                errorBorderColor = PremiumError,
                cursorColor = PremiumGold,
                focusedLeadingIconColor = PremiumGold,
                unfocusedLeadingIconColor = PremiumTextMuted
            )
        )
        
        if (errorMessage != null) {
            Text(
                text = errorMessage,
                style = MaterialTheme.typography.bodySmall,
                color = PremiumError,
                modifier = Modifier.padding(start = 4.dp, top = 4.dp)
            )
        }
    }
}
