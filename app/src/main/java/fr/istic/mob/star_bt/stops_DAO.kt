package fr.istic.mob.star_bt

import androidx.room.*

@Dao
interface stops_DAO {
    @Insert
    fun addObjet(objet: stops):Long

    @Query("SELECT * FROM stops")
    fun getAllObjects():List<stops>

    @Query("DELETE FROM stops")
    fun deleteAllObjects()
}