package fr.istic.mob.star_bt

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.os.AsyncTask
import android.util.Log
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVParser
import java.io.*
import java.net.URL
import java.util.zip.ZipFile


class DownloadAsyncTask (activity: Activity, fileName: String) : AsyncTask<String, Int, ByteArray>()
{
    private lateinit  var dialog : ProgressDialog
    private lateinit var file: String
    private lateinit var activity: Activity
    private var besoin = ArrayList<String>()
    init{
        this.dialog = ProgressDialog(activity)
        this.file = fileName
        this.activity = activity
        this.besoin.add("calendar.txt")
        this.besoin.add("routes.txt")
        this.besoin.add("stop_times.txt")
        this.besoin.add("stops.txt")
        this.besoin.add("trips.txt")
    }
    override fun onPreExecute() {
        dialog.setMessage("Download in progress, \nDue to big files, it may takes 15 mins")
        dialog.show()
    }
    override fun doInBackground(vararg params: String):ByteArray? {
            return downloadUrl(params[0])
    }
    override fun onProgressUpdate(vararg values: Int?){}

    override fun onPostExecute(result: ByteArray) {

        try{
            val outputStream = activity.openFileOutput(file, Context.MODE_PRIVATE)
            outputStream.write(result)
            outputStream.flush()
            outputStream.close()
            //unzip
            val zipFile : File = File(this.activity.applicationContext.filesDir,"data.zip")
            val destDir =  this.activity.applicationContext.filesDir.toString() +  File.separator + "DATA"
            UnzipUtils.unzip(zipFile,destDir)
            // recup des fichiers dont on a besoin
            var fichier = ArrayList<File>()
            File(destDir).walk().forEach {
                if( besoin.contains(it.name.toString()))
                    fichier.add(it)
            }
            //var csv = CSVParser()
            RoomService.appDatabase.getRouteDAO().deleteAllObjects()
            RoomService.appDatabase.getStopsDAO().deleteAllObjects()
            RoomService.appDatabase.getCalendarDAO().deleteAllObjects()
            RoomService.appDatabase.getTripDAO().deleteAllObjects()
            RoomService.appDatabase.getStopsTimeDAO().deleteAllObjects()
            for(file : File in fichier){
                val bufferedReader =  file.bufferedReader();
                insertintoDataBase(bufferedReader, file.nameWithoutExtension)
                println("j'ai inseré "+ file.nameWithoutExtension)
            }
            println("--------JE VAIS RESTITUER---------")
            getRoutesFromDB()
            //getRoutesNameFromDB()
            getCalendarFromDB()
            getStopsFromDB()
            getTripFromDB()
            //getStopTimesFromDB()

            //lecture du fichier txt
            //val csvReader = CSVReader(this.activity,File(this.activity.applicationContext.filesDir.toString() + File.separator +"DATA/","calendar.txt"))

             //   val rows = csvReader.readCSV()
//                for (i in 0 until rows!!.size) {
//                    Log.e(
//                        "",
//                        java.lang.String.format(
//                            "row %s: %s, %s",
//                            i,
//                            rows!!.get(i).get(0),
//                            rows!!.get(i).get(1)
//                        )
//                    )
//                }

        }catch(e: IOException) { e.printStackTrace() }
        if(dialog.isShowing) { dialog.dismiss() }
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
    //ecriture dans le zip
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
    /**
     * UnzipUtils class extracts files and sub-directories of a standard zip file to
     * a destination directory.
     *
     */
    object UnzipUtils {
        /**
         * @param zipFilePath
         * @param destDirectory
         * @throws IOException
         */
        @Throws(IOException::class)
        fun unzip(zipFilePath: File, destDirectory: String) {

            File(destDirectory).run {
                if (!exists()) {
                    mkdirs()
                }
            }

            ZipFile(zipFilePath).use { zip ->

                zip.entries().asSequence().forEach { entry ->

                    zip.getInputStream(entry).use { input ->


                        val filePath = destDirectory + File.separator + entry.name

                        if (!entry.isDirectory) {
                            // if the entry is a file, extracts it
                            extractFile(input, filePath)
                        } else {
                            // if the entry is a directory, make the directory
                            val dir = File(filePath)
                            dir.mkdir()
                        }

                    }

                }
            }
        }

        /**
         * Extracts a zip entry (file entry)
         * @param inputStream
         * @param destFilePath
         * @throws IOException
         */
        @Throws(IOException::class)
        private fun extractFile(inputStream: InputStream, destFilePath: String) {
            val bos = BufferedOutputStream(FileOutputStream(destFilePath))
            val bytesIn = ByteArray(BUFFER_SIZE)
            var read: Int
            while (inputStream.read(bytesIn).also { read = it } != -1) {
                bos.write(bytesIn, 0, read)
            }
            bos.close()
        }

        /**
         * Size of the buffer to read/write data
         */
        private const val BUFFER_SIZE = 4096

    }

    private fun insertintoDataBase(buffer : BufferedReader, table : String){

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
            "calendar" -> {
                Log.i("Calendar : ", "insertion")
                for (csvRecord in csvParser) {
                    var calendar = calendar(
                        csvRecord.get("service_id"),
                        csvRecord.get("monday"),
                        csvRecord.get("tuesday"),
                        csvRecord.get("wednesday"),
                        csvRecord.get("thursday"),
                        csvRecord.get("friday"),
                        csvRecord.get("saturday"),
                        csvRecord.get("sunday"),
                        csvRecord.get("start_date"),
                        csvRecord.get("end_date"),
                    )
                    //this.database.routes().insert(calendar)
                    RoomService.appDatabase.getCalendarDAO().addObjet(calendar)
                    /*
                    println("calendar id : "+ calendar.service_id + "\t calendar start day :"+ calendar.start_date+"/n calendar end date :" +calendar.end_date
                            +"\n calendar monday :"+calendar.monday+"\t calendar tuesday  :"+calendar.tuesday+"/n calendar wednesday :"+calendar.wednesday
                            +"\n calendar thursday :"+calendar.thursday+"\n calendar friday :"+calendar.friday+"\n calendar sat :"+calendar.saturday)

                     */


                }
                println("calendar saved")
            }
            "stops" -> {
                Log.i("Stops : ", "insertion")
                for (csvRecord in csvParser) {
                    var stop = stops(
                        csvRecord.get("stop_id"),
                        csvRecord.get("stop_code"),
                        csvRecord.get("stop_name"),
                        csvRecord.get("stop_desc"),
                        csvRecord.get("stop_lat"),
                        csvRecord.get("stop_lon"),
                        csvRecord.get("wheelchair_boarding"),
                    )
                    //this.database.stops().insert(stop)
                    RoomService.appDatabase.getStopsDAO().addObjet(stop)
                    /*
                    println("stop id : "+ stop.stop_id+ "\t stop code :"+ stop.stop_code+"\t stop name :" +stop.stop_name
                            +"\t stop desc :"+stop.stop_desc+"\t stop lati  :"+stop.stop_lat+"\t stop long :"+stop.stop_lon
                            +"\n stop wheelchair"+ stop.wheelchair)

                     */

                }
                println("stops saved")
            }
/*
            "stop_times" -> {
                Log.i("StopTimes : ", "insertion")
                for (csvRecord in csvParser) {
                    var stopsTime = stopTime(
                        csvRecord.get("trip_id"),
                        csvRecord.get("arrival_time"),
                        csvRecord.get("departure_time"),
                        csvRecord.get("stop_id"),
                        csvRecord.get("stop_sequence"),
                    )
                    //this.database.stopsTimes().insert(stopsTime)
                    RoomService.appDatabase.getStopsTimeDAO().addObjet(stopsTime)
                    /*
                    println("trip  id : "+ stopsTime.trip_id + "\t stop arrival  :"+stopsTime.arrival_time+"\t stop departure :" +stopsTime.departure_time
                            +"\t stop id :"+stopsTime.stop_id+"\t stop seq  :"+stopsTime.stopSeq )

                     */
                }
                println("stops times saved")


            }
 */

            "trips" -> {
                Log.i("trip : ", "insertion")
                for (csvRecord in csvParser) {
                    var trip = trip(
                        csvRecord.get("route_id"),
                        csvRecord.get("service_id"),
                        csvRecord.get("trip_id"),
                        csvRecord.get("trip_headsign"),
                        csvRecord.get("direction_id"),
                        csvRecord.get("block_id"),
                        csvRecord.get("wheelchair_accessible"),
                    )
                    //this.database.trips().insert(trip)
                    RoomService.appDatabase.getTripDAO().addObjet(trip)
                    /*
                    println("trip route  : "+ trip.route_id+ "\t trip srvice id  :"+ trip.service_id+"\t trip id :" +trip.trip_id
                            +"\t trip headsign :"+trip.headsign+"\t trip direction  :"+trip.direction_id+"\t trip blockid :"+trip.blockid
                            +"\n trip wheelchair"+ trip.wheelchairaccessible)

                     */
                    //println("trip saved")
                }
                println("trips  saved")
            }
        }
    }
    private fun getRoutesFromDB() : List<bus_route>{
        println("--------JE SUIS getRoutesFromDB---------")
        Log.i("Routes : ", "restitution")
        var objets = RoomService.appDatabase.getRouteDAO().getAllObjects()
//        for (route in objets){
//            var route = objets.first({ it.route_id == route.route_id })
//            println("route id : "+ route.route_id+ "\t route color :"+ route.color+"\t route text color :" +route.text_color
//                    +"\t route desc :"+route.desc+"\t route long  :"+route.long_name+"\t route short :"+route.short_name)
//        }
        println("--------JE SUIS getRoutesFromDB FINI---------")
        return objets
    }

    private fun getRoutesNameFromDB() : List<String>{
        println("--------JE SUIS getRoutesNameFromDB----NAME-----")
        Log.i("Routes : ", "restitution")
        var objets = RoomService.appDatabase.getRouteDAO().getAllRoutesNames()
//        for (route in objets){
//            println(route)
//        }
        println("--------JE SUIS getRoutesNameFromDB FINI----NAME-----")
        return objets
    }

    private fun getCalendarFromDB() : List<calendar>{
        Log.i("Calendar : ", "restitution")
        var objets = RoomService.appDatabase.getCalendarDAO().getAllObjects()
//        for (calendars in objets){
//            var calendar = objets.first({ it.service_id == calendars.service_id })
//            println("calendar id : "+ calendar.service_id+ "\t calendar monday :"+ calendar.monday+"\t calendar tuesday:" +calendar.tuesday
//                    +"\t calendar start day :"+calendar.start_date+"\t calendar end date   :"+calendar.end_date)
//        }
        return objets
    }
    private fun getStopsFromDB() : List<stops>{
        Log.i("Stops : ", "restitution")
        var objets = RoomService.appDatabase.getStopsDAO().getAllObjects()
//        for (stops in objets){
//            var stop = objets.first({ it.stop_id == stops.stop_id })
//            println("stop id : "+ stop.stop_id+ "\t stop code :"+ stop.stop_code+"\t stop name :" +stop.stop_name
//                    +"\t stop desc :"+stop.stop_desc+"\t stop lati  :"+stop.stop_lat+"\t stop long :"+stop.stop_lon
//                    +"\n stop wheelchair"+ stop.wheelchair)
//        }
        return objets
    }
    /*
    private fun getStopTimesFromDB() : List<stopTime>{
        Log.i("StopTimes : ", "restitution")
        var objets = RoomService.appDatabase.getStopsTimeDAO().getAllObjects()
//        for (stopTime in objets){
//            var stopsTime = objets.first({ it.stop_time_id == stopTime.stop_time_id })
//            println("trip  id : "+ stopsTime.trip_id + "\t stop arrival  :"+stopsTime.arrival_time+"\t stop departure :" +stopsTime.departure_time
//                    +"\t stop id :"+stopsTime.stop_id+"\t stop seq  :"+stopsTime.stopSeq )
//
//        }
        return objets
    }
     */
    private fun getTripFromDB() : List<trip>{
        Log.i("Trip : ", "restitution")
        var objets = RoomService.appDatabase.getTripDAO().getAllObjects()
//        for (trip in objets){
//            var trip = objets.first({ it.trip_id == trip.trip_id})
//            println("trip route  : "+ trip.route_id+ "\t trip srvice id  :"+ trip.service_id+"\t trip id :" +trip.trip_id
//                    +"\t trip headsign :"+trip.headsign+"\t trip direction  :"+trip.direction_id+"\t trip blockid :"+trip.blockid
//                    +"\n trip wheelchair"+ trip.wheelchairaccessible)
//            println(trip)
//        }
        return objets
    }


}