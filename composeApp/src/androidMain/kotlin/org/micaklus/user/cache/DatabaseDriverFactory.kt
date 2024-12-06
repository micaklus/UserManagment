package org.micaklus.user.cache


import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import sqldelight.org.micaklus.user.database.AppDatabase

  class AndroidDatabaseDriverFactory (val context: Context)  : DatabaseDriverFactory{
   override fun createDriver(): SqlDriver {
        return AndroidSqliteDriver(AppDatabase.Schema, context, "app.db")
    }
}