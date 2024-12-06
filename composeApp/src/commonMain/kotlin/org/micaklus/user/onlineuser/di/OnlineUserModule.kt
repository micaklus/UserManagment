package org.micaklus.user.onlineuser.di

import org.koin.dsl.module
import org.micaklus.user.onlineuser.data.network.JsonPlaceholderApi
import org.micaklus.user.onlineuser.data.repository.OnlineUserRepositoryImpl
import org.micaklus.user.onlineuser.data.repository.PostRepositoryImpl
import org.micaklus.user.onlineuser.domain.repository.OnlineUserRepository
import org.micaklus.user.onlineuser.domain.repository.PostRepository
import org.micaklus.user.onlineuser.ui.OnlineUserViewModel
import org.micaklus.user.onlineuser.ui.PostViewModel
import org.micaklus.user.statistic.ui.StatisticViewModel

val onlineUserModule = module {

    //Online dependencies
    single<JsonPlaceholderApi> { JsonPlaceholderApi() }


    //Repository dependencies
    single<OnlineUserRepository> { OnlineUserRepositoryImpl(get()) }
    single<PostRepository> { PostRepositoryImpl(get()) }


    //ViewModel dependencies
    factory { OnlineUserViewModel(get()) }
    factory { PostViewModel(get()) }
    factory { StatisticViewModel(get()) }
}

