package com.example.roomdatabase.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [(Movie::class), (Actors::class)],
    version = 1
)
abstract class MovieDB : RoomDatabase() {
    abstract fun movieDao(): MovieDao
    abstract fun actorsDao(): ActorsDao
    abstract fun moviecastDao(): MovieCastDao

    companion object {
        private var sInstance: MovieDB? = null

        @Synchronized
        fun get(context: Context): MovieDB {
            if (sInstance == null) {
                sInstance =
                    Room.databaseBuilder(
                        context.applicationContext,
                        MovieDB::class.java, "movies.db"
                    ).build()
            }
            return sInstance!!
        }
    }
}
