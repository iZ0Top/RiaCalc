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

        Log.d("mtag", "----input Event----\nid=${event.id} \ntype=${event.type } \ncost=${event.cost} \ndescription=${event.description} \ndate=${event.date} \n----")

        modifyView(event)

        val dialog = AlertDialog.Builder(requireContext())
            .setView(binding.root)
            .setCancelable(true)
            .setPositiveButton(R.string.text_btn_ok, null)
            .setNegativeButton(R.string.text_btn_cancel, null)
            .create()

        Log.d("mtag", "dialog created")

        when (event.type){
            TYPE_INSPECTION, TYPE_INSPECTION_CAR_DEALERSHIP, TYPE_INSPECTION_CAR_PARK, TYPE_INSPECTION_CONST_PROGRESS, TYPE_INSPECTION_OTHER -> {

                createSpinner()
                val position = convertTypeToPosition(event.type)
                mSpinner?.setSelection(position)

                mSpinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {
                        Log.d("mtag", "mSpinner onItemSelectedListener")
                        val cost = when (position){
                            0 -> AppPreferences.getReviewDefaultCost()
                            1 -> AppPreferences.getReviewCarDealershipCost()
                            2 -> AppPreferences.getReviewCarParkDefaultCost()
                            3 -> AppPreferences.getReviewConstProgress()
                            else -> {0}
                        }
                        Log.d("mtag", "mSpinner onItemSelectedListener - position=$position, cost=$cost")

                        binding.etDialogCost.setText(cost.toString())
                        event.type = convertPositionToType(position)
                    }
                    override fun onNothingSelected(parent: AdapterView<*>?) {
                    }
                }
            }
        }

        dialog.setOnShowListener {
            dialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener {

                Log.d("mtag", "dialog.setOnShowListener")

                //Натиснення кнопки ОК, перевірка данних і запис події
                //Якшо тип перевірка, то перевірити чи значення вартості не порожне
                //Якщо тип поїздка або інші витрати то перевірити чи є опис

                val cost = binding.etDialogCost.text.toString().toIntOrNull()
                val description = binding.etDialogDescription.text.toString()

                if (cost == null || cost == 0) {
                    Log.d("mtag", "dialog.setOnShowListener: cost == null || cost == 0")
                    return@setOnClickListener
                }

                if (event.type == TYPE_OTHER || event.type == TYPE_INSPECTION){
                    Log.d("mtag", "dialog.setOnShowListener: event.type == TYPE_OTHER || event.type == TYPE_INSPECTION")
                    addTextChangeListener()
                    if (description.isEmpty()){
                        binding.layEtDialogDescription.error = resources.getString(R.string.text_need_add_description)
                        return@setOnClickListener
                    }
                }
                event.cost = cost
                event.description = description
                Log.d("mtag", "----output Event----\nid=${event.id} \ntype=${event.type } \ncost=${event.cost} \ndescription=${event.description} \ndate=${event.date} \n----")

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
            TYPE_INSPECTION -> 0
            TYPE_INSPECTION_CAR_DEALERSHIP -> 1
            TYPE_INSPECTION_CAR_PARK -> 2
            TYPE_INSPECTION_CONST_PROGRESS -> 3
            TYPE_INSPECTION_OTHER -> 4
            else -> {0}
        }
        Log.d("mtag", "getCurrentPosition, position from input event=$p")
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


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        Log.d("TAG", "DialogAdd - onDestroy")
    }

    private fun modifyView (event: Event) {
        Log.d("mtag", "modifyView")

        binding.etDialogDescription.setText(event.description)
        binding.etDialogCost.setText(event.cost.toString())

        when (event.type) {TYPE_INSPECTION, TYPE_INSPECTION_CAR_DEALERSHIP, TYPE_INSPECTION_CAR_PARK, TYPE_INSPECTION_CONST_PROGRESS, TYPE_INSPECTION_OTHER -> {
                Log.d("mtag", "modifyView - TYPE INSPECTION")
                binding.textSet.visibility = View.GONE
                binding.spinnerInspectionType.visibility = View.VISIBLE
            }
            TYPE_TRIP -> {
                Log.d("mtag", "modifyView - TYPE TRIP")
                binding.spinnerInspectionType.visibility = View.GONE
                binding.textSet.visibility = View.VISIBLE
                binding.textSet.text = resources.getString(R.string.text_cost)
                binding.textDialogTitle.text = resources.getString(R.string.text_trip)
            }
            TYPE_OTHER -> {
                Log.d("mtag", "modifyView - TYPE OTHER")
                binding.spinnerInspectionType.visibility = View.GONE
                binding.textSet.visibility = View.VISIBLE
                binding.textSet.text = resources.getString(R.string.text_cost)
                binding.textDialogTitle.text = resources.getString(R.string.text_other_expense)
            }
        }
    }

    private fun createSpinner(){

        Log.d("mtag", "createSpinner")
        val textArray = resources.getStringArray(R.array.inspection_type)
        mSpinner = binding.spinnerInspectionType
        val spinnerAdapter = ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_item, textArray)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        mSpinner!!.adapter = spinnerAdapter
    }


    private fun addTextChangeListener() {
        binding.etDialogDescription.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (!p0.isNullOrBlank()) binding.layEtDialogDescription.error = null
            }
            override fun afterTextChanged(p0: Editable?) {}
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