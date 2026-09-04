package com.mathi.finance.core.di

import androidx.room.Room
import com.mathi.finance.core.database.AppDatabase
import com.mathi.finance.core.network.ConnectivityObserver
import com.mathi.finance.core.network.NetworkObserver
import com.mathi.finance.core.prefs.PreferenceManager
import com.mathi.finance.features.auth.data.repository.AuthRepositoryImpl
import com.mathi.finance.features.auth.domain.repository.AuthRepository
import com.mathi.finance.features.auth.presentation.LoginViewModel
import com.mathi.finance.features.contacts.data.repository.ContactRepositoryImpl
import com.mathi.finance.features.contacts.domain.repository.ContactRepository
import com.mathi.finance.features.contacts.presentation.ContactViewModel
import com.mathi.finance.features.home.HomeViewModel
import com.mathi.finance.features.home.data.repository.HomeRepositoryImpl
import com.mathi.finance.features.home.domain.repository.HomeRepository
import com.mathi.finance.features.master.data.repository.MasterRepositoryImpl
import com.mathi.finance.features.master.domain.repository.MasterRepository
import com.mathi.finance.features.master.presentation.MasterViewModel
import com.mathi.finance.features.transactions.data.repository.TransactionRepositoryImpl
import com.mathi.finance.features.transactions.domain.repository.TransactionRepository
import com.mathi.finance.features.transactions.presentation.TransactionViewModel
import org.koin.android.ext.koin.androidApplication
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
    single { PreferenceManager(androidContext()) }
    single<NetworkObserver> { ConnectivityObserver(androidContext()) }
    
    // Repositories
    single<AuthRepository> { AuthRepositoryImpl(get()) }
    single<MasterRepository> { MasterRepositoryImpl(get()) }
    single<TransactionRepository> { TransactionRepositoryImpl(get()) }
    single<ContactRepository> { ContactRepositoryImpl(get(), get()) }
    single<HomeRepository> { HomeRepositoryImpl(get()) }
    
    viewModel { LoginViewModel(get()) }
    viewModel { MasterViewModel(get()) }
    viewModel { TransactionViewModel(get()) }
    viewModel { ContactViewModel(androidApplication(), get(), get()) }
    viewModel { HomeViewModel(get(), get()) }
}
