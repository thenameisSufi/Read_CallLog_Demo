package main.example.myapplication

import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Binder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.CallLog
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewbinding.ViewBindings
import main.example.myapplication.adapter.MyCallLogAdapter
import main.example.myapplication.data.CallLogModel
import main.example.myapplication.databinding.ActivityMainBinding
import java.text.SimpleDateFormat
import java.util.Calendar

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    val CALLLOG_REQUEST_CODE = 201
    lateinit var cursor: Cursor
    lateinit var callLogModel: CallLogModel
    lateinit var callLogAdapter: MyCallLogAdapter
    val allCallLogList = ArrayList<CallLogModel>()
    lateinit var calendar: Calendar
    lateinit var simpleDateFormat: SimpleDateFormat
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.ReadCallLogRecycle.setHasFixedSize(true)
        binding.ReadCallLogRecycle.layoutManager = LinearLayoutManager(this@MainActivity,LinearLayoutManager.VERTICAL,false)

        callLogAdapter = MyCallLogAdapter(this@MainActivity,allCallLogList)
        binding.ReadCallLogRecycle.adapter = callLogAdapter

        binding.loadCallLog.setOnClickListener {
            permissionInit()
        }
    }

    private fun permissionInit() {
        if (ContextCompat.checkSelfPermission(this@MainActivity, android.Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this@MainActivity, arrayOf(android.Manifest.permission.READ_CALL_LOG), CALLLOG_REQUEST_CODE
            )
        else {

            loadAllCallLogInfo()

        }
    }

    private fun loadAllCallLogInfo() {
        val contentResolver = contentResolver
        val uri = CallLog.Calls.CONTENT_URI
        val projection = arrayOf(CallLog.Calls.CACHED_NAME,CallLog.Calls.NUMBER,CallLog.Calls.DATE,CallLog.Calls.DURATION,CallLog.Calls.TYPE)
        val selection = null
        val args = null
        val sortedOrder = CallLog.Calls.DATE + " DESC"

        cursor = contentResolver.query(uri,projection,selection,args,sortedOrder)!!
        if (cursor.count>0 && cursor!= null) {

            while (cursor.moveToNext()) {
                val name = cursor.getString(cursor.getColumnIndexOrThrow(CallLog.Calls.CACHED_NAME))
                val number = cursor.getString(cursor.getColumnIndexOrThrow(CallLog.Calls.NUMBER))
                val date = cursor.getLong(cursor.getColumnIndexOrThrow(CallLog.Calls.DATE))
                val duration = cursor.getLong(cursor.getColumnIndexOrThrow(CallLog.Calls.DURATION))
                val type = cursor.getString(cursor.getColumnIndexOrThrow(CallLog.Calls.TYPE))

                val calendar = Calendar.getInstance()
                calendar.timeInMillis = date

                val hours = (duration / 3600)
                val minutes = (duration % 3600) / 60
                val seconds = (duration % 60)

                val formattedDuration = String.format("%02d:%02d:%02d", hours, minutes, seconds)

                val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm:ss aaa z")
                val dateTime = simpleDateFormat.format(calendar.time)

                val callLogModel = CallLogModel("$name", number, dateTime, formattedDuration, type)
                allCallLogList.add(callLogModel)
                callLogAdapter.notifyDataSetChanged()
            }

        } else {
                Toast.makeText(this@MainActivity,"No Call Log Found on this Device",Toast.LENGTH_SHORT).show()
            }



    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            CALLLOG_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    loadAllCallLogInfo()
                } else {
                    Toast.makeText(this@MainActivity,"No Permission Granted For Call Log", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}