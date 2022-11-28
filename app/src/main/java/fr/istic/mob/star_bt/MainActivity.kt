package fr.istic.mob.star_bt

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Adapter
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.core.view.get

class MainActivity : AppCompatActivity() {
    lateinit var spinner_LigneBus: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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
}

private operator fun AdapterView.OnItemSelectedListener?.invoke(onItemSelectedListener: AdapterView.OnItemSelectedListener) {
    //ODO("Not yet implemented")

}
