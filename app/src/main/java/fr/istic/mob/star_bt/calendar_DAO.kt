package fr.istic.mob.star_bt

import androidx.room.*
import java.util.Calendar

@Dao
interface calendar_DAO {
    @Insert
    fun addObjet(objet: calendar):Long

    @Query("SELECT * FROM calendar")
    fun getAllObjects():List<calendar>

    @Query("DELETE FROM calendar")
    fun deleteAllObjects()

    @Query("SELECT * FROM calendar WHERE start_date = :startDate")
    fun getAllByDate(startDate: String):List<calendar>
/*
    @Query("SELECT service_id FROM calendar WHERE start_date = :startDate")
    fun getServiceId(startDate: String):List<calendar>
 */
}