package fr.istic.mob.star_bt

import androidx.room.*

@Entity (
    tableName = "stoptime"

    //FK : trip_id ,stop_id => PK

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

data class stopTime(
    val arrival_time : String,
    val departure_time: String,
    val StopSeq : String,
)
//{
//    @PrimaryKey(autoGenerate = true)
//    var trip_id: Int? = null
//}

