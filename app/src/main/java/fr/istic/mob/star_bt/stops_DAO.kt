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

    @Query("SELECT * FROM stops WHERE stop_id = :stopId")
    fun getStopById(stopId: String):List<stops>
/*
    @Query("SELECT stop_name FROM stops WHERE stop_id = :stopId")
    fun getStopName(stopId: String):List<stops>
*/
    @Query("SELECT * FROM stops WHERE wheelchair = 1")
    fun stopAcceptWheelchair():List<stops>

/*
    @Query("SELECT wheelchair FROM stops WHERE stop_id = :stopId")
    fun stopAcceptWheelchair(stopId: String):List<String>

 */
}