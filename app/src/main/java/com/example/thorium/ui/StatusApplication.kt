package com.example.thorium.ui

import android.app.Application
import com.example.thorium.db.AppDatabase
import com.example.thorium.repositories.StatusRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob


class StatusApplication : Application() {

    val applicationScope = CoroutineScope(SupervisorJob())

    val database by lazy { AppDatabase.getDatabase(this, applicationScope) }
    val repository by lazy { StatusRepository(database.getStatusDao()) }
}