package fragment

import activitiy.DashBoardActivity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.interviewreminderapp.databinding.FragmentAddBinding
import viewmodel.AddInterviewViewModel
import java.text.SimpleDateFormat
import java.util.*


class AddFragment : Fragment() {
    private lateinit var binding: FragmentAddBinding
    private lateinit var viewModel: AddInterviewViewModel
    private val cal = Calendar.getInstance()
    private lateinit var datePickerDialog: DatePickerDialog
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddBinding.inflate(layoutInflater)
        binding.lifecycleOwner = this
        viewModel = ViewModelProvider(this)[AddInterviewViewModel::class.java]
        binding.addInterview = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val activity = activity as AppCompatActivity?
        activity!!.setSupportActionBar(binding.appBar)
        activity.supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        activity.supportActionBar!!.setHomeButtonEnabled(true)
        binding.appBar.setNavigationOnClickListener {
            val intent = Intent(requireContext(), DashBoardActivity::class.java)
            startActivity(intent)
        }
        viewModel.getDataOnDepartmentSpinner()


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
        viewModel.toastMessage.observe(viewLifecycleOwner) {
            it?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT)
                    .show()
            }
        }
        viewModel.navigateToListScreen.observe(viewLifecycleOwner) {
            val intent = Intent(requireContext(), DashBoardActivity::class.java)
            startActivity(intent)
        }
        viewModel.departmentLivedata.observe(viewLifecycleOwner) {
            val arrayAdapter =
                ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, it)
            binding.spinnerDepartment.adapter = arrayAdapter
            arrayAdapter.setDropDownViewResource(android.R.layout.simple_list_item_1)
        }
        viewModel.interviewerLiveData.observe(viewLifecycleOwner) {
            val adapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_item,
                it
            )
            binding.spinnerName.adapter = adapter
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }

    }

    private fun showDate() {

        val year = cal.get(Calendar.YEAR)
        val month = cal.get(Calendar.MONTH)
        val day = cal.get(Calendar.DAY_OF_MONTH)
        datePickerDialog =
            DatePickerDialog(requireContext(), dateSelectedListener, year, month, day)
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
            requireContext(),
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
                    Toast.makeText(requireContext(), "Can not use past time!!", Toast.LENGTH_SHORT)
                        .show()
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