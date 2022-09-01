package Interfaces

import model.Department

interface LoadFirebase {

    fun onFirebaseLoadSuccess(departmentList:List<Department>)
    fun onFirebaseLoadFailed(message:String)
}