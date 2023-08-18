package com.alex.riacalc.screens.day

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
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
import com.alex.riacalc.utils.AppPreferences
import java.util.Calendar

class DayFragment : Fragment(), OnClickListener {

    private var _binding: FragmentDayBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: DayFragmentVM
    private lateinit var adapter: AdapterDay
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var observerEvents: Observer<List<Event>>
    private lateinit var observerDate: Observer<Calendar>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("TAG", "DayFragment - onCreate")

        viewModel = ViewModelProvider(this).get(DayFragmentVM::class.java)

        adapter = AdapterDay(object : ActionListener {
            override fun onEditEvent(event: Event) { dialogEditEvent(event) }
            override fun onDeleteEvent(event: Event) { viewModel.deleteEvent(event) }
            override fun onShowDetails(event: Event) { showDialogDescription(event) }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("TAG", "DayFragment - onCreateView")

        _binding = FragmentDayBinding.inflate(inflater, container, false)

        layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.adapter = adapter

        binding.btnSetting.setOnClickListener(this)
        binding.btnMonth.setOnClickListener(this)
        binding.frameDate.setOnClickListener(this)
        binding.btnAddInspection.setOnClickListener(this)
        binding.btnAddTrip.setOnClickListener(this)
        binding.btnAddOther.setOnClickListener(this)

        setupDialogListener()

        observerDate = Observer { setDate(it) }
        observerEvents = Observer { adapter.setList(it) }

        viewModel.calendarLD.observe(viewLifecycleOwner, observerDate)
        viewModel.eventListLD.observe(viewLifecycleOwner, observerEvents)

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d("TAG", "DayFragment - onDestroy")
        viewModel.eventListLD.removeObserver(observerEvents)
        _binding = null
    }

    override fun onClick(v: View?) {
        Log.d("TAG", "DayFragment - onClick")

        if (v != null) {
            when (v.id) {
                R.id.btn_setting -> { findNavController().navigate(R.id.action_dayFragment_to_settingFragment) }
                R.id.btn_month -> { findNavController().navigate(R.id.action_dayFragment_to_monthFragment) }
                R.id.frame_date -> { showDatePickerDialog(viewModel.calendarLD.value!!) }
                R.id.btn_add_inspection -> { dialogAddEvent(DialogAdd.TYPE_INSPECTION) }
                R.id.btn_add_trip -> { dialogAddEvent(DialogAdd.TYPE_TRIP) }
                R.id.btn_add_other -> { dialogAddEvent(DialogAdd.TYPE_OTHER) }
            }
        }
    }

    private fun setDate(calendar: Calendar) {
        Log.d("TAG", "DayFragment - setDate")

        val dayNames = resources.getStringArray(R.array.day_name)
        val monthNames = resources.getStringArray(R.array.month_name)

        val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)        // 1
        val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)      // 1
        val montNumber = calendar.get(Calendar.MONTH)             // 0
        val year = calendar.get(Calendar.YEAR)

        /////////////////////////////////////////
        val realDate = Calendar.getInstance()

        val y = year == realDate.get(Calendar.YEAR)
        val m = montNumber == realDate.get(Calendar.MONTH)
        val dm = dayOfMonth == realDate.get(Calendar.DAY_OF_MONTH)
        Log.d(
            "TAG", "DayFragment - compare = $y, $m, $dm\n" +
                    "setDate. year = ${year}, mountNumber = $montNumber, dayOfMonth = $dayOfMonth, dayOfWeek = $dayOfWeek \n" +
                    "realDate. year = ${realDate.get(Calendar.YEAR)}, mountNumber = ${
                        realDate.get(
                            Calendar.MONTH
                        )
                    }, dayOfMonth = ${realDate.get(Calendar.DAY_OF_MONTH)}, dayOfWeek = ${
                        realDate.get(
                            Calendar.DAY_OF_WEEK
                        )
                    }"
        )

        binding.textDate.text = resources.getString(R.string.template_date, dayOfMonth, monthNames[montNumber])
        binding.textDayName.text = dayNames[dayOfWeek - 1]
    }


    private fun dialogAddEvent(type: Int){
        val defaultCost = if (type == DialogAdd.TYPE_INSPECTION) AppPreferences.getReviewDefaultCost() else 0
        val event = Event(
            id = 0,
            type = type,
            cost = defaultCost,
            description = "",
            date = Calendar.getInstance().toString()
        )
        DialogAdd.show(parentFragmentManager, event, true)
    }

    private fun dialogEditEvent(event: Event){
        DialogAdd.show(parentFragmentManager, event, false)
    }

    private fun setupDialogListener() {
        Log.d("TAG", "DayFragment - setupDialogListener")
        DialogAdd.setupListener(parentFragmentManager, viewLifecycleOwner) {resultEvent, resultIsNew->
            Log.d("TAG", "DayFragment - setupDialogListener - result")
            if (resultIsNew) viewModel.insertEvent(resultEvent) else viewModel.editEvent(resultEvent)
        }
    }

    private fun showDatePickerDialog(currentDate: Calendar) {
        Log.d("TAG", "DayFragment - showDatePickerDialog")

        DatePickerDialog(
            requireContext(), DatePickerDialog.OnDateSetListener { _, i, i2, i3 ->
                val cal = Calendar.getInstance()
                cal.set(i, i2, i3)
                viewModel.calendarLD.value = cal
            },
            currentDate.get(Calendar.YEAR),
            currentDate.get(Calendar.MONTH),
            currentDate.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    fun showDialogDescription(event: Event) {
        Log.d("TAG", "DayFragment - showDialogDescription")

        AlertDialog.Builder(context)
            .setMessage(event.toString())
            .setCancelable(true)
            .setPositiveButton("Ok", null)
            .create()
            .show()
    }


    companion object {
        const val KEY_REQUEST_DIALOG_INSPECTION = "dialog_inspection"
        const val KEY_REQUEST_DIALOG_TRIP = "dialog_trip"
        const val KEY_REQUEST_DIALOG_OTHER = "dialog_other"
    }
}