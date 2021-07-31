package com.obezhik.inputefield

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel: ViewModel() {

    val textValue = MutableLiveData("")
    val listValue = MutableLiveData("")
    val passwordValue = MutableLiveData("")
    val errorText = MutableLiveData("")
    val errorList = MutableLiveData("")
    val errorPassword = MutableLiveData("")
    val entity = MutableLiveData(arrayListOf("100", "1000", "1450"))

    fun addToEntity() {
        if (entity.value?.size!! > 5) {
            errorList.value = "Its to match!!!"
        } else {
            entity.value?.add("104".plus(entity.value?.size))
        }
    }

}