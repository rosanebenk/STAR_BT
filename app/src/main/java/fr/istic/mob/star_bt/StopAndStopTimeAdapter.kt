package fr.istic.mob.star_bt

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView

class StopAndStopTimeAdapter(private val context: Context, private val myObjects: List<stops>, private val date: String, private val heure: String, private val ligne: String, private val direction: String, private val stopid: String, private val tripid: String):
    RecyclerView.Adapter<StopAndStopTimeAdapter.ViewHolder>() {
    lateinit var dateFormatBDD: String
    lateinit var heureFormatBDD: String
    lateinit var selectedItemLigneBus: String
    lateinit var selectedItemDirection: String
    lateinit var stopID: String


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.my_stoptime, parent, false)

        dateFormatBDD = date
        heureFormatBDD = heure
        selectedItemLigneBus = ligne
        selectedItemDirection = direction
        stopID = stopid

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val current = myObjects[position]
        holder.textView1.text = current.stop_name
        var stoptime = RoomService.appDatabase.getStopsTimeDAO().getStopTimeByStopIDandTripID(current.stop_id, tripid)

        holder.textView2.text = stoptime.first().arrival_time

        holder.itemView.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("heureFormatBDD", heureFormatBDD)
            bundle.putString("dateFormatBDD", dateFormatBDD)
            bundle.putString("selectedItemLigneBus", selectedItemLigneBus)
            bundle.putString("selectedItemDirection", selectedItemDirection)
            bundle.putString("stopID", stopID)
            bundle.putString("tripID", tripid)

            val fragment3 = ThirdFragment()
            fragment3.arguments = bundle
            //(context as MainActivity).navigateTo(ThirdFragment(), true)

            val transaction = (context as AppCompatActivity).supportFragmentManager.beginTransaction()
            transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.exit_to_left, R.anim.exit_to_right)
            transaction.replace(R.id.fragment_container, fragment3)
            transaction.addToBackStack(null)
            transaction.commit()
        }
    }

    override fun getItemCount() = myObjects.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView1: TextView = itemView.findViewById(R.id.textView4)
        val textView2: TextView = itemView.findViewById(R.id.textView5)
    }
}