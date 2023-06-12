package ru.ilya.moviereviews.util

import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlin.reflect.KClass

class BottomNavigationVisibilityUtils(private val bottomNavigationView: BottomNavigationView) {
    // управляет видимостью BottomNavigationView (только на двух фрагментах)
    private val visibleFragments = mutableListOf<KClass<out Fragment>>()

    fun addVisibleFragment(fragmentClass: KClass<out Fragment>) {
        visibleFragments.add(fragmentClass)
    }

    fun setBottomNavigationViewVisibility(fragment: Fragment) {
        if (visibleFragments.contains(fragment::class)) {
            bottomNavigationView.visibility = View.VISIBLE
        } else {
            bottomNavigationView.visibility = View.GONE
        }
    }
}
