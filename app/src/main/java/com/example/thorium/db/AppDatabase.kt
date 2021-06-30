package com.example.thorium.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.thorium.db.entities.Status
import com.example.thorium.db.entities.StatusDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [Status::class],
    version = 3,
     exportSchema = false
)
abstract class AppDatabase : RoomDatabase(){

    abstract fun getStatusDao() : StatusDao

    companion object{

        @Volatile
        private var instance: AppDatabase?= null

        fun getDatabase(context: Context, scope: CoroutineScope): AppDatabase {
            return instance ?: synchronized(this) {
                val ins = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "mydb.db"
                ).fallbackToDestructiveMigration().addCallback(StatusDatabaseCallback(scope)).build()
                instance = ins
                return ins
            }
        }


        private class StatusDatabaseCallback(
            private val scope: CoroutineScope
        ) : RoomDatabase.Callback() {
            /**
             * Override the onCreate method to populate the database.
             */
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                // If you want to keep the data through app restarts,
                // comment out the following line.
                instance?.let { database ->
                    scope.launch(Dispatchers.IO) {
                        populateDatabase(database.getStatusDao())
                    }
                }
            }

            suspend fun populateDatabase(statusDao: StatusDao) {

            }
        }


    }
}
