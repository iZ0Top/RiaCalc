package com.alex.riacalc.screens

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.DialogFragment
import com.alex.riacalc.R
import com.alex.riacalc.databinding.DialogAddBinding

class DialogAdd: DialogFragment() {

    private var _binding: DialogAddBinding? = null
    private val binding get() = _binding!!

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val inflater = requireActivity().layoutInflater
        _binding = DialogAddBinding.inflate(inflater)

        val dialog = AlertDialog.Builder(requireActivity())
        dialog.setView(binding.root)
            .setCancelable(true)
            .setPositiveButton("OK", null)
            .create()

        return dialog.show()
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        val text = binding.etDialogDescription.toString()
        if (text.isEmpty()){
            return
        }
        Log.d("TAG","onDismiss ")
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object{
        const val DIALOG_TAG = "add_dialog"
    }

}