package com.example.coroutine

import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.R
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

fun curThread() = println("curThread: [${Thread.currentThread().name},${Thread.currentThread().id}]")

class CoroutineActivity : AppCompatActivity(), CoroutineScope by MainScope() {
    val job = Job()
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    var TAG = "CoroutineActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_coroutine)
        curThread()

        /*button.setOnClickListener {
            launch(Dispatchers.Default) {
                curThread()
                Looper.prepare()
                Toast.makeText(baseContext,"测试",Toast.LENGTH_SHORT).show()
                Looper.loop()
            }
            curThread()
        }*/
    }
}
