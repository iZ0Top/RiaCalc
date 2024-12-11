package com.alex.riacalc.screens.day

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.alex.riacalc.MainActivity
import com.alex.riacalc.R
import com.alex.riacalc.databinding.ActivityMainBinding
import com.alex.riacalc.databinding.DialogDescriptionBinding
import com.alex.riacalc.databinding.FragmentDayBinding
import com.alex.riacalc.model.Event
import com.alex.riacalc.model.Statistic
import com.alex.riacalc.screens.DialogAdd
import com.alex.riacalc.screens.DialogCostSetting
import com.alex.riacalc.utils.AppPreferences
import com.alex.riacalc.utils.KEY_ARGUMENTS_TO_DAY
import com.alex.riacalc.utils.KEY_ARGUMENTS_TO_MONTH
import com.alex.riacalc.utils.TYPE_INSPECTION
import com.alex.riacalc.utils.TYPE_INSPECTION_CAR_DEALERSHIP
import com.alex.riacalc.utils.TYPE_INSPECTION_CAR_PARK
import com.alex.riacalc.utils.TYPE_INSPECTION_CONST_PROGRESS
import com.alex.riacalc.utils.TYPE_INSPECTION_OTHER
import com.alex.riacalc.utils.TYPE_OTHER
import com.alex.riacalc.utils.TYPE_TRIP
import java.util.Calendar

class DayFragment : Fragment(), OnClickListener {

    private var _binding: FragmentDayBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: DayFragmentVM
    private lateinit var adapter: AdapterForDay
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var observerMediatorLD: Observer<List<Event>>
    private lateinit var observerDate: Observer<Calendar>
    private lateinit var observerStatistic: Observer<Statistic>
    private lateinit var date: Calendar
    private lateinit var mainBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this)[DayFragmentVM::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDayBinding.inflate(inflater, container, false)
        mainBinding = (activity as MainActivity).binding

        if (AppPreferences.getReviewDefaultCost() == 0) showDialogSetting()

        if (arguments != null){
            viewModel.setNewDate(requireArguments().getSerializable(KEY_ARGUMENTS_TO_DAY) as Calendar)
        }

        (activity as MainActivity).window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)

        layoutManager = LinearLayoutManager(requireContext())

        initAdapter()
        initObservers()
        setupObservers()
        setupDialogListener()

        binding.recyclerViewDay.layoutManager = layoutManager
        binding.recyclerViewDay.adapter = adapter
        binding.btnAddInspection.setOnClickListener(this)
        binding.btnAddTrip.setOnClickListener(this)
        binding.btnAddOther.setOnClickListener(this)

        with(mainBinding.toolbar){

            toolbarBtnExport.visibility = View.GONE
            toolbarBtnBack.visibility = View.GONE

            toolbarBtnSetting.visibility = View.VISIBLE
            toolbarBtnMonth.visibility = View.VISIBLE

            toolbarFrameDate.setOnClickListener(this@DayFragment)
            toolbarBtnSetting.setOnClickListener(this@DayFragment)
            toolbarBtnMonth.setOnClickListener(this@DayFragment)
        }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()

        viewModel.getMediatorLiveData().removeObserver(observerMediatorLD)
        viewModel.calendarLD.removeObserver(observerDate)
        viewModel.statisticLD.removeObserver(observerStatistic)
        _binding = null
    }

    private fun initAdapter() {

        adapter = AdapterForDay(object : ActionListener {
            override fun onEditEvent(event: Event) { showDialogEditEvent(event) }
            override fun onDeleteEvent(event: Event) { viewModel.deleteEvent(event) }
            override fun onShowDetails(event: Event) { showDialogDescription(event) }
        })
    }

    private fun initObservers(){

        observerDate = Observer {
            changeDate(it)
            viewModel.loadEventsForDay(it)
        }
        observerMediatorLD = Observer {
            adapter.setList(it)
            viewModel.calculateDay(it)
        }
        observerStatistic = Observer {
            updateInfo(it)
        }
    }

    private fun setupObservers() {

        viewModel.calendarLD.observe(viewLifecycleOwner, observerDate)
        viewModel.getMediatorLiveData().observe(viewLifecycleOwner, observerMediatorLD)
        viewModel.statisticLD.observe(viewLifecycleOwner, observerStatistic)
    }

    override fun onClick(v: View?) {

        if (v != null) {
            when (v.id) {
                R.id.toolbar_btn_setting -> { showDialogSetting() }
                R.id.toolbar_frame_date -> { showDatePickerDialog() }
                R.id.btn_add_inspection -> { showDialogAddEvent(TYPE_INSPECTION) }
                R.id.btn_add_trip -> { showDialogAddEvent(TYPE_TRIP) }
                R.id.btn_add_other -> { showDialogAddEvent(TYPE_OTHER) }
                R.id.toolbar_btn_month -> {
                    val bundle = Bundle()
                    bundle.putSerializable(KEY_ARGUMENTS_TO_MONTH, date)
                    findNavController().navigate(R.id.action_dayFragment_to_monthFragment, bundle)
                }
            }
        }
    }

    private fun updateInfo(stats: Statistic){

        with(mainBinding.toolbar){
            toolbarTxtReviewsCount.text = stats.inspectionsCount.toString()
            toolbarTxtTripsCount.text= stats.tripsCount.toString()
            toolbarTxtOtherCount.text = stats.otherCount.toString()
            toolbarTxtReviewsSum.text = resources.getString(R.string.template_formatted_currency, stats.inspectionsSum)
            toolbarTxtTripsSum.text = resources.getString(R.string.template_formatted_currency, stats.tripsSum)
            toolbarTxtOtherSum.text = resources.getString(R.string.template_formatted_currency, stats.otherSum)
        }
    }

    private fun showDialogAddEvent(type: Int){

        val defaultCost = if (type == TYPE_INSPECTION) AppPreferences.getReviewDefaultCost() else 0
        val event = Event(
            id = 0,
            type = type,
            cost = defaultCost,
            description = "",
            date = date
        )
        DialogAdd.show(parentFragmentManager, event, true)
    }

    private fun showDialogSetting(){
        DialogCostSetting().show(parentFragmentManager, DialogCostSetting.DIALOG_SETTING_TAG )
    }

    private fun showDialogEditEvent(event: Event){
        DialogAdd.show(parentFragmentManager, event, false)
    }

    private fun showDatePickerDialog() {

        DatePickerDialog(
            requireContext(), { _, i, i2, i3 ->
                val newCalendar = Calendar.getInstance()
                newCalendar.set(i, i2, i3)
                viewModel.setNewDate(newCalendar)
            },
            date.get(Calendar.YEAR),
            date.get(Calendar.MONTH),
            date.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    private fun setupDialogListener() {

        DialogAdd.setupListener(parentFragmentManager, viewLifecycleOwner) {resultEvent, resultIsNew->
            if (resultIsNew) viewModel.insertEvent(resultEvent) else viewModel.editEvent(resultEvent)
        }
    }

    private fun changeDate(calendar: Calendar) {

        val dayNames = resources.getStringArray(R.array.day_name)
        val monthNames = resources.getStringArray(R.array.month_name)

        val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)        // 1
        val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)      // 1
        val montNumber = calendar.get(Calendar.MONTH)             // 0

        mainBinding.toolbar.toolbarTextDateFirst.text = dayNames[dayOfWeek - 1]
        mainBinding.toolbar.toolbarTextDateSecond.text = resources.getString(R.string.template_date, dayOfMonth, monthNames[montNumber])

        date = calendar
    }

    private fun showDialogDescription(event: Event) {

        val dialog = AlertDialog.Builder(context)
        val dialogBinding = DialogDescriptionBinding.inflate(layoutInflater)

        if (event.description.isNotEmpty()) {
            dialogBinding.dialogDescriptionNoDesc.visibility = View.GONE
            dialogBinding.dialogDescriptionDesc.visibility = View.VISIBLE
            dialogBinding.dialogDescriptionDesc.text = event.description
        }

        when(event.type){
            TYPE_INSPECTION, TYPE_INSPECTION_CAR_DEALERSHIP, TYPE_INSPECTION_CAR_PARK, TYPE_INSPECTION_CONST_PROGRESS, TYPE_INSPECTION_OTHER -> {
                dialogBinding.dialogDescriptionTitle.text = resources.getString(R.string.text_inspection)
                dialogBinding.dialogDescriptionCost.text = resources.getString(R.string.template_plus, event.cost)
            }
            TYPE_TRIP -> {
                dialogBinding.dialogDescriptionTitle.text = resources.getString(R.string.text_trip)
                dialogBinding.dialogDescriptionCost.text = resources.getString(R.string.template_minus, event.cost)
            }
            TYPE_OTHER -> {
                dialogBinding.dialogDescriptionTitle.text = resources.getString(R.string.text_other_expense)
                dialogBinding.dialogDescriptionCost.text = resources.getString(R.string.template_minus, event.cost)
            }
            else -> {
                return
            }
        }

        dialog.setCancelable(true)
            .setView(dialogBinding.root)
            .setPositiveButton(R.string.text_btn_ok, null)
            .create()
            .show()
    }
}