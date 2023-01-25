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
    lateinit var buttonRetour: Button

    lateinit var dateFormatBDD: String
    lateinit var heureFormatBDD: String
    lateinit var selectedItemLigneBus: String
    lateinit var selectedItemDirection: String

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_second, container, false)
        lateinit var viewAdapter: RecyclerView.Adapter<*>
        lateinit var viewManager: RecyclerView.LayoutManager
        var objects: List<stops> = listOf()

        buttonRetour = view.findViewById(R.id.retourFrag2)

        buttonRetour.setOnClickListener{
            navigateTo(FirstFragment(),false)
        }

        dateFormatBDD = arguments?.getString("dateFormatBDD").toString()
        heureFormatBDD = arguments?.getString("heureFormatBDD").toString()
        selectedItemLigneBus = arguments?.getString("selectedItemLigneBus").toString()
        selectedItemDirection = arguments?.getString("selectedItemDirection").toString()

        //Toast.makeText(requireActivity(), dateFormatBDD + " ; " + heureFormatBDD + " ; " + selectedItemLigneBus + " ; " + selectedItemDirection, Toast.LENGTH_LONG)
        //    .show()

        // Récupération des objets à afficher
        objects = RoomService.appDatabase.getStopsDAO().getAllObjects()

        // Initialisation du RecyclerView
        viewManager = LinearLayoutManager(context)
        viewAdapter = StopsAdapter(objects, dateFormatBDD, heureFormatBDD, selectedItemLigneBus, selectedItemDirection) /*{ myObject: stops /*-> myObjectClicked(myObject)*/ }*/
        recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view).apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }
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

    fun navigateTo(fragment: Fragment, addToBackstack: Boolean) {
        val transaction = requireActivity().supportFragmentManager
            .beginTransaction()
            .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.exit_to_left, R.anim.exit_to_right)
            .replace(R.id.fragment_container, fragment)
        if (addToBackstack) {
            transaction.addToBackStack(null)
        }
        transaction.commit()
    }
}