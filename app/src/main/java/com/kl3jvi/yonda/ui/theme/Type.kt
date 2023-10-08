package com.kl3jvi.yonda.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp

object TypographyTokens {
    val BodyLarge = TextStyle(
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp,
    )
    val DisplayLarge = TextStyle(fontSize = 30.sp)
    val BodyMedium = TextStyle(fontSize = 16.sp)
    val BodySmall = TextStyle(fontSize = 14.sp)
    val DisplayMedium = TextStyle(fontSize = 20.sp)
    val DisplaySmall = TextStyle(fontSize = 16.sp)
}

val Typography = Typography(
    bodyLarge = TypographyTokens.BodyLarge,
    displayLarge = TypographyTokens.DisplayLarge,
    bodyMedium = TypographyTokens.BodyMedium,
    bodySmall = TypographyTokens.BodySmall,
    displayMedium = TypographyTokens.DisplayMedium,
    displaySmall = TypographyTokens.DisplaySmall,
)
