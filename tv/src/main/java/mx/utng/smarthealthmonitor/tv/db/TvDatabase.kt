// tv/.../tv/db/TvDatabase.kt
package mx.utng.smarthealthmonitor.tv.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [TvLecturaFC::class], version = 1, exportSchema = false)
abstract class TvDatabase : RoomDatabase() {

    abstract fun tvLecturaDao(): TvLecturaFCDao

    companion object {
        @Volatile
        private var INSTANCE: TvDatabase? = null

        fun getDatabase(context: Context): TvDatabase =
            INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    TvDatabase::class.java,
                    "tv_smart_health_db"
                ).build().also { INSTANCE = it }
            }
    }
}
