package model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class AddInterviewModel(
    val candidateName: String? = null,
    val experience: String? = null,
    val technology: String? = null,
    val interviewDate: String? = null,
    val interviewTime: String? = null,
    val department: String? = null,
    val interviewerId:String?=null,
    val interviewerName: String? = null,
    val remarks: String? = null,
    val status:Int?=0,
    var id:String?=null,
    var interviewerEmail:String?=null
):Parcelable


