package fr.istic.mob.star_bt

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import java.util.Calendar


class MainActivity : AppCompatActivity() {
    lateinit var spinner_LigneBus: Spinner
    lateinit var datePickerDialog: DatePickerDialog
    lateinit var buttonDate: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initDatePicker()
        buttonDate = findViewById(R.id.datePickerButton)
        buttonDate.text = getTodaysDate()
        spinner_LigneBus = findViewById(R.id.spinner_LigneBus)

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
}

private operator fun AdapterView.OnItemSelectedListener?.invoke(onItemSelectedListener: AdapterView.OnItemSelectedListener) {
    //ODO("Not yet implemented")

}
