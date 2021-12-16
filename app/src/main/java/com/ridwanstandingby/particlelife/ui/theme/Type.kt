package com.ridwanstandingby.particlelife.ui.theme

import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

val fontFamily: FontFamily = FontFamily.Default

val Typography = Typography(
    h5 = TextStyle(
        fontFamily = fontFamily,
        fontWeight = FontWeight.W600,
        fontSize = 18.sp
    ),
    body1 = TextStyle(
        fontFamily = fontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp
    ),
    button = TextStyle(
        fontFamily = fontFamily,
        fontWeight = FontWeight.W500,
        fontSize = 14.sp
    ),
    caption = TextStyle(
        fontFamily = fontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 10.sp
    )
)
