package ru.ilya.moviereviews.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ru.ilya.moviereviews.R
import ru.ilya.moviereviews.databinding.ActivityMainBinding
import ru.ilya.moviereviews.presentation.critics_screen.CriticsFragment
import ru.ilya.moviereviews.presentation.reviews_screen.ReviewsFragment
import ru.ilya.moviereviews.util.BottomNavigationVisibilityUtils

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    lateinit var bottomNavigation: BottomNavigationVisibilityUtils

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, ReviewsFragment())
                .addToBackStack(null)
                .commit()
        }

        setupBottomNavigationView()
    }

    private fun setupBottomNavigationView() {
        //  классы фрагментов, на которых должен отображаться BottomNavigationView
        bottomNavigation = BottomNavigationVisibilityUtils(binding.bottomNavigationView)
        bottomNavigation.addVisibleFragment(ReviewsFragment::class)
        bottomNavigation.addVisibleFragment(CriticsFragment::class)

        binding.bottomNavigationView.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.navigation_reviews -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, ReviewsFragment())
                        .commit()
                    true
                }
                R.id.navigation_critics -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, CriticsFragment())
                        .commit()
                    true
                }
                else -> false
            }
        }
    }
}