package activitiy
import android.R
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.interviewreminderapp.databinding.ActivityAddReminderBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import viewmodel.AddInterviewViewModel
import java.util.*


class AddInterview : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
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
        auth = FirebaseAuth.getInstance()
        val year = cal.get(Calendar.YEAR)
        val month = cal.get(Calendar.MONTH)
        val day = cal.get(Calendar.DAY_OF_MONTH)
        datePickerDialog = DatePickerDialog(this, dateSelectedListener, year, month, day)
        cal.set(year, month, day)
        datePickerDialog.datePicker.minDate = cal.timeInMillis

        binding.etInterviewDate.setOnClickListener {
            datePickerDialog.show()
        }
        getDataOnDepartmentSpinner()
        getDataOnInterviewerNameSpinner()
        observer()
    }

    private fun getDataOnDepartmentSpinner() {
        val departmentList = ArrayList<String>()
        val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
        firestore.collection("Department")
            .get()
            .addOnSuccessListener {
                val department1 = it.documents[0].data?.get("departmentName").toString()
                val department2 = it.documents[1].data?.get("departmentName").toString()
                val department3 = it.documents[2].data?.get("departmentName").toString()
                val department4 = it.documents[3].data?.get("departmentName").toString()
                val department5 = it.documents[4].data?.get("departmentName").toString()
                val department6 = it.documents[5].data?.get("departmentName").toString()
                departmentList.add(department1)
                departmentList.add(department2)
                departmentList.add(department3)
                departmentList.add(department4)
                departmentList.add(department5)
                departmentList.add(department6)
                val adapter = ArrayAdapter(
                    this@AddInterview,
                    R.layout.simple_spinner_item,
                    departmentList
                )
                adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
                binding.spinnerDepartment.adapter = adapter
            }
    }

    private fun getDataOnInterviewerNameSpinner() {
        val interviewerList = ArrayList<String>()
        val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
        firestore.collection("InterviewerName")
            .get()
            .addOnSuccessListener {
                val interviewer1 = it.documents[0].data?.get("interviewerName").toString()
                val interviewer2 = it.documents[1].data?.get("interviewerName").toString()
                val interviewer3 = it.documents[2].data?.get("interviewerName").toString()
                val interviewer4 = it.documents[3].data?.get("interviewerName").toString()
                val interviewer5 = it.documents[4].data?.get("interviewerName").toString()
                val interviewer6 = it.documents[5].data?.get("interviewerName").toString()
                interviewerList.add(interviewer1)
                interviewerList.add(interviewer2)
                interviewerList.add(interviewer3)
                interviewerList.add(interviewer4)
                interviewerList.add(interviewer5)
                interviewerList.add(interviewer6)
                val adapter = ArrayAdapter(
                    this@AddInterview,
                    R.layout.simple_spinner_item,
                    interviewerList
                )
                adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
                binding.spinnerName.adapter = adapter
            }
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
