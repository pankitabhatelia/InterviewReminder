package activitiy

import android.R
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.interviewreminderapp.databinding.ActivityAddReminderBinding
import viewmodel.AddInterviewViewModel
import java.text.SimpleDateFormat
import java.util.*


class AddInterview : AppCompatActivity() {
    private lateinit var binding: ActivityAddReminderBinding
    private lateinit var viewModel: AddInterviewViewModel
    private val cal = Calendar.getInstance()
    private lateinit var datePickerDialog: DatePickerDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddReminderBinding.inflate(layoutInflater)
        binding.lifecycleOwner = this
        viewModel = ViewModelProvider(this)[AddInterviewViewModel::class.java]
        binding.addInterview = viewModel
        setContentView(binding.root)
        viewModel.getDataOnDepartmentSpinner()
        viewModel.getDataOnInterviewerNameSpinner()
        binding.etInterviewDate.setOnClickListener {
            showDate()
        }
        binding.etInterviewTime.setOnClickListener {
            showTime()
        }
        observer()
    }


    private val dateSelectedListener =
        DatePickerDialog.OnDateSetListener { _, myear, mmonth, mdayOfMonth ->
            val date = "$mdayOfMonth/${mmonth + 1}/$myear"
            binding.etInterviewDate.setText(date)
        }

    private fun observer() {
        viewModel.toastMessage.observe(this) {
            it?.let {
                Toast.makeText(this, it, Toast.LENGTH_SHORT)
                    .show()
            }
        }
        viewModel.navigateToListScreen.observe(this) {
            val intent = Intent(this, HomeScreen::class.java)
            startActivity(intent)
            finish()
        }
        viewModel.departmentLivedata.observe(this) {
            val arrayAdapter =
                ArrayAdapter(this@AddInterview, R.layout.simple_spinner_dropdown_item, it)
            binding.spinnerDepartment.adapter = arrayAdapter
            arrayAdapter.setDropDownViewResource(R.layout.simple_list_item_1)
        }
        viewModel.interviewerLiveData.observe(this) {
            val adapter = ArrayAdapter(
                this@AddInterview,
                R.layout.simple_spinner_item,
                it
            )
            binding.spinnerName.adapter = adapter
            adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
        }

    }

    private fun showDate() {
        val year = cal.get(Calendar.YEAR)
        val month = cal.get(Calendar.MONTH)
        val day = cal.get(Calendar.DAY_OF_MONTH)
        datePickerDialog = DatePickerDialog(this, dateSelectedListener, year, month, day)
        cal.set(year, month, day)
        datePickerDialog.datePicker.minDate = cal.timeInMillis

        binding.etInterviewDate.setOnClickListener {
            datePickerDialog.show()
        }
    }

    private fun showTime() {
        var amPm = ""
        val hour = cal.get(Calendar.HOUR_OF_DAY)
        val minute = cal.get(Calendar.MINUTE)
        val timePicker = TimePickerDialog(
            this@AddInterview,
            { view, hourOfDay, minuteOfDay ->
                cal.set(Calendar.HOUR_OF_DAY, hourOfDay)
                cal.set(Calendar.MINUTE, minuteOfDay)
                if (cal.get(Calendar.AM_PM) == Calendar.AM) {
                    amPm = "AM"
                } else if (cal.get(Calendar.AM_PM) == Calendar.PM) {
                    amPm = "PM"
                }
                val hrs =
                    if (cal.get(Calendar.HOUR) == 0) "12" else cal.get(Calendar.HOUR).toString()
                val showHours =
                    if (cal.get(Calendar.HOUR) <= 9 && cal.get(Calendar.HOUR) != 0) "0$hrs" else hrs
                val showMinutes =
                    if (cal.get(Calendar.MINUTE) <= 9) "0${cal.get(Calendar.MINUTE)}"
                    else "${cal.get(Calendar.MINUTE)}"
                val time = "$showHours:$showMinutes $amPm"
                if (compareTwoTimes(getCurrentTime()!!, time)) {
                    binding.etInterviewTime.setText(time)
                } else {
                    Toast.makeText(this, "Can not use past time!!", Toast.LENGTH_SHORT).show()
                }
            }, hour, minute, false
        )
        timePicker.show()
    }

    private fun getCurrentTime(): String? {

        val simpleDateFormat = SimpleDateFormat("hh:mm a")
        return simpleDateFormat.format(Calendar.getInstance().time)

    }

    private fun compareTwoTimes(fromTime: String, currentTime: String): Boolean {
        val sdf = SimpleDateFormat("hh:mm a")
        val time1 = sdf.parse(fromTime)
        val time2 = sdf.parse(currentTime)
        return !time2!!.before(time1)
    }
}
