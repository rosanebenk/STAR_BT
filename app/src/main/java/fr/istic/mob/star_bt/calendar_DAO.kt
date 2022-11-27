package fr.istic.mob.star_bt

import androidx.room.*

interface calendar_DAO {
    @Insert
    fun addObjet(objet: calendar):Long

    @Query("SELECT * FROM calendar")
    fun getAllObjects():List<calendar>

    @Query("DELETE FROM calendar")
    fun deleteAllObjects()
}