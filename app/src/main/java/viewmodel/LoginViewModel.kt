package viewmodel
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

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
    private val _showProgress: MutableLiveData<Boolean> = MutableLiveData()
    val showProgress: LiveData<Boolean> = _showProgress
    fun loginClick() {
        if (isValid()) {
            loginUser()
        }
    }

    private fun isValid(): Boolean {
        emailError.postValue(null)
        passwordError.postValue(null)

        when {
            email.value.isNullOrEmpty() -> {
                emailError.postValue("Please Enter Email")
            }
            (!Patterns.EMAIL_ADDRESS.matcher(email.value.orEmpty())
                .matches()) -> {
                emailError.postValue("Please Enter valid Email")
            }
            password.value?.length ?: 0 < 8 -> {
                passwordError.postValue("Minimum 8 characters required")
            }
            else -> {
                return true
            }
        }
        return false
    }

    private fun loginUser() {
        _showProgress.value = true
        auth = FirebaseAuth.getInstance()
        val email = email.value.toString()
        val password = password.value.toString()
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            _showProgress.value = false
            if (task.isSuccessful) {
                _showProgress.postValue(true)
                _navigateToListScreen.postValue(Unit)
                _toastMessage.value = "successfully Logged in !"
            } else {
                _toastMessage.value = "User is not registered !"
            }
        }
    }
}