package viewmodel

import android.app.*
import android.content.Context.ALARM_SERVICE
import android.content.Intent
import android.os.Build
import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import model.AddInterviewModel
import model.Fragments
import notification.AlarmReceiver
import notification.AlarmService
import java.text.SimpleDateFormat
import java.util.*

class FragmentViewModel : ViewModel() {
    val candidateName: MutableLiveData<String?> = MutableLiveData()
    val experience: MutableLiveData<String?> = MutableLiveData()
    val technology: MutableLiveData<String?> = MutableLiveData()
    val interviewDate: MutableLiveData<String?> = MutableLiveData()
    val interviewTime: MutableLiveData<String?> = MutableLiveData()
    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    private var firebaseUser: FirebaseUser? = auth.currentUser
    val remarks: MutableLiveData<String?> = MutableLiveData()
    private val _getAllInterviewInfo: MutableLiveData<List<AddInterviewModel>?> = MutableLiveData()
    val getAllInterviewInfo: LiveData<List<AddInterviewModel>?> = _getAllInterviewInfo
    private val _navigateToListScreen: MutableLiveData<Unit> = MutableLiveData()
    val navigateToListScreen: LiveData<Unit> = _navigateToListScreen
    var interviewList = arrayListOf<AddInterviewModel>()
    private var interviewListOnDone = arrayListOf<AddInterviewModel>()
    private var interviewListOnCancelled = arrayListOf<AddInterviewModel>()
    private val formatter = SimpleDateFormat("dd/M/yyyy")
    private val date = Date()
    private val current = formatter.format(date)
    private val cal = Calendar.getInstance()
    var id: MutableLiveData<String?> = MutableLiveData()
    private val fireStore: FirebaseFirestore = FirebaseFirestore.getInstance()
    var button: MutableLiveData<Boolean?> = MutableLiveData()
    private lateinit var alarmManager: AlarmManager
    private lateinit var pendingIntent: PendingIntent
    private lateinit var alarmService: AlarmService


    fun floatingAddOnClick() {
        _navigateToListScreen.postValue(Unit)
    }

    fun showData() {
        fireStore.collection("AddInterview").whereEqualTo("interviewerId", firebaseUser?.uid)
            .whereEqualTo("status", 0)
            .get()
            .addOnSuccessListener {
                val documents = it?.documents
                documents?.forEach { it1 ->
                    val user: AddInterviewModel? = it1.toObject(AddInterviewModel::class.java)
                    interviewList.add(user!!)
                }
                _getAllInterviewInfo.postValue(interviewList)
            }
    }

    fun getInterviewData(addInterviewModel: AddInterviewModel?) {
        candidateName.value = addInterviewModel?.candidateName
        experience.value = addInterviewModel?.experience
        technology.value = addInterviewModel?.technology
        interviewDate.value = addInterviewModel?.interviewDate
        interviewTime.value = addInterviewModel?.interviewTime
        remarks.value = addInterviewModel?.remarks
        id.value = addInterviewModel?.id
    }


    fun getDoneInterviewData() {
        fireStore.collection("AddInterview").whereEqualTo("interviewerId", firebaseUser?.uid)
            .whereEqualTo("status", 1)
            .get()
            .addOnSuccessListener {
                val documents = it?.documents
                documents?.forEach { it1 ->
                    val user: AddInterviewModel? = it1.toObject(AddInterviewModel::class.java)
                    if (user != null) {
                        interviewListOnDone.add(user)
                    }
                }
                _getAllInterviewInfo.postValue(interviewListOnDone)
            }
        interviewListOnDone.clear()
    }


    fun getCancelledInterviewData() {
        fireStore.collection("AddInterview").whereEqualTo("interviewerId", firebaseUser?.uid)
            .whereEqualTo("status", 2)
            .get()
            .addOnSuccessListener {
                val documents = it?.documents
                documents?.forEach { it1 ->
                    val user: AddInterviewModel? = it1.toObject(AddInterviewModel::class.java)
                    if (user != null) {
                        interviewListOnCancelled.add(user)
                    }
                }
                _getAllInterviewInfo.postValue(interviewListOnCancelled)
            }
        interviewListOnCancelled.clear()
    }

    fun updateStatusOnFirebase() {
        cal.add(Calendar.MONTH, -2)
        val preDate = cal.time
        val previousDate = formatter.format(preDate)
        fireStore.collection("AddInterview").whereEqualTo("interviewerId", firebaseUser?.uid)
            .whereEqualTo("status", 0)
            .get()
            .addOnSuccessListener {
                it.forEach { it1 ->
                    val date = it1.data["interviewDate"]
                    if (date.toString() < previousDate) {
                        fireStore.collection("AddInterview").document(it1.id)
                            .update("status", 2)
                            .addOnSuccessListener {

                            }
                    }
                }
            }
    }

    private fun onClickOnDone(): Boolean {
        _navigateToListScreen.postValue(Unit)
        return true
    }

    fun changeStatusOnDone() {
        fireStore.collection("AddInterview").whereEqualTo("interviewerId", firebaseUser?.uid)
            .get()
            .addOnSuccessListener {
                if (onClickOnDone()) {
                    it.forEach { it1 ->
                        val interviewId = it1.data["id"]
                        if (interviewId == id.value.toString()) {
                            fireStore.collection("AddInterview").document(it1.id)
                                .update("status", 1)
                                .addOnSuccessListener {

                                }
                        }
                    }
                }
            }
    }

    private fun onClickOnCancel(view: View): Boolean {
        cancelAlarm(view)
        _navigateToListScreen.postValue(Unit)
        return true
    }

    fun changeStatusOnCancel(view: View) {
        fireStore.collection("AddInterview").whereEqualTo("interviewerId", firebaseUser?.uid)
            .get()
            .addOnSuccessListener {
                if (onClickOnCancel(view)) {
                    it.forEach { it1 ->
                        val interviewId = it1.data["id"]
                        if (interviewId == id.value.toString()) {
                            fireStore.collection("AddInterview").document(it1.id)
                                .update("status", 2)
                                .addOnSuccessListener {

                                }
                        }
                    }
                }
            }
    }

    fun setFromFragment(fromFragment: String) {
        if (fromFragment == Fragments.upcomingFragment) {
            button.value = true
        } else if (fromFragment == Fragments.cancelledFragment || fromFragment == Fragments.doneFragment) {
            button.value = false
        }
    }

    fun onReminderButtonClick(view: View) {
        alarmService = AlarmService(view.context)
        setAlarm(view)
        _navigateToListScreen.postValue(Unit)
    }

    fun createNotificationChannel(view: View) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name: CharSequence = "InterviewReminder"
            val description = "Reminder for Interview Scheduled"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel("InterviewReminder", name, importance)
            channel.description = description
            val notificationManager: NotificationManager =
                view.context.getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun setAlarm(view: View) {
        alarmManager = view.context.getSystemService(ALARM_SERVICE) as AlarmManager
        val intent = Intent(view.context, AlarmReceiver::class.java)
        pendingIntent = PendingIntent.getBroadcast(view.context, 0, intent, 0)
        try {
            alarmManager.set(
                AlarmManager.RTC_WAKEUP, cal.timeInMillis, pendingIntent
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun cancelAlarm(view: View) {
        alarmManager = view.context.getSystemService(ALARM_SERVICE) as AlarmManager
        val intent = Intent(view.context, AlarmReceiver::class.java)
        pendingIntent = PendingIntent.getBroadcast(view.context, 0, intent, 0)
        alarmManager.cancel(pendingIntent)
    }


}