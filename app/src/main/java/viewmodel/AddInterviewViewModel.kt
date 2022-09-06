package viewmodel

import android.util.Log
import android.view.View
import android.widget.AdapterView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import model.AddInterviewModel

class AddInterviewViewModel : ViewModel() {

    private lateinit var databaseReference: DatabaseReference
    val candidateName: MutableLiveData<String> = MutableLiveData()
    val experience: MutableLiveData<String> = MutableLiveData()
    val technology: MutableLiveData<String> = MutableLiveData()
    val interviewDate: MutableLiveData<String> = MutableLiveData()
    val interviewTime: MutableLiveData<String> = MutableLiveData()
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

    fun addOnClick() {
        addData()
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
        databaseReference = FirebaseDatabase.getInstance().getReference("AddInterview")
        val addInterviewData = AddInterviewModel(
            candidateName.value?.toString(),
            experience.value?.toString(),
            technology.value?.toString(),
            interviewDate.value?.toString(),
            interviewTime.value?.toString(),
            department,
            interviewerName,
            remarks.value?.toString()
        )
        _getAllInterviewInfo.postValue(listOf(addInterviewData))

        databaseReference.child(databaseReference.push().key.toString()).setValue(addInterviewData)
            .addOnCompleteListener {
                _navigateToListScreen.postValue(Unit)
                _toastMessage.value = "Data is inserted successfully"
            }.addOnFailureListener {
                _toastMessage.value = "Data is Failed to insert!!"
            }
    }

    fun getDataOnDepartmentSpinner() {
        val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
        firestore.collection("Department")
            .get()
            .addOnSuccessListener {
                for (i in 0 until it.documents.size) {
                    department = it.documents[i].data?.get("departmentName").toString()
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
                for (i in 0 until it.documents.size) {
                    interviewerName = it.documents[i].data?.get("interviewerName").toString()
                    spinnerInterviewerList.add(interviewerName)
                }
                _interviewerLivedata.postValue(spinnerInterviewerList)
            }
    }
}