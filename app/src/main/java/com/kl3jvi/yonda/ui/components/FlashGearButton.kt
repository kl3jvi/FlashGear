package com.kl3jvi.yonda.ui.components

import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun FlashGearButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Button(
        modifier = modifier,
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF589DFF)),
    ) {
        Text(text = text, color = Color.White)
    }
}

@Preview
@Composable
fun FlashGearButtonPreview() {
    FlashGearButton(text = "Connect", onClick = {})
}
