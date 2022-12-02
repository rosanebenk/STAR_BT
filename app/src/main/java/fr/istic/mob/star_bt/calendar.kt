package fr.istic.mob.star_bt

import androidx.room.*
import java.text.DateFormat
import java.util.*


@Entity(
    tableName = "calendar")
data class calendar(
    @PrimaryKey() var service_id: String,
    val StartDate : String,
    val EndDate: String ,
    val monday : String,
    val tuesday : String,
    val wednesday: String,
    val thursday : String,
    val friday : String,
    val saturday : String,
    val sunday : String
)

