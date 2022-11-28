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
}