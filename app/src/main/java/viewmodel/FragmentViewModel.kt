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
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
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
    private val cal = Calendar.getInstance()
    var id: MutableLiveData<String?> = MutableLiveData()
    private val fireStore: FirebaseFirestore = FirebaseFirestore.getInstance()
    var button: MutableLiveData<Boolean?> = MutableLiveData()
    private lateinit var alarmManager: AlarmManager
    private lateinit var pendingIntent: PendingIntent
    private val _toastMessage: MutableLiveData<String> = MutableLiveData()
    val toastMessage: LiveData<String> = _toastMessage

    fun floatingAddOnClick() {
        _navigateToListScreen.postValue(Unit)
    }

    fun showData() {
        fireStore.collection("AddInterview").whereEqualTo("interviewerId", firebaseUser?.uid)
            .whereEqualTo("interviewerEmail", firebaseUser?.email)
            .whereEqualTo("status", 0)
            .get()
            .addOnSuccessListener {
                val documents = it?.documents
                Log.d("id", firebaseUser?.email.toString())
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
        val current = LocalDateTime.now()
        val getTime = LocalTime.now()
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        val formatted = current.format(formatter)
        val sdf = DateTimeFormatter.ofPattern("hh:mm a")
        val formatterTime = getTime.format(sdf)
        fireStore.collection("AddInterview").whereEqualTo("interviewerId", firebaseUser?.uid)
            .whereEqualTo("status", 0)
            .get()
            .addOnSuccessListener {
                it.forEach { it1 ->
                    val date = it1.data["interviewDate"]
                    val time = it1.data["interviewTime"]
                    val compareDate =
                        SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).parse(date as String)
                    val currentDate = SimpleDateFormat(
                        "dd/MM/yyyy",
                        Locale.getDefault()
                    ).parse(formatted as String)
                    val compareTime =
                        SimpleDateFormat("hh:mm aa", Locale.getDefault()).parse(time as String)
                    val currentTime = SimpleDateFormat(
                        "hh:mm aa",
                        Locale.getDefault()
                    ).parse(formatterTime as String)
                    if (compareTime != null) {
                        if (compareDate != null) {
                            if (compareDate.before(currentDate) || (compareDate.equals(currentDate)) && compareTime.before(
                                    currentTime
                                )
                            ) {
                                fireStore.collection("AddInterview").document(it1.id)
                                    .update("status", 2)
                                    .addOnSuccessListener {

                                    }
                            }
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

    fun onClickOnCancel(view: View) {
        val dialogBuilder = AlertDialog.Builder(view.context)
        dialogBuilder.setMessage("Are you sure you want to cancel?")
            .setCancelable(false)
            .setPositiveButton("No") { _, _ ->

            }
            .setNegativeButton("Yes") { dialog, _ ->
                dialog.cancel()
                changeStatusOnCancel()
                cancelAlarm(view)
                _navigateToListScreen.postValue(Unit)
            }
        val alert = dialogBuilder.create()
        alert.setTitle("Alert!!!!")
        alert.show()
    }

    private fun changeStatusOnCancel() {
        fireStore.collection("AddInterview").whereEqualTo("interviewerId", firebaseUser?.uid)
            .get()
            .addOnSuccessListener {

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

    fun setFromFragment(fromFragment: String) {
        if (fromFragment == Fragments.upcomingFragment) {
            button.value = true
        } else if (fromFragment == Fragments.cancelledFragment || fromFragment == Fragments.doneFragment) {
            button.value = false
        }
    }

    fun onReminderButtonClick(view: View) {
        setAlarm(view)
        _toastMessage.value = "Reminder is set!!!"
    }

    fun createNotificationChannel(view: View) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
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
        fireStore.collection("AddInterview").whereEqualTo("interviewerId", firebaseUser?.uid)
            .whereEqualTo("status", 0)
            .get()
            .addOnSuccessListener {
                it.forEach { it1 ->
                    val interviewId = it1.data["id"]
                    val time = it1.data["interviewTime"]
                    if (interviewId == id.value.toString()) {
                        val compareTime =
                            SimpleDateFormat("hh:mm aa", Locale.getDefault()).parse(time as String)
                        alarmManager = view.context.getSystemService(ALARM_SERVICE) as AlarmManager
                        val intent = Intent(view.context, AlarmReceiver::class.java)
                        pendingIntent = PendingIntent.getBroadcast(
                            view.context,
                            0,
                            intent,
                            PendingIntent.FLAG_IMMUTABLE
                        )
                        if (compareTime != null) {
                            alarmManager.set(
                                AlarmManager.RTC_WAKEUP, compareTime.time, pendingIntent
                            )
                        }
                        _toastMessage.value = "Alarm is set for $time"
                    }

                }

            }

    }

    private fun cancelAlarm(view: View) {
        fireStore.collection("AddInterview").whereEqualTo("interviewerId", firebaseUser?.uid)
            .whereEqualTo("status", 0)
            .get()
            .addOnSuccessListener {
                it.forEach { it1 ->
                    val interviewId = it1.data["id"]
                    val time = it1.data["interviewTime"]
                    if (interviewId == id.value.toString()) {
                        val compareTime =
                            SimpleDateFormat("hh:mm aa", Locale.getDefault()).parse(time as String)
                        alarmManager = view.context.getSystemService(ALARM_SERVICE) as AlarmManager
                        val intent = Intent(view.context, AlarmReceiver::class.java)
                        pendingIntent = PendingIntent.getBroadcast(
                            view.context,
                            0,
                            intent,
                            PendingIntent.FLAG_IMMUTABLE
                        )
                        if (compareTime != null) {
                            alarmManager.set(
                                AlarmManager.RTC_WAKEUP,
                                compareTime.time,
                                pendingIntent
                            )
                        }
                        alarmManager.cancel(pendingIntent)
                        _toastMessage.value = "Alarm is cancelled for $time"
                    }
                }
            }

    }

}