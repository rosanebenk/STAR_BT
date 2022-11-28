package fr.istic.mob.star_bt

import androidx.room.*
import java.util.*


@Entity(
    tableName = "calendar")
data class calendar(
    val StartDate : Date,
    val EndDate: Date,
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

