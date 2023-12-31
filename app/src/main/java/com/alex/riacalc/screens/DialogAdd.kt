package com.alex.riacalc.screens

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentResultListener
import androidx.lifecycle.LifecycleOwner
import com.alex.riacalc.R
import com.alex.riacalc.databinding.DialogAddBinding
import com.alex.riacalc.model.Event
import com.alex.riacalc.utils.AppPreferences
import com.alex.riacalc.utils.TYPE_INSPECTION
import com.alex.riacalc.utils.TYPE_INSPECTION_CAR_DEALERSHIP
import com.alex.riacalc.utils.TYPE_OTHER
import com.alex.riacalc.utils.TYPE_TRIP

class DialogAdd : DialogFragment() {

    private var _binding: DialogAddBinding? = null
    private val binding get() = _binding!!

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        Log.d("TAG", "DialogAdd - onCreateDialog")

        val inflater = requireActivity().layoutInflater
        _binding = DialogAddBinding.inflate(inflater)

        val event = arguments?.getSerializable(BUNDLE_EVENT_KEY) as Event
        val isNew = arguments?.getBoolean(BUNDLE_TYPE_KEY) as Boolean

        changeView(event.type)

        if (!isNew) {
            binding.etDialogDescription.setText(event.description)

            if (event.type == TYPE_INSPECTION_CAR_DEALERSHIP) {
                binding.checkboxCarDealership.isChecked = true
            }

            if (event.type == TYPE_OTHER || event.type == TYPE_TRIP) {
                binding.etDialogPrice.setText(event.cost.toString())
            }
        }

        val dialog = AlertDialog.Builder(requireContext())
            .setView(binding.root)
            .setCancelable(true)
            .setPositiveButton(R.string.text_btn_ok, null)
            .setNegativeButton(R.string.text_btn_cancel, null)
            .create()

        dialog.setOnShowListener {
            dialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener {

                when (event.type) {
                    TYPE_INSPECTION,
                    TYPE_INSPECTION_CAR_DEALERSHIP -> {
                        event.description = binding.etDialogDescription.text.toString()

                        if (binding.checkboxCarDealership.isChecked) {
                            event.cost = AppPreferences.getReviewCarDealershipCost()
                            event.type = TYPE_INSPECTION_CAR_DEALERSHIP
                        } else {
                            event.cost = AppPreferences.getReviewDefaultCost()
                            event.type = TYPE_INSPECTION
                        }
                    }
                    TYPE_TRIP,
                    TYPE_OTHER -> {
                        val description = binding.etDialogDescription.text.toString()
                        val cost = binding.etDialogPrice.text.toString().toIntOrNull()

                        addTextChangeListener()

                        if (description.isEmpty()) {
                            binding.layEtDialogDescription.error =
                                resources.getString(R.string.text_need_add_description)
                            return@setOnClickListener
                        }

                        if (cost == null || cost == 0) {
                            binding.layEtDialogPrice.error = " "
                            return@setOnClickListener
                        }
                        event.description = description
                        event.cost = cost
                    }
                }
                val bundleRequest = Bundle()
                bundleRequest.putSerializable(BUNDLE_EVENT_KEY, event)
                bundleRequest.putBoolean(BUNDLE_TYPE_KEY, isNew)
                parentFragmentManager.setFragmentResult(DIALOG_REQUEST_KEY, bundleRequest)
                dialog.dismiss()
            }
        }
        return dialog
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        Log.d("TAG", "DialogAdd - onDestroy")
    }

    private fun changeView(type: Int) {

        when (type) {
            TYPE_INSPECTION,
            TYPE_INSPECTION_CAR_DEALERSHIP -> {
                binding.checkboxCarDealership.visibility = View.VISIBLE
                binding.layEtDialogPrice.visibility = View.GONE
                binding.textDialogPrice.visibility = View.GONE
                binding.textDialogCurrency.visibility = View.GONE
            }

            TYPE_TRIP -> {
                binding.textDialogTitle.text = resources.getString(R.string.text_trip)
                binding.textDialogPrice.text = resources.getString(R.string.text_cost)
            }

            TYPE_OTHER -> {
                binding.textDialogTitle.text = resources.getString(R.string.text_other_expense)
                binding.textDialogPrice.text = resources.getString(R.string.text_sum)
            }
        }
    }

    private fun addTextChangeListener() {
        binding.etDialogDescription.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (!p0.isNullOrBlank()) binding.layEtDialogDescription.error = null
            }
            override fun afterTextChanged(p0: Editable?) {}
        })
        binding.etDialogPrice.addTextChangedListener { object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (!p0.isNullOrBlank()) binding.layEtDialogPrice.error = null
            }
            override fun afterTextChanged(p0: Editable?) {}
        } }
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