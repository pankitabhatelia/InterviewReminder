package viewmodel

import android.app.*
import android.os.Build
import android.util.Log
import android.view.View
import android.widget.AdapterView
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import model.AddInterviewModel
import java.text.SimpleDateFormat
import java.util.*

class AddInterviewViewModel(application: Application) : AndroidViewModel(application) {
    val candidateName: MutableLiveData<String?> = MutableLiveData()
    val experience: MutableLiveData<String?> = MutableLiveData()
    val technology: MutableLiveData<String?> = MutableLiveData()
    val interviewDate: MutableLiveData<String?> = MutableLiveData()
    val interviewTime: MutableLiveData<String?> = MutableLiveData()
    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    private var firebaseUser: FirebaseUser? = auth.currentUser
    private lateinit var department: String
    private lateinit var interviewerName: String
    private val spinnerDepartmentList = ArrayList<String>()
    private val spinnerInterviewerList = ArrayList<String>()
    val remarks: MutableLiveData<String?> = MutableLiveData()
    private val _toastMessage: MutableLiveData<String> = MutableLiveData()
    val toastMessage: LiveData<String> = _toastMessage
    private val _departmentLivedata: MutableLiveData<ArrayList<String>> = MutableLiveData()
    val departmentLivedata: LiveData<ArrayList<String>> = _departmentLivedata
    private val _interviewerLivedata: MutableLiveData<ArrayList<String>> = MutableLiveData()
    val interviewerLiveData: LiveData<ArrayList<String>> = _interviewerLivedata
    private val _navigateToListScreen: MutableLiveData<Unit> = MutableLiveData()
    val navigateToListScreen: LiveData<Unit> = _navigateToListScreen
    val nameError: MutableLiveData<String?> = MutableLiveData()
    val experienceError: MutableLiveData<String?> = MutableLiveData()
    val technologyError: MutableLiveData<String?> = MutableLiveData()
    val interviewDateError: MutableLiveData<String?> = MutableLiveData()
    val interviewerTimeError: MutableLiveData<String?> = MutableLiveData()
    val remarksError: MutableLiveData<String?> = MutableLiveData()
    private val _showProgress: MutableLiveData<Boolean> = MutableLiveData()
    val showProgress: LiveData<Boolean> = _showProgress
    private val cal = Calendar.getInstance()
    private lateinit var documentReference: DocumentReference


    fun addOnClick() {
        if (isValid()) {
            addData()
        }

    }

    private fun isValid(): Boolean {
        nameError.postValue(null)
        experienceError.postValue(null)
        technologyError.postValue(null)
        interviewDateError.postValue(null)
        interviewDateError.postValue(null)
        interviewerTimeError.postValue(null)
        remarksError.postValue(null)

        when {
            candidateName.value.isNullOrEmpty() -> {
                nameError.postValue("Please Enter CandidateName")
            }
            experience.value.isNullOrEmpty() -> {
                experienceError.postValue("Please Enter Experience")
            }
            technology.value.isNullOrEmpty() -> {
                technologyError.postValue("Please Enter Technology")
            }
            interviewDate.value.isNullOrEmpty() -> {
                interviewDateError.postValue("Please Enter Date")
            }
            interviewTime.value.isNullOrEmpty() -> {
                interviewerTimeError.postValue("Please Enter Time")
            }
            remarks.value.isNullOrEmpty() -> {
                remarksError.postValue("Please Enter Remarks")
            }
            else -> {
                return true
            }
        }
        return false
    }

    fun departmentOnItemSelected(
        parent: AdapterView<*>?,
        view: View?,
        position: Int,
        id: Long
    ) {
        department = parent?.selectedItem.toString()
        Log.d("TAG", department)
        getDataOnInterviewerNameSpinner(department)
        spinnerInterviewerList.clear()

    }

    fun interviewerOnItemSelected(
        parent: AdapterView<*>?,
        view: View?,
        position: Int,
        id: Long
    ) {
        interviewerName = parent?.selectedItem.toString()
        Log.d("TAG", interviewerName)

    }

    private fun addData() {
        val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
        _showProgress.value = true
        documentReference = firestore.collection("AddInterview").document()
        val addInterviewData = AddInterviewModel(
            candidateName.value?.toString(),
            experience.value?.toString(),
            technology.value?.toString(),
            interviewDate.value?.toString(),
            interviewTime.value?.toString(),
            department,
            firebaseUser?.uid,
            interviewerName,
            remarks.value?.toString(),
            0,
            documentReference.id

        )
        firestore.collection("AddInterview").add(addInterviewData)
            .addOnSuccessListener {
                _navigateToListScreen.postValue(Unit)
                _showProgress.value = false

                _toastMessage.value = "Data is inserted successfully"
            }.addOnFailureListener {
                _toastMessage.value = "Data is Failed to insert!!"
            }
    }

    fun getDataOnDepartmentSpinner() {
        spinnerDepartmentList.add(0, "Select Department")
        val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
        firestore.collection("Department")
            .get()
            .addOnSuccessListener {
                val documents = it?.documents
                documents?.forEach { it1 ->
                    department = it1.data?.get("departmentName").toString()
                    spinnerDepartmentList.add(department)
                }
                _departmentLivedata.postValue(spinnerDepartmentList)
            }
    }

    private fun getDataOnInterviewerNameSpinner(departmentName: String) {
        val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
        firestore.collection("InterviewerName").whereEqualTo("departmentName", departmentName)
            .get()
            .addOnSuccessListener {
                val documents = it?.documents
                documents?.forEach { it1 ->
                    interviewerName = it1.data?.get("interviewerName").toString()
                    spinnerInterviewerList.add(interviewerName)
                }
                _interviewerLivedata.postValue(spinnerInterviewerList)
            }
    }

    fun showDate(view: View) {
        val context = view.context
        val year = cal.get(Calendar.YEAR)
        val month = cal.get(Calendar.MONTH)
        val day = cal.get(Calendar.DAY_OF_MONTH)
        val datePickerDialog = DatePickerDialog(
            context,
            { _, myear, mmonth, mdayOfMonth ->
                val date = "$mdayOfMonth/${mmonth + 1}/$myear"
                interviewDate.value = date
            }, year, month, day
        )
        datePickerDialog.show()
        cal.set(year, month, day)
        datePickerDialog.datePicker.minDate = cal.timeInMillis
    }

    fun showTime(view: View) {
        val context = view.context
        var amPm = ""
        val hour = cal.get(Calendar.HOUR_OF_DAY)
        val minute = cal.get(Calendar.MINUTE)
        val timePicker = TimePickerDialog(
            context,
            { _, hourOfDay, minuteOfDay ->
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
                if (getCurrentTime()?.let { compareTwoTimes(it, time) } == true) {
                    interviewTime.value = time
                } else {
                    _toastMessage.value = "Can not use past time!!"
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
        return !time2.before(time1)
    }


}