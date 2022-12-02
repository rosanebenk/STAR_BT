package fr.istic.mob.star_bt

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.os.AsyncTask
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
            for(file : File in fichier){
                val bufferedReader =  file.bufferedReader();
                insertintoDataBase(bufferedReader, file.nameWithoutExtension)
                println("j'ai inseré")
                restitutefromDataBase()


            }

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
        RoomService.appDatabase.getRouteDAO().deleteAllObjects()
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
                    println("route id : "+ route.route_id+ "/n route color :"+ route.Color+"/n route text color :" +route.TextColor
                            +"/n route desc :"+route.Description+"/n route long  :"+route.LongName+"/n route short :"+route.ShortName)


                }
                println("C BON !")
            }
        }
    }
    private fun restitutefromDataBase(){
        println("je restitue")
        var objets = RoomService.appDatabase.getRouteDAO().getAllObjects()
        for (route in objets){
            var route = objets.first({ it.route_id == route.route_id })
            println("route id : "+ route.route_id+ "/n route color :"+ route.Color+"/n route text color :" +route.TextColor
                    +"/n route desc :"+route.Description+"/n route long  :"+route.LongName+"/n route short :"+route.ShortName)
        }
    }

}