package fr.istic.mob.star_bt


import androidx.room.*

@Entity (
    tableName = "stops"

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

data class stops(
    @PrimaryKey() var stop_id: String,
    val Stopcode : String,
    val Stopname: String,
    val Stopdesc : String,
    val Longitude : String,
    val Latitude : String,
    val Weelchair : String,
)
