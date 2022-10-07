package fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.interviewreminderapp.R
import com.example.interviewreminderapp.databinding.FragmentAddBinding
import utils.CustomProgressDialog
import viewmodel.AddInterviewViewModel


class AddFragment : Fragment() {
    private lateinit var binding: FragmentAddBinding
    private lateinit var viewModel: AddInterviewViewModel
    private val progressDialog by lazy { CustomProgressDialog(requireContext()) }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddBinding.inflate(layoutInflater)
        binding.lifecycleOwner = this
        viewModel = ViewModelProvider(this)[AddInterviewViewModel::class.java]
        binding.addInterview = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            findNavController().navigate(R.id.action_addFragment_to_dashBoardActivity)
        }
        (requireActivity() as? AppCompatActivity)?.apply {
            setSupportActionBar(binding.appBar)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.setHomeButtonEnabled(true)
        }
        binding.appBar.setNavigationOnClickListener {
            findNavController().navigate(R.id.action_addFragment_to_dashBoardActivity)
        }
        viewModel.getDataOnDepartmentSpinner()
        observer()
    }


    private fun observer() {
        viewModel.toastMessage.observe(viewLifecycleOwner) {
            it?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT)
                    .show()
            }
        }
        viewModel.navigateToListScreen.observe(viewLifecycleOwner) {
           findNavController().navigate(R.id.action_addFragment_to_dashBoardActivity)

        }
        viewModel.showProgress.observe(viewLifecycleOwner) {
            if (it) {
                progressDialog.start()
            } else {
                progressDialog.stop()
            }
        }
        viewModel.departmentLivedata.observe(viewLifecycleOwner) {
            val arrayAdapter =
                ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, it)
            binding.spinnerDepartment.adapter = arrayAdapter
            arrayAdapter.setDropDownViewResource(android.R.layout.simple_list_item_1)
        }
        viewModel.interviewerLiveData.observe(viewLifecycleOwner) {
            val adapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_item,
                it
            )
            binding.spinnerName.adapter = adapter
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }

    }

}