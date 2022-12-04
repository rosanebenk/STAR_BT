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
        //set database
        RoomService.context = applicationContext
        setContentView(R.layout.activity_main)
        //création du channel de notification
        createNotificationChannel()
        //Initialisation du spinner de date et récupération des autres spinner
        initDatePicker()
        buttonDate = findViewById(R.id.datePickerButton)
        buttonDate.text = getTodaysDate()

        timeButton = findViewById(R.id.timeButton)
        spinner_LigneBus = findViewById(R.id.spinner_LigneBus)
        spinner_DirectionBus = findViewById(R.id.spinner_Direction)

        //pour tester si c'est la 1ère exe de l'app
        prefs = getSharedPreferences("fr.istic.mob.star_bt", MODE_PRIVATE);
       // downloadFileFromWeb(url)

        // Fonction qui attend que la base soit remplie pour remplir les spinner de sélection de ligne et de direction
        //Comme la base se remplie de manière insynchrone, on peut attendre le temps que la base se remplie
        fun waitForIt() {
            if (RoomService.appDatabase.getRouteDAO().getAllRoutesNames().isEmpty()) {
                Handler().postDelayed({
                    waitForIt()
                }, 1000)
                println("------------J'ATTEND------------")
                //var busRoute = RoomService.appDatabase.getRouteDAO().getAllObjects()
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

    /**
     * Alimente le spinner des lignes de bus avec les lignes de bus présentent dans la base
     * Et défini les réactions à adopter en fonction de l'action utilisateur
     */
    fun alimenterSpinnerListeBus(spinnerLignebus: Spinner?) {
        //val busRoute: Array<bus_route> = bus_route_DAO.getAllObjects()
        var busRoute = RoomService.appDatabase.getRouteDAO().getAllObjects()
       // println(busRoute)
        //var lignes = arrayOf("Tests","Test2")
        //On ajoute les lignes de bus dans une liste
        for (bus in busRoute) {
            var bus = busRoute.first({ it.route_id == bus.route_id })
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
                //Changement de sélection de la ligne de bus donc appel au renouvellement des données du spinner des directions
                alimenterSpinnerDirectionBus(spinner_DirectionBus)
            }
        }
    }

    /**
     * Alimente le spinner des directions avec les terminus de la ligne sélectionnée avec le spinner spinner_LigneBus
     * Et défini les réactions à adopter en fonction de l'action utilisateur
     */
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

    /**
     * Retourne la date du jour
     * Utilisé pour le spinner de date
     */
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

    /**
     * Initialise le spinner de date
     */
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

    /**
     * Reformate le format de la date sélectionnée dans le spinner
     */
    private fun makeDateString(day: Int, month: Int, year: Int): String {
        dateFormatBDD = "" + year + monthtoString(month) + daytoString(day)
        return "" + day + " " + getMonthFormat(month) + " " + year
    }

    /**
     * Retourne un format de date (mois) compréhensible par l'utilisateur
     */
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

    /**
     * Change le format du mois de Int à String
     * @param month un Int
     */
    private fun monthtoString(month: Int): String {
        monthString = if (month in 1..9) {
            "0$month"
        } else {
            "" + month
        }
        return monthString
    }

    /**
     * Change le format du mois de Int à String
     * @param day un Int
     */
    private fun daytoString(day: Int): String {
        dayString = if (day in 1..9) {
            "0$day"
        } else {
            "" + day
        }
        return dayString
    }

    /**
     * Ouvre la pop up avec le spinner de date
     */
    fun openDatePicker(view: View) {
        datePickerDialog.show()

        //dateFormatBDD = ""+year+monthtoString(month)+daytoString(day)
        Toast.makeText(this, "Date sélectionnée : " + dateFormatBDD, Toast.LENGTH_LONG).show()
    }

    /**
     * Ouvre la pop up avec le "spinner" de l'heure
     * Et reformate la sélection de l'utilisateur dans la variable @heureFormatBDD
     */
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

    /**
     * Télécharge le fichier zip disponible sur le lien
     * @param url le lien pour télécharger le fichier zip
     */
    fun downloadFileFromWeb(url: String) {
        val connMgr = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connMgr.activeNetworkInfo
        /*6*il faut tester d’abord la connexion internet7*/
        if (networkInfo != null && networkInfo.isConnected) {
            DownloadAsyncTask(this, "data.zip").execute(url)
        } else {
            Log.e("download", "Connexion réseau indisponible.")
        }
    }

    /**
     * Télécharge le fichier json contenant les liens vers les autres fichiers zip disponible
     * @param urlJson le lien pour télécharger le fichier json
     */
    fun getJsonWithLinkToZipFile(urlJson: String) {
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

    /**
     * Crée un channel de notification
     */
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