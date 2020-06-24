package com.iflippie.mychat

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SettingsFragmentViewModel : ViewModel(){
    private val colorRepository = ColorRepository()

    val colorItems = MutableLiveData<List<ColorItem>>().apply {
        value = colorRepository.getColorItems()
    }
}