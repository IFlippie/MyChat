package com.iflippie.mychat.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.iflippie.mychat.model.ColorItem

@Database(entities = [ColorItem::class], version = 1, exportSchema = false)
abstract class ColoursRoomDatabase : RoomDatabase() {

    abstract fun colorDao(): ColorDao

    companion object {
        private const val DATABASE_NAME = "REMINDER_DATABASE"

        @Volatile
        private var coloursRoomDatabaseInstance: ColoursRoomDatabase? = null

        fun getDatabase(context: Context): ColoursRoomDatabase? {
            if (coloursRoomDatabaseInstance == null) {
                synchronized(ColoursRoomDatabase::class.java) {
                    if (coloursRoomDatabaseInstance == null) {
                        coloursRoomDatabaseInstance = Room.databaseBuilder(context.applicationContext,ColoursRoomDatabase::class.java,DATABASE_NAME).build()
                    }
                }
            }
            return coloursRoomDatabaseInstance
        }
    }

}
