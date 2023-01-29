package fr.istic.mob.star_bt

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import java.util.*

class FirstFragment : Fragment() {

    var prefs: SharedPreferences? = null

    lateinit var spinner_LigneBus: Spinner
    lateinit var spinner_DirectionBus: Spinner

    //lateinit var listeLignesBus:
    lateinit var datePickerDialog: DatePickerDialog
    lateinit var buttonDate: Button
    lateinit var timeButton: Button
    lateinit var validerFrag1: Button
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



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_first, container, false)
        //Initialisation du spinner de date et récupération des autres spinner
        initDatePicker()
        buttonDate = view.findViewById(R.id.datePickerButton)
        buttonDate.text = getTodaysDate()

        timeButton = view.findViewById(R.id.timeButton)
        val currentTime = Calendar.getInstance().apply {
            val hour = get(Calendar.HOUR_OF_DAY)
            val minute = get(Calendar.MINUTE)
            val currentTime = String.format("%02d:%02d", hour, minute)
            timeButton.text = currentTime
            heureFormatBDD = timeButton?.text.toString() + ":00"
        }

        spinner_LigneBus = view.findViewById(R.id.spinner_LigneBus)
        spinner_DirectionBus = view.findViewById(R.id.spinner_Direction)
        validerFrag1 = view.findViewById(R.id.validerFrag1)

        alimenterSpinnerListeBus(spinner_LigneBus)
        alimenterSpinnerDirectionBus(spinner_DirectionBus)

        timeButton.setOnClickListener {
            popTimePicker(it)
        }
        buttonDate.setOnClickListener {
            openDatePicker(it)
        }

        validerFrag1.setOnClickListener {
            validateFrag1(it)
        }

        return view
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
        val adapter = SpinAdapter(requireActivity(), android.R.layout.simple_list_item_1, lignes)
        spinner_LigneBus.adapter = adapter
        //var busSelected: String = spinner_LigneBus.selectedItem.toString()


        spinner_LigneBus.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
                Toast.makeText(requireActivity(), "Ligne sélectionnée : $selectedItemLigneBus", Toast.LENGTH_LONG).show()

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                //onItemSelected(parent, view, position, id)
                selectedItemLigneBus = parent?.getItemAtPosition(position).toString()
                println(selectedItemLigneBus)
                Toast.makeText(requireActivity(), "Ligne sélectionnée : $selectedItemLigneBus", Toast.LENGTH_LONG).show()
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
        val adapter = ArrayAdapter(requireActivity(), android.R.layout.simple_list_item_1, listeDirections)
        spinner_DirectionBus.adapter = adapter

        spinner_DirectionBus.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
                Toast.makeText(requireActivity(), "Direction sélectionnée : $selectedItemDirection", Toast.LENGTH_LONG).show()
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                //onItemSelected(parent, view, position, id)
                selectedItemDirection = parent?.getItemAtPosition(position).toString()
                println(selectedItemDirection)
                Toast.makeText(requireActivity(), "Direction sélectionnée : $selectedItemDirection", Toast.LENGTH_LONG).show()
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
        Toast.makeText(requireActivity(), "Date sélectionnée : " + dateFormatBDD, Toast.LENGTH_LONG).show()

        return makeDateString(day, month, year)
    }

    /**
     * Initialise le spinner de date
     */
    private fun initDatePicker() {
        val dateSetListener =
            DatePickerDialog.OnDateSetListener { datePicker, year, month, day ->
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

        datePickerDialog = DatePickerDialog(requireActivity(), style, dateSetListener, year, month, day)
        datePickerDialog.datePicker.minDate = System.currentTimeMillis() - 600000
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
        Toast.makeText(requireActivity(), "Date sélectionnée : " + dateFormatBDD, Toast.LENGTH_LONG).show()
    }

    /**
     * Ouvre la pop up avec le "spinner" de l'heure
     * Et reformate la sélection de l'utilisateur dans la variable @heureFormatBDD
     */
    fun popTimePicker(view: View?) {
        val onTimeSetListener =
            TimePickerDialog.OnTimeSetListener { timePicker, selectedHour, selectedMinute ->
                hour = selectedHour
                minute = selectedMinute
                timeButton?.text = java.lang.String.format(
                    Locale.getDefault(),
                    "%02d:%02d",
                    hour,
                    minute
                )
                heureFormatBDD = timeButton?.text.toString() + ":00"
                Toast.makeText(requireActivity(), "Heure sélectionnée : " + heureFormatBDD, Toast.LENGTH_SHORT)
                    .show()
            }

        // int style = AlertDialog.THEME_HOLO_DARK;
        val timePickerDialog =
            TimePickerDialog(requireActivity(),  /*style,*/onTimeSetListener, hour, minute, true)
        timePickerDialog.setTitle("Select Time")
        timePickerDialog.show()

    }

    /**
     * Valide les choix de l'utilisateur et passe au fragment 2
     */
    fun validateFrag1(view: View?){
        if(timeButton?.text != "Choisir une heure"){

            navigateToFragment2()

        }else{
            Toast.makeText(requireActivity(), "Veuillez sélectionner une heure pour continuer.", Toast.LENGTH_LONG)
                .show()
        }
    }

    /**
     * Appel le fragment 2 avec une animation
     * Place dans un bundle les choix de l'utilisateur
     */
    fun navigateToFragment2() {
        val fragment2 = SecondFragment()
        val bundle = Bundle()
        bundle.putString("heureFormatBDD", heureFormatBDD)
        bundle.putString("dateFormatBDD", dateFormatBDD)
        bundle.putString("selectedItemLigneBus", selectedItemLigneBus)
        bundle.putString("selectedItemDirection", selectedItemDirection)

        fragment2.arguments = bundle
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.exit_to_left, R.anim.exit_to_right)
        transaction.replace(R.id.fragment_container, fragment2)
        transaction.addToBackStack(null)
        transaction.commit()
    }
}