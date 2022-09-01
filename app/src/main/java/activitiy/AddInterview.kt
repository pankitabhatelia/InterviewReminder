package activitiy

import Interfaces.LoadFirebase
import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.interviewreminderapp.databinding.ActivityAddReminderBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import model.Department
import viewmodel.AddInterviewViewModel
import java.util.*
import kotlin.collections.ArrayList

class AddInterview : AppCompatActivity(), LoadFirebase {
    private lateinit var databaseReference: DatabaseReference
    private lateinit var firebaseUSer: FirebaseUser
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityAddReminderBinding
    private lateinit var viewModel: AddInterviewViewModel
    private lateinit var loadFirebase: LoadFirebase
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

        databaseReference = FirebaseDatabase.getInstance().getReference("Department")
        loadFirebase = this
        databaseReference.addValueEventListener(object : ValueEventListener {
            var departmentList: MutableList<Department> = ArrayList<Department>()
            override fun onDataChange(snapshot: DataSnapshot) {
                for (departmentSnapshot in snapshot.children) {
                    departmentList.add(departmentSnapshot.getValue<Department>(Department::class.java)!!)
                    loadFirebase.onFirebaseLoadSuccess(departmentList)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                loadFirebase.onFirebaseLoadFailed(error.message)
            }
        })
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

    override fun onFirebaseLoadSuccess(departmentList: List<Department>) {
        val departmentName = getdepartmentList(departmentList)
        val adapter =
            ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, departmentName)
        binding.spinnerDepartment.adapter = adapter
    }

    override fun onFirebaseLoadFailed(message: String) {

    }

    private fun getdepartmentList(departmentList: List<Department>): List<String> {
        val result = ArrayList<String>()
        for (department in departmentList) {
            result.add(department.departmentName!!)
        }
        return result
    }
  /*  fDatabaseRoot.child("areas").addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            for (DataSnapshot areaSnapshot: dataSnapshot.getChildren()) {
            String areaName = areaSnapshot.child("areaName").getValue(String.class);

            Spinner areaSpinner = (Spinner) findViewById(R.id.spinner);
            final String[] areas = {areaName};
            ArrayAdapter<String> areasAdapter = new ArrayAdapter<String>(UAdminActivity.this, android.R.layout.simple_spinner_item, areas);
            areasAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            areaSpinner.setAdapter(areasAdapter);
        }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    });*/

}
