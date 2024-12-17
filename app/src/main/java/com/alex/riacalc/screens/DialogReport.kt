package com.alex.riacalc.screens

import android.app.Dialog
import android.content.ClipData
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.compose.ui.platform.ClipboardManager
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.alex.riacalc.R
import com.alex.riacalc.databinding.DialogReportBinding

class DialogReport: DialogFragment() {

    private var _binding: DialogReportBinding? = null
    private val binding get() = _binding!!

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val inflater = requireActivity().layoutInflater
        _binding = DialogReportBinding.inflate(inflater)

        val report = arguments?.getSerializable("bundle_report_key") as String
        Log.d("mtag", report)
        binding.dialogReportEt.setText(report)


        val dialog = AlertDialog.Builder(requireContext())
            .setView(binding.root)
            .setCancelable(false)
            .create()

//        dialog.window?.setBackgroundDrawable(ColorDrawable(android.graphics.Color.TRANSPARENT))
//        dialog.window?.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
//        dialog.window?.setDimAmount(0.6f)


        binding.dialogReportBtn.setOnClickListener {
            copyToBuffer()
            dialog.dismiss()
        }

        return dialog
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun copyToBuffer(){
        val text = binding.dialogReportEt.text
        val clipboardManager = requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager
        clipboardManager.setPrimaryClip(ClipData.newPlainText("label", text))

        val toast = Toast.makeText(requireContext(), resources.getText(R.string.text_report_copied), Toast.LENGTH_SHORT)
        toast.show()
    }

    companion object {
        private const val DIALOG_REPORT_TAG = "report_dialog"
        private const val BUNDLE_REPORT_KEY = "bundle_report_key"


        fun show(fManager: FragmentManager, report: String) {
            val dialogFragment = DialogReport()
            val bundle = Bundle()
            bundle.putSerializable(BUNDLE_REPORT_KEY,report)
            dialogFragment.arguments = bundle
            dialogFragment.show(fManager, DIALOG_REPORT_TAG)
        }
    }
}