package com.example.hackatonprjoect.common.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.provider.Settings
import androidx.fragment.app.FragmentActivity
import com.example.hackatonprjoect.common.model.fids.FidsReqVO
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

object Utility {
    fun parseBP(boardingPass: String?, context: Context): FidsReqVO? {
        val paxName = boardingPass?.substring(2, 22)?.trim()
        val fromTo = boardingPass?.substring(30, 36)?.trim()
        val globalSearch = fromTo?.substring(3, 6)?.trim()
        var flightNo = boardingPass?.substring(36, 44)?.trim()
        val flightDate = boardingPass?.substring(44, 47)?.trim()
        var fidsReqVO: FidsReqVO? = null

        if (flightNo != null) {
            if (flightNo.length > 5) {
                val separated =
                    flightNo.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                if (separated.size > 0 && separated[1].length > 3 && separated[1].startsWith("0")) {
                    flightNo = flightNo.substring(0, 3) + flightNo.substring(3 + 1)
                }

                var calendar = Calendar.getInstance()
                val bpCalendar = Calendar.getInstance()
                val serviceCalendar = Calendar.getInstance()

                /*   Log.i("Year flightDate", calendar.get(Calendar.DAY_OF_YEAR) + "flightDate" + flightDate);
                Log.i("Year before", calendar.get(Calendar.YEAR) + "YEAR");

        */
                serviceCalendar[Calendar.DAY_OF_YEAR] = flightDate?.toIntOrNull() ?: -1

                //        Log.i("Year serviceCalendar", serviceCalendar.get(Calendar.YEAR) + "YEAR");
                if (flightDate != null) {
                    if (flightDate.toInt() < 5 && calendar[Calendar.MONTH] == 11) {
                        bpCalendar[Calendar.YEAR] = calendar[Calendar.YEAR] + 1
                        bpCalendar[Calendar.DAY_OF_YEAR] = 1

                        val diff = bpCalendar.timeInMillis - calendar.timeInMillis
                        // Calculate difference in days
                        val diffDays = diff / (24 * 60 * 60 * 1000)

                        if (diffDays < 4) {
                            bpCalendar[Calendar.DAY_OF_YEAR] = flightDate.toInt()
                            calendar = bpCalendar
                        } else {
                            calendar = serviceCalendar
                        }
                    } else {
                        calendar = serviceCalendar
                    }
                }

                val passDate = Date(calendar.timeInMillis)

                calendar.set(Calendar.DAY_OF_YEAR, Integer.valueOf(flightDate))

                val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
                val dateStart = dateFormat.format(passDate) + " 00:00:01"
                val dateEnd = dateFormat.format(passDate) + " 23:59:59"

                fidsReqVO = FidsReqVO(
                    null,
                    startTime = dateStart,
                    endTime = dateEnd,
                    globalSearch = flightNo.replace(" ", ""),
                    type = "departures",
                    limit = "1",
                    order = "ASC"
                )
            }
        }
        return fidsReqVO
    }


    @SuppressLint("HardwareIds")
    fun getDeviceId(context: Activity?): String {
        if (context != null) {
            return Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
        }

        return "Default"
    }
}