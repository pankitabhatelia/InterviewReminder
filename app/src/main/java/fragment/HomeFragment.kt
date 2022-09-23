package fragment

import adapter.MyAdapter
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.interviewreminderapp.R
import com.example.interviewreminderapp.databinding.FragmentHomeBinding
import com.google.android.material.tabs.TabLayout
import viewmodel.AddInterviewViewModel

class HomeFragment : Fragment() {
    private lateinit var adapter: MyAdapter
    private lateinit var binding: FragmentHomeBinding
    private lateinit var viewModel: AddInterviewViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        binding.lifecycleOwner = this
        viewModel = ViewModelProvider(requireActivity())[AddInterviewViewModel::class.java]
        binding.addData = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText(R.string.upcoming))
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText(R.string.cancelled))
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText(R.string.done))
        binding.tabLayout.tabGravity = TabLayout.GRAVITY_FILL

        adapter = MyAdapter(requireContext(), parentFragmentManager, binding.tabLayout.tabCount)
        binding.viewPager.adapter = adapter
        binding.viewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(binding.tabLayout))
        observer()

    }

    private fun observer() {
        viewModel.navigateToListScreen.observe(viewLifecycleOwner) {
            findNavController().navigate(R.id.action_homeFragment_to_addFragment)
        }
    }
}
