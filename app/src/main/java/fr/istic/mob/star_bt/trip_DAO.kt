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

    @Query("SELECT DISTINCT headsign FROM trip WHERE trip_id = :id" /*and direction = :direction*/)
    fun getDirection(id: Int /*, direction: Int*/):List<String>
}