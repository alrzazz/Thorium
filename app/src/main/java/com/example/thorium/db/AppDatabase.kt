package com.example.thorium.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.thorium.db.entities.Status
import com.example.thorium.db.entities.StatusDao

@Database(
    entities = [Status::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase(){
    abstract fun getStatusDao() : StatusDao

    companion object{

        private var instance: AppDatabase?= null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK){
            instance ?: buildDatabase(context).also { instance = it }
        }

        fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "mydb.db"
        ).build()

    }
}
