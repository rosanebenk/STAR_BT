package fr.istic.mob.star_bt

import android.content.Context
import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import java.util.jar.Attributes.Name


class SpinAdapter(
    context: Context, textViewResourceId: Int,
    values: List<String>
) : ArrayAdapter<String>(context, textViewResourceId, values) {
    // Your sent context
    private val context: Context

    // Your custom values for the spinner (User)
    private val values: List<String>

    init {
        this.context = context
        this.values = values
    }

    override fun getCount(): Int {
        return values.size
    }

    override fun getItem(position: Int): String {
        return values[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    // And the "magic" goes here
    // This is for the "passive" state of the spinner
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        // I created a dynamic TextView here, but you can reference your own  custom layout for each spinner item
        val label = super.getView(position, convertView, parent!!) as TextView
        label.setTextColor(Color.BLACK)
        label.setTextSize(22F)
        // Récupération des couleurs
        var backgroundColor: String = RoomService.appDatabase.getRouteDAO().getColorByName(values[position])[0]

       // affichage des couleurs
        label.setBackgroundColor(Color.parseColor("#"+backgroundColor))
        // And finally return your dynamic (or custom) view for each spinner item
        return label
    }

    // And here is when the "chooser" is popped up
    // Normally is the same view, but you can customize it if you want
    override fun getDropDownView(
        position: Int, convertView: View?,
        parent: ViewGroup?
    ): View {
        val label = super.getDropDownView(position, convertView, parent) as TextView
        label.setTextColor(Color.BLACK)
        var backgroundColor: String = RoomService.appDatabase.getRouteDAO().getColorByName(values[position])[0]
        label.setBackgroundColor(Color.parseColor("#"+backgroundColor))
        return label
    }
}