package com.iflippie.mychat.fragments

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.iflippie.mychat.ColorRepository
import com.iflippie.mychat.model.ColorItem

class SettingsFragmentViewModel : ViewModel(){
    private val colorRepository = ColorRepository()

    val colorItems = MutableLiveData<List<ColorItem>>().apply {
        value = colorRepository.getColorItems()
    }
}