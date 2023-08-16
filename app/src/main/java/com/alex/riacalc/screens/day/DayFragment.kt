package com.alex.riacalc.screens.day

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.alex.riacalc.R
import com.alex.riacalc.databinding.FragmentDayBinding
import com.alex.riacalc.model.Event
import com.alex.riacalc.screens.ActionListener
import com.alex.riacalc.screens.AdapterDay
import com.alex.riacalc.screens.DialogAdd
import com.alex.riacalc.screens.MyViewModelFactory

class DayFragment : Fragment(), OnClickListener {

    private var _binding: FragmentDayBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: DayFragmentVM
    private lateinit var adapter: AdapterDay
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var observer: Observer<List<Event>>

    private var list = mutableListOf<Event>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("TAG", "DayFragment - onCreate" )

        viewModel = ViewModelProvider(this).get(DayFragmentVM::class.java)

        viewModel.initDatabase()

        adapter = AdapterDay(object : ActionListener {
            override fun onEditEvent(event: Event) { /* Dialog edit Event */ }
            override fun onDeleteEvent(event: Event) { /* Repository delete Event */ }
            override fun onShowDetails(event: Event) { showDialogDescription(event) }
        })

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("TAG", "DayFragment - onCreateView" )

        _binding = FragmentDayBinding.inflate(inflater, container, false)

        layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.adapter = adapter

        binding.btnSetting.setOnClickListener(this)
        binding.btnMonth.setOnClickListener(this)
        binding.btnAddInspection.setOnClickListener(this)
        binding.btnAddTrip.setOnClickListener(this)
        binding.btnAddOther.setOnClickListener(this)

        setupDialogListener()

        observer = Observer { adapter.setList(it) }
        viewModel.eventListLD.observe(viewLifecycleOwner, observer)

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("TAG", "DayFragment - onDestroy" )

        viewModel.eventListLD.removeObserver(observer)
        _binding = null
    }


    override fun onClick(v: View?) {
        Log.d("TAG","DayFragment - onClick")

        if (v != null) {
            when(v.id){
                R.id.btn_setting -> {
                    findNavController().navigate(R.id.action_dayFragment_to_settingFragment)
                }
                R.id.btn_month -> {
                    findNavController().navigate(R.id.action_dayFragment_to_monthFragment)
                }
                R.id.btn_add_inspection -> {
                    DialogAdd.show(parentFragmentManager, createEvent(DialogAdd.TYPE_INSPECTION))
                }
                R.id.btn_add_trip -> {
                    DialogAdd.show(parentFragmentManager, createEvent(DialogAdd.TYPE_TRIP))
                }
                R.id.btn_add_other -> {
                    DialogAdd.show(parentFragmentManager, createEvent(DialogAdd.TYPE_OTHER))
                }
            }
        }
    }

    private fun createEvent(type: Int): Event{
        Log.d("TAG","DayFragment - createEvent")

        val defaultCost = if (type == DialogAdd.TYPE_INSPECTION) 80 else 0

        return Event(
            id = 0,
            type = type,
            cost = defaultCost,
            description = "",
            date =  "" )
    }

    private fun setupDialogListener(){
        DialogAdd.setupListener(parentFragmentManager, viewLifecycleOwner) {
            // Add result to Repository
            Log.d("TAG", "DayFragment - setUpDialogListener result\n + ${it.toString()}" )
        }
    }

    fun showDialogDescription(event: Event){
        AlertDialog.Builder(context)
            .setMessage(event.toString())
            .setCancelable(true)
            .setPositiveButton("Ok", null)
            .create()
            .show()
    }


    companion object{
        const val KEY_REQUEST_DIALOG_INSPECTION = "dialog_inspection"
        const val KEY_REQUEST_DIALOG_TRIP = "dialog_trip"
        const val KEY_REQUEST_DIALOG_OTHER = "dialog_other"
    }
}