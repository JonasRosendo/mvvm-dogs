package com.jonasrosendo.mvvmdogs.view

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.jonasrosendo.mvvmdogs.R
import com.jonasrosendo.mvvmdogs.util.REQUEST_CODE_PERMISSION_SEND_SMS
import com.jonasrosendo.mvvmdogs.view.fragments.DetailFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //sets navigation controller
        navController = Navigation.findNavController(this, R.id.fragment)
        NavigationUI.setupActionBarWithNavController(this, navController)
    }
    //back arrow pressed
    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(navController, null)
    }

    fun checkSmsPermission() {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.SEND_SMS)){
                AlertDialog.Builder(this)
                    .setIcon(R.drawable.dog)
                    .setTitle("Send SMS Permission")
                    .setMessage("This app requires access to send an SMS")
                    .setPositiveButton("Ask me"){dialogInterface, which ->
                        requestSmsPermission()
                    }
                    .setNegativeButton("No"){dialogInterface, which ->
                        notifyDetailFragmentPermissionGranted(false)
                    }
            }else{
                requestSmsPermission()
            }
        }else{
            notifyDetailFragmentPermissionGranted(true)
        }
    }

    private fun notifyDetailFragmentPermissionGranted(permissionGranted: Boolean) {
        val activeFragment = fragment.childFragmentManager.primaryNavigationFragment
        if(activeFragment is DetailFragment){
            activeFragment.onPermissionResult(permissionGranted)
        }
    }

    private fun requestSmsPermission() {
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.SEND_SMS),
            REQUEST_CODE_PERMISSION_SEND_SMS )
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {

        when(requestCode){
            REQUEST_CODE_PERMISSION_SEND_SMS -> {
                if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    notifyDetailFragmentPermissionGranted(true)
                }else{
                    notifyDetailFragmentPermissionGranted(false)
                }
            }
        }
    }
}