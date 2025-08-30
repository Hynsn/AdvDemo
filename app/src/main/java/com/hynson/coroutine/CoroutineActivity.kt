package com.hynson.coroutine

import android.os.Bundle
import androidx.lifecycle.LifecycleOwner
import com.fastdroid.ktbase.BaseActivity
import com.hynson.R
import com.hynson.databinding.ActivityCoroutineBinding
import com.hynson.remoteconfig.RemoteConfigManager
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

fun curThread() = println("curThread: [${Thread.currentThread().name},${Thread.currentThread().id}]")

class CoroutineActivity : BaseActivity<ActivityCoroutineBinding>(), CoroutineScope by MainScope() {
    val job = Job()
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    override fun initData(owner: LifecycleOwner, savedInstanceState: Bundle?) {
        super.initData(owner, savedInstanceState)

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
        bind.btnRemoteconfig.setOnClickListener {
            RemoteConfigManager.fetchAndActivate()
        }
    }
}
/*
协程解决的问题，使用同步思维达到异步的效果


串行思维

解决接口过多回调嵌套的情况

suspend 挂起函数，可能会执行异常操作调用需要传递，


协程原理
3个点
首次执行传入续体和上下文
with resume

挂起枚举状态

调用机传递，最终会调用invoke suspend 以此类推最后跳出循环。

suspend就是一个提醒功能，需要传递
怎么让多个协程同时执行，最后再一起执行？
async{}.await()
 */

