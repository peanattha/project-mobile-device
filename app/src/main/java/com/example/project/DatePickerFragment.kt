package com.example.project

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.widget.DatePicker
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import java.text.SimpleDateFormat
import java.util.*

class DatePickerFragment: DialogFragment(), DatePickerDialog.OnDateSetListener {
    private lateinit var calendar: Calendar

    @SuppressLint("ResourceType")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        return DatePickerDialog(
            requireContext(),
            this,
            year,
            month,
            day
        )
    }

    override fun onDateSet(View: DatePicker?, year: Int, month: Int, day: Int) {
        var dateOfcalendar : TextView? = activity?.findViewById(R.id.btn_date)
        dateOfcalendar!!.text = formatDate(year,month,day)
    }

    override fun onCancel(dialog: DialogInterface) {
        Toast.makeText(activity, "Pls select a date.", Toast.LENGTH_LONG).show()
        super.onCancel(dialog)
    }
    private fun formatDate(year: Int, month: Int, day: Int):String{
        var calendar: Calendar = Calendar.getInstance();
        calendar.set(year,month,day)

        val myFormat = "yyyy-M-dd"
        val sdf = SimpleDateFormat(myFormat)
        val chosenDate = calendar.time

        val df = sdf.format(chosenDate)
        return df
    }
}