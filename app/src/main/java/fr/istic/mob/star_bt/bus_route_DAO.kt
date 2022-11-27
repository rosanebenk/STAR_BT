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
}