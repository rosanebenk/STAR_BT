package fr.istic.mob.star_bt

import android.app.*
import android.app.DatePickerDialog.OnDateSetListener
import android.app.TimePickerDialog.OnTimeSetListener
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import java.util.*
import kotlin.io.path.createTempDirectory


class MainActivity : AppCompatActivity() {

    var prefs: SharedPreferences? = null

    lateinit var spinner_LigneBus: Spinner
    lateinit var spinner_DirectionBus: Spinner

    //lateinit var listeLignesBus:
    lateinit var datePickerDialog: DatePickerDialog
    lateinit var buttonDate: Button
    var timeButton: Button? = null
    var dateFormatBDD: String = ""
    var heureFormatBDD: String = ""
    var monthString: String = ""
    var dayString: String = ""
    var hour: Int = 0
    var minute: Int = 0
    var isPaused: Boolean = false
    var lignes = listOf<String>()
    var listeDirections = listOf<String>()

    public var selectedItemLigneBus:String = "C1"
    public var selectedItemDirection: String = ""
    public var idLigneBus:String = ""

    private var url =
        "https://eu.ftp.opendatasoft.com/star/gtfs/GTFS_2022.3.3.0_20221128_20221218.zip"

    private val urlJson = "https://data.explore.star.fr/explore/dataset/tco-busmetro-horaires-gtfs-versions-td/download/?format=json&timezone=Europe/Berlin&lang=fr"
    //private var filePath = "src/resources/myfile.txt"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        RoomService.context = applicationContext
        setContentView(R.layout.activity_main)
        createNotificationChannel()
        initDatePicker()
        buttonDate = findViewById(R.id.datePickerButton)
        buttonDate.text = getTodaysDate()
        timeButton = findViewById(R.id.timeButton)
        spinner_LigneBus = findViewById(R.id.spinner_LigneBus)
        spinner_DirectionBus = findViewById(R.id.spinner_Direction)

        //pour tester si c'est la 1ère exe de l'app
        prefs = getSharedPreferences("fr.istic.mob.star_bt", MODE_PRIVATE);
        isPaused = true
       // downloadFileFromWeb(url)

        fun waitForIt() {
            if (RoomService.appDatabase.getRouteDAO().getAllRoutesNames().isEmpty()) {
                Handler().postDelayed({
                    waitForIt()
                }, 1000)
                println("------------J'ATTEND------------")
                var busRoute = RoomService.appDatabase.getRouteDAO().getAllObjects()
                //println(busRoute)
                //setTimeout(fun() { waitForIt() }, 100);
            } else {
                alimenterSpinnerListeBus(spinner_LigneBus)
                alimenterSpinnerDirectionBus(spinner_DirectionBus)

            }
        }
        waitForIt()

    }
    override fun onResume() {
        super.onResume()
        Log.i("Preferences","je teste si c'est la 1 ere exe ?")
        if (prefs!!.getBoolean("firstrun", true)) {
            println(prefs)
            // Do first run stuff here then set 'firstrun' as false
            // using the following line to edit/commit prefs
            downloadFileFromWeb(url)
            prefs!!.edit().putBoolean("firstrun", false).commit()
            println("c'etait la 1ere exe :")
        }
        else {
            Log.i("Preferences : ","c'est pas la premier exe, c'est trop là!!! ")
        }
    }

    fun alimenterSpinnerListeBus(spinnerLignebus: Spinner?) {
        //val busRoute: Array<bus_route> = bus_route_DAO.getAllObjects()
        var busRoute = RoomService.appDatabase.getRouteDAO().getAllObjects()
       // println(busRoute)
        //var lignes = arrayOf("Tests","Test2")
        //On ajoute les lignes de bus dans une liste
        for (bus in busRoute) {
            var bus = busRoute.first({ it.route_id == bus.route_id })
            //println("----------------------------------------------")
            //println(bus.short_name)
            lignes += bus.short_name
        }
        val adapter = SpinAdapter(this, android.R.layout.simple_list_item_1, lignes)
        spinner_LigneBus.adapter = adapter
        //var busSelected: String = spinner_LigneBus.selectedItem.toString()


        spinner_LigneBus.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
                Toast.makeText(this@MainActivity, "Ligne sélectionnée : $selectedItemLigneBus", Toast.LENGTH_LONG).show()

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                //onItemSelected(parent, view, position, id)
                selectedItemLigneBus = parent?.getItemAtPosition(position).toString()
                println(selectedItemLigneBus)
                Toast.makeText(this@MainActivity, "Ligne sélectionnée : $selectedItemLigneBus", Toast.LENGTH_LONG).show()
                alimenterSpinnerDirectionBus(spinner_DirectionBus)
            }
        }
    }

    fun alimenterSpinnerDirectionBus(spinnerDirectionBus: Spinner?) {
        idLigneBus = RoomService.appDatabase.getRouteDAO().getRouteIdByName(selectedItemLigneBus)[0].toString()
        println(idLigneBus)
        //var directions = RoomService.appDatabase.getTripDAO().getDirection(idLigneBus)
        //println(directions)
        var directions = RoomService.appDatabase.getTripDAO().getDirections(idLigneBus)
        listeDirections = listOf<String>()
        for(direction in directions){
            //if(direction.route_id == idLigneBus){
            listeDirections += direction
            println(direction)
            //}
        }
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, listeDirections)
        spinner_DirectionBus.adapter = adapter

        spinner_DirectionBus.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
                Toast.makeText(this@MainActivity, "Direction sélectionnée : $selectedItemDirection", Toast.LENGTH_LONG).show()
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                //onItemSelected(parent, view, position, id)
                selectedItemDirection = parent?.getItemAtPosition(position).toString()
                println(selectedItemDirection)
                Toast.makeText(this@MainActivity, "Direction sélectionnée : $selectedItemDirection", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun getTodaysDate(): String {
        val cal: Calendar = Calendar.getInstance()
        val year: Int = cal.get(Calendar.YEAR)
        var month: Int = cal.get(Calendar.MONTH)
        month += 1
        val day: Int = cal.get(Calendar.DAY_OF_MONTH)
        dateFormatBDD = "" + year + monthtoString(month) + daytoString(day)
        Toast.makeText(this, "Date sélectionnée : " + dateFormatBDD, Toast.LENGTH_LONG).show()

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
        datePickerDialog.datePicker.minDate = System.currentTimeMillis() - 1000
    }

    private fun makeDateString(day: Int, month: Int, year: Int): String {
        dateFormatBDD = "" + year + monthtoString(month) + daytoString(day)
        return "" + day + " " + getMonthFormat(month) + " " + year
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

    private fun monthtoString(month: Int): String {
        monthString = if (month in 1..9) {
            "0$month"
        } else {
            "" + month
        }
        return monthString
    }

    private fun daytoString(day: Int): String {
        dayString = if (day in 1..9) {
            "0$day"
        } else {
            "" + day
        }
        return dayString
    }

    fun openDatePicker(view: View) {
        datePickerDialog.show()

        //dateFormatBDD = ""+year+monthtoString(month)+daytoString(day)
        Toast.makeText(this, "Date sélectionnée : " + dateFormatBDD, Toast.LENGTH_LONG).show()
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
                heureFormatBDD = timeButton?.text.toString() + ":00"
                Toast.makeText(this, "Heure sélectionnée : " + heureFormatBDD, Toast.LENGTH_SHORT)
                    .show()
            }

        // int style = AlertDialog.THEME_HOLO_DARK;
        val timePickerDialog =
            TimePickerDialog(this,  /*style,*/onTimeSetListener, hour, minute, true)
        timePickerDialog.setTitle("Select Time")
        timePickerDialog.show()

    }

    fun downloadFileFromWeb(url: String) {
        //isPaused = true
        val connMgr = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connMgr.activeNetworkInfo
        /*6*il faut tester d’abord la connexion internet7*/
        if (networkInfo != null && networkInfo.isConnected) {
            DownloadAsyncTask(this, "data.zip").execute(url)
        } else {
            Log.e("download", "Connexion réseau indisponible.")
        }
    }

    fun getJsonWithLinkToZipFile(urlJson: String) {
        //isPaused = true
        val connMgr = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connMgr.activeNetworkInfo
        /*6*il faut tester d’abord la connexion internet7*/
        if (networkInfo != null && networkInfo.isConnected) {
            DownloadJsonAsyncTask(this, "file.json").execute(url)
        } else {
            Log.e("download", "Connexion réseau indisponible.")
        }
    }

    //Envoie une notification
    //A faire : vérifier si un nouvau fichier est dispo. Si c'est le cas, lancer son téléchargement
    fun refreshData(view: View?) {
        getJsonWithLinkToZipFile(urlJson)

        val intent = Intent(applicationContext, Notification::class.java)
        val title = "Informations : Actualisation de l'app"
        val message = "Un nouveau fichier est-il disponible ? Relancer l'app pour le savoir"
        intent.putExtra(titleExtra, title)
        intent.putExtra(messageExtra, message)

        val pendingIntent = PendingIntent.getBroadcast(
            applicationContext,
            notificationID,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val time = 12
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            time.toLong(),
            pendingIntent
        )
        showAlert(time.toLong(), title, message)
    }

    //Affiche une pop-up avec le contenu de la notification
    private fun showAlert(time: Long, title: String, message: String) {
        val date = Date(time)
        val dateFormat = android.text.format.DateFormat.getLongDateFormat(applicationContext)
        val timeFormat = android.text.format.DateFormat.getTimeFormat(applicationContext)

        /*AlertDialog.Builder(this)
            .setTitle("Notification Scheduled")
            .setMessage(
                "Title: " + title +
                        "\nMessage: " + message +
                        "\nAt: " + dateFormat.format(date) + " " + timeFormat.format(date)
            )
            .setPositiveButton("Okay") { _, _ -> }
            .show()

         */
    }

    private fun createNotificationChannel() {
        val name = "STAR_BT"
        val desc = "Notification de la STAR"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(channelID, name, importance)
        channel.description = desc
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

}