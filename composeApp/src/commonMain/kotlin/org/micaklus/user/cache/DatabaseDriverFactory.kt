package org.micaklus.user.cache

import app.cash.sqldelight.db.SqlDriver

 interface DatabaseDriverFactory {
    fun createDriver(): SqlDriver
}