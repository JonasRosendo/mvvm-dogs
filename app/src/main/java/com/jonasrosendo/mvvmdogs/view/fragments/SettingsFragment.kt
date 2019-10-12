package com.jonasrosendo.mvvmdogs.view.fragments

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat

import com.jonasrosendo.mvvmdogs.R

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)
    }

}

