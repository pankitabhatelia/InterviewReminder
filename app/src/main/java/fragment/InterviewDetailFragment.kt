package fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.interviewreminderapp.R
import com.example.interviewreminderapp.databinding.FragmentInterviewDetailBinding
import model.Fragments
import viewmodel.AddInterviewViewModel


class InterviewDetailFragment : Fragment() {
    private val args by navArgs<InterviewDetailFragmentArgs>()
    private lateinit var binding: FragmentInterviewDetailBinding
    private lateinit var viewModel: AddInterviewViewModel
    private lateinit var fragment: ArrayList<Fragment>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentInterviewDetailBinding.inflate(layoutInflater)
        binding.lifecycleOwner = this
        viewModel = ViewModelProvider(this)[AddInterviewViewModel::class.java]
        binding.getInterviewData = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as? AppCompatActivity)?.apply {
            setSupportActionBar(binding.appBar)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.setHomeButtonEnabled(true)
        }
        binding.appBar.setNavigationOnClickListener {
            findNavController().navigate(R.id.action_interviewDetailFragment_to_dashBoardActivity)
        }
        if (args.currentInterview != null) {
            viewModel.getInterviewData(args.currentInterview)
        }
        if(args.fromFragment != null){
           if(arguments?.getString("args") == Fragments.upcomingFragment){
              viewModel.button.value = true
           }else if(arguments?.getString("args") == Fragments.cancelledFragment){
               viewModel.button.value = false
           }else if(arguments?.getString("args") == Fragments.doneFragment){
               viewModel.button.value = false
           }
        }
        observer()
    }

    private fun observer() {
        viewModel.navigateToListScreen.observe(viewLifecycleOwner) {
            findNavController().navigate(R.id.action_interviewDetailFragment_to_dashBoardActivity)
        }
    }


}