package fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.interviewreminderapp.R
import com.example.interviewreminderapp.databinding.FragmentInterviewDetailBinding
import viewmodel.FragmentViewModel


class InterviewDetailFragment : Fragment() {
    private val args by navArgs<InterviewDetailFragmentArgs>()
    private lateinit var binding: FragmentInterviewDetailBinding
    private lateinit var viewModel: FragmentViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentInterviewDetailBinding.inflate(layoutInflater)
        binding.lifecycleOwner = this
        viewModel = ViewModelProvider(this)[FragmentViewModel::class.java]
        binding.getInterviewData = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observer()
    }

    override fun onResume() {
        super.onResume()
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            findNavController().navigate(R.id.action_interviewDetailFragment_to_dashBoardActivity)
        }

        viewModel.setFromFragment(args.fromFragment)
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
    }

    private fun observer() {
        viewModel.navigateToListScreen.observe(viewLifecycleOwner) {
            findNavController().navigate(R.id.action_interviewDetailFragment_to_dashBoardActivity)
        }
        viewModel.toastMessage.observe(viewLifecycleOwner) {
            it?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }


}