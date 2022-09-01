package activitiy

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.interviewreminderapp.databinding.ActivityAddReminderBinding
import viewmodel.AddInterviewViewModel
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
        val year = cal.get(Calendar.YEAR)
        val month = cal.get(Calendar.MONTH)
        val day = cal.get(Calendar.DAY_OF_MONTH)
        datePickerDialog = DatePickerDialog(this, dateSelectedListener, year, month, day)
        cal.set(year, month, day)
        datePickerDialog.datePicker.minDate = cal.timeInMillis

        binding.etInterviewDate.setOnClickListener {
            datePickerDialog.show()
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
            if (it == "Data is inserted successfully") {
                val intent = Intent(this, HomeScreen::class.java)
                startActivity(intent)
                finish()
            }
        }
    }

}