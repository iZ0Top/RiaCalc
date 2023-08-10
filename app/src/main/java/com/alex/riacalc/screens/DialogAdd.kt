package com.alex.riacalc.screens

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentResultListener
import androidx.lifecycle.LifecycleOwner
import com.alex.riacalc.R
import com.alex.riacalc.databinding.DialogAddBinding
import com.alex.riacalc.model.Event
import java.util.Calendar

class DialogAdd: DialogFragment() {

    private var _binding: DialogAddBinding? = null
    private val binding get() = _binding!!
    private val event: Event
        get() = requireArguments().getSerializable(BUNDLE_KEY) as Event

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        Log.d("TAG", "DialogAdd - onCreateDialog")

        val inflater = requireActivity().layoutInflater
        _binding = DialogAddBinding.inflate(inflater)

        changeView(event.type)

        val dialog = AlertDialog.Builder(requireContext())
            .setView(binding.root)
            .setCancelable(true)
            .setPositiveButton("OK", null)
            .create()

        dialog.setOnShowListener {
            dialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener {
                val description = binding.etDialogDescription.text.toString()
                val cost = binding.etDialogPrice.text.toString().toIntOrNull()

                if (cost == 0 || cost == null){
                    binding.etDialogPrice.text = null
                    binding.etDialogPrice.hint = "0"
                    binding.etDialogPrice.setHintTextColor(resources.getColor(R.color.red_500))
                    return@setOnClickListener
                }

                event.description = description
                event.cost = cost
                event.date = Calendar.getInstance().toString()

                parentFragmentManager.setFragmentResult(DIALOG_REQUEST_KEY, bundleOf(BUNDLE_KEY to event))
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

    private fun changeView(type: Int){
        when(type){
            TYPE_INSPECTION -> { return }
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



    companion object{

        const val TYPE_INSPECTION = 0
        const val TYPE_TRIP = 1
        const val TYPE_OTHER = 2

        const val DIALOG_TAG = "add_dialog"
        const val BUNDLE_KEY = "bundle_key"
        const val ARG_KEY = "arguments_key"
        const val DIALOG_REQUEST_KEY = "add_request-key"

        fun show(fManager: FragmentManager, event: Event){                                          //публічний метод з конструктором (фрагментменеджер та аргументи)
            val dialogFragment = DialogAdd()                                                        //створюємо екземляр діалога
            dialogFragment.arguments = bundleOf(BUNDLE_KEY to event )                         //передаемо аргументи в діалог через бандл, бандл створюємо тут же
            dialogFragment.show(fManager, DIALOG_TAG)                                               //показ діалога (фрагмент менеджер і тег діалога)
        }

        fun setupListener(fManager: FragmentManager, lcOwner: LifecycleOwner, listener: (Event) -> Unit){
            fManager.setFragmentResultListener(DIALOG_REQUEST_KEY, lcOwner, FragmentResultListener { _, result ->
                listener.invoke(result.getSerializable(BUNDLE_KEY) as Event)
            })
        }
    }
}