package activitiy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.interviewreminderapp.databinding.ActivityMainBinding
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth


class MainActivity : AppCompatActivity() {
    private lateinit var binding:ActivityMainBinding
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        FirebaseApp.initializeApp(this)
        auth= FirebaseAuth.getInstance()
        binding.btnLogin.setOnClickListener {
            loginUser()
        }
        binding.tvRedirectToLogin.setOnClickListener {
            val intent = Intent(this, RegistrationActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
    private fun loginUser() {
        val email = binding.etEmail.text.toString().trim()
        val password = binding.etPassword.text.toString().trim()

        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(this, " successfully Logged in !", Toast.LENGTH_SHORT)
                    .show()
                val intent = Intent(this, AddInterview::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, task.exception.toString(), Toast.LENGTH_SHORT).show()
            }
        }
    }
}