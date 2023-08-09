package com.alex.riacalc.screens

import android.content.Context
import androidx.appcompat.app.AlertDialog
import com.alex.riacalc.MainActivity
import com.alex.riacalc.databinding.DialogAddBinding

class Dialogs() {

    private var _binding: DialogAddBinding? = null
    private val binding get() = _binding!!

    fun getDialog(activity: MainActivity, id: Int): AlertDialog{

        val inflater = activity.layoutInflater
        _binding = DialogAddBinding.inflate(inflater)

        val builder = AlertDialog.Builder(activity)
        builder.setView(binding.root)
            .setCancelable(true)
            .setPositiveButton("OK", null)


        val myDialog = builder.create()

        myDialog.setOnShowListener {

        }

    }



}