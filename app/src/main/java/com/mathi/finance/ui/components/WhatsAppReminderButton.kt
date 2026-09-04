package com.mathi.finance.ui.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.mathi.finance.core.util.WhatsAppHelper
import com.mathi.finance.ui.WhatsAppVector

/**
 * A Jetpack Compose button that triggers a WhatsApp reminder.
 * Styled with a characteristic WhatsApp green color.
 */
@Composable
fun WhatsAppReminderButton(
    phoneNumber: String,
    message: String,
    modifier: Modifier = Modifier,
    buttonText: String = "WhatsApp"
) {
    val context = LocalContext.current
    val whatsAppGreen = Color(0xFF25D366)

    Button(
        onClick = {
            WhatsAppHelper.sendWhatsAppReminder(context, phoneNumber, message)
        },
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(
            containerColor = whatsAppGreen,
            contentColor = Color.White
        )
    ) {
        Icon(
            imageVector = WhatsAppVector,
            contentDescription = null
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = buttonText)
    }
}
