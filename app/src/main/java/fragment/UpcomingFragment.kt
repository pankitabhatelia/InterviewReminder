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
import itemdecoration.SpaceItemDecoration
import model.AddInterviewModel
import model.Fragments
import viewmodel.FragmentViewModel
import kotlin.collections.ArrayList


class UpcomingFragment : Fragment(), UpcomingAdapter.onItemClickListener {

    private lateinit var binding: FragmentUpcomingBinding
    private lateinit var interviewViewModel: FragmentViewModel
    private lateinit var adapter: UpcomingAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentUpcomingBinding.inflate(inflater, container, false)
        interviewViewModel = ViewModelProvider(this)[FragmentViewModel::class.java]
        binding.interviewViewModel = interviewViewModel
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
        interviewViewModel.updateStatusOnFirebase()
        interviewViewModel.showData()
        interviewViewModel.interviewList.clear()
    }

    private fun fragmentStudentObserver() {
        interviewViewModel.getAllInterviewInfo.observe(viewLifecycleOwner) {
            it?.let {
                adapter.setData(it as ArrayList<AddInterviewModel>)
            }
        }
        interviewViewModel.navigateToListScreen.observe(viewLifecycleOwner) {
            findNavController().navigate(R.id.action_homeFragment_to_dashBoardActivity)
        }


    }

    private fun setUpRecyclerview() {
        adapter = UpcomingAdapter()
        binding.rvUpcoming.adapter = adapter
        val itemMargin = SpaceItemDecoration(10, 10, 10, 10)
        binding.rvUpcoming.addItemDecoration(itemMargin)
        adapter.setOnItemClickListener(this)
    }

    override fun onItemClick(data: AddInterviewModel) {
        val action = HomeFragmentDirections.actionHomeFragmentToInterviewDetailFragment(
            data,
            Fragments.upcomingFragment
        )
        findNavController().navigate(action)
    }

    override fun onCancelClick(data: AddInterviewModel) {
        interviewViewModel.onClickOnCancel(requireView())
    }

    override fun onNotificationClick(data: AddInterviewModel) {
        interviewViewModel.setAlarm(requireView())
    }

}