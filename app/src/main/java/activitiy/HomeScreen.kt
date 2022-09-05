package activitiy

import Adapter.MyAdapter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.interviewreminderapp.databinding.ActivityHomeScreenBinding
import com.google.android.material.tabs.TabLayout

class HomeScreen : AppCompatActivity() {
    private lateinit var adapter:MyAdapter
    private lateinit var binding:ActivityHomeScreenBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityHomeScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Upcoming"))
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Cancelled"))
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Done"))
        binding.tabLayout.tabGravity=TabLayout.GRAVITY_FILL

        adapter = MyAdapter(this, supportFragmentManager,binding.tabLayout.tabCount)
        binding.viewPager.adapter = adapter

        binding.viewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(binding.tabLayout))

        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                binding.viewPager.currentItem = tab.position
            }
            override fun onTabUnselected(tab: TabLayout.Tab) {

            }
            override fun onTabReselected(tab: TabLayout.Tab) {

            }
        })
    }
}