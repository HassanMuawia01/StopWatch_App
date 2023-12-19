package com.example.stopwatchapp

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Chronometer
import android.widget.NumberPicker
import android.widget.Toast
import com.example.stopwatchapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    var isRunning = false
    private var minutes: String? = "00.00.00"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // For listView data show
        var lapsList = ArrayList<String>()
        var arrayAdapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, lapsList)
        binding.listView.adapter = arrayAdapter
        binding.Labbutton.setOnClickListener {
            if (isRunning) {
                lapsList.add(binding.chronometer.text.toString())
                arrayAdapter.notifyDataSetChanged()
            }
        }

        // Click clock img and show Dialog to pick timer
        binding.clock.setOnClickListener {
            val dialog = Dialog(this)
            dialog.setContentView(R.layout.dialog) // Use the correct layout resource
            val numberPicker = dialog.findViewById<NumberPicker>(R.id.numberPicker)
            numberPicker.minValue = 0
            numberPicker.maxValue = 5
            dialog.findViewById<Button>(R.id.setTime).setOnClickListener {
                minutes = numberPicker.value.toString()
                binding.clockTime.text =
                    dialog.findViewById<NumberPicker>(R.id.numberPicker).value.toString() + " minutes"
                dialog.dismiss()
            }
            dialog.show()
        }
        binding.rest.setOnClickListener{
            binding.chronometer.base = SystemClock.elapsedRealtime()
        }
        binding.RunBtn.setOnClickListener {
            if (!isRunning) {
                isRunning = true // Set isRunning to true when starting
                if (!minutes.equals("00.00.00")) {
                    var totalMins = minutes!!.toInt()*60*1000L
                    binding.chronometer.base = SystemClock.elapsedRealtime() + totalMins
                    binding.chronometer.format = "%S %S"
                    binding.chronometer.onChronometerTickListener =
                        Chronometer.OnChronometerTickListener {
                            var elapsedTime = SystemClock.elapsedRealtime()- binding.chronometer.base
                            if (elapsedTime <= totalMins) {
                                Toast.makeText(this,"chronomter",Toast.LENGTH_LONG).show()

                                binding.chronometer.stop()
                                isRunning = false // Set isRunning to false when stopping
                                binding.RunBtn.text = "Run"
                            }
                        }
                } else {
                     isRunning = true // Set isRunning to true when starting
                    binding.chronometer.base = SystemClock.elapsedRealtime()
                    binding.RunBtn.text = "Stop"
                    binding.chronometer.start()
                }
            }
            else {
                binding.chronometer.stop()
                isRunning = false // Set isRunning to false when stopping
                binding.RunBtn.text = "Run"
            }


        }
    }
}
