package activitiy

import Adapter.MyAdapter
import Adapter.UpcomingAdapter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.interviewreminderapp.R
import com.example.interviewreminderapp.databinding.ActivityHomeScreenBinding
import com.google.android.material.tabs.TabLayout
import fragment.CancelledFragment
import fragment.DoneFragment
import fragment.UpcomingFragment

class HomeScreen : AppCompatActivity() {
    private lateinit var adapter: MyAdapter
    private lateinit var binding: ActivityHomeScreenBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tabLayout.addTab(binding.tabLayout.newTab().setText(R.string.upcoming))
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText(R.string.cancelled))
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText(R.string.done))
        binding.tabLayout.tabGravity = TabLayout.GRAVITY_FILL

        adapter = MyAdapter(this, supportFragmentManager, binding.tabLayout.tabCount)
        binding.viewPager.adapter = adapter

        binding.viewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(binding.tabLayout))

        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                when (binding.viewPager.currentItem) {
                    0 -> {
                         UpcomingFragment()
                    }
                    1->{
                        CancelledFragment()
                    }
                    2->{
                        DoneFragment()
                    }
                }


            }

            override fun onTabUnselected(tab: TabLayout.Tab) {

            }

            override fun onTabReselected(tab: TabLayout.Tab) {

            }
        })
    }
}