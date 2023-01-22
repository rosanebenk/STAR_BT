package fr.istic.mob.star_bt

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity

interface MyFragmentActivity  {
    // déclarez ici les méthodes et les propriétés que vous souhaitez utiliser de FragmentActivity
    fun navigateTo(fragment: Fragment, addToBackstack: Boolean)
}