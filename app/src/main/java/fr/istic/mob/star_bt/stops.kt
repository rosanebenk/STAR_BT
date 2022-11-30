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
    val Stopcode : String,
    val Stopname: String,
    val Stopdesc : String,
    val Longitude : Long,
    val Latitude : Long,
    val Weelchair : Int,
){
    @PrimaryKey(autoGenerate = true)
    var stop_id: Int? = null
}
