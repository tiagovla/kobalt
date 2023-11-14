package com.example.kobalt

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import java.util.*
import android.os.Handler
import android.os.Looper


class MainActivity : AppCompatActivity() {

    private lateinit var button: Button
    private lateinit var mainHandler: Handler

    private var time: Long = 0
    private var accumulatedTime: Long = 0
    private var isPaused: Boolean = true

    private val updateTextTask = object : Runnable {
        override fun run() {
            updateTimer()
            mainHandler.postDelayed(this, 1000)
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button = findViewById<Button>(R.id.button)
        button.setOnClickListener {
            if (isPaused){
                unpauseTimer()
            }
            else{
                pauseTimer()
            }
        }
        button.setOnLongClickListener{
            resetTimer()
            return@setOnLongClickListener true
        }
        button.text = formatTime(0)
        mainHandler = Handler(Looper.getMainLooper())
    }

    private fun unpauseTimer(){
        time = System.currentTimeMillis()
        mainHandler.post(updateTextTask)
        isPaused = false
    }
    private fun pauseTimer(){
        accumulatedTime += elapsedTime()
        mainHandler.removeCallbacks(updateTextTask)
        isPaused = true
    }


    private fun resetTimer(){
        pauseTimer()
        accumulatedTime = 0
        button.text = formatTime(0)
    }

    private fun elapsedTime(): Long = System.currentTimeMillis() - time

    private fun formatTime(t: Long): String{
        val seconds = (t / 1000).toInt()
        val minutes = seconds / 60
        val hours = minutes / 60
        return String.format(Locale.getDefault(), "%d:%02d:%02d", hours, minutes.mod(60), seconds.mod(60))
    }

    private fun updateTimer(){
            val elapsedTime = accumulatedTime + elapsedTime()
            button.text = formatTime(elapsedTime)
    }
}
