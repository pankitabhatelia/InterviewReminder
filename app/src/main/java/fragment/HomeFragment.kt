package fragment

import activitiy.LoginActivity
import adapter.MyAdapter
import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.interviewreminderapp.R
import com.example.interviewreminderapp.databinding.FragmentHomeBinding
import com.google.android.material.tabs.TabLayout
import data.PreferenceDataStore
import kotlinx.coroutines.launch
import viewmodel.FragmentViewModel

class HomeFragment : Fragment(), MenuProvider {
    private lateinit var adapter: MyAdapter
    private lateinit var binding: FragmentHomeBinding
    private lateinit var viewModel: FragmentViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // setHasOptionsMenu(true)
        binding = FragmentHomeBinding.inflate(layoutInflater)
        binding.lifecycleOwner = this
        viewModel = ViewModelProvider(requireActivity())[FragmentViewModel::class.java]
        binding.addData = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as? AppCompatActivity)?.apply {
            setSupportActionBar(binding.appBar)
            supportActionBar?.setDisplayHomeAsUpEnabled(false)
            supportActionBar?.setHomeButtonEnabled(true)
        }
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText(R.string.upcoming))
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText(R.string.cancelled))
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText(R.string.done))
        binding.tabLayout.tabGravity = TabLayout.GRAVITY_FILL
        activity?.addMenuProvider(this)
        adapter = MyAdapter(parentFragmentManager, binding.tabLayout.tabCount)
        binding.viewPager.adapter = adapter
        binding.viewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(binding.tabLayout))
        observer()

    }


    private fun observer() {
        viewModel.navigateToListScreen.observe(viewLifecycleOwner) {
            findNavController().navigate(R.id.action_homeFragment_to_addFragment)
        }
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.interview_menu, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        if (menuItem.itemId == R.id.logout) {
            lifecycleScope.launch {
                PreferenceDataStore(requireContext()).clearLoggedInSession()
            }
            val intent = Intent(requireContext(), LoginActivity::class.java)
            startActivity(intent)
        }
        return false
    }
}
