package com.example.aidl

import android.app.Service
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.widget.TextView
import android.widget.Toast
import com.example.aidl.client.R
import com.example.aidl.service.Dto

class MainActivity : AppCompatActivity() {

    private var aidlManage: IAIDLManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val intent = Intent()
        // 注意在 Android 5.0以后，不能通过隐式 Intent 启动 service，必须制定包名
        intent.setClassName("com.aidl.server", "com.example.aidl.service.DemoIPCService")
        bindService(intent, object : ServiceConnection {
            override fun onServiceConnected(p0: ComponentName?, p1: IBinder?) {
                Toast.makeText(this@MainActivity, "connected", Toast.LENGTH_SHORT).show()
                this@MainActivity.aidlManage = IAIDLManager.Stub.asInterface(p1)
            }

            override fun onServiceDisconnected(p0: ComponentName?) {
                Toast.makeText(this@MainActivity, "disconnect", Toast.LENGTH_SHORT).show()
            }
        }, Service.BIND_AUTO_CREATE)
        findViewById<TextView>(R.id.btn_from_service).setOnClickListener {
            findViewById<TextView>(R.id.tv_from_service).text = aidlManage?.dataFromIPC?.data1
        }
        findViewById<TextView>(R.id.btn_to_service).setOnClickListener {
            val dto = Dto()
            dto.data1 = "数据1"
            aidlManage?.setDataToIPC(dto, object : IAIDLListener.Stub() {
                override fun onSuccess(message: String?) {
                    findViewById<TextView>(R.id.tv_to_service).text = message
                }

                override fun onError() {
                }

            })
        }
        findViewById<TextView>(R.id.btn_to_activity).setOnClickListener {
            aidlManage?.startIPCActivity(1)
        }
    }
}