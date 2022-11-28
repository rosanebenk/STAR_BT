package fr.istic.mob.star_bt

import androidx.room.*
import java.text.DateFormat
import java.util.*


@Entity(
    tableName = "calendar")
data class calendar(
    val StartDate : String,
    val EndDate: String ,
    val monday : Int,
    val tuesday : Int,
    val wednesday: Int,
    val thursday : Int,
    val friday : Int,
    val saturday : Int,
    val sunday : Int
){
    @PrimaryKey(autoGenerate = true)
    var service_id: Int? = null
}

