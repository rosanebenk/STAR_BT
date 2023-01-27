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

    @Query("SELECT * FROM stopTime WHERE stop_id=:stopid and trip_id=:tripid")
    fun getStopTimeByStopIDandTripID(stopid: String, tripid: String):List<stopTime>

    @Query("SELECT * FROM stopTime WHERE stop_id=:stopid and trip_id=:tripid")
    fun getArrivalTimeByStopIDandTripID(stopid: String, tripid: String):List<stopTime>

    @Query("SELECT st.* FROM stoptime st " +
            "INNER JOIN trip t ON st.trip_id = t.trip_id " +
            "INNER JOIN calendar c ON t.service_id = c.service_id " +
            "WHERE stop_id = :stopId AND arrival_time > :arrivalTime AND arrival_time < \"24:00:01\" " +
            "AND :date  BETWEEN start_date AND end_date " +
            "AND monday = :lundi AND tuesday = :mardi  " +
            "AND wednesday = :mercredi AND thursday = :jeudi " +
            "AND friday = :vendredi AND saturday = :samedi AND sunday = :dimanche " +
            "ORDER BY start_date,arrival_time")
    fun getStopTimesWithGivenIdFromTime( stopId: String, arrivalTime: String, date: String, lundi : Int, mardi : Int, mercredi : Int, jeudi : Int, vendredi : Int, samedi : Int, dimanche : Int): List<stopTime>
}