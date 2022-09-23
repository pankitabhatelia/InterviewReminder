package fragment

import activitiy.DashBoardActivity
import android.content.Intent
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
import com.example.interviewreminderapp.databinding.FragmentAddBinding
import model.AddInterviewModel
import utils.CustomProgressDialog
import viewmodel.AddInterviewViewModel


class AddFragment : Fragment() {
    private lateinit var binding: FragmentAddBinding
    private lateinit var viewModel: AddInterviewViewModel
    private lateinit var progressDialog: CustomProgressDialog
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
            val intent = Intent(requireContext(), DashBoardActivity::class.java)
            startActivity(intent)
        }
        val activity = activity as AppCompatActivity?
        activity!!.setSupportActionBar(binding.appBar)
        activity.supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        activity.supportActionBar!!.setHomeButtonEnabled(true)
        binding.appBar.setNavigationOnClickListener {
            val intent = Intent(requireContext(), DashBoardActivity::class.java)
            startActivity(intent)
        }
        progressDialog = CustomProgressDialog(requireContext())
        viewModel.getDataOnDepartmentSpinner()
        progressDialog = CustomProgressDialog(requireContext())
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
            val intent = Intent(requireContext(), DashBoardActivity::class.java)
            intent.putExtra("addinterviewModel", AddInterviewModel())
            startActivity(intent)

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