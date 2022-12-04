package fr.istic.mob.star_bt


import androidx.room.*

@Dao
interface  bus_route_DAO {
    @Insert
    fun addObjet(objet: bus_route):Long

    @Query("SELECT * FROM bus_route")
    fun getAllObjects():List<bus_route>

    @Query("DELETE FROM bus_route")
    fun deleteAllObjects()

    @Query("SELECT DISTINCT short_name FROM bus_route")
    fun getAllRoutesNames():List<String>

    @Query("SELECT route_id FROM bus_route where short_name = :shortName")
    fun getRouteIdByName(shortName: String):List<String>

    @Query("SELECT color FROM bus_route where short_name = :shortName")
    fun getColorByName(shortName: String):List<String>

    @Query("SELECT * FROM bus_route where route_id = :routeId")
    fun getRouteById(routeId: String):List<bus_route>
}