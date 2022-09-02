package viewmodel

import android.view.View
import android.widget.AdapterView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import model.AddInterviewModel


class AddInterviewViewModel: ViewModel()  {
    private lateinit var auth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    private lateinit var firebaseUser: FirebaseUser
    val candidateName: MutableLiveData<String> = MutableLiveData()
    val experience: MutableLiveData<String> = MutableLiveData()
    val technology: MutableLiveData<String> = MutableLiveData()
    val interviewDate: MutableLiveData<String> = MutableLiveData()
    val department: MutableLiveData<String> = MutableLiveData()
    val interviewerName: MutableLiveData<String> = MutableLiveData()
    val remarks: MutableLiveData<String> = MutableLiveData()
    private val _toastMessage: MutableLiveData<String> = MutableLiveData()
    val toastMessage: LiveData<String> = _toastMessage

         fun addOnClick(){
            addData()
        }

    private fun addData() {
        auth = FirebaseAuth.getInstance()
        firebaseUser = auth.currentUser!!
        databaseReference = FirebaseDatabase.getInstance().getReference("AddInterview")
        val addInterviewData = AddInterviewModel(
            candidateName.value?.toString(),
            experience.value?.toString(),
            technology.value?.toString(),
            interviewDate.value?.toString(),
            department.value?.toString(),
            interviewerName.value?.toString(),
            remarks.value?.toString()
        )
        databaseReference.child(databaseReference.push().key.toString()).setValue(addInterviewData).addOnCompleteListener {
            _toastMessage.value = "Data is inserted successfully"
        }.addOnFailureListener {
            _toastMessage.value = "Data is Failed to insert!!"
        }
    }
}