package com.hynson.coroutine

import kotlinx.coroutines.*

fun curThread(msg:String) = println("curThread: [${Thread.currentThread().name},${Thread.currentThread().id}] log:$msg")

class CoroutineUse {

}

fun main(){
    // 1
    /*testBlock()
    curThread("main");*/

    // 2
    testJob()

    // 3
//    testAsync()
}

/**
 * block
 */
fun testBlock() = runBlocking {
    repeat(10){
        curThread("repeattime --> $it");
        delay(100)
    }
}

/**
 * job
 */
fun testJob() {
    var job = GlobalScope.launch {
        curThread("job")
        val start = System.currentTimeMillis()
        var t1 = withContext(Dispatchers.IO){
            delay(100)
            curThread("t1")
        }
        var t2 = withContext(Dispatchers.Default){
            delay(100)
            curThread("t2")
        }
        val now = System.currentTimeMillis()
        curThread("time ${now-start}")
    }
    Thread.sleep(300)
    curThread("main")
}

fun testAsync() = runBlocking{
    val job = async {
        delay(100L)
        curThread("async")

        return@async "result"
    }
    curThread(job.await())
}


