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

}