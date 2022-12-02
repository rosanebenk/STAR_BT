package fr.istic.mob.star_bt

import androidx.room.*

@Dao
interface stop_times_DAO {
    @Insert
    fun addObjet(objet: stopTime):Long

    @Query("SELECT * FROM stopTime")
    fun getAllObjects():List<stopTime>

    @Query("DELETE FROM stopTime")
    fun deleteAllObjects()
}