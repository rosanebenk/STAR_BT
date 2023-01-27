package fr.istic.mob.star_bt
import androidx.room.*

@Dao
interface trip_DAO {
    @Insert
    fun addObjet(objet: trip):Long

    @Query("SELECT * FROM trip")
    fun getAllObjects():List<trip>

    @Query("DELETE FROM trip")
    fun deleteAllObjects()
    // use this to search for direction for the list
    @Query("SELECT DISTINCT headsign FROM trip WHERE route_id = :id" /*and direction = :direction*/)
    fun getDirections(id: String /*, direction: Int*/):List<String>

    @Query("SELECT DISTINCT * FROM trip WHERE route_id = :id" /*and direction = :direction*/)
    fun getDirectionsV2(id: String /*, direction: Int*/):List<trip>

    @Query("SELECT DISTINCT headsign FROM trip WHERE trip_id = :id" /*and direction = :direction*/)
    fun getDirection(id: String /*, direction: Int*/):List<String>

    @Query("SELECT * FROM trip where route_id = :routeId and trip_id = :tripId and direction_id = :directionId and service_id = :serviceId")
    fun getTripsByDirectionAndDate(routeId: String, tripId: String, directionId: String, serviceId: String):List<trip>

    @Query("SELECT * FROM trip where route_id = :routeId and trip_id = :tripId and headsign = :headsign")
    fun getTripsByDirection(routeId: String, tripId: String, headsign: String):List<trip>

    @Query("SELECT * FROM trip where route_id = :routeId and trip_id = :tripId and headsign = :headsign and service_id = :serviceId and wheelchairaccessible = 1")
    fun getTripsByAccessibilityAndDirectionAndDate(routeId: String, tripId: String, headsign: String, serviceId: String):List<trip>

    @Query("SELECT trip_id FROM trip where route_id = :routeId and headsign = :headsign")
    fun getTripsByRouteAndDirection(routeId: String, headsign: String):List<String>

    @Query("SELECT route_id FROM trip where trip_id = :tripId")
    fun getRouteIDbyTripID(tripId: String):List<String>
}