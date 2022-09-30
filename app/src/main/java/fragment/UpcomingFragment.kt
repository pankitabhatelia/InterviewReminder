package fragment

import activitiy.DashBoardActivity
import adapter.UpcomingAdapter
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Context.ALARM_SERVICE
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.interviewreminderapp.R
import com.example.interviewreminderapp.databinding.FragmentUpcomingBinding
import com.google.firebase.auth.FirebaseAuth
import itemdecoration.SpaceItemDecoration
import model.AddInterviewModel
import model.Fragments
import notification.AlarmReceiver
import viewmodel.AddInterviewViewModel
import viewmodel.FragmentViewModel
import java.util.*
import kotlin.collections.ArrayList


class UpcomingFragment : Fragment() {

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
            findNavController().navigateUp()
        }


    }

    private fun setUpRecyclerview() {
        adapter = UpcomingAdapter()
        binding.rvUpcoming.adapter = adapter
        val itemMargin = SpaceItemDecoration(10, 10, 10, 10)
        binding.rvUpcoming.addItemDecoration(itemMargin)
        adapter.setOnItemClickListener {
            val action =
                HomeFragmentDirections.actionHomeFragmentToInterviewDetailFragment(
                    it,
                    Fragments.upcomingFragment
                )
            findNavController().navigate(action)
        }
    }


}