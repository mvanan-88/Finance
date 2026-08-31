package com.mathi.finance.features.contacts.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ContactDao {
    @Query("SELECT * FROM contacts")
    fun getAllContacts(): Flow<List<ContactEntity>>

    @Query("SELECT * FROM contacts WHERE isSynced = 0")
    suspend fun getUnsyncedContacts(): List<ContactEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertContacts(contacts: List<ContactEntity>)

    @Query("UPDATE contacts SET isSynced = 1 WHERE id IN (:ids)")
    suspend fun markAsSynced(ids: List<String>)

    @Query("DELETE FROM contacts")
    suspend fun clearAll()
}
