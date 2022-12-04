package fr.istic.mob.star_bt

import android.app.Activity
import android.app.ProgressDialog
import android.os.AsyncTask
import android.util.Log
import com.google.gson.GsonBuilder
import com.google.gson.JsonArray
import com.google.gson.JsonParser
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVParser
import java.io.*
import java.net.URL
import java.nio.charset.StandardCharsets


class DownloadJsonAsyncTask (activity: Activity, fileName: String) : AsyncTask<String, Int, ByteArray>()
{
    private lateinit  var dialog : ProgressDialog
    private lateinit var file: String
    private lateinit var activity: Activity
    private var besoin = ArrayList<String>()
    init{
        this.dialog = ProgressDialog(activity)
        this.file = fileName
        this.activity = activity

    }
    override fun onPreExecute() {
        dialog.setMessage("Download in progress")
        dialog.show()
    }
    override fun doInBackground(vararg params: String):ByteArray? {
            return downloadUrl(params[0])
    }
    override fun onProgressUpdate(vararg values: Int?){}

    override fun onPostExecute(result: ByteArray) {
        if(dialog.isShowing) { dialog.dismiss() }
        try{
            println(result)

            println("------------------------------------------------------------------")
            println("------------------------------------------------------------------")
            println("------------------------------------------------------------------")
            println("------------------------------------------------------------------")
            println("------------------------------------------------------------------")
            println("------------------------------------------------------------------")
            println("------------------------------------------------------------------")
            // Conversion de l'ArrayByte en string
            val resultString = String(
                result,
                StandardCharsets.UTF_8
            )
            println(resultString)


            // Conversion du String en Json
            val jsonParser = JsonParser()
            val jsonArrayOutput: JsonArray = jsonParser.parse(
                resultString
            ) as JsonArray
            println(
                "Output : "
                        + jsonArrayOutput
            )
        }catch(e: IOException) { e.printStackTrace() }
    }

    private fun downloadUrl(myurl: String): ByteArray? {
        var open: DataInputStream? = null
        try{
            val url = URL(myurl)
            open = DataInputStream(url.openStream())
            return readIt(open)

        }catch(e: Exception) {
            e.printStackTrace()
        }
        finally{
            if(open !=null) { open.close() }
        }
        return null
    }

    private fun readIt(stream: InputStream): ByteArray? {
        try {
            val buffer = ByteArrayOutputStream()
            var i = stream.read()
            while (i != -1) {
                buffer.write(i)
                i = stream.read()
            }

            return buffer.toByteArray()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }



    private fun insertintoDataBase(buffer : BufferedReader, table : String){
        RoomService.appDatabase.getRouteDAO().deleteAllObjects()
        RoomService.appDatabase.getStopsDAO().deleteAllObjects()
        RoomService.appDatabase.getCalendarDAO().deleteAllObjects()
        RoomService.appDatabase.getTripDAO().deleteAllObjects()
        RoomService.appDatabase.getStopsTimeDAO().deleteAllObjects()
        //lecture du fichier txt
       // val csvReader = CSVReader(this.activity,File(this.activity.applicationContext.filesDir.toString() + File.separator +"DATA/","calendar.txt"))

        //val rows = csvReader.readCSV()
        val csvParser = CSVParser(buffer, CSVFormat.DEFAULT
            .withFirstRecordAsHeader()
            .withIgnoreHeaderCase()
            .withTrim())
        CSVFormat.DEFAULT
            .withDelimiter(',')
            .withQuote('"')
            .withRecordSeparator("\r\n")

        when (table){
            "routes" -> {
                Log.i("Route : ", "insertion")
                for (csvRecord in csvParser) {
                    var route = bus_route(
                        csvRecord.get("route_id"),
                        csvRecord.get("route_short_name"),
                        csvRecord.get("route_long_name"),
                        csvRecord.get("route_desc"),
                        csvRecord.get("route_type"),
                        csvRecord.get("route_color"),
                        csvRecord.get("route_text_color"),
                    )
                    //this.database.routes().insert(route)
                    RoomService.appDatabase.getRouteDAO().addObjet(route)
                    /*
                    println("route id : "+ route.route_id+ "/n route color :"+ route.color+"/n route text color :" +route.text_color
                            +"/n route desc :"+route.desc+"/n route long  :"+route.long_name+"/n route short :"+route.short_name)

                     */

                }
                println("routes saved")
            }

        }
    }
}