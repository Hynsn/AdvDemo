package com.hynson.chart

data class BloodPressureChartData(
    val spInMmHgMax:Int,
    val spInMmHgMin:Int,
    val dpInMmHgMax:Int,
    val dpInMmHgMin:Int,
    val spInKpaMax:Int,
    val spInKpaMin:Int,
    val dpInKpaMax:Int,
    val dpInKpaMin:Int,
    val maxTimestamp:Long,
    val minTimestamp:Long,
    val timeStr:String
)
