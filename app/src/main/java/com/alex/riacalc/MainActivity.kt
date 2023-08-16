package com.alex.riacalc

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.alex.riacalc.databinding.ActivityMainBinding
import com.alex.riacalc.utils.AppPreferences

class MainActivity : AppCompatActivity() {


    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("TAG", "MainActivity - onCreate" )
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        val navHost = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
//        navController = navHost.navController

        AppPreferences.getPreferences(this)
    }



    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}