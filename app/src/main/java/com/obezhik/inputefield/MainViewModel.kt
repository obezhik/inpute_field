package com.obezhik.inputefield

import android.view.View
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.material.textfield.TextInputLayout

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

    fun checkTextValid(v: View){
        if (textValue.value.isNullOrEmpty()){
            errorText.value = "Can`t be empty!"
        }

        Toast.makeText(v.context, "Success!", Toast.LENGTH_SHORT).show()
    }

}