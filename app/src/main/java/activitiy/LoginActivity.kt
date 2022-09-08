package activitiy

import android.R
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.interviewreminderapp.CustomProgressDialog
import com.example.interviewreminderapp.databinding.ActivityLoginBinding
import com.google.firebase.FirebaseApp
import viewmodel.LoginViewModel

class LoginActivity : AppCompatActivity() {

    private val progressDialog = CustomProgressDialog(this)
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
        observer()

    }

    private fun observer() {
        viewModel.toastMessage.observe(this) {
            progressDialog.start("Loading....")
            Handler(Looper.getMainLooper()).postDelayed({
                // Dismiss progress bar after 4 seconds
                progressDialog.stop()
            }, 4000)
            it?.let {
                Toast.makeText(this, it, Toast.LENGTH_SHORT)
                    .show()
            }
        }
        viewModel.navigateToListScreen.observe(this) {
            val intent = Intent(this, DashBoardActivity::class.java)
            startActivity(intent)
            finish()
        }
    }



}