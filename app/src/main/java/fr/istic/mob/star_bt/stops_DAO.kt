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

    @Query(" SELECT s.* FROM stops s JOIN (SELECT * FROM trip t \n" +
            "        INNER JOIN stoptime AS st ON t.trip_id = st.trip_id\n" +
            "        WHERE t.route_id=:routeid AND t.headsign=:direction \n" +
            "        GROUP BY stop_id ORDER BY cast(stopSeq as NUMERIC) \n" +
            "        ASC) \n" +
            "as t1 ON s.stop_id= t1.stop_id")
    fun getStopByRouteAndDirection(routeid: String, direction: String):List<stops>

/*
    @Query("SELECT wheelchair FROM stops WHERE stop_id = :stopId")
    fun stopAcceptWheelchair(stopId: String):List<String>

 */
}