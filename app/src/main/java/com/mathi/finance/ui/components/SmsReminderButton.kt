package com.mathi.finance.ui.components

import android.Manifest
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Sms
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import com.mathi.finance.core.util.SmsHelper

/**
 * A Jetpack Compose button component that handles SMS permissions and
 * triggers a background payment reminder.
 */
@Composable
fun SmsReminderButton(
    phoneNumber: String,
    message: String,
    modifier: Modifier = Modifier,
    buttonText: String = "Send Reminder"
) {
    val context = LocalContext.current
    
    val smsLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (isGranted) {
                SmsHelper.sendSmsReminder(context, phoneNumber, message)
            } else {
                Toast.makeText(context, "SMS Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }
    )

    Button(
        onClick = {
            when (PackageManager.PERMISSION_GRANTED) {
                ContextCompat.checkSelfPermission(context, Manifest.permission.SEND_SMS) -> {
                    SmsHelper.sendSmsReminder(context, phoneNumber, message)
                }
                else -> {
                    smsLauncher.launch(Manifest.permission.SEND_SMS)
                }
            }
        },
        modifier = modifier
    ) {
        Icon(imageVector = Icons.Default.Sms, contentDescription = null)
        Text(text = " $buttonText")
    }
}
