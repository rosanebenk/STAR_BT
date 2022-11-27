package fr.istic.mob.star_bt

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
    val ShortName : String,
    val LongName: String,
    val Description : String,
    val Type : Int,
    val Color: Int,
    val TextColor : Int,
){
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null
}


