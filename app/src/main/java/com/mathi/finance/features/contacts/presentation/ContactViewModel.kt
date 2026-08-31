package com.mathi.finance.features.contacts.presentation

import android.app.Application
import android.provider.ContactsContract
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.mathi.finance.core.network.SupabaseClient
import com.mathi.finance.core.network.NetworkObserver
import com.mathi.finance.core.prefs.PreferenceManager
import com.mathi.finance.features.contacts.data.repository.ContactRepository
import com.mathi.finance.features.contacts.domain.model.Contact
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ContactViewModel(
    application: Application,
    private val preferenceManager: PreferenceManager,
    private val repository: ContactRepository,
    private val networkObserver: NetworkObserver
) : AndroidViewModel(application) {

    val contacts: StateFlow<List<Contact>> = repository.contacts.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _syncStatus = MutableStateFlow<String?>(null)
    val syncStatus: StateFlow<String?> = _syncStatus

    init {
        observeNetwork()
    }

    private fun observeNetwork() {
        viewModelScope.launch {
            networkObserver.observe().collectLatest { status ->
                if (status == NetworkObserver.Status.Available) {
                    val currentUserId = preferenceManager.getUserId()
                    if (currentUserId != -1) {
                        try {
                            repository.syncWithRemote(currentUserId)
                            _syncStatus.value = "Auto-sync successful"
                        } catch (e: Exception) {
                            // Silently fail or log for auto-sync
                        }
                    }
                }
            }
        }
    }

    fun fetchContacts() {
        viewModelScope.launch {
            _isLoading.value = true
            val contactList = withContext(Dispatchers.IO) {
                getContacts()
            }
            
            val currentUserId = preferenceManager.getUserId()
            if (currentUserId != -1) {
                repository.saveContactsLocally(contactList, currentUserId)
            }
            
            _isLoading.value = false
        }
    }

    fun syncContacts() {
        val currentUserId = preferenceManager.getUserId()
        if (currentUserId == -1) {
            _syncStatus.value = "User not logged in"
            return
        }

        viewModelScope.launch {
            _isLoading.value = true
            _syncStatus.value = "Syncing..."
            try {
                repository.syncWithRemote(currentUserId)
                _syncStatus.value = "Sync successful"
            } catch (e: Exception) {
                _syncStatus.value = "Sync failed: ${e.localizedMessage}"
            }
            _isLoading.value = false
        }
    }

    private fun getContacts(): List<Contact> {
        val contacts = mutableListOf<Contact>()
        val contentResolver = getApplication<Application>().contentResolver
        val cursor = contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            null,
            null,
            null,
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC"
        )

        cursor?.use {
            val idIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID)
            val nameIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
            val numberIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)

            while (it.moveToNext()) {
                val id = it.getInt(idIndex)
                val name = it.getString(nameIndex)
                val number = it.getString(numberIndex)
                contacts.add(Contact(id, name, number))
            }
        }
        return contacts
    }
}
