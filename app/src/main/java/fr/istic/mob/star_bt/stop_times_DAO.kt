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

    @Query("SELECT arrival_time FROM stopTime WHERE trip_id = :tripId and stop_id = :stopId")
    fun getArrivalTime(tripId: String, stopId: String):List<String>

    @Query("SELECT departure_time FROM stopTime WHERE trip_id = :tripId and stop_id = :stopId")
    fun getDepartureTime(tripId: String, stopId: String):List<String>

    @Query("SELECT * FROM stopTime WHERE stop_id=:stopid")
    fun getStopTimeByStopID(stopid: String):List<stopTime>
}