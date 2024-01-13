package com.dhruv194.pathorfloating

import android.app.ActivityManager
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var dialog: AlertDialog
    private lateinit var btnlaunchwidget: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnlaunchwidget = findViewById(R.id.launch_widget)
        val action: String? = intent?.action
        val data: Uri? = intent?.data
       if(data!=null){
            Toast.makeText(this,"Here",Toast.LENGTH_SHORT).show()
            if(checkOverlayPermission()){

                startService(Intent(this@MainActivity,FloatingWidget::class.java))
                finish()
            }else{
                requestFloatingWindowPermission()
            }
        }
     /*   if(isServiceRunning()){
            stopService(Intent(this@MainActivity,FloatingWidget::class.java))
        }*/
     /*   Handler(Looper.getMainLooper()).postDelayed({
            if(checkOverlayPermission()){
                startService(Intent(this@MainActivity,FloatingWidget::class.java))
                finish()
            }else{
                requestFloatingWindowPermission()
            }
        }, 2000)*/

        btnlaunchwidget.setOnClickListener {
            if(checkOverlayPermission()){
                val value: String = "from"// or just your string
                val intent1 = Intent(this@MainActivity,FloatingWidget::class.java)
                intent1.putExtra("value", value)
                startService(intent1)
                finish()
            }else{
                requestFloatingWindowPermission()
            }
        }
    }
/*    private fun isServiceRunning() : Boolean{
        val manager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (service in manager.getRunningServices(Int.MAX_VALUE)){
            if(FloatingWidget::class.java.name==service.service.className){
                return true
            }
        }
        return false
    }*/

    private fun requestFloatingWindowPermission() {
    val builder = AlertDialog.Builder(this)
        builder.setCancelable(true)
        builder.setTitle("Permission Request")
        builder.setMessage("Enable 'display over other apps' from settings")
        builder.setPositiveButton("Open Settings", DialogInterface.OnClickListener{
            dialogInterface, i ->  val intentt = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:$packageName"))
            startActivityForResult(intentt, RESULT_OK)
        })

        dialog = builder.create()
        dialog.show()
    }

    private fun checkOverlayPermission():Boolean{
      return  if(Build.VERSION.SDK_INT> Build.VERSION_CODES.M){
            Settings.canDrawOverlays(this)
        }
        else return true
    }
}