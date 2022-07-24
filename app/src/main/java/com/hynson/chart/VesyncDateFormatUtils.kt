package com.hynson.chart

import android.text.TextUtils
import java.text.SimpleDateFormat
import java.util.*

/**
 * Vesync 日期格式优化统一工具类
 *
 * 以下为各个线所用到的日期格式：
 *
 * 健康线：
 *   M/d
 *   M/d/yyyy
 *   M/yyyy
 *   MM/dd/yyyy
 *   MMM d, yyyy
 *   MMM d
 *
 * 厨电线：
 *
 *  yyyy/MM/dd, HH:mm 24小时制
 *  yyyy/M/d, h:mm aaa 12小时制
 *  MMM dd,yyyy at HH:mm:ss 24小时制
 *  MMM d,yyyy at h:mm:ss aaa 12小时制
 *  yyyy-MM-dd HH:mm:ss
 *
 * 净化器加湿线：
 * 平台：
 */

object VesyncDateFormatUtils {

    const val DatePattern_M = "M"
    const val DatePattern_M_d = "M/d"
    const val DatePattern_M_dd = "M/dd"
    const val DatePattern_M_d_yyyy = "M/d/yyyy"
    const val DatePattern_M_yyyy = "M/yyyy"
    const val DatePattern_MM_dd_yyyy = "MM/dd/yyyy"
    const val DatePattern_MMM_d_yyyy = "MMM d, yyyy"
    const val DatePattern_MMM_dd_yyyy = "MMM dd, yyyy"
    const val DatePattern_MMM_d = "MMM d"
    const val DatePattern_M_dd_yyyy = "M/dd/yyyy"
    const val DatePattern_M_d_yyyy_HH_mm = "M/d/yyyy HH:mm"
    const val DatePattern_M_d_yyyy_h_mm_a = "M/d/yyyy h:mm a"
    const val DatePattern_M_d_yyyy_ = "M-d-yyyy"

    const val DatePattern_yyyy_MM_dd_HH_mm = "yyyy/MM/dd, HH:mm"
    const val DatePattern_yyyy_M_d_H_mm_a = "yyyy/M/d, h:mm aaa"
    const val DatePattern_MMM_dd_yyyy_ = "MMM dd,yyyy"
    const val DatePattern_HH_mm_ss = "HH:mm:ss"
    const val DatePattern_MMM_d_yyyy_ = "MMM d,yyyy"
    const val DatePattern_h_mm_ss_a = "h:mm:ss aaa"
    const val DatePattern_yyyy_MM_dd_HH_mm_ss = "yyyy-MM-dd HH:mm:ss"

    /**
     * 传入毫秒值和相应格式，获取对应国家日期格式
     */
    fun dateFormatForSecondTimestamp(secondTimestamp: Long, pattern: String): String {
        val dateFormat = SimpleDateFormat(
            getLocalizePattern(pattern, getOriginalLocal()), getOriginalLocal())
        return dateFormat.format(Date(secondTimestamp * 1000))
    }

    fun dateFormatForMillionTimestamp(millionTimestamp: Long, pattern: String): String {
        val dateFormat = SimpleDateFormat(
            getLocalizePattern(pattern, getOriginalLocal()), getOriginalLocal())
        return dateFormat.format(Date(millionTimestamp))
    }

    fun dateFormatForDate(date: Date, pattern: String): String {
        val dateFormat = SimpleDateFormat(
            getLocalizePattern(pattern, getOriginalLocal()), getOriginalLocal())
        return dateFormat.format(date)
    }

    fun dateFormatForSecondTimestamp(secondTimestamp: Long, locale: Locale, pattern: String): String {
        val dateFormat = SimpleDateFormat(getLocalizePattern(pattern, locale), locale)
        return dateFormat.format(Date(secondTimestamp * 1000))
    }

    fun dateFormatForMillionTimestamp(millionTimestamp: Long, locale: Locale, pattern: String): String {
        val dateFormat = SimpleDateFormat(getLocalizePattern(pattern, locale), locale)
        return dateFormat.format(Date(millionTimestamp))
    }

    // 获取给定日期（long型的时间戳）所在星期的星期天到星期六的日期字符
    fun getSundayToSaturdayOfWeek(currentTimestamp: Long): String {
        val cal = convertLongToCalendar(currentTimestamp)
        val stringBuilder = StringBuilder()
        try {
            val days = cal[Calendar.DAY_OF_WEEK] - 2
            val dates = cal[Calendar.DAY_OF_MONTH]
            val months = cal[Calendar.MONTH]
            val years = cal[Calendar.YEAR]
            // 获取周日
            cal[years, months, dates - days - 1, 0, 0] = 0
            val sunday = cal.time
            val mondayStr = dateFormatForDate(sunday, DatePattern_M_d)
            // 获取周六
            cal[years, months, dates - days + 5, 23, 59] = 59
            val saturday = cal.time
            val sundayStr = dateFormatForDate(saturday, DatePattern_M_d)
            stringBuilder.append(mondayStr).append("-").append(sundayStr)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return stringBuilder.toString()
    }

    private fun convertLongToCalendar(currentTimestamp: Long): Calendar {
        //将毫秒数转化为日期
        val date = Date(currentTimestamp)
        val cal = Calendar.getInstance(getOriginalLocal())
        cal.time = date
        return cal
    }

    // 校验日期字符长度
    fun checkDateLength(date: String, datePatternLength: Int): Boolean {
        return !TextUtils.isEmpty(date) && date.length == datePatternLength
    }
    // 校验日期字符长度在某一范围
    fun checkDateLengthInRange(date: String, datePatternLength: Int): Boolean {
        return !TextUtils.isEmpty(date) && (date.length in datePatternLength - 1..datePatternLength)
    }

    // 获取对应日期格式
    fun getLocalizePattern(pattern: String, locale: Locale): String {
        val localJP = Locale("ja", "JP") // ja_JP
        val localDE = Locale("de", "DE") // de_DE
        val localFR = Locale("fr", "FR") // fr_FR
        val localUS = Locale("en", "US") // en_US
        val localGB = Locale("en", "GB") // en_GB
        val localIT = Locale("it", "IT") // it_IT
        val localES = Locale("es", "ES") // es_ES
        return when(locale) {
            localJP -> {
                when(pattern) {
                    "M/d" -> "M/d"
                    "M/dd" -> "M/dd"
                    "M/d/yyyy" -> "yyyy/M/d"
                    "M/dd/yyyy" -> "yyyy/M/dd"
                    "M-d-yyyy" -> "yyyy-M-d"
                    "M-dd-yyyy" -> "yyyy-M-dd"
                    "M/yyyy" -> "yyyy/M"
                    "MM/dd/yyyy" -> "yyyy/MM/dd"
                    "MMM d, yyyy" -> "yyyy年M月d日"
                    "MMM dd, yyyy" -> "yyyy年M月dd日"
                    "MMM d" -> "M月d日"
                    "yyyy/MM/dd, HH:mm" -> "yyyy/MM/dd H:mm"
                    "yyyy/M/d, h:mm aaa" -> "yyyy/M/d aaaK:mm"
                    "MMM dd,yyyy" -> "yyyy年M月dd日"
                    "HH:mm:ss" -> "H:mm:ss"
                    "MMM d,yyyy" -> "yyyy年M月d日"
                    "h:mm:ss aaa" -> "aaaK:mm:ss"
                    "yyyy-MM-dd HH:mm:ss" -> "yyyy/MM/dd H:mm:ss"
                    "M/d/yyyy HH:mm" -> "yyyy/M/d HH:mm"
                    "M/d/yyyy h:mm a" -> "yyyy/M/d h:mm a"
                    else -> pattern
                }
            }

            localDE -> {
                when(pattern) {
                    "M/d" -> "d.M."
                    "M/dd" -> "dd.M."
                    "M/d/yyyy" -> "d.M.yyyy"
                    "M/dd/yyyy" -> "dd.M.yyyy"
                    "M-d-yyyy" -> "d-M-yyyy"
                    "M-dd-yyyy" -> "dd-M-yyyy"
                    "M/yyyy" -> "M.yyyy"
                    "MM/dd/yyyy" -> "dd.MM.yyyy"
                    "MMM d, yyyy" -> "d. MMM yyyy"
                    "MMM dd, yyyy" -> "dd. MMM yyyy"
                    "MMM d" -> "d. MMM"
                    "yyyy/MM/dd, HH:mm" -> "dd.MM.yyyy, HH:mm"
                    "yyyy/M/d, h:mm aaa" -> "d.M.yyyy, h:mm aaa"
                    "MMM dd,yyyy" -> "dd. MMM yyyy"
                    "HH:mm:ss" -> "HH:mm:ss"
                    "MMM d,yyyy" -> "d. MMM yyyy"
                    "h:mm:ss aaa" -> "h:mm:ss aaa"
                    "yyyy-MM-dd HH:mm:ss" -> "dd.MM.yyyy, HH:mm:ss"
                    "M/d/yyyy HH:mm" -> "d.M.yyyy HH:mm"
                    "M/d/yyyy h:mm a" -> "d.M.yyyy h:mm a"
                    else -> pattern
                }
            }

            localFR -> {
                when(pattern) {
                    "M/d" -> "dd/MM"
                    "M/dd" -> "dd/MM"
                    "M/d/yyyy" -> "dd/MM/yyyy"
                    "M/dd/yyyy" -> "dd/MM/yyyy"
                    "M-d-yyyy" -> "dd-MM-yyyy"
                    "M-dd-yyyy" -> "dd-MM-yyyy"
                    "M/yyyy" -> "MM/yyyy"
                    "MM/dd/yyyy" -> "dd/MM/yyyy"
                    "MMM d, yyyy" -> "d MMM yyyy"
                    "MMM dd, yyyy" -> "dd MMM yyyy"
                    "MMM d" -> "d MMM"
                    "yyyy/MM/dd, HH:mm" -> "dd/MM/yyyy à HH:mm"
                    "yyyy/M/d, h:mm aaa" -> "dd/MM/yyyy à h:mm aaa"
                    "MMM dd,yyyy" -> "dd MMM yyyy"
                    "HH:mm:ss" -> "HH:mm:ss"
                    "MMM d,yyyy" -> "d MMM yyyy"
                    "h:mm:ss aaa" -> "h:mm:ss aaa"
                    "yyyy-MM-dd HH:mm:ss" -> "dd/MM/yyyy à HH:mm:ss"
                    "M/d/yyyy HH:mm" -> "dd/MM/yyyy HH:mm"
                    "M/d/yyyy h:mm a" -> "dd/MM/yyyy h:mm a"
                    else -> pattern
                }
            }

            localUS -> {
                when(pattern) {
                    "M/d" -> "M/d"
                    "M/dd" -> "M/dd"
                    "M/d/yyyy" -> "M/d/yyyy"
                    "M/dd/yyyy" -> "M/dd/yyyy"
                    "M-d-yyyy" -> "M-d-yyyy"
                    "M-dd-yyyy" -> "M-dd-yyyy"
                    "M/yyyy" -> "M/yyyy"
                    "MM/dd/yyyy" -> "MM/dd/yyyy"
                    "MMM d, yyyy" -> "MMM d, yyyy"
                    "MMM dd, yyyy" -> "MMM dd, yyyy"
                    "MMM d" -> "MMM d"
                    "yyyy/MM/dd, HH:mm" -> "MM/dd/yyyy, HH:mm"
                    "yyyy/M/d, h:mm aaa" -> "M/d/yyyy, h:mm aaa"
                    "MMM dd,yyyy" -> "MMM dd, yyyy"
                    "HH:mm:ss" -> "HH:mm:ss"
                    "MMM d,yyyy" -> "MMM d, yyyy"
                    "h:mm:ss aaa" -> "h:mm:ss aaa"
                    "yyyy-MM-dd HH:mm:ss" -> "MM/dd/yyyy, HH:mm:ss"
                    "M/d/yyyy HH:mm" -> "M/d/yyyy HH:mm"
                    "M/d/yyyy h:mm a" -> "M/d/yyyy h:mm a"
                    else -> pattern
                }
            }

            localGB -> {
                when(pattern) {
                    "M/d" -> "dd/MM"
                    "M/dd" -> "dd/MM"
                    "M/d/yyyy" -> "dd/MM/yyyy"
                    "M/dd/yyyy" -> "dd/MM/yyyy"
                    "M-d-yyyy" -> "dd-MM-yyyy"
                    "M-dd-yyyy" -> "dd-MM-yyyy"
                    "M/yyyy" -> "MM/yyyy"
                    "MM/dd/yyyy" -> "dd/MM/yyyy"
                    "MMM d, yyyy" -> "d MMM yyyy"
                    "MMM dd, yyyy" -> "dd MMM yyyy"
                    "MMM d" -> "d MMM"
                    "yyyy/MM/dd, HH:mm" -> "dd/MM/yyyy, HH:mm"
                    "yyyy/M/d, h:mm aaa" -> "dd/MM/yyyy, h:mm aaa"
                    "MMM dd,yyyy" -> "dd MMM yyyy"
                    "HH:mm:ss" -> "HH:mm:ss"
                    "MMM d,yyyy" -> "d MMM yyyy"
                    "h:mm:ss aaa" -> "h:mm:ss aaa"
                    "yyyy-MM-dd HH:mm:ss" -> "dd/MM/yyyy, HH:mm:ss"
                    "M/d/yyyy HH:mm" -> "dd/MM/yyyy HH:mm"
                    "M/d/yyyy h:mm a" -> "dd/MM/yyyy h:mm a"
                    else -> pattern
                }
            }

            localIT -> {
                when(pattern) {
                    "M/d" -> "d/M"
                    "M/dd" -> "dd/M"
                    "M/d/yyyy" -> "d/M/yyyy"
                    "M/dd/yyyy" -> "d/M/yyyy"
                    "M-d-yyyy" -> "d-M-yyyy"
                    "M-dd-yyyy" -> "d-M-yyyy"
                    "M/yyyy" -> "M/yyyy"
                    "MM/dd/yyyy" -> "dd/MM/yyyy"
                    "MMM d, yyyy" -> "d MMM yyyy"
                    "MMM dd, yyyy" -> "dd MMM yyyy"
                    "MMM d" -> "d MMM"
                    "yyyy/MM/dd, HH:mm" -> "dd/MM/yyyy, HH:mm"
                    "yyyy/M/d, h:mm aaa" -> "d/M/yyyy, h:mm aaa"
                    "MMM dd,yyyy" -> "dd MMM yyyy"
                    "HH:mm:ss" -> "HH:mm:ss"
                    "MMM d,yyyy" -> "d MMM yyyy"
                    "h:mm:ss aaa" -> "h:mm:ss aaa"
                    "yyyy-MM-dd HH:mm:ss" -> "dd/MM/yyyy, HH:mm:ss"
                    "M/d/yyyy HH:mm" -> "d/M/yyyy HH:mm"
                    "M/d/yyyy h:mm a" -> "d/M/yyyy h:mm a"
                    else -> pattern
                }
            }

            localES -> {
                when(pattern) {
                    "M/d" -> "d/M"
                    "M/dd" -> "dd/M"
                    "M/d/yyyy" -> "d/M/yyyy"
                    "M/dd/yyyy" -> "dd/M/yyyy"
                    "M-d-yyyy" -> "d-M-yyyy"
                    "M-dd-yyyy" -> "dd-M-yyyy"
                    "M/yyyy" -> "M/yyyy"
                    "MM/dd/yyyy" -> "dd/MM/yyyy"
                    "MMM d, yyyy" -> "d MMM yyyy"
                    "MMM dd, yyyy" -> "dd MMM yyyy"
                    "MMM d" -> "d MMM"
                    "yyyy/MM/dd, HH:mm" -> "dd/MM/yyyy H:mm"
                    "yyyy/M/d, h:mm aaa" -> "d/M/yyyy h:mm aaa"
                    "MMM dd,yyyy" -> "dd MMM yyyy"
                    "HH:mm:ss" -> "H:mm:ss"
                    "MMM d,yyyy" -> "d MMM yyyy"
                    "h:mm:ss aaa" -> "h:mm:ss aaa"
                    "yyyy-MM-dd HH:mm:ss" -> "dd/MM/yyyy H:mm:ss"
                    "M/d/yyyy HH:mm" -> "d/M/yyyy HH:mm"
                    "M/d/yyyy h:mm a" -> "d/M/yyyy h:mm a"
                    else -> pattern
                }
            }

            else -> pattern
        }
    }

    fun getOriginalLocal() : Locale {
        return Locale.getDefault()
    }
}
