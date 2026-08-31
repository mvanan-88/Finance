package com.mathi.finance.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mathi.finance.features.contacts.data.local.ContactDao
import com.mathi.finance.features.contacts.data.local.ContactEntity

@Database(entities = [ContactEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun contactDao(): ContactDao
}
