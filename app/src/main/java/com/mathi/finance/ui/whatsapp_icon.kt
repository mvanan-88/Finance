package com.mathi.finance.ui

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val WhatsAppVector: ImageVector
    get() = ImageVector.Builder(
        name = "WhatsApp",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f
    ).path(fill = SolidColor(Color.Unspecified)) {
        moveTo(12.012f, 2f)
        curveTo(6.48f, 2f, 2f, 6.48f, 2f, 12.012f)
        curveTo(2f, 13.78f, 2.463f, 15.512f, 3.342f, 17.037f)
        lineTo(2f, 22f)
        lineTo(7.085f, 20.672f)
        curveTo(8.563f, 21.48f, 10.25f, 21.912f, 12.012f, 21.912f)
        curveTo(17.544f, 21.912f, 22.024f, 17.432f, 22.024f, 11.9f)
        curveTo(22.024f, 6.368f, 17.544f, 2f, 12.012f, 2f)
        close()
        moveTo(16.59f, 15.42f)
        curveTo(16.4f, 15.95f, 15.65f, 16.39f, 15.05f, 16.52f)
        curveTo(14.64f, 16.6f, 14.1f, 16.67f, 12.31f, 15.93f)
        curveTo(10.02f, 14.98f, 8.54f, 12.65f, 8.43f, 12.5f)
        curveTo(8.32f, 12.35f, 7.51f, 11.28f, 7.51f, 10.17f)
        curveTo(7.51f, 9.06f, 8.08f, 8.52f, 8.31f, 8.28f)
        curveTo(8.5f, 8.08f, 8.78f, 7.99f, 9.06f, 7.99f)
        curveTo(9.15f, 7.99f, 9.24f, 7.99f, 9.32f, 8f)
        curveTo(9.56f, 8.01f, 9.68f, 8.03f, 9.84f, 8.41f)
        curveTo(10.04f, 8.89f, 10.53f, 10.09f, 10.59f, 10.22f)
        curveTo(10.65f, 10.35f, 10.71f, 10.52f, 10.62f, 10.7f)
        curveTo(10.53f, 10.88f, 10.46f, 10.97f, 10.33f, 11.12f)
        curveTo(10.2f, 11.27f, 10.06f, 11.45f, 9.94f, 11.58f)
        curveTo(9.81f, 11.72f, 9.68f, 11.87f, 9.83f, 12.13f)
        curveTo(9.98f, 12.39f, 10.5f, 13.24f, 11.26f, 13.92f)
        curveTo(12.24f, 14.79f, 13.04f, 15.07f, 13.32f, 15.19f)
        curveTo(13.58f, 15.3f, 13.73f, 15.28f, 13.88f, 15.1f)
        curveTo(14.04f, 14.92f, 14.54f, 14.34f, 14.71f, 14.1f)
        curveTo(14.88f, 13.86f, 15.06f, 13.9f, 15.3f, 13.99f)
        curveTo(15.54f, 14.08f, 16.83f, 14.72f, 17.1f, 14.85f)
        curveTo(17.37f, 14.98f, 17.55f, 15.05f, 17.62f, 15.16f)
        curveTo(17.68f, 15.27f, 17.68f, 15.75f, 17.49f, 16.28f)
        close()
    }.build()

@Composable
fun WhatsAppIcon(modifier: Modifier = Modifier) {
    Icon(
        imageVector = WhatsAppVector,
        contentDescription = "WhatsApp",
        tint = Color(0xFF25D366),
        modifier = modifier.size(24.dp)
    )
}
