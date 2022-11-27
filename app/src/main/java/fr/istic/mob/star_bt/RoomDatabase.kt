package fr.istic.mob.star_bt


import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [bus_route::class,calendar::class,trip::class], version = 1, exportSchema = false)
public abstract class RoomDatabase : RoomDatabase() {

    abstract fun bus_route_DAO(): bus_route_DAO
    abstract fun calendar_DAO(): calendar_DAO
    abstract fun trip_DAO(): trip_DAO


    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: RoomDatabase? = null

        fun getDatabase(context: Context): RoomDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    RoomDatabase::class.java,
                    "StarBus"
                ).build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}