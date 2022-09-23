package fragment

import activitiy.DashBoardActivity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.example.interviewreminderapp.databinding.FragmentInterviewDetailBinding
import viewmodel.AddInterviewViewModel


class InterviewDetailFragment : Fragment() {
    private val args by navArgs<InterviewDetailFragmentArgs>()
    private lateinit var binding: FragmentInterviewDetailBinding
    private lateinit var viewModel: AddInterviewViewModel

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
        val activity = activity as AppCompatActivity?
        activity!!.setSupportActionBar(binding.appBar)
        activity.supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        activity.supportActionBar!!.setHomeButtonEnabled(true)
        binding.appBar.setNavigationOnClickListener {
            val intent = Intent(requireContext(), DashBoardActivity::class.java)
            startActivity(intent)
        }
        if (args.currentInterview != null) {
            viewModel.getInterviewData(args.currentInterview!!)
        }
    }

}