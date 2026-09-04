package com.mathi.finance.core.util

import android.content.Context
import android.telephony.SmsManager
import android.telephony.TelephonyManager
import android.widget.Toast

object SmsHelper {
    /**
     * Helper function to send an SMS background reminder.
     * Handles multi-part messages and basic error checking.
     */
    fun sendSmsReminder(
        context: Context,
        phoneNumber: String,
        message: String
    ) {
        val telephonyManager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        if (telephonyManager.simState != TelephonyManager.SIM_STATE_READY) {
            Toast.makeText(context, "No SIM card detected or not ready", Toast.LENGTH_LONG).show()
            return
        }

        if (phoneNumber.isBlank()) {
            Toast.makeText(context, "Invalid phone number", Toast.LENGTH_SHORT).show()
            return
        }

        try {
            val smsManager: SmsManager = context.getSystemService(SmsManager::class.java)
                ?: @Suppress("DEPRECATION") SmsManager.getDefault()

            // Handle multi-part SMS for long strings
            val parts = smsManager.divideMessage(message)
            
            if (parts.size > 1) {
                smsManager.sendMultipartTextMessage(phoneNumber, null, parts, null, null)
            } else {
                smsManager.sendTextMessage(phoneNumber, null, message, null, null)
            }
            
            Toast.makeText(context, "Reminder sent to $phoneNumber", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Toast.makeText(context, "SMS failed: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
            e.printStackTrace()
        }
    }
}
