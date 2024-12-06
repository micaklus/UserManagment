package org.micaklus.user

import androidx.compose.ui.window.ComposeUIViewController
import org.micaklus.user.cache.DatabaseDriverFactory
import org.micaklus.user.user.di.UserModule

fun MainViewController() = ComposeUIViewController { App() }
val userModule = UserModule(DatabaseDriverFactory())
val userRepository = userModule.userRepository