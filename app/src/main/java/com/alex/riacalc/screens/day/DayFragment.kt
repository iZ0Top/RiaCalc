package com.alex.riacalc.screens.day

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.Gravity
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
import com.alex.riacalc.screens.AdapterForDay
import com.alex.riacalc.screens.DialogAdd
import com.alex.riacalc.screens.SharedViewModel
import com.alex.riacalc.utils.AppPreferences
import com.alex.riacalc.utils.TYPE_INSPECTION
import com.alex.riacalc.utils.TYPE_OTHER
import com.alex.riacalc.utils.TYPE_TRIP
import com.alex.riacalc.utils.dateAndTimeFormatterForDB
import java.util.Calendar

class DayFragment : Fragment(), OnClickListener {

    private var _binding: FragmentDayBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: DayFragmentVM
    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var adapter: AdapterForDay
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var observerMediatorLD: Observer<List<Event>>
    private lateinit var observerDate: Observer<Calendar>
    private lateinit var observerStatistic: Observer<DayFragmentVM.Companion.Statistic>
    private lateinit var date: Calendar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("TAG", "DayFragment - onCreate")

        viewModel = ViewModelProvider(this).get(DayFragmentVM::class.java)
        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)

        adapter = AdapterForDay(object : ActionListener {
            override fun onEditEvent(event: Event) { showDialogEditEvent(event) }
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

        val arg = arguments?.getSerializable("K")

        date = Calendar.getInstance()

        layoutManager = LinearLayoutManager(requireContext())

        binding.recyclerViewDay.layoutManager = layoutManager
        binding.recyclerViewDay.adapter = adapter

        binding.includeDayHeader.btnExport.visibility = View.GONE

        binding.includeDayHeader.btnSetting.setOnClickListener(this)
        binding.includeDayHeader.btnMonthDay.setOnClickListener(this)
        binding.includeDayHeader.frameDate.setOnClickListener(this)
        binding.btnAddInspection.setOnClickListener(this)
        binding.btnAddTrip.setOnClickListener(this)
        binding.btnAddOther.setOnClickListener(this)

        setupDialogListener()

        observerDate = Observer {
            Log.d("TAG", "observerDate")
            changeDate(it)
            viewModel.loadEventsForDate(it)
        }

        observerMediatorLD = Observer {
            Log.d("TAG", "observerMediatorLD")
            adapter.setList(it)
            viewModel.calculateDay(it)
        }

        observerStatistic = Observer {
            Log.d("TAG", "observerStatistic")
            with(binding.includeDayHeader){
                txtReviewsCount.text = it.inspectionsCount
                txtReviewsSum.text = it.inspectionsSum
                txtTripsCount.text = it.tripsCount
                txtTripsSum.text = it.tripsSum
                txtOtherCount.text = it.otherCount
                txtOtherSum.text = it.otherSum
            }
        }

        if (!AppPreferences.getShowCost()) changeView()

        viewModel.calendarLD.observe(viewLifecycleOwner, observerDate)
        viewModel.getMediatorLiveData().observe(viewLifecycleOwner, observerMediatorLD)
        viewModel.statisticLD.observe(viewLifecycleOwner, observerStatistic)

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d("TAG", "DayFragment - onDestroy")
        viewModel.getMediatorLiveData().removeObserver(observerMediatorLD)
        viewModel.calendarLD.removeObserver(observerDate)
        viewModel.statisticLD.removeObserver(observerStatistic)
        _binding = null
    }

    private fun init(){

    }

    override fun onClick(v: View?) {
        Log.d("TAG", "DayFragment - onClick")

        if (v != null) {
            when (v.id) {
                R.id.btn_setting -> { findNavController().navigate(R.id.action_dayFragment_to_settingFragment) }
                R.id.frame_date -> { showDatePickerDialog() }
                R.id.btn_add_inspection -> { showDialogAddEvent(TYPE_INSPECTION) }
                R.id.btn_add_trip -> { showDialogAddEvent(TYPE_TRIP) }
                R.id.btn_add_other -> { showDialogAddEvent(TYPE_OTHER) }
                R.id.btn_month_day -> { findNavController().navigate(R.id.action_dayFragment_to_monthFragment) }
            }
        }
    }

    private fun showDialogAddEvent(type: Int){
        val defaultCost = if (type == TYPE_INSPECTION) AppPreferences.getReviewDefaultCost() else 0
        val event = Event(
            id = 0,
            type = type,
            cost = defaultCost,
            description = "",
            date = dateAndTimeFormatterForDB(date)
        )
        DialogAdd.show(parentFragmentManager, event, true)
    }

    private fun showDialogEditEvent(event: Event){
        DialogAdd.show(parentFragmentManager, event, false)
    }

    private fun setupDialogListener() {
        Log.d("TAG", "DayFragment - setupDialogListener")
        DialogAdd.setupListener(parentFragmentManager, viewLifecycleOwner) {resultEvent, resultIsNew->
            Log.d("TAG", "DayFragment - setupDialogListener - result")
            if (resultIsNew) viewModel.insertEvent(resultEvent) else viewModel.editEvent(resultEvent)
        }
    }

    private fun showDatePickerDialog() {
        Log.d("TAG", "DayFragment - showDatePickerDialog")

        DatePickerDialog(
            requireContext(), DatePickerDialog.OnDateSetListener { _, i, i2, i3 ->
                val newCalendar = Calendar.getInstance()
                newCalendar.set(i, i2, i3)
                viewModel.setNewDate(newCalendar)
            },
            date.get(Calendar.YEAR),
            date.get(Calendar.MONTH),
            date.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    private fun changeDate(calendar: Calendar) {
        Log.d("TAG", "DayFragment - setDate")

        val dayNames = resources.getStringArray(R.array.day_name)
        val monthNames = resources.getStringArray(R.array.month_name)

        val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)        // 1
        val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)      // 1
        val montNumber = calendar.get(Calendar.MONTH)             // 0

        binding.includeDayHeader.textDate.text = resources.getString(R.string.template_date, dayOfMonth, monthNames[montNumber])
        binding.includeDayHeader.textDayName.text = dayNames[dayOfWeek - 1]

        date = calendar
    }

    private fun showDialogDescription(event: Event) {
        Log.d("TAG", "DayFragment - showDialogDescription")

        AlertDialog.Builder(context)
            .setMessage(event.toString())
            .setCancelable(true)
            .setPositiveButton("Ok", null)
            .create()
            .show()
    }

    private fun changeView(){
        with(binding.includeDayHeader){
            txtReviewsSum.visibility = View.GONE
            txtReviewsCount.gravity = Gravity.END
            txtTripsSum.visibility = View.GONE
            txtTripsCount.gravity= Gravity.END
            txtOtherSum.visibility= View.GONE
            txtOtherCount.gravity= Gravity.END
        }
    }
}