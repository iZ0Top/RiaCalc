package com.alex.riacalc.screens

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatSpinner
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import com.alex.riacalc.R
import com.alex.riacalc.databinding.DialogAddBinding
import com.alex.riacalc.model.Event
import com.alex.riacalc.utils.AppPreferences
import com.alex.riacalc.utils.TYPE_BONUS
import com.alex.riacalc.utils.TYPE_INSPECTION
import com.alex.riacalc.utils.TYPE_INSPECTION_CAR_DEALERSHIP
import com.alex.riacalc.utils.TYPE_INSPECTION_CAR_PARK
import com.alex.riacalc.utils.TYPE_INSPECTION_CONST_PROGRESS
import com.alex.riacalc.utils.TYPE_INSPECTION_OTHER
import com.alex.riacalc.utils.TYPE_OTHER
import com.alex.riacalc.utils.TYPE_TRIP

class DialogAdd : DialogFragment() {

    private var _binding: DialogAddBinding? = null
    private val binding get() = _binding!!

    private var mSpinner: AppCompatSpinner? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        Log.d("mtag", "DialogAdd - onCreateDialog")

        val inflater = requireActivity().layoutInflater
        _binding = DialogAddBinding.inflate(inflater)

        val event = arguments?.getSerializable(BUNDLE_EVENT_KEY) as Event
        val isNew = arguments?.getBoolean(BUNDLE_TYPE_KEY) as Boolean

        val dialog = AlertDialog.Builder(requireContext())
            .setView(binding.root)
            .setCancelable(true)
            .setPositiveButton(R.string.text_btn_ok, null)
            .setNegativeButton(R.string.text_btn_cancel, null)
            .create()

        Log.d("mtag", "dialog created")

        binding.etDialogDescription.setText(event.description)
        binding.etDialogCost.setText(event.cost.toString())

        when (event.type){
            TYPE_INSPECTION, TYPE_INSPECTION_CAR_DEALERSHIP, TYPE_INSPECTION_CAR_PARK, TYPE_INSPECTION_CONST_PROGRESS, TYPE_INSPECTION_OTHER -> {
                Log.d("mtag", "dialog type = INSPECTION")

                binding.textDialogTitle.text = resources.getString(R.string.text_description_inspection)
                binding.textSet.visibility = View.GONE
                binding.spinnerInspectionType.visibility = View.VISIBLE

                val itemNames = requireContext().resources.getStringArray(R.array.inspection_names)
                createSpinner(itemNames)
                val inputPosition = convertTypeToPosition(event.type)
                mSpinner?.setSelection(inputPosition)

                mSpinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {
                        val cost = when (position){
                            0 -> AppPreferences.getReviewDefaultCost()
                            1 -> AppPreferences.getReviewCarDealershipCost()
                            2 -> AppPreferences.getReviewCarParkDefaultCost()
                            3 -> AppPreferences.getReviewConstProgress()
                            else -> {0}
                        }
                        val description = when (position){
                            1 -> resources.getString(R.string.text_car_dealership)
                            2 -> resources.getString(R.string.text_car_park)
                            3 -> resources.getString(R.string.text_const_progress)
                            else -> ""
                        }

                        if (event.type != convertPositionToType(position)){
                            binding.etDialogDescription.setText(description)
                            event.type = convertPositionToType(position)
                            binding.etDialogCost.setText(cost.toString())
                        }
                    }
                    override fun onNothingSelected(parent: AdapterView<*>?) {
                    }
                }
            }
            TYPE_TRIP, TYPE_OTHER-> {
                Log.d("mtag", "dialog type = TRIP")

                binding.textSet.visibility = View.GONE
                binding.spinnerInspectionType.visibility = View.VISIBLE
                binding.textDialogTitle.text = resources.getString(R.string.text_trip)

                val itemNames = requireContext().resources.getStringArray(R.array.spending_names)
                createSpinner(itemNames)
                val inputPosition = convertTypeToPosition(event.type)
                mSpinner?.setSelection(inputPosition)

                mSpinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {

                        val title = when (position){
                            0 -> resources.getString(R.string.text_trip)
                            1 -> resources.getString(R.string.text_other_expense)
                            else -> ""
                        }
                        if (event.type != convertPositionToTypeSpending(position)){
                            binding.textDialogTitle.text = title
                            binding.etDialogCost.setText("0")
                        }
                    }
                    override fun onNothingSelected(p0: AdapterView<*>?) {
                    }
                }
            }
            TYPE_BONUS -> {
                Log.d("mtag", "dialog type = BONUS")
                binding.textDialogTitle.text = resources.getString(R.string.text_bonus_title)
                binding.spinnerInspectionType.visibility = View.GONE
                binding.textSet.visibility = View.VISIBLE
            }
        }

        dialog.setOnShowListener {
            dialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener {

                val cost = binding.etDialogCost.text.toString().toIntOrNull()
                val description = binding.etDialogDescription.text.toString()

                addTextChangeListener()
                var isCorrectData = true

                when(event.type){
                    TYPE_INSPECTION -> {
                        if (cost == null || cost == 0){
                            binding.etDialogCost.setText("0")
                            binding.etDialogCost.setTextColor(resources.getColor(R.color.red_300))
                            isCorrectData = false
                        }
                    }
                    TYPE_INSPECTION_CAR_DEALERSHIP, TYPE_INSPECTION_CAR_PARK, TYPE_INSPECTION_CONST_PROGRESS, TYPE_INSPECTION_OTHER, TYPE_TRIP, TYPE_OTHER, TYPE_BONUS -> {
                        if (cost == null || cost == 0){
                            binding.etDialogCost.setText("0")
                            binding.etDialogCost.setTextColor(resources.getColor(R.color.red_300))
                            isCorrectData = false
                        }
                        if (description.isBlank()){
                            binding.layEtDialogDescription.error = resources.getString(R.string.text_need_add_description)
                            isCorrectData = false
                        }
                    }
                }
                if (!isCorrectData) return@setOnClickListener
                event.cost = cost!!
                event.description = description

                val bundleRequest = Bundle()
                bundleRequest.putSerializable(BUNDLE_EVENT_KEY, event)
                bundleRequest.putBoolean(BUNDLE_TYPE_KEY, isNew)
                parentFragmentManager.setFragmentResult(DIALOG_REQUEST_KEY, bundleRequest)
                dialog.dismiss()
            }
        }
        return dialog
    }

    private fun convertTypeToPosition(type: Int): Int {
        val p = when(type){
            TYPE_INSPECTION, TYPE_TRIP -> 0
            TYPE_INSPECTION_CAR_DEALERSHIP, TYPE_OTHER -> 1
            TYPE_INSPECTION_CAR_PARK -> 2
            TYPE_INSPECTION_CONST_PROGRESS -> 3
            TYPE_INSPECTION_OTHER -> 4
            else -> {0}
        }
        return p
    }
    private fun convertPositionToType(position: Int): Int{
        val p = when(position){
            0 -> TYPE_INSPECTION
            1 -> TYPE_INSPECTION_CAR_DEALERSHIP
            2 -> TYPE_INSPECTION_CAR_PARK
            3 -> TYPE_INSPECTION_CONST_PROGRESS
            4 -> TYPE_INSPECTION_OTHER
            else -> {0}
        }
        return p
    }
    private fun convertPositionToTypeSpending(position: Int): Int{
        val p = when(position){
            0 -> TYPE_TRIP
            1 -> TYPE_OTHER
            else -> {0}
        }
        return p
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        Log.d("TAG", "DialogAdd - onDestroy")
    }

    private fun createSpinner(names: Array<String>){

        Log.d("mtag", "createSpinner")
        //val textArray = resources.getStringArray(R.array.inspection_type)
        mSpinner = binding.spinnerInspectionType
        val spinnerAdapter = ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_item, names)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        mSpinner!!.adapter = spinnerAdapter
    }


    private fun addTextChangeListener() {
        Log.d("mtag", "addTextChangeListener")
        binding.etDialogDescription.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                binding.etDialogCost.setTextColor(resources.getColor(R.color.black))
                if (!p0.isNullOrBlank()) binding.layEtDialogDescription.error = null
            }
            override fun afterTextChanged(p0: Editable?) { }
        })
        binding.etDialogCost.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.etDialogCost.setTextColor(resources.getColor(R.color.black))
                Log.d("mtag", ">>>  etDialogCost onTextChanged")
            }
            override fun afterTextChanged(s: Editable?) { }
        })
    }


    companion object {
        private const val DIALOG_ADD_TAG = "add_dialog"
        const val BUNDLE_EVENT_KEY = "bundle_event_key"
        const val BUNDLE_TYPE_KEY = "bundle_status_key"
        const val DIALOG_REQUEST_KEY = "add_request-key"

        fun show(fManager: FragmentManager, event: Event, isNew: Boolean) {
            val dialogFragment = DialogAdd()
            val bundle = Bundle()
            bundle.putSerializable(BUNDLE_EVENT_KEY, event)
            bundle.putBoolean(BUNDLE_TYPE_KEY, isNew)
            dialogFragment.arguments = bundle
            dialogFragment.show(fManager, DIALOG_ADD_TAG)
        }

        fun setupListener(
            fManager: FragmentManager,
            lcOwner: LifecycleOwner,
            listener: (Event, Boolean) -> Unit
        ) {
            Log.d("TAG", "DialogAdd - setupListener")
            fManager.setFragmentResultListener(DIALOG_REQUEST_KEY, lcOwner) { _, result ->
                val event = result.getSerializable(BUNDLE_EVENT_KEY) as Event
                val isNew = result.getBoolean(BUNDLE_TYPE_KEY)
                listener.invoke(event, isNew)
            }
        }
    }
}