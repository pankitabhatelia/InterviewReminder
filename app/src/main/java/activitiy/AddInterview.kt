package activitiy

import Interfaces.LoadFirebase
import android.R
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.interviewreminderapp.databinding.ActivityAddReminderBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.firestore.FirebaseFirestore
import model.Department
import viewmodel.AddInterviewViewModel
import java.util.*


class AddInterview : AppCompatActivity() {
    private lateinit var firebaseUSer: FirebaseUser
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityAddReminderBinding
    private lateinit var viewModel: AddInterviewViewModel
    private val cal = Calendar.getInstance()
    private lateinit var datePickerDialog: DatePickerDialog
    private lateinit var databaseReference: DatabaseReference
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
        getData()
        observer()
    }

    private fun getData() {
        var departmentList = ArrayList<String>()
        lateinit var firestore: FirebaseFirestore
        firestore = FirebaseFirestore.getInstance()
        firestore.collection("Department")
            .get()
            .addOnSuccessListener {
                val department1 = it.documents.get(0).data?.get("departmentName").toString()
                val department2 = it.documents.get(1).data?.get("departmentName").toString()
                val department3 = it.documents.get(2).data?.get("departmentName").toString()
                val department4 = it.documents.get(3).data?.get("departmentName").toString()
                val department5 = it.documents.get(4).data?.get("departmentName").toString()
                val department6 = it.documents.get(5).data?.get("departmentName").toString()
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
