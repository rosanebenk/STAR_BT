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
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import java.util.*
import kotlin.io.path.createTempDirectory


class MainActivity : AppCompatActivity(), MyFragmentActivity {

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
        //setContentView(R.layout.activity_main)
        setContentView(R.layout.frame_layout)
        //création du channel de notification
        createNotificationChannel()

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
                if (savedInstanceState == null) {
                    navigateTo(FirstFragment(),false)
                }
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


    override fun navigateTo(fragment: Fragment, addToBackstack: Boolean) {
        val transaction = supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, fragment)
        if (addToBackstack) {
            transaction.addToBackStack(null)
        }
        transaction.commit()
    }

}