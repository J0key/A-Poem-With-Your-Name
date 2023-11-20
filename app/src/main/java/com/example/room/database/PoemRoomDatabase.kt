package com.example.room.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Poem::class], version = 1, exportSchema = false)
abstract class PoemRoomDatabase : RoomDatabase(){
    abstract fun PoemDao(): PoemDao?

    companion object{
        @Volatile
        private var INSTANCE:PoemRoomDatabase? = null
        fun getDatabase(context: Context): PoemRoomDatabase?{
            if (INSTANCE == null){
                synchronized(PoemRoomDatabase::class.java){
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        PoemRoomDatabase::class.java,
                        "poem_database"
                    ).build()
                }
            }
            return INSTANCE
        }
    }

}