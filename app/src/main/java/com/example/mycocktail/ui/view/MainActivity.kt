package com.example.mycocktail.ui.view

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.example.mycocktail.R
import com.example.mycocktail.databinding.ActivityMainBinding
import com.example.mycocktail.preferences.SharedPreferencesManager
import com.example.mycocktail.ui.viewmodel.DrinkViewModel
import com.example.mycocktail.utils.NetworkManager

class MainActivity : AppCompatActivity() {

    private var binding: ActivityMainBinding? = null
    private val homeFragment: HomeFragment = HomeFragment()
    private val favoriteFragment: FavoriteFragment = FavoriteFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
        )
        replaceFragment(homeFragment)

        binding?.bnNavigation?.setOnItemSelectedListener { item ->
            when(item.itemId){
                R.id.action_home -> replaceFragment(homeFragment)
                R.id.action_favorites -> replaceFragment(favoriteFragment)
            }
            true
        }
    }

    fun replaceFragment(fragment: Fragment){
        val fragmentManager = supportFragmentManager

        fragmentManager.commit {
            when(fragment){
                is ErrorFragment -> {
                    replace(R.id.fl_content, fragment)
                    addToBackStack(null)
                    setReorderingAllowed(true)
                    binding?.bnNavigation?.visibility = View.GONE
                }
                else -> {
                    replace(R.id.fl_content, fragment)
                    addToBackStack(null)
                    setReorderingAllowed(true)
                    binding?.bnNavigation?.visibility = View.VISIBLE
                }
            }
        }
    }
}