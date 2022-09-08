package viewmodel

import activitiy.LoginActivity
import android.app.AlertDialog
import android.app.Application
import android.content.ContentProvider
import android.content.Context
import android.content.DialogInterface
import android.util.Patterns
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelStoreOwner
import com.google.firebase.auth.FirebaseAuth
import kotlin.coroutines.coroutineContext

class LoginViewModel : ViewModel() {

    val email: MutableLiveData<String> = MutableLiveData()
    val password: MutableLiveData<String> = MutableLiveData()
    private val _toastMessage: MutableLiveData<String> = MutableLiveData()
    val toastMessage: LiveData<String> = _toastMessage
    private lateinit var auth: FirebaseAuth
    val emailError: MutableLiveData<String?> = MutableLiveData()
    val passwordError: MutableLiveData<String?> = MutableLiveData()
    private val _navigateToListScreen: MutableLiveData<Unit> = MutableLiveData()
    val navigateToListScreen: LiveData<Unit> = _navigateToListScreen
    fun loginClick() {
        if (isValid()) {
            loginUser()
        }
    }

    private fun isValid(): Boolean {
        emailError.postValue(null)
        passwordError.postValue(null)

        if (email.value.isNullOrEmpty()) {
            emailError.postValue("Please Enter Email")
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email.value.orEmpty())
                .matches()
        ) {
            emailError.postValue("Please Enter valid Email")
        } else if (password.value?.length ?: 0 < 8) {
            passwordError.postValue("Minimum 8 characters required")
        } else {
            return true
        }
        return false
    }

    private fun loginUser() {
        auth = FirebaseAuth.getInstance()
        val email = email.value.toString()
        val password = password.value.toString()
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                _navigateToListScreen.postValue(Unit)
                _toastMessage.value = "successfully Logged in !"
            } else {
                _toastMessage.value = "User is not registered !"
            }
        }

    }
}