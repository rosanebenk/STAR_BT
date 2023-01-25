package fr.istic.mob.star_bt

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

class ThirdFragment: Fragment() {
    lateinit var dateFormatBDD: String
    lateinit var heureFormatBDD: String
    lateinit var selectedItemLigneBus: String
    lateinit var selectedItemDirection: String
    lateinit var stopID: String

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_third, container, false)

        dateFormatBDD = arguments?.getString("dateFormatBDD").toString()
        heureFormatBDD = arguments?.getString("heureFormatBDD").toString()
        selectedItemLigneBus = arguments?.getString("selectedItemLigneBus").toString()
        selectedItemDirection = arguments?.getString("selectedItemDirection").toString()
        stopID = arguments?.getString("stopID").toString()




        return view
    }
}