package fr.istic.mob.star_bt

import androidx.room.*

@Entity (
    tableName = "stoptime",

    //FK : trip_id ,stop_id => PK

    foreignKeys = arrayOf(
        ForeignKey(
            entity = trip::class,
            parentColumns = arrayOf("trip_id"),
            childColumns = arrayOf("trip_id"),
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE
        )
    )



)

data class stopTime(
    @PrimaryKey(autoGenerate = true) val stop_time_id : Int,
    val arrival_time : String,
    val departure_time: String,
    val stopSeq : String,
    val trip_id : String
)


