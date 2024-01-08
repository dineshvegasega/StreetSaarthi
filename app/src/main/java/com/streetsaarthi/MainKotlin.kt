package com.streetsaarthi

import android.Manifest
import android.content.Context
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.RequestMultiplePermissions
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import java.util.Arrays

class MainKotlin : AppCompatActivity() {


    var permissionsList: java.util.ArrayList<String>? = null
    var permissionsStr = arrayOf(
        Manifest.permission.CAMERA,
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )
    var permissionsCount = 0


//    var permissionsLauncher =
//        registerForActivityResult<Array<String>, Map<String, Boolean>>(RequestMultiplePermissions()
//        ) { result ->
//            ActivityResultCallback<Map<String?, Boolean?>?> { result ->
//                val list = ArrayList(result!!.values)
//                permissionsList = ArrayList()
//                permissionsCount = 0
//                for (i in list.indices) {
//                    if (shouldShowRequestPermissionRationale(permissionsStr[i])) {
//                        permissionsList!!.add(permissionsStr[i])
//                    } else if (!hasPermission(this@MainKotlin, permissionsStr[i])) {
//                        permissionsCount++
//                    }
//                }
//
//                if (permissionsList!!.size > 0) {
//                    //Some permissions are denied and can be asked again.
//                    askForPermissions(permissionsList!!)
//                } else if (permissionsCount > 0) {
//                    //Show alert dialog
//                    showPermissionDialog()
//                } else {
//                    Log.e("TAG", "AAAAAAAAA")
//                }
//            }
//        }




    private fun hasPermission(context: Context, permissionStr: String): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            permissionStr
        ) == PackageManager.PERMISSION_GRANTED
    }

    var alertDialog: AlertDialog? = null

    private fun showPermissionDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Permission required")
            .setMessage("Some permissions are needed to be allowed to use this app without any problems.")
            .setPositiveButton(
                "Ok"
            ) { dialog: DialogInterface, which: Int -> dialog.dismiss() }
        if (alertDialog == null) {
            alertDialog = builder.create()
            if (!alertDialog!!.isShowing) {
                alertDialog!!.show()
            }
        }
    }


    private val permissionsLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions())
        {

        }


        private fun askForPermissions(permissionsList: ArrayList<String>) {
        val newPermissionStr = arrayOfNulls<String>(permissionsList.size)
            for (i in newPermissionStr.indices) {
                newPermissionStr[i] = permissionsList[i]
            }
        if (newPermissionStr.size > 0) {
            //permissionsLauncher.launch(newPermissionStr)
        } else {

            showPermissionDialog()
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.main)

//        var button = findViewById<Button>(R.id.button)

//        button.setOnClickListener {
////            permissionsList = ArrayList<String>()
////            permissionsList!!.addAll(Arrays.asList<String>(*permissionsStr))
////            askForPermissions(permissionsList!!)
//
//            permissionsList = java.util.ArrayList()
//            permissionsList!!.addAll(Arrays.asList(*permissionsStr))
//            askForPermissions(permissionsList!!)
//        }
    }
}


