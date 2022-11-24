package fr.istic.mob.star_bt

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(
    tableName = "trip"
    // FK : route_id, service_id
)
data class trip(
    val headsign : String,
    val direction: Int,
    val blockid : Int,
    val wheelchairaccessible : Int,

){
    @PrimaryKey(autoGenerate = true)
    var trip_id: Int? = null
}
