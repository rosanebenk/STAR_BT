package fr.istic.mob.star_bt

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.os.AsyncTask
import android.util.Log
import java.io.*
import java.net.URL
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream

class DownloadAsyncTask (activity: Activity, fileName: String) : AsyncTask<String, Int, ByteArray>()
{
    private lateinit  var dialog : ProgressDialog
    private lateinit var file: String
    private lateinit var activity: Activity
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
            val outputStream = activity.openFileOutput(file, Context.MODE_PRIVATE)
            outputStream.write(result)
            outputStream.flush()
            outputStream.close()
        }catch(e: IOException) {  }
    }
    private fun downloadUrl(myurl: String): ByteArray? {
        var open: DataInputStream? = null
        try{
            val url = URL(myurl)
            open = DataInputStream(url.openStream())
            return readIt(open)
//            val zipFile : File = File(applicationContext.filesDir,"test.zip")
//            unpackZip(zipFile.path)
//
//
//            val textFile : File = File(applicationContext.filesDir,"agency.txt")
//
//
//            var open = DataInputStream(url.openStream())
//            val inputAsString = open.readTextAndClose();
//            return textFile.bufferedReader().use { it.readText() }
        }catch(e: Exception) { }
        finally{
            if(open !=null) { open.close() }
        }
        return null
    }
    private fun readIt(stream: InputStream): ByteArray? {
        try {
            val bo = ByteArrayOutputStream()
            var i = stream.read()
            while (i != -1) {
                bo.write(i)
                i = stream.read()
            }

            //Log.e("", dezipper(bo))
            return bo.toByteArray()
        } catch (e: IOException) {
            ""
        }
        return null
    }
//    fun unpackZip(filePath : String){
//        var inputStream : InputStream
//        var zipInputStream : ZipInputStream
//
//        try{
//            var zipFile = File(filePath)
//            var parentFolder = zipFile.parentFile.path
//            var filename = ""
//            inputStream = FileInputStream(filePath)
//            zipInputStream = ZipInputStream(BufferedInputStream(inputStream))
//
//
//            val buffer = ByteArray(1024)
//            var count: Int
//
//            var  zipEntry : ZipEntry? = zipInputStream.nextEntry
//            while (zipEntry != null) {
//                filename = zipEntry.getName()
//                if (zipEntry.isDirectory()) {
//                    val fmd = File("$parentFolder/$filename")
//                    fmd.mkdirs()
//                    continue
//                }
//                val fout = FileOutputStream("$parentFolder/$filename")
//                Log.i("TAG","UNZIPPING $filename")
//                while (zipInputStream.read(buffer).also { count = it } !== -1) {
//                    fout.write(buffer, 0, count)
//                }
//
//                fout.close()
//
//                zipEntry = zipInputStream.nextEntry
//                zipInputStream.closeEntry()
//
//            }
//
//
//            zipInputStream.close()
//
//        }
//        catch (e: IOException){
//
//            e.printStackTrace();
//        }
//    }
}