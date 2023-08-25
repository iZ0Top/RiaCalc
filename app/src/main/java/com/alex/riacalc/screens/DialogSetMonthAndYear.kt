package com.alex.riacalc.screens

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.widget.NumberPicker
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentResultListener
import androidx.lifecycle.LifecycleOwner
import com.alex.riacalc.R
import com.alex.riacalc.databinding.DialogMonthAndYearBinding
import java.util.Calendar

class DialogSetMonthAndYear : DialogFragment() {

    private var _binding: DialogMonthAndYearBinding? = null
    val binding get() = _binding!!


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val inflater = requireActivity().layoutInflater
        _binding = DialogMonthAndYearBinding.inflate(inflater)

        val calendar = Calendar.getInstance()

        val monthNames = resources.getStringArray(R.array.month_name_for_picker)

        var month = arguments?.getInt(BUNDLE_KEY_MONTH) ?: (calendar.get(Calendar.MONTH))
        var year = arguments?.getInt(BUNDLE_KEY_YEAR) ?: calendar.get(Calendar.YEAR)


        with(binding.pickerMonth) {
            minValue = 0
            maxValue = monthNames.size - 1
            displayedValues = monthNames
            value = month
            wrapSelectorWheel = false
            setOnValueChangedListener(NumberPicker.OnValueChangeListener { _, old, new ->
                month = new
            })
        }

        with(binding.pickerYear) {
            minValue = 2000
            maxValue = Calendar.getInstance().get(Calendar.YEAR)
            value = year
            wrapSelectorWheel = false
            setOnValueChangedListener(NumberPicker.OnValueChangeListener { _, old, new ->
                year = new
            })
        }

        val dialog = AlertDialog.Builder(requireContext())
            .setView(binding.root)
            .setCancelable(true)
            .setPositiveButton("ok", DialogInterface.OnClickListener { _, _ ->
                val bundleRequest = Bundle()
                bundleRequest.putInt(BUNDLE_KEY_MONTH, month)
                bundleRequest.putInt(BUNDLE_KEY_YEAR, year)
                parentFragmentManager.setFragmentResult(REQUEST_KEY_DIALOG_MONTH_AND_YEAR, bundleRequest)
            })
        return dialog.create()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


    companion object {

        const val BUNDLE_KEY_MONTH = "bundle_key_month"
        const val BUNDLE_KEY_YEAR = "bundle_key_year"
        const val TAG_DIALOG_MONTH_AND_YEAR = "tag_dialog_month_and_year"
        const val REQUEST_KEY_DIALOG_MONTH_AND_YEAR = "request_key_dialog_month_and_year"

        fun show(fManager: FragmentManager, month: Int, year: Int) {
            val dialog = DialogSetMonthAndYear()
            val bundle = Bundle()
            bundle.putInt(BUNDLE_KEY_MONTH, month)
            bundle.putInt(BUNDLE_KEY_YEAR, year)
            dialog.arguments = bundle
            dialog.show(fManager, TAG_DIALOG_MONTH_AND_YEAR)
        }

        fun setupListener(fManager: FragmentManager, lcOwner: LifecycleOwner, listener: (Int, Int) -> Unit) {
            fManager.setFragmentResultListener(REQUEST_KEY_DIALOG_MONTH_AND_YEAR, lcOwner, FragmentResultListener { requestKey, result ->
                val month = result.getInt(BUNDLE_KEY_MONTH)
                val year = result.getInt(BUNDLE_KEY_YEAR)
                listener.invoke(month, year)
            })
        }
    }
}