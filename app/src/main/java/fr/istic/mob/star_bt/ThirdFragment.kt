package fr.istic.mob.star_bt

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ThirdFragment: Fragment() {
    lateinit var dateFormatBDD: String
    lateinit var heureFormatBDD: String
    lateinit var selectedItemLigneBus: String
    lateinit var selectedItemDirection: String
    lateinit var stopID: String

    lateinit var NomLigne: TextView
    lateinit var NomArret: TextView
    private lateinit var recyclerView: RecyclerView
    lateinit var buttonRetour: Button

    lateinit var stop_times:List<stopTime>

    var lundi = 0
    var mardi = 0
    var mercredi = 0
    var jeudi = 0
    var vendredi = 0
    var samedi = 0
    var dimanche = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_third, container, false)

        lateinit var viewAdapter: RecyclerView.Adapter<*>
        lateinit var viewManager: RecyclerView.LayoutManager

        dateFormatBDD = arguments?.getString("dateFormatBDD").toString()
        heureFormatBDD = arguments?.getString("heureFormatBDD").toString()
        selectedItemLigneBus = arguments?.getString("selectedItemLigneBus").toString()
        selectedItemDirection = arguments?.getString("selectedItemDirection").toString()
        stopID = arguments?.getString("stopID").toString()

        NomLigne = view.findViewById(R.id.Frag3BusName)
        NomLigne.text = selectedItemLigneBus
        NomLigne.setTextColor((Color.parseColor("#"+RoomService.appDatabase.getRouteDAO().getTextColorByName(selectedItemLigneBus).first())))
        NomLigne.setBackgroundColor((Color.parseColor("#"+RoomService.appDatabase.getRouteDAO().getColorByName(selectedItemLigneBus).first())))
        NomArret = view.findViewById(R.id.Frag3NomArret)
        NomArret.text = RoomService.appDatabase.getStopsDAO().getStopById(stopID)[0].stop_name

        buttonRetour = view.findViewById(R.id.retourFrag3)

        buttonRetour.setOnClickListener{
            navigateToFrag2()
        }

        var routeID= RoomService.appDatabase.getRouteDAO().getRouteIdByName(selectedItemLigneBus)
        var tripID = RoomService.appDatabase.getTripDAO().getTripsByRouteAndDirection(routeID.first(), selectedItemDirection)

        //Récupération du jour pour lier à la table calendrier :
        val inputFormat = SimpleDateFormat("yyyyMMdd")
        val date = inputFormat.parse(dateFormatBDD)
        val calendar = Calendar.getInstance()
        calendar.time = date
        //Numéro du jour de la semaine
        val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
        when(dayOfWeek){
            Calendar.MONDAY -> lundi = 1
            Calendar.TUESDAY -> mardi = 1
            Calendar.WEDNESDAY -> mercredi = 1
            Calendar.THURSDAY -> jeudi = 1
            Calendar.FRIDAY -> vendredi = 1
            Calendar.SATURDAY -> samedi = 1
            Calendar.SUNDAY -> dimanche = 1
            else -> Toast.makeText(requireActivity(), "Invalid day", Toast.LENGTH_LONG).show()
        }

        var stop_times = RoomService.appDatabase.getStopsTimeDAO().getStopTimesWithGivenIdFromTime(stopID, heureFormatBDD, dateFormatBDD,lundi,mardi,mercredi,jeudi,vendredi,samedi,dimanche)
        println(stop_times)

        // Initialisation du RecyclerView
        viewManager = LinearLayoutManager(context)
        viewAdapter = StopTimeAdapter(requireActivity(), stop_times, dateFormatBDD, heureFormatBDD, selectedItemLigneBus, selectedItemDirection, stopID)
        recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view2).apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }


        return view
    }

    fun navigateToFrag2() {

        val bundle = Bundle()
        bundle.putString("heureFormatBDD", heureFormatBDD)
        bundle.putString("dateFormatBDD", dateFormatBDD)
        bundle.putString("selectedItemLigneBus", selectedItemLigneBus)
        bundle.putString("selectedItemDirection", selectedItemDirection)
        bundle.putString("stopID", stopID)

        val fragment2 = SecondFragment()
        fragment2.arguments = bundle

        val transaction = (context as AppCompatActivity).supportFragmentManager.beginTransaction()
        transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.exit_to_left, R.anim.exit_to_right)
        transaction.replace(R.id.fragment_container, fragment2)
        transaction.addToBackStack(null)
        transaction.commit()
    }
}