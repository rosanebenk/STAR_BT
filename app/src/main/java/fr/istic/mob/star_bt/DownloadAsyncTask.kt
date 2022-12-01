package fr.istic.mob.star_bt

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.os.AsyncTask
import android.util.Log
import java.io.*
import java.net.URL
import java.util.zip.ZipEntry
import java.util.zip.ZipFile
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
            val zipFile : File = File(this.activity.applicationContext.filesDir,"data.zip")
            val destDir =  this.activity.applicationContext.filesDir.toString() +  File.separator + "DATA"
            UnzipUtils.unzip(zipFile,destDir)
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

}