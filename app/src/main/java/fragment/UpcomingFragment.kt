package fragment

import adapter.UpcomingAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.interviewreminderapp.R
import com.example.interviewreminderapp.databinding.FragmentUpcomingBinding
import com.google.firebase.auth.FirebaseAuth
import itemdecoration.SimpleItemDecoration
import model.AddInterviewModel
import viewmodel.AddInterviewViewModel


class UpcomingFragment : Fragment(), UpcomingAdapter.OnItemClickListener {
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: FragmentUpcomingBinding
    private lateinit var interviewViewModel: AddInterviewViewModel
    private lateinit var adapter: UpcomingAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentUpcomingBinding.inflate(inflater, container, false)
        interviewViewModel = ViewModelProvider(this)[AddInterviewViewModel::class.java]
        binding.interviewViewModel = interviewViewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpRecyclerview()
        auth = FirebaseAuth.getInstance()
        interviewViewModel.showData()
        interviewViewModel.interviewList.clear()
        fragmentStudentObserver()

    }

    private fun fragmentStudentObserver() {
        interviewViewModel.getAllInterviewInfo.observe(viewLifecycleOwner) {
            it?.let {
                adapter.setData(it as ArrayList<AddInterviewModel>)
            }
        }
        interviewViewModel.navigateToListScreen.observe(viewLifecycleOwner) {
            findNavController().navigateUp()
        }

    }

    private fun setUpRecyclerview() {
        adapter = UpcomingAdapter()
        binding.rvUpcoming.adapter = adapter
        val itemMargin = SimpleItemDecoration()
        binding.rvUpcoming.addItemDecoration(itemMargin)
        adapter.setOnItemClickListener(this)
    }

    override fun onItemClick(data: AddInterviewModel) {
        val action =
            HomeFragmentDirections.actionHomeFragmentToInterviewDetailFragment(data)
        findNavController().navigate(action)

    }
}