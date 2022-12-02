package fr.istic.mob.star_bt

import android.content.Context
import java.io.*

class CSVReader (context: Context, file: File) {
    val context: Context
    val file: File
    var rows: MutableList<Array<String>> = ArrayList()

    init {
        this.context = context
        this.file = file
    }


    @Throws(IOException::class)
    fun readCSV(): List<Array<String>>? {
        val fileInputStream = FileInputStream(file)
        val isr = InputStreamReader(fileInputStream)
        val br = BufferedReader(isr)
        val csvSplitBy = ","
        br.readLine()
        br.forEachLine {
            val ligne = it.split(csvSplitBy).toTypedArray()
            rows.add(ligne)
        }
        return rows
    }
}