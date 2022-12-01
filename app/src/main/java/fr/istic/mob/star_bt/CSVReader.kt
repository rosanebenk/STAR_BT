//package fr.istic.mob.star_bt
//
//import android.content.Context
//import java.io.*
//
//class CSVReader {
//    var context: Context? = null
//    var file: File = null
//    var rows: MutableList<Array<String>> = ArrayList()
//
//    fun CSVReader(context: Context?, file: File) {
//        this.context = context
//        this.file = file
//    }
//
//    @Throws(IOException::class)
//    fun readCSV(): List<Array<String>>? {
//        val fileInputStream = FileInputStream(file)
//        val isr = InputStreamReader(fileInputStream)
//        val br = BufferedReader(isr)
//        val csvSplitBy = ","
//        br.readLine()
//        while (br.readLine().also { line = it } != null) {
//            val row = line.split(csvSplitBy).toTypedArray()
//            rows.add(row)
//        }
//        return rows
//    }
//}