package fr.istic.mob.star_bt

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONObject
import java.io.*
import java.net.URL
import java.nio.charset.StandardCharsets


class DownloadJsonAsyncTask (activity: Activity, fileName: String) : AsyncTask<String, Int, ByteArray>()
{
    private lateinit var file: String
    private lateinit var activity: Activity
    private var besoin = ArrayList<String>()
    init{
        this.file = fileName
        this.activity = activity

    }
    override fun onPreExecute() {
    }
    override fun doInBackground(vararg params: String):ByteArray? {
            return downloadJson(params[0])
    }
    override fun onProgressUpdate(vararg values: Int?){}

    override fun onPostExecute(result: ByteArray){
        var url=""
        try{
            //println("json"+result)

            // Conversion de l'ArrayByte en string
            val resultString = String(
                result,
                StandardCharsets.UTF_8
            )
            //println(resultString)
            var stringjson =resultString.substring(1,resultString.length-1)
            //println(stringjson)
            var json = stringjson.toByteArray()

            val outputStream = activity.openFileOutput(file, Context.MODE_PRIVATE)
            outputStream.write(json)
            outputStream.flush()
            outputStream.close()
            getNewlinkfromJson("update.json")
        }catch(e: IOException) { e.printStackTrace() }
    }

    private fun downloadJson(myurl: String): ByteArray? {
        var open: DataInputStream? = null
        try{
            val url = URL(myurl)
            open = DataInputStream(url.openStream())
            return readfile(open)

        }catch(e: Exception) {
            e.printStackTrace()
        }
        finally{
            if(open !=null) { open.close() }
        }
        return null
    }

    private fun readfile(stream: InputStream): ByteArray? {
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

    private fun getNewlinkfromJson(fileJson:String){
        val file = File(this.activity.applicationContext.filesDir,fileJson)
        val fileReader = FileReader(file)
        val bufferedReader = BufferedReader(fileReader)
        val stringBuilder = StringBuilder()
        var line = bufferedReader.readLine()
        while (line != null) {
            stringBuilder.append(line).append("\n")
            line = bufferedReader.readLine()
        }
        val inputString = bufferedReader.use { it.readText() }
        //println("inputstring" +inputString)
        bufferedReader.close()

        // This responce will have Json Format String
        val responce = stringBuilder.toString()
        val jsonObject = JSONObject(responce)
        var url = jsonObject.getJSONObject("fields").getString("url")
        doSave(url)
        println("new URL "+url)
    }
    fun doSave(url : String) {
       if (url != getPreviousLink()){
           val sharedPreferences = this.activity.getSharedPreferences("fr.istic.mob.star_bt", AppCompatActivity.MODE_PRIVATE)
           val editor = sharedPreferences.edit()
           editor.putString("urlDownload", url)
           editor.putBoolean("linkUpdated", true)
           editor.apply()
       }

    }
    fun getPreviousLink():String{
        var oldUrl=""
        var Url = ""
        val sharedPreferences = this.activity.getSharedPreferences("fr.istic.mob.star_bt", AppCompatActivity.MODE_PRIVATE)
        if (sharedPreferences != null) {
            Url = sharedPreferences.getString("urlDownload", oldUrl).toString()
            println("previous link  "+Url)
        }
        return Url
    }


}