package fragment

import Adapter.UpcomingAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.interviewreminderapp.INTERVIEWER
import com.example.interviewreminderapp.PreferenceDataStore
import com.example.interviewreminderapp.USER_IS_LOGGED_IN
import com.example.interviewreminderapp.databinding.FragmentUpcomingBinding
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch
import model.AddInterviewModel
import viewmodel.AddInterviewViewModel


class UpcomingFragment : Fragment() {
    private lateinit var auth:FirebaseAuth
    private lateinit var binding: FragmentUpcomingBinding
    private lateinit var interviewViewModel: AddInterviewViewModel
    private lateinit var adapter: UpcomingAdapter
    private lateinit var name:String

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
        auth= FirebaseAuth.getInstance()
        lifecycleScope.launch {
             name= PreferenceDataStore(requireContext()).getString(INTERVIEWER).toString()
        }

        interviewViewModel.showData(name)
        interviewViewModel.interviewList.clear()
        fragmentStudentObserver()

    }

    private fun fragmentStudentObserver() {
        interviewViewModel.getAllInterviewInfo.observe(viewLifecycleOwner) {
            it?.let {

                    adapter.setData(it as ArrayList<AddInterviewModel>)
            }
        }
    }

    private fun setUpRecyclerview() {
        adapter = UpcomingAdapter()
        binding.rvUpcoming.adapter = adapter
    }
}