package fr.istic.mob.star_bt

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import fr.istic.mob.star_bt.RoomService.context
import kotlinx.coroutines.*
import java.io.*
import java.util.*
import java.util.zip.ZipFile


class MainActivity : AppCompatActivity() {
    lateinit var spinner_LigneBus: Spinner
    lateinit var datePickerDialog: DatePickerDialog
    lateinit var buttonDate: Button
    private var url = "https://eu.ftp.opendatasoft.com/star/gtfs/GTFS_2022.3.3.0_20221128_20221218.zip"
    //private var filePath = "src/resources/myfile.txt"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initDatePicker()
        buttonDate = findViewById(R.id.datePickerButton)
        buttonDate.text = getTodaysDate()
        spinner_LigneBus = findViewById(R.id.spinner_LigneBus)
        downloadFileFromWeb(url)

        //Employee[] employees = EmployeeDataUtils.getEmployees();
        val lignes = arrayOf("C1","C2","C3","C4","C5","...")
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, lignes)
        spinner_LigneBus.adapter = adapter

        spinner_LigneBus.onItemSelectedListener(object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                TODO("Not yet implemented")
                //spinner_LigneBus.
                onItemSelected(parent, view, position, id)
                /*val selectedItem = spinner_LigneBus[position]
                Toast.makeText(, "selectedItem : $selectedItem", Toast.LENGTH_LONG)
*/
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        })

    }

    private fun getTodaysDate(): String {
        val cal: Calendar = Calendar.getInstance()
        val year: Int = cal.get(Calendar.YEAR)
        var month: Int = cal.get(Calendar.MONTH)
        month += 1
        val day: Int = cal.get(Calendar.DAY_OF_MONTH)
        return makeDateString(day, month, year)
    }

    private fun initDatePicker() {
        val dateSetListener =
            OnDateSetListener { datePicker, year, month, day ->
                var month = month
                month += 1
                val date: String = makeDateString(day, month, year)
                buttonDate.text = date
            }

        val cal: Calendar = Calendar.getInstance()
        val year: Int = cal.get(Calendar.YEAR)
        val month: Int = cal.get(Calendar.MONTH)
        val day: Int = cal.get(Calendar.DAY_OF_MONTH)

        val style: Int = AlertDialog.THEME_HOLO_LIGHT

        datePickerDialog = DatePickerDialog(this, style, dateSetListener, year, month, day)
        datePickerDialog.datePicker.minDate = System.currentTimeMillis()-1000
    }

    private fun makeDateString(day: Int, month: Int, year: Int): String {
        return getMonthFormat(month) + " " + day + " " + year
    }

    private fun getMonthFormat(month: Int): String {
        if (month == 1) return "JAN"
        if (month == 2) return "FEB"
        if (month == 3) return "MAR"
        if (month == 4) return "APR"
        if (month == 5) return "MAY"
        if (month == 6) return "JUN"
        if (month == 7) return "JUL"
        if (month == 8) return "AUG"
        if (month == 9) return "SEP"
        if (month == 10) return "OCT"
        if (month == 11) return "NOV"
        return if (month == 12) "DEC" else "JAN"
    }

    fun openDatePicker(view: View) {
        datePickerDialog.show()
    }
    fun downloadFileFromWeb(url : String) {
        val connMgr = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connMgr.activeNetworkInfo
        /*6*il faut tester d’abord la connexion internet7*/
        if(networkInfo !=null&& networkInfo.isConnected) {
            DownloadAsyncTask(this, "data.zip").execute(url)
            while (File(applicationContext.filesDir,"data.zip") == null){}
                val zipFile : File = File(applicationContext.filesDir,"data.zip")
                val destDir =  applicationContext.filesDir.toString() +  File.separator + "DATA"
                UnzipUtils.unzip(zipFile,destDir)



        }
        else{
            Log.e("download", "Connexion réseau indisponible.")}
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

private operator fun AdapterView.OnItemSelectedListener?.invoke(onItemSelectedListener: AdapterView.OnItemSelectedListener) {
    //ODO("Not yet implemented")

}
