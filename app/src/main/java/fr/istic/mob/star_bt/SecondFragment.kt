package fr.istic.mob.star_bt

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class SecondFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView

    lateinit var dateFormatBDD: String
    lateinit var heureFormatBDD: String
    lateinit var selectedItemLigneBus: String
    lateinit var selectedItemDirection: String

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_second, container, false)
        lateinit var viewAdapter: RecyclerView.Adapter<*>
        lateinit var viewManager: RecyclerView.LayoutManager
        var objects: List<stops> = listOf()

        dateFormatBDD = arguments?.getString("dateFormatBDD").toString()
        heureFormatBDD = arguments?.getString("heureFormatBDD").toString()
        selectedItemLigneBus = arguments?.getString("selectedItemLigneBus").toString()
        selectedItemDirection = arguments?.getString("selectedItemDirection").toString()

        Toast.makeText(requireActivity(), dateFormatBDD + " ; " + heureFormatBDD + " ; " + selectedItemLigneBus + " ; " + selectedItemDirection, Toast.LENGTH_LONG)
            .show()

        // Récupération des objets à afficher
        objects = RoomService.appDatabase.getStopsDAO().getAllObjects()

        // Initialisation du RecyclerView
        viewManager = LinearLayoutManager(context)
        viewAdapter = StopsAdapter(objects) /*{ myObject: stops /*-> myObjectClicked(myObject)*/ }*/
        recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view).apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }
        /*
        Donc je n'ai plus besoin de cette ligne dans la classe SeconFragment : viewAdapter = StopsAdapter(objects) { myObject: stops -> myObjectClicked(myObject) }
        Peux-tu changer le code de la classe SecondFragment en conséquence ?
         */
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    private fun myObjectClicked(myObject: stops) {
        // Code pour gérer le clic sur un objet et ouvrir le fragment 3 en lui passant des informations
        /*
        val bundle = Bundle()
        bundle.putString("param1", myObject.param1)
        bundle.putString("param2", myObject.param2)
        val thirdFragment = ThirdFragment()
        thirdFragment.arguments = bundle
        navigateTo(thirdFragment, true)
         */
    }
}