package fragment

import adapter.DoneAdapter
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.interviewreminderapp.databinding.FragmentDoneBinding
import itemdecoration.SimpleItemDecoration
import model.AddInterviewModel
import viewmodel.AddInterviewViewModel
import kotlin.collections.ArrayList

class DoneFragment : Fragment() {
    private lateinit var binding: FragmentDoneBinding
    private lateinit var interviewViewModel: AddInterviewViewModel
    private lateinit var adapter: DoneAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDoneBinding.inflate(inflater, container, false)
        interviewViewModel = ViewModelProvider(this)[AddInterviewViewModel::class.java]
        binding.doneInterview = interviewViewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpRecyclerview()
        interviewViewModel.getDoneInterviewData()
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
        adapter = DoneAdapter()
        binding.rvDone.adapter = adapter
        val itemMargin = SimpleItemDecoration()
        binding.rvDone.addItemDecoration(itemMargin)
    }

}