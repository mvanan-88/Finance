package com.mathi.finance.features.contacts.domain.repository

import com.mathi.finance.features.contacts.domain.model.Contact
import kotlinx.coroutines.flow.Flow

interface ContactRepository {
    val contacts: Flow<List<Contact>>
    suspend fun saveContactsLocally(contacts: List<Contact>)
    suspend fun syncWithRemote()
}
