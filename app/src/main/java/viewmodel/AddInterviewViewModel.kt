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
import java.text.SimpleDateFormat
import java.util.*

class AddInterviewViewModel : ViewModel() {
    val candidateName: MutableLiveData<String> = MutableLiveData()
    val experience: MutableLiveData<String> = MutableLiveData()
    val technology: MutableLiveData<String> = MutableLiveData()
    val interviewDate: MutableLiveData<String> = MutableLiveData()
    val interviewTime: MutableLiveData<String> = MutableLiveData()
    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    private var firebaseUser: FirebaseUser = auth.currentUser!!
    private lateinit var department: String
    private lateinit var interviewerName: String
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
    private val _showProgress: MutableLiveData<Boolean> = MutableLiveData()
    val showProgress: LiveData<Boolean> = _showProgress
    private var interviewListOnDone = arrayListOf<AddInterviewModel>()
    private var interviewListOnCancelled = arrayListOf<AddInterviewModel>()
    private val formatter = SimpleDateFormat("dd/M/yyyy")
    private val date = Date()
    private val current = formatter.format(date)
    private val cal = Calendar.getInstance()
    val id: MutableLiveData<String> = MutableLiveData()
    private val time = Calendar.getInstance().time
    private val formatterTime = SimpleDateFormat("HH:mm")
    // private val currentTime = formatterTime.format(time)


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
        _showProgress.value = true
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
            remarks.value?.toString(),
            0,
            id.value.toString()
        )
        _getAllInterviewInfo.postValue(listOf(addInterviewData))
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

    fun showData() {
     /*   val preDay = System.currentTimeMillis() - 1000 * 60 * 60 * 24
        val prev = Date(preDay)*/
        val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
        firestore.collection("AddInterview").whereEqualTo("interviewerId", firebaseUser.uid)
            .whereEqualTo("status", 0)
            .get()
            .addOnSuccessListener {
                val documents = it?.documents
                documents?.forEach { it1 ->
                    val user: AddInterviewModel? = it1.toObject(AddInterviewModel::class.java)
                    id.postValue(it1.id)
                    interviewList.add(user!!)
                    if (user.interviewDate!! < current) {
                        firestore.collection("AddInterview").document(id.value.toString())
                            .update("status", 2)
                            .addOnSuccessListener {
                                _navigateToListScreen.postValue(Unit)
                            }
                    }
                }
                _getAllInterviewInfo.postValue(interviewList)
            }
    }

    fun getInterviewData(addInterviewModel: AddInterviewModel) {
        candidateName.value = addInterviewModel.candidateName!!
        experience.value = addInterviewModel.experience!!
        technology.value = addInterviewModel.technology!!
        interviewDate.value = addInterviewModel.interviewDate!!
        interviewTime.value = addInterviewModel.interviewTime!!
        remarks.value = addInterviewModel.remarks!!
    }

    fun getDoneInterviewData() {
        val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
        firestore.collection("AddInterview").whereEqualTo("interviewerId", firebaseUser.uid)
            .whereEqualTo("status", 1)
            .get()
            .addOnSuccessListener {
                val documents = it?.documents
                documents?.forEach { it1 ->
                    val user: AddInterviewModel? = it1.toObject(AddInterviewModel::class.java)
                    interviewListOnDone.add(user!!)
                }
                _getAllInterviewInfo.postValue(interviewListOnDone)
            }
        interviewListOnDone.clear()
    }

    fun updateStatusOnFirebase() {
        cal.add(Calendar.MONTH, -2)
        val preDate = cal.time
        val previousDate = formatter.format(preDate)
        val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
        Log.d("FAG", id.value.toString())
        if (interviewDate == arrayListOf(previousDate, current)) {
            firestore.collection("AddInterview").document(id.value.toString()).update("status", 1)
                .addOnSuccessListener {
                    _navigateToListScreen.postValue(Unit)
                }
        }
    }


    fun getCancelledInterviewData() {
        val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
        firestore.collection("AddInterview").whereEqualTo("interviewerId", firebaseUser.uid)
            .whereEqualTo("status", 2)
            .get()
            .addOnSuccessListener {
                val documents = it?.documents
                documents?.forEach { it1 ->
                    val user: AddInterviewModel? = it1.toObject(AddInterviewModel::class.java)
                    interviewListOnCancelled.add(user!!)
                }
                _getAllInterviewInfo.postValue(interviewListOnCancelled)
            }
        interviewListOnCancelled.clear()
    }

}