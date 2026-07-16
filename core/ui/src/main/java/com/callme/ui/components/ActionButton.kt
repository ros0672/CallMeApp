package com.callme.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun ActionButton(
    text: String,
    action: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isSecondary: Boolean = false
) {
    Button(
        onClick = action,
        modifier = modifier.fillMaxWidth(),
        enabled = enabled,
        colors = if (isSecondary) ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            contentColor = MaterialTheme.colorScheme.secondary,
        ) else ButtonDefaults.buttonColors()
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Medium
        )
    }
}

@Preview
@Composable
fun ActionButtonPreview() {
    ActionButton("Войти", {})
}

@Preview
@Composable
fun ActionButtonDisabledPreview() {
    ActionButton("Войти", {}, enabled = false)
}

@Preview
@Composable
fun ActionButtonSecondaryPreview() {
    ActionButton("Войти", {}, isSecondary = true)
}