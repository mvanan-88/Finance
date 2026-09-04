package com.mathi.finance.core.util

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast

object WhatsAppHelper {
    /**
     * Opens WhatsApp with a pre-filled message for the given phone number.
     * Handles number formatting, URL encoding, and missing app scenarios.
     */
    fun sendWhatsAppReminder(
        context: Context,
        phoneNumber: String,
        message: String
    ) {
        // Remove non-numeric characters and ensure country code (defaulting to +91 for India if missing)
        var cleanNumber = phoneNumber.replace(Regex("[^0-9]"), "")
        if (cleanNumber.length == 10) {
            cleanNumber = "91$cleanNumber"
        }

        val uri = Uri.parse("https://api.whatsapp.com/send?phone=$cleanNumber&text=${Uri.encode(message)}")
        val intent = Intent(Intent.ACTION_VIEW, uri).apply {
            setPackage("com.whatsapp")
        }

        try {
            context.startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            // Fallback for Business WhatsApp or regular WhatsApp via browser-style intent if direct package fails
            try {
                val genericIntent = Intent(Intent.ACTION_VIEW, uri)
                context.startActivity(genericIntent)
            } catch (ex: Exception) {
                Toast.makeText(context, "WhatsApp is not installed", Toast.LENGTH_LONG).show()
            }
        } catch (e: Exception) {
            Toast.makeText(context, "Failed to open WhatsApp: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
        }
    }
}
