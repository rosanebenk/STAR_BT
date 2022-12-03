package fr.istic.mob.star_bt

import androidx.room.*

@Entity (
    tableName = "stoptime",

    //FK : trip_id ,stop_id => PK

//    foreignKeys = arrayOf(
//        ForeignKey(
//            entity = trip::class,
//            parentColumns = arrayOf("trip_id"),
//            childColumns = arrayOf("trip_id"),
//            onUpdate = ForeignKey.CASCADE,
//            onDelete = ForeignKey.CASCADE
//        ),
//        ForeignKey(
//            entity = stops::class,
//            parentColumns = arrayOf("stop_id"),
//            childColumns = arrayOf("stop_id"),
//            onUpdate = ForeignKey.CASCADE,
//            onDelete = ForeignKey.CASCADE
//        )
//
//    )



)

data class stopTime(

    val trip_id : String,
    val arrival_time : String,
    val departure_time: String,
    val stop_id : String,
    val stopSeq : String
)
{
    @PrimaryKey(autoGenerate = true) var stop_time_id : Int? = null
}


