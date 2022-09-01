package activitiy

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager.widget.ViewPager
import com.example.interviewreminderapp.databinding.ActivityHomeScreenBinding
import com.google.android.material.tabs.TabLayout

class HomeScreen : AppCompatActivity() {
    private lateinit var binding:ActivityHomeScreenBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityHomeScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Upcoming"))
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Cancelled"))
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Done"))
        binding.tabLayout.tabGravity=TabLayout.GRAVITY_FILL
    }
}