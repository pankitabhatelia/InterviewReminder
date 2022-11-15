package fragment

import adapter.UpcomingAdapter
import android.app.AlarmManager
import android.app.AlertDialog
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.interviewreminderapp.R
import com.example.interviewreminderapp.databinding.FragmentUpcomingBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import itemdecoration.SpaceItemDecoration
import model.AddInterviewModel
import model.Fragments
import notification.AlarmReceiver
import viewmodel.FragmentViewModel
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class UpcomingFragment : Fragment(), UpcomingAdapter.onItemClickListener {
    private val fireStore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    private var firebaseUser: FirebaseUser? = auth.currentUser
    private val cal = Calendar.getInstance()
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
        interviewViewModel.createNotificationChannel(requireView())
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
        val dialogBuilder = AlertDialog.Builder(requireContext())
        dialogBuilder.setMessage("Are you sure you want to cancel?")
            .setCancelable(false)
            .setPositiveButton("Yes") { _, _ ->
                Log.d("idd", data.id.toString())
                fireStore.collection("AddInterview")
                    .whereEqualTo("interviewerId", firebaseUser?.uid)
                    .get()
                    .addOnSuccessListener {
                        it.forEach { it1 ->
                            val interviewId = it1.data["id"]
                            if (interviewId == data.id) {
                                fireStore.collection("AddInterview").document(it1.id)
                                    .update("status", 2)
                                    .addOnSuccessListener {
                                        findNavController().navigate(R.id.action_homeFragment_to_dashBoardActivity)
                                    }
                            }
                        }
                    }
            }
            .setNegativeButton("No") { dialog, _ ->
                dialog.cancel()
            }
        val alert = dialogBuilder.create()
        alert.setTitle("Alert!!!!")
        alert.show()
    }

    override fun onNotificationClick(data: AddInterviewModel) {
        val date = data.interviewDate
        val compareDate =
            SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).parse(date as String)
        val time = data.interviewTime
        val compareTime =
            SimpleDateFormat("hh:mm aa", Locale.getDefault()).parse(time as String)
        if (compareTime != null && compareDate != null) {
            cal.apply {
                set(Calendar.DATE, compareDate.date)
                set(Calendar.HOUR_OF_DAY, compareTime.hours)
                set(Calendar.MINUTE, compareTime.minutes)
                set(Calendar.SECOND, 0)
            }
            getAlarm(cal.timeInMillis)
        }

    }

    private fun getAlarm(timeInMillis: Long) {
        val alarmManager = requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(requireContext(), AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(requireContext(), 0, intent, 0)
        alarmManager.setExact(
            AlarmManager.RTC,
            timeInMillis,
            pendingIntent
        )
        Toast.makeText(requireContext(), "Alarm Reminder is set....", Toast.LENGTH_SHORT).show()
    }

}