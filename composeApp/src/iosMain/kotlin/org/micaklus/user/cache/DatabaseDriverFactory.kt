package org.micaklus.user.cache

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import sqldelight.org.micaklus.user.database.AppDatabase

actual class DatabaseDriverFactory  {
   actual fun createDriver(): SqlDriver {
        return NativeSqliteDriver(AppDatabase.Schema, "app.db")
    }
}