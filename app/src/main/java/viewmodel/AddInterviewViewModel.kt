package viewmodel

import android.util.Log
import android.view.View
import android.widget.AdapterView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import model.AddInterviewModel
import kotlin.collections.ArrayList

class AddInterviewViewModel : ViewModel() {
    val candidateName: MutableLiveData<String> = MutableLiveData()
    val experience: MutableLiveData<String> = MutableLiveData()
    val technology: MutableLiveData<String> = MutableLiveData()
    val interviewDate: MutableLiveData<String> = MutableLiveData()
    val interviewTime: MutableLiveData<String> = MutableLiveData()
    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    private var firebaseUser: FirebaseUser = auth.currentUser!!
    private lateinit var department: String
    lateinit var interviewerName: String
    private val spinnerDepartmentList = ArrayList<String>()
    private val spinnerInterviewerList = ArrayList<String>()
    val remarks: MutableLiveData<String> = MutableLiveData()
    private val _toastMessage: MutableLiveData<String> = MutableLiveData()
    val toastMessage: LiveData<String> = _toastMessage
    private val _departmentLivedata: MutableLiveData<ArrayList<String>> = MutableLiveData()
    val departmentLivedata: LiveData<ArrayList<String>> = _departmentLivedata
    private val _interviewerLivedata: MutableLiveData<ArrayList<String>> = MutableLiveData()
    val interviewerLiveData: LiveData<ArrayList<String>> = _interviewerLivedata
    private val _getAllInterviewInfo: MutableLiveData<List<AddInterviewModel>?> = MutableLiveData()
    val getAllInterviewInfo: LiveData<List<AddInterviewModel>?> = _getAllInterviewInfo
    private val _navigateToListScreen: MutableLiveData<Unit> = MutableLiveData()
    val navigateToListScreen: LiveData<Unit> = _navigateToListScreen
    var interviewList = arrayListOf<AddInterviewModel>()
    val nameError: MutableLiveData<String?> = MutableLiveData()
    val experienceError: MutableLiveData<String?> = MutableLiveData()
    val technologyError: MutableLiveData<String?> = MutableLiveData()
    val interviewDateError: MutableLiveData<String?> = MutableLiveData()
    val interviewerTimeError: MutableLiveData<String?> = MutableLiveData()
    val remarksError: MutableLiveData<String?> = MutableLiveData()


    fun floatingAddOnClick() {
        _navigateToListScreen.postValue(Unit)
    }

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
        val firestore = FirebaseFirestore.getInstance()
        val addInterviewData = AddInterviewModel(
            candidateName.value?.toString(),
            experience.value?.toString(),
            technology.value?.toString(),
            interviewDate.value?.toString(),
            interviewTime.value?.toString(),
            department,
            firebaseUser.uid,
            interviewerName,
            remarks.value?.toString()
        )
        _getAllInterviewInfo.postValue(listOf(addInterviewData))
        firestore.collection("AddInterview").add(addInterviewData)
            .addOnSuccessListener {
                _navigateToListScreen.postValue(Unit)
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
                it.documents.forEach {
                    department = it.data?.get("departmentName").toString()
                    spinnerDepartmentList.add(department)
                }
                _departmentLivedata.postValue(spinnerDepartmentList)
            }
    }

    fun getDataOnInterviewerNameSpinner(departmentName: String) {
        val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
        firestore.collection("InterviewerName").whereEqualTo("departmentName", departmentName)
            .get()
            .addOnSuccessListener {
                it.documents.forEach {
                    interviewerName = it.data?.get("interviewerName").toString()
                    spinnerInterviewerList.add(interviewerName)
                }
                _interviewerLivedata.postValue(spinnerInterviewerList)
            }
    }

   fun showData() {
        val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
        firestore.collection("AddInterview").whereEqualTo("interviewerId", firebaseUser.uid)
            .get()
            .addOnSuccessListener {
                for (data in it.documents) {
                    val user: AddInterviewModel? = data.toObject(AddInterviewModel::class.java)
                    interviewList.add(user!!)

                }
                _getAllInterviewInfo.postValue(interviewList)
            }
    }
}