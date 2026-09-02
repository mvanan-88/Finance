package com.mathi.finance.core.di

import androidx.room.Room
import com.mathi.finance.core.database.AppDatabase
import com.mathi.finance.core.network.ConnectivityObserver
import com.mathi.finance.core.network.NetworkObserver
import com.mathi.finance.core.prefs.PreferenceManager
import com.mathi.finance.features.auth.presentation.LoginViewModel
import com.mathi.finance.features.contacts.data.repository.ContactRepository
import com.mathi.finance.features.contacts.presentation.ContactViewModel
import com.mathi.finance.features.master.presentation.MasterViewModel
import com.mathi.finance.features.transactions.presentation.TransactionViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single { 
        Room.databaseBuilder(
            androidContext(),
            AppDatabase::class.java,
            "my_finance_db"
        ).build()
    }
    single { get<AppDatabase>().contactDao() }
    single { ContactRepository(get()) }
    single { PreferenceManager(androidContext()) }
    single<NetworkObserver> { ConnectivityObserver(androidContext()) }
    
    viewModel { LoginViewModel(get()) }
    viewModel { MasterViewModel(get()) }
    viewModel { TransactionViewModel(get()) }
    viewModel { ContactViewModel(get(), get(), get(), get()) }
}
