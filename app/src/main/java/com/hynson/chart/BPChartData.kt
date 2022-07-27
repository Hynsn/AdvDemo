package com.hynson.chart

data class BPChartData(
    val spInMax:Int,
    val spInMin:Int,
    val dpInMax:Int,
    val dpInMin:Int,
    val maxTimestamp:Long,
    val minTimestamp:Long,
    val timeStr:String
)
