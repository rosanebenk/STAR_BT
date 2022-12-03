package fr.istic.mob.star_bt

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity (
    tableName = "bus_route"

    //FK : route id, agency_id

    /*foreignKeys = arrayOf(
        ForeignKey(
            entity = ObjetData::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("idObjet1"),
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE
        )
    )

     */

)
data class bus_route(
    @PrimaryKey() var route_id: String,
    val short_name: String,
    val long_name: String,
    val desc: String,
    val type: String,
    val color: String,
    val text_color: String

    )


