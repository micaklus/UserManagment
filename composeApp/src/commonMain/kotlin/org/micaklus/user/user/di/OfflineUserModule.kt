package org.micaklus.user.user.di

import org.koin.dsl.module
import org.micaklus.user.user.data.local.UserLocalDataSource
import org.micaklus.user.user.data.repository.UserRepositoryImpl
import org.micaklus.user.user.domain.repository.UserRepository
import org.micaklus.user.user.ui.FormUserViewModel
import org.micaklus.user.user.ui.UserViewModel

val offlineUserModule = module {
    single { UserLocalDataSource(get()) }

    //Repository dependencies
    single<UserRepository> { UserRepositoryImpl(get()) }


    //ViewModel dependencies
    factory { FormUserViewModel(get()) }
    factory { UserViewModel(get()) }

}

