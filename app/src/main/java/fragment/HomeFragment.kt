package fragment

import Adapter.MyAdapter
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.interviewreminderapp.R
import com.example.interviewreminderapp.databinding.FragmentHomeBinding
import com.google.android.material.tabs.TabLayout

class HomeFragment : Fragment() {
    private lateinit var adapter: MyAdapter
    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText(R.string.upcoming))
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText(R.string.cancelled))
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText(R.string.done))
        binding.tabLayout.tabGravity = TabLayout.GRAVITY_FILL

        adapter = MyAdapter(requireContext(),parentFragmentManager, binding.tabLayout.tabCount)
        binding.viewPager.adapter = adapter

        binding.btnAdd.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_addFragment)
        }
        binding.viewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(binding.tabLayout))

        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                when (binding.viewPager.currentItem) {
                    0 -> {
                        UpcomingFragment()
                    }
                    1 -> {
                        CancelledFragment()
                    }
                    2 -> {
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
