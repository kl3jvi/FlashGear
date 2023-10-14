package com.kl3jvi.yonda.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun FlashGearButtonSimple(
    modifier: Modifier = Modifier,
    text: String,
    onClick: () -> Unit,
) {
    Button(
        modifier = modifier,
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF589DFF),
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
            disabledContainerColor = MaterialTheme.colorScheme.tertiaryContainer,
            disabledContentColor = MaterialTheme.colorScheme.onTertiaryContainer,
        ),
    ) {
        Text(text = text, color = Color.White)
    }
}

@Composable
fun FlashGearButtonGradient(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    text: String,
    enabled: Boolean = true,
    colors: List<Color> = listOf(Color(0xFFFFB73D), Color(0xFFFFA000)),
    cornerRadius: Dp = 8.dp,
) {
    Button(
        modifier = modifier
            .background(
                Brush.linearGradient(
                    if (enabled) colors else listOf(Color(0xFFFF3D3D), Color(0xFFFF0000)),
                ),
                shape = RoundedCornerShape(cornerRadius),
            ),
        onClick = onClick,
        colors = ButtonDefaults.textButtonColors(
            contentColor = Color.Transparent
        ),
    ) {
        Text(text, color = Color.White)
    }
}

@Preview
@Composable
fun FlashGearButtonPreview() {
    Column {
        FlashGearButtonSimple(text = "Connect", onClick = {})
        FlashGearButtonGradient(text = "Connect", onClick = {})
    }
}
