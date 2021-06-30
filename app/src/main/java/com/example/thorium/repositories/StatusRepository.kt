package com.example.thorium.repositories

import androidx.annotation.WorkerThread
import com.example.thorium.db.entities.Status
import com.example.thorium.db.entities.StatusDao
import kotlinx.coroutines.flow.Flow


class StatusRepository(private val statusDao: StatusDao) {

    val allStatus: Flow<List<Status>> = statusDao.getAll()

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(status: Status) {
        statusDao.inpsert(status)
    }
}