package fr.istic.mob.star_bt

import android.content.Context
import androidx.room.Room

object RoomService {
    lateinit var context: Context
    val appDatabase:RoomDatabase by lazy { Room.databaseBuilder(
        context,
        RoomDatabase::class.java,
        "StarBus"
    ).allowMainThreadQueries().build()
    }
}