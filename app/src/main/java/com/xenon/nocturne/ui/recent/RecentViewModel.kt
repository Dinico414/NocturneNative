package com.xenon.nocturne.ui.recent

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class RecentViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is Recent Fragment"
    }
    val text: LiveData<String> = _text
}