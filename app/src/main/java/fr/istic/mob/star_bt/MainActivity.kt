package fr.istic.mob.star_bt

import android.content.Context
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Adapter
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.view.get
import kotlinx.coroutines.*
import java.io.IOException

class MainActivity : AppCompatActivity() {
    lateinit var spinner_LigneBus: Spinner
    private var url = "https://eu.ftp.opendatasoft.com/star/gtfs/GTFS_2022.3.3.0_20221128_20221218.zip"
    //private var filePath = "src/resources/myfile.txt"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        GlobalScope.launch{
//            downloadFileFromWeb(url)
//        }
        downloadFileFromWeb(url)
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
    fun downloadFileFromWeb(url : String) {
        val connMgr = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connMgr.activeNetworkInfo
        /*6*il faut tester d’abord la connexion internet7*/
        if(networkInfo !=null&& networkInfo.isConnected) {
            DownloadAsyncTask(this, "test.zip").execute(url)
        }
        else{
            Log.e("download", "Connexion réseau indisponible.")}
    }

}
//download(url)
    // connectivity manger = get systemservice (context.connectivity_service) as connectivitymanager
//network capapilities
//global scope
//value = downloadUrl()Url
//download (url )
//datainputstream
//applicationContext.filesDir .use output copyto
//zip à deziper
//recupere les noms des fichiers dans le truc dezipé
//unzip



private operator fun AdapterView.OnItemSelectedListener?.invoke(onItemSelectedListener: AdapterView.OnItemSelectedListener) {
    //ODO("Not yet implemented")

}
