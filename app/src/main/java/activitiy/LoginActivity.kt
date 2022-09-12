package activitiy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import utils.CustomProgressDialog
import com.example.interviewreminderapp.PreferenceDataStore
import com.example.interviewreminderapp.R
import com.example.interviewreminderapp.USER_IS_LOGGED_IN
import com.example.interviewreminderapp.databinding.ActivityLoginBinding
import com.google.firebase.FirebaseApp
import kotlinx.coroutines.launch
import viewmodel.LoginViewModel

class LoginActivity : AppCompatActivity() {

    private lateinit var progressDialog: CustomProgressDialog
    private lateinit var binding: ActivityLoginBinding
    private lateinit var viewModel: LoginViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        binding.lifecycleOwner = this
        viewModel = ViewModelProvider(this)[LoginViewModel::class.java]
        binding.loginViewModel = viewModel
        setContentView(binding.root)
        FirebaseApp.initializeApp(this)
        progressDialog = CustomProgressDialog(this)
        observer()
    }

    private fun observer() {

        viewModel.toastMessage.observe(this) {
            it?.let {
                Toast.makeText(this, it, Toast.LENGTH_SHORT)
                    .show()
            }
        }
        viewModel.showProgress.observe(this) {
            if (it) {
                progressDialog.start()
            } else {
                progressDialog.stop()
            }
        }
        viewModel.navigateToListScreen.observe(this) {
            lifecycleScope.launch {
                PreferenceDataStore(this@LoginActivity).putBoolean(USER_IS_LOGGED_IN, true)
            }
            val intent = Intent(this, DashBoardActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}