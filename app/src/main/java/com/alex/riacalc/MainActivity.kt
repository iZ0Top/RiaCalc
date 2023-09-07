package com.alex.riacalc

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.widget.Toolbar
import androidx.navigation.NavController
import com.alex.riacalc.databinding.ActivityMainBinding
import com.alex.riacalc.utils.AppPreferences

class MainActivity : AppCompatActivity() {


    private var _binding: ActivityMainBinding? = null
    val binding get() = _binding!!
    private lateinit var toolbar: Toolbar


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("TAG", "MainActivity - onCreate" )
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        AppPreferences.getPreferences(this)
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}