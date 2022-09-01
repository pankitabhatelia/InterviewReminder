package activitiy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.interviewreminderapp.databinding.ActivityRegistrationBinding
import com.google.firebase.auth.FirebaseAuth

class RegistrationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegistrationBinding
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()
        binding.btnCreate.setOnClickListener {
            signUpUser()
        }
        binding.tvRedirectToLogin.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
    private fun isvalidEmailAndPassword(): Boolean {
        val email = binding.email.text.toString().trim()
        val password = binding.password.text.toString().trim()
        val confirmPassword = binding.confirmPassword.text.toString().trim()
        if (email.isBlank() || password.isBlank() || confirmPassword.isBlank()) {
            Toast.makeText(this, "Email and Password can't be blank", Toast.LENGTH_SHORT).show()
        } else {
            return true
        }
        if (password != confirmPassword) {
            Toast.makeText(this, "Password and Confirm Password do not match", Toast.LENGTH_SHORT)
                .show()
        } else {
            return true
        }
        return false
    }
    private fun signUpUser() {
        val email = binding.email.text.toString().trim()
        val password = binding.password.text.toString().trim()
        if (isvalidEmailAndPassword()) {
            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "created account successfully !", Toast.LENGTH_SHORT)
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
}