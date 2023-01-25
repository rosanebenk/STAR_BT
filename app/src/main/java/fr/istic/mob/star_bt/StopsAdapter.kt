package fr.istic.mob.star_bt

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class StopsAdapter(private val myObjects: List<stops>) :
    RecyclerView.Adapter<StopsAdapter.ViewHolder>() {

    //lateinit var textView1: TextView

    //class ViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.my_stop, parent, false)

        //textView1 = view.findViewById<TextView>(R.id.text_field1)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val current = myObjects[position]
        holder.textView1.text = current.stop_name
        holder.textView2.text = current.stop_desc
        holder.textView3.text = current.stop_code
        holder.itemView.setOnClickListener {
            // Handle click event
            // Open ThirdFragment and pass myObject as a argument
        }
    }

    override fun getItemCount() = myObjects.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView1: TextView = itemView.findViewById(R.id.textView1)
        val textView2: TextView = itemView.findViewById(R.id.textView2)
        val textView3: TextView = itemView.findViewById(R.id.textView3)
    }
}