package fr.istic.mob.star_bt

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.TimePickerDialog
import android.app.TimePickerDialog.OnTimeSetListener
import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.*
import java.io.*
import java.util.*


class MainActivity : AppCompatActivity() {
    lateinit var spinner_LigneBus: Spinner
    lateinit var datePickerDialog: DatePickerDialog
    lateinit var buttonDate: Button
    var timeButton: Button? = null
    var dateFormatBDD: String = ""
    var heureFormatBDD:String = ""
    var monthString: String = ""
    var dayString: String = ""
    var hour: Int = 0
    var minute:Int = 0
    private var url = "https://eu.ftp.opendatasoft.com/star/gtfs/GTFS_2022.3.3.0_20221128_20221218.zip"
    //private var filePath = "src/resources/myfile.txt"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        RoomService.context = applicationContext
        setContentView(R.layout.activity_main)
        initDatePicker()
        buttonDate = findViewById(R.id.datePickerButton)
        buttonDate.text = getTodaysDate()
        timeButton = findViewById(R.id.timeButton);
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
        dateFormatBDD = ""+year+monthtoString(month)+daytoString(day)
        Toast.makeText(this, "Date sélectionnée : " +dateFormatBDD, Toast.LENGTH_LONG).show()

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
        dateFormatBDD = ""+year+monthtoString(month)+daytoString(day)
        return ""+ day + " " + getMonthFormat(month) + " " + year
    }

    private fun getMonthFormat(month: Int): String {
        if (month == 1) return resources.getString(R.string.JAN)
        if (month == 2) return resources.getString(R.string.FEB)
        if (month == 3) return resources.getString(R.string.MAR)
        if (month == 4) return resources.getString(R.string.APR)
        if (month == 5) return resources.getString(R.string.MAY)
        if (month == 6) return resources.getString(R.string.JUN)
        if (month == 7) return resources.getString(R.string.JUL)
        if (month == 8) return resources.getString(R.string.AUG)
        if (month == 9) return resources.getString(R.string.SEP)
        if (month == 10) return resources.getString(R.string.OCT)
        if (month == 11) return resources.getString(R.string.NOV)
        return if (month == 12) resources.getString(R.string.DEC) else resources.getString(R.string.JAN)
    }

    private fun monthtoString(month: Int):String{
        monthString = if(month in 1..9){
            "0$month"
        }else{
            ""+month
        }
        return monthString
    }

    private fun daytoString(day: Int):String{
        dayString = if(day in 1..9){
            "0$day"
        }else{
            ""+day
        }
        return dayString
    }

    fun openDatePicker(view: View) {
        datePickerDialog.show()

        //dateFormatBDD = ""+year+monthtoString(month)+daytoString(day)
        Toast.makeText(this, "Date sélectionnée : " +dateFormatBDD, Toast.LENGTH_LONG).show()
    }

    fun popTimePicker(view: View?) {
        val onTimeSetListener =
            OnTimeSetListener { timePicker, selectedHour, selectedMinute ->
                hour = selectedHour
                minute = selectedMinute
                timeButton?.text = java.lang.String.format(
                    Locale.getDefault(),
                    "%02d:%02d",
                    hour,
                    minute
                )
                heureFormatBDD = timeButton?.text.toString()+":00"
                Toast.makeText(this, "Heure sélectionnée : "+heureFormatBDD, Toast.LENGTH_SHORT).show()
            }

        // int style = AlertDialog.THEME_HOLO_DARK;
        val timePickerDialog =
            TimePickerDialog(this,  /*style,*/onTimeSetListener, hour, minute, true)
        timePickerDialog.setTitle("Select Time")
        timePickerDialog.show()

    }

    fun downloadFileFromWeb(url : String) {
        val connMgr = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connMgr.activeNetworkInfo
        /*6*il faut tester d’abord la connexion internet7*/
        if(networkInfo !=null&& networkInfo.isConnected) {
            DownloadAsyncTask(this, "data.zip").execute(url)
        }
        else{
            Log.e("download", "Connexion réseau indisponible.")}
    }


}

private operator fun AdapterView.OnItemSelectedListener?.invoke(onItemSelectedListener: AdapterView.OnItemSelectedListener) {
    //ODO("Not yet implemented")

}
