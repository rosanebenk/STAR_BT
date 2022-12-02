package fr.istic.mob.star_bt

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.util.*

@Entity(
    tableName = "trip",
    // FK : route_id, service_id
    foreignKeys = arrayOf(
        ForeignKey(
            entity = bus_route::class,
            parentColumns = arrayOf("route_id"),
            childColumns = arrayOf("route_id"),
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE
        )
        ,
        ForeignKey(
            entity = calendar::class,
            parentColumns = arrayOf("service_id"),
            childColumns = arrayOf("service_id"),
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE
        )
    )
)
data class trip(
    @PrimaryKey() var trip_id: String,
    val headsign : String,
    val direction: String,
    val blockid : String,
    val wheelchairaccessible : String,
    val route_id :String,
    val service_id:String,
)
