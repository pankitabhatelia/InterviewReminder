package activitiy

import android.content.Context

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.interviewreminderapp.R
import com.google.firebase.firestore.FirebaseFirestore

class DashBoardActivity : AppCompatActivity(R.layout.activity_dashboard) {
    private lateinit var db: FirebaseFirestore
    private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainer) as NavHostFragment
        navController = navHostFragment.navController

        /*  var sharedPref = this.getPreferences(Context.MODE_PRIVATE) ?: return
        val isLogin = sharedPref.getString("Email", "1")

        val email = intent.getStringExtra("email")

        if (isLogin == "1") {

        } else {
            if (isLogin != null) {
                setText(isLogin)
            }
        }


    }

    private fun setText(email: String) {
        db= FirebaseFirestore.getInstance()
        db.collection("InterviewerName").document(email).get()
            .addOnSuccessListener {
                val email = findViewById<>(R.id.)
            }

    }
    }*/
    }
}