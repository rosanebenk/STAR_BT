package fr.istic.mob.star_bt

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class FourthFragment: Fragment() {
    lateinit var dateFormatBDD: String
    lateinit var heureFormatBDD: String
    lateinit var selectedItemLigneBus: String
    lateinit var selectedItemDirection: String
    lateinit var stopID: String
    lateinit var tripID: String

    lateinit var NomLigne: TextView
    lateinit var NomArret: TextView
    private lateinit var recyclerView: RecyclerView
    lateinit var buttonRetour: Button

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_fourth, container, false)
        lateinit var viewAdapter: RecyclerView.Adapter<*>
        lateinit var viewManager: RecyclerView.LayoutManager
        var objects: List<stops> = listOf()

        dateFormatBDD = arguments?.getString("dateFormatBDD").toString()
        heureFormatBDD = arguments?.getString("heureFormatBDD").toString()
        selectedItemLigneBus = arguments?.getString("selectedItemLigneBus").toString()
        selectedItemDirection = arguments?.getString("selectedItemDirection").toString()
        stopID = arguments?.getString("stopID").toString()
        tripID = arguments?.getString("tripID").toString()

        if (savedInstanceState != null) {
            // Récupération des données stockées
            dateFormatBDD = savedInstanceState.getString("KEY_MY_DATE").toString()
            heureFormatBDD = savedInstanceState.getString("KEY_MY_HOUR").toString()
            selectedItemLigneBus = savedInstanceState.getString("KEY_MY_LIGNE").toString()
            selectedItemDirection = savedInstanceState.getString("KEY_MY_DIRECTION").toString()
            stopID = savedInstanceState.getString("KEY_MY_STOPID").toString()
            stopID = savedInstanceState.getString("KEY_MY_TRIPID").toString()
            // Utilisation des données récupérées
        }

        NomLigne = view.findViewById(R.id.Frag4BusName)
        NomLigne.text = selectedItemLigneBus
        NomLigne.setTextColor((Color.parseColor("#"+RoomService.appDatabase.getRouteDAO().getTextColorByName(selectedItemLigneBus).first())))
        NomLigne.setBackgroundColor((Color.parseColor("#"+RoomService.appDatabase.getRouteDAO().getColorByName(selectedItemLigneBus).first())))

        buttonRetour = view.findViewById(R.id.retourFrag4)

        buttonRetour.setOnClickListener {
            navigateToFrag3()
        }

        //Récupération de la liste des arrêts
        val routeID = RoomService.appDatabase.getTripDAO().getRouteIDbyTripID(tripID)
        objects = RoomService.appDatabase.getStopsDAO().getStopByRouteAndDirection(routeID.first(), selectedItemDirection)

        println(objects)
        var keep = false
        val filteredStops = objects.dropWhile {
            if (it.stop_id != stopID) {
                true
            } else {
                keep = true
                false
            }
        }

        println(filteredStops)

        // Initialisation du RecyclerView
        viewManager = LinearLayoutManager(context)

        viewAdapter = StopAndStopTimeAdapter(
            requireActivity(),
            filteredStops,
            dateFormatBDD,
            heureFormatBDD,
            selectedItemLigneBus,
            selectedItemDirection,
            stopID,
            tripID
        )
        recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view3).apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }

        return view
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        // Sauvegarde les données importantes avant qu'elles ne soient détruites
        outState.putSerializable("KEY_MY_DATE", dateFormatBDD)
        outState.putSerializable("KEY_MY_HOUR", heureFormatBDD)
        outState.putSerializable("KEY_MY_LIGNE", selectedItemLigneBus)
        outState.putSerializable("KEY_MY_DIRECTION", selectedItemDirection)
        outState.putSerializable("KEY_MY_STOPID", stopID)
        outState.putSerializable("KEY_MY_TRIPID", tripID)
    }

    fun navigateToFrag3() {

        val bundle = Bundle()
        bundle.putString("heureFormatBDD", heureFormatBDD)
        bundle.putString("dateFormatBDD", dateFormatBDD)
        bundle.putString("selectedItemLigneBus", selectedItemLigneBus)
        bundle.putString("selectedItemDirection", selectedItemDirection)
        bundle.putString("stopID", stopID)

        val fragment3 = ThirdFragment()
        fragment3.arguments = bundle

        val transaction = (context as AppCompatActivity).supportFragmentManager.beginTransaction()
        transaction.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_left)
        transaction.replace(R.id.fragment_container, fragment3)
        transaction.addToBackStack(null)
        transaction.commit()
    }
}