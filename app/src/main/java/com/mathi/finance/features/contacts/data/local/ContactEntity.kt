package com.mathi.finance.features.contacts.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "contacts")
data class ContactEntity(
    @PrimaryKey val id: String,
    val name: String,
    val phoneNumber: String?,
    val createdBy: Int?,
    val isSynced: Boolean = false
)
