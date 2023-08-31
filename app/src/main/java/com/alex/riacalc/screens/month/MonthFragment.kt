package com.alex.riacalc.screens.month

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
import com.alex.riacalc.databinding.FragmentMonthBinding
import com.alex.riacalc.model.Day
import com.alex.riacalc.screens.AdapterForMonth
import com.alex.riacalc.screens.DialogSetMonthAndYear
import java.util.Calendar

class MonthFragment : Fragment(), OnClickListener {

    private lateinit var viewModel: MonthFragmentVM

    private var _binding: FragmentMonthBinding? = null
    private val binding get() = _binding!!
    private lateinit var observerDate: Observer<Calendar>
    private lateinit var observerDays: Observer<List<Day>>
    private lateinit var adapter: AdapterForMonth
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var calendar: Calendar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MonthFragmentVM::class.java)
    }

    override fun onCreateView(

        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("TAGM", "MonthFragment - onCreateView")

        _binding = FragmentMonthBinding.inflate(inflater, container, false)

        val argCalendar = requireArguments().getSerializable(KEY_ARGUMENTS) as Calendar
        viewModel.setDate(argCalendar)

        layoutManager = LinearLayoutManager(requireContext())
        adapter = AdapterForMonth()

        binding.recyclerViewMonth.layoutManager = layoutManager
        binding.recyclerViewMonth.adapter = adapter

        initObservers()

        setupDialogSetMonthAndYearListener()

        viewModel.calendarLD.observe(viewLifecycleOwner, observerDate)
        viewModel.loadEventsForMonth().observe(viewLifecycleOwner, observerDays)

        binding.includedMonthHeader.btnMonthDay.setImageDrawable(resources.getDrawable(R.drawable.ic_calendar_day))
        binding.includedMonthHeader.frameDate.setOnClickListener(this)
        binding.includedMonthHeader.btnMonthDay.setOnClickListener(this)
        binding.includedMonthHeader.btnExport.setOnClickListener(this)
        binding.includedMonthHeader.btnSetting.setOnClickListener(this)

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.calendarLD.removeObserver(observerDate)
        _binding = null
    }


    override fun onClick(v: View?) {

        if (v != null) {
            when (v.id) {
                R.id.btn_setting -> {
                    findNavController().navigate(R.id.action_monthFragment_to_settingFragment)
                }
                R.id.btn_month_day -> {
                    findNavController().navigate(R.id.action_monthFragment_to_dayFragment)
                }
                R.id.btn_export -> {
                    Toast.makeText(requireContext(), "in progress..", Toast.LENGTH_SHORT).show()
                }
                R.id.frame_date -> {
                    showDialogSetMonthAndYear()
                }
            }
        }
    }

    private fun initObservers(){

        observerDate = Observer<Calendar> {
            Log.d("TAGM", "MonthFragment - onCreateView - observerDate")
            calendar = it
            updateView()
            loadEvents()
        }

        observerDays = Observer {
            Log.d("TAGM", "MonthFragment - onCreateView - observerDays. list size: = ${it.size}")

            updateInfo(it)
            adapter.setList(it)
        }
    }

    private fun updateView() {
        Log.d("TAGM", "MonthFragment - updateView")
        val monthNames = resources.getStringArray(R.array.month_name_for_picker)
        with(binding.includedMonthHeader) {
            textDateFirst.text = monthNames[calendar.get(Calendar.MONTH)]
            textDateSecond.text = calendar.get(Calendar.YEAR).toString()
        }
    }

    private fun loadEvents() {
        viewModel.loadEventsForMonth()
        Log.d("TAGM", "MonthFragment - loadEvents")
    }

    private fun showDialogSetMonthAndYear() {
        Log.d("TAGM", "MonthFragment - showDialogSetMonthAndYear")
        DialogSetMonthAndYear.show(
            parentFragmentManager,
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.YEAR)
        )
    }

    private fun setupDialogSetMonthAndYearListener() {
        Log.d("TAGM", "MonthFragment - setupDialogSetMonthAndYearListener")
        DialogSetMonthAndYear.setupListener(
            parentFragmentManager,
            viewLifecycleOwner
        ) { month, year ->

            val newDate = Calendar.getInstance()
            newDate.set(Calendar.YEAR, year)
            newDate.set(Calendar.MONTH, month)
            viewModel.setDate(newDate)
        }
    }

    private fun updateInfo(listDays: List<Day>){

        with(binding.includedMonthHeader){
            txtReviewsCount.text = listDays.sumOf {it.inspectionCount }.toString()
            txtTripsCount.text = listDays.sumOf { it.tripCount }.toString()
            txtOtherCount.text = listDays.sumOf { it.otherCount }.toString()

            txtReviewsSum.text = resources.getString(
                R.string.template_formatted_currency,
                listDays.sumOf { it.inspectionSum }
            )
            txtTripsSum.text = resources.getString(
                R.string.template_formatted_currency,
                listDays.sumOf { it.tripSum }
            )
            txtOtherSum.text = resources.getString(
                R.string.template_formatted_currency,
                listDays.sumOf { it.otherSum }
            )
        }
    }

    companion object {
        const val KEY_ARGUMENTS = "key_argument"
    }
}