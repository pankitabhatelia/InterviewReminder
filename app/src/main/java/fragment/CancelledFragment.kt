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
import viewmodel.AddInterviewViewModel

class CancelledFragment : Fragment(), CancelAdapter.OnItemClickListener  {
    private lateinit var binding: FragmentCancelledBinding
    private lateinit var interviewViewModel: AddInterviewViewModel
    private lateinit var adapter: CancelAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCancelledBinding.inflate(inflater, container, false)
        interviewViewModel = ViewModelProvider(this)[AddInterviewViewModel::class.java]
        binding.cancelledInterview = interviewViewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpRecyclerview()
        interviewViewModel.getCancelledInterviewData()
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
        adapter = CancelAdapter()
        binding.rvCancelled.adapter = adapter
        val itemMargin = SpaceItemDecoration(10,10,10,10)
        binding.rvCancelled.addItemDecoration(itemMargin)
        adapter.setOnItemClickListener(this)
    }

    override fun onItemClick(data: AddInterviewModel) {
        val action =
            HomeFragmentDirections.actionHomeFragmentToInterviewDetailFragment(data,
                Fragments.cancelledFragment)
        findNavController().navigate(action)
    }

}