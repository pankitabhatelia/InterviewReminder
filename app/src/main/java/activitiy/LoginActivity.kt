package activitiy

import android.content.Intent
import android.content.res.Configuration
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import utils.CustomProgressDialog
import com.example.interviewreminderapp.databinding.ActivityLoginBinding
import com.google.firebase.FirebaseApp
import kotlinx.coroutines.launch
import viewmodel.LoginViewModel
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.asLiveData
import data.PreferenceDataStore
import data.USER_IS_LOGGED_IN

class LoginActivity : AppCompatActivity() {
    private var _isNightModeActive: Boolean = false


    private val progressDialog by lazy { CustomProgressDialog(this) }
    private lateinit var binding: ActivityLoginBinding
    private lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        binding.lifecycleOwner = this
        viewModel = ViewModelProvider(this)[LoginViewModel::class.java]
        binding.loginViewModel = viewModel
        sessionObserver()
        setContentView(binding.root)
        FirebaseApp.initializeApp(this)
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
    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        _isNightModeActive =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                resources.configuration.isNightModeActive
            } else {
                newConfig.uiMode and
                        Configuration.UI_MODE_NIGHT_MASK ==
                        Configuration.UI_MODE_NIGHT_YES
            }
    }
    private fun sessionObserver() {
        lifecycleScope.launch {
            PreferenceDataStore(this@LoginActivity).getBoolean(
                USER_IS_LOGGED_IN
            ).asLiveData().observe(
                this@LoginActivity
            ) {
                if (it == true) {
                    val intent = Intent(this@LoginActivity, DashBoardActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        }
    }


}