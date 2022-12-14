package fragment

import adapter.CancelAdapter
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.interviewreminderapp.databinding.FragmentCancelledBinding
import itemdecoration.SpaceItemDecoration
import model.AddInterviewModel
import model.Fragments
import viewmodel.FragmentViewModel

class CancelledFragment : Fragment() {
    private lateinit var binding: FragmentCancelledBinding
    private lateinit var interviewViewModel: FragmentViewModel
    private lateinit var adapter: CancelAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCancelledBinding.inflate(inflater, container, false)
        interviewViewModel = ViewModelProvider(this)[FragmentViewModel::class.java]
        binding.cancelledInterview = interviewViewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpRecyclerview()
        fragmentStudentObserver()
    }

    override fun onResume() {
        super.onResume()
        interviewViewModel.getCancelledInterviewData()
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
        adapter = CancelAdapter()
        binding.rvCancelled.adapter = adapter
        val itemMargin = SpaceItemDecoration(10, 10, 10, 10)
        binding.rvCancelled.addItemDecoration(itemMargin)
        adapter.setOnItemClickListener {
            val action =
                HomeFragmentDirections.actionHomeFragmentToInterviewDetailFragment(
                    it,
                    Fragments.cancelledFragment
                )
            findNavController().navigate(action)
        }
    }


}