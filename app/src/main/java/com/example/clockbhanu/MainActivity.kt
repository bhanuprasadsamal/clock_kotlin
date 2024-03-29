package com.example.clockbhanu

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Handler
import android.provider.AlarmClock
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.TimePicker
import android.widget.Toast
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var timeHour: EditText
    private lateinit var timeMinute: EditText
    private lateinit var setTime: Button
    private lateinit var setAlarm: Button
    private lateinit var textClock: TextView // TextView for digital clock
    private lateinit var timePickerDialog: TimePickerDialog
    private lateinit var calendar: Calendar
    private var currentHour: Int = 0
    private var currentMinute: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        timeHour = findViewById(R.id.etHour)
        timeMinute = findViewById(R.id.etMinute)
        setTime = findViewById(R.id.btnTime)
        setAlarm = findViewById(R.id.btnAlarm)
        textClock = findViewById(R.id.textClock) // Initialize the TextView

        setTime.setOnClickListener {
            calendar = Calendar.getInstance()
            currentHour = calendar.get(Calendar.HOUR_OF_DAY)
            currentMinute = calendar.get(Calendar.MINUTE)

            timePickerDialog = TimePickerDialog(this, TimePickerDialog.OnTimeSetListener { timePicker: TimePicker, hourOfDay: Int, minutes: Int ->
                timeHour.setText(String.format("%02d", hourOfDay))
                timeMinute.setText(String.format("%02d", minutes))
            }, currentHour, currentMinute, false)

            timePickerDialog.show()
        }

        setAlarm.setOnClickListener {
            if (timeHour.text.isNotEmpty() && timeMinute.text.isNotEmpty()) {
                val intent = Intent(AlarmClock.ACTION_SET_ALARM).apply {
                    putExtra(AlarmClock.EXTRA_HOUR, timeHour.text.toString().toInt())
                    putExtra(AlarmClock.EXTRA_MINUTES, timeMinute.text.toString().toInt())
                    putExtra(AlarmClock.EXTRA_MESSAGE, "1st Alarm")
                }

                val intent2 = Intent(AlarmClock.ACTION_SET_ALARM).apply {
                    putExtra(AlarmClock.EXTRA_HOUR, timeHour.text.toString().toInt())
                    putExtra(AlarmClock.EXTRA_MINUTES, timeMinute.text.toString().toInt() + 5)
                    putExtra(AlarmClock.EXTRA_MESSAGE, "2nd alarm")
                }

                val intent3 = Intent(AlarmClock.ACTION_SET_ALARM).apply {
                    putExtra(AlarmClock.EXTRA_HOUR, timeHour.text.toString().toInt())
                    putExtra(AlarmClock.EXTRA_MINUTES, timeMinute.text.toString().toInt() + 10)
                    putExtra(AlarmClock.EXTRA_MESSAGE, "3rd alarm")
                }

                if (intent.resolveActivity(packageManager) != null) {
                    startActivity(intent)
                    finish()

                    val handler2 = Handler()
                    handler2.postDelayed({
                        startActivity(intent2)
                        finish()
                    }, 300)

                    val handler3 = Handler()
                    handler3.postDelayed({
                        startActivity(intent3)
                        finish()
                    }, 700)

                } else {
                    Toast.makeText(this, "There is no app that supports this action", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Please choose a time", Toast.LENGTH_SHORT).show()
            }
        }

        // Update the digital clock every second
        val handler = Handler()
        handler.post(object : Runnable {
            override fun run() {
                val currentTime = Calendar.getInstance().time
                val timeFormat = android.text.format.DateFormat.getTimeFormat(applicationContext)
                val formattedTime = timeFormat.format(currentTime)
                textClock.text = "Current Time: $formattedTime"
                handler.postDelayed(this, 1000)
            }
        })
    }
}
