package com.example.hackatonprjoect.common.utils

import android.content.Context
import com.google.gson.Gson
import com.hia.common.model.content.MobileContent
import kotlinx.coroutines.flow.Flow
import java.io.IOException
import java.lang.reflect.Type
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit
import kotlin.math.abs

object HIAUtil {
    // For more complex types or values that might change, use compositionLocalOf
    var placeString: String = ""
    var categoriesString: String = ""
    const val DWELL_TIME_INHRS: Int = 24

    // this timestamp iois used on first launch service call. the value will be that datetime of creating inbuilt dbcontent before app release
    const val APP_RELEASE_TIMESTAMP: String = "1756989407000"


    fun listToStringWithoutBrackets(list: List<String>): String {
        return list.joinToString(separator = ",")
    }


    fun convertMillisToDate(millis: String): String {
        // Convert the millis string to a long value
        val timestamp = millis.toLong() * 1000  // Assuming the input is in seconds

        // Create a Date object from the timestamp
        val date = Date(timestamp)

        // Define the format pattern (HH:mm for hours and minutes)
        val formatter = SimpleDateFormat("HH:mm", Locale.getDefault())

        // Return the formatted date
        return formatter.format(date)
    }

    fun convertMillisToDate(millis: String, format: String): String {
        // Convert the millis string to a long value
        val timestamp = millis.toLong() * 1000  // Assuming the input is in seconds

        // Create a Date object from the timestamp
        val date = Date(timestamp)

        // Define the format pattern (HH:mm for hours and minutes)
        val formatter = SimpleDateFormat(format, Locale.getDefault())

        // Return the formatted date
        return formatter.format(date)
    }


    suspend fun createPlaceJson(
        content: Flow<List<MobileContent>>,
        selectedCategory: String?
    ): Map<String, Any?> {

        // Collect content from Flow
        val contentList = mutableListOf<MobileContent>()
        content.collect { contentList.addAll(it) }

        // Filter content by selected category if not null or empty
        val filteredContent = if (!selectedCategory.isNullOrEmpty()) {
            contentList.filter { it.content.mcnNtype == selectedCategory }
        } else {
            contentList
        }

        val contentMap = mutableMapOf<String, Map<String, Any?>>()
        val categoriesSet = mutableSetOf<String>() // Use a Set to store unique categories

        for (aContent in filteredContent) {
            val mcnNid = aContent.content.mcnNid
            val mcnMapLocation = aContent.content.mcnMapLocation
            val mcnType = aContent.content.mcnNtype?.lowercase()

            // Only proceed if mcnNid and mcnMapLocation are not null
            if (!mcnNid.isNullOrEmpty() && !mcnMapLocation.isNullOrEmpty() && (mcnType.equals("shop")
                        || mcnType.equals("dine")
                        || mcnType.equals("relax")
                        || mcnType.equals("gates_and_belts")
                        || mcnType.equals("facilities")
                        || mcnType.equals("lounge")
                        || mcnType.equals("art")
                        || mcnType.equals("page")
                        )
            ) {
                val mcnTitle = aContent.mcnContent.firstOrNull()?.mcnTitle
                val mcnBodyValue = aContent.mcnContent.firstOrNull()?.mcnBody
                val mcnCategory = aContent.content.mcnNtype
                // Add the category to the Set if it exists
                mcnCategory?.let { categoriesSet.add(it) }
                val mcnLogo = aContent.content.mcnContentLogo
                val mcnHeaderImage = aContent.mcnContent.firstOrNull()?.mcnHeaderImage


                // Process each location in mcnMapLocation
                for (vid in mcnMapLocation) {
                    val location = when {
                        vid.contains("IDA") -> "Concourse A"
                        vid.contains("IDB") -> "Concourse B"
                        else -> "Concourse C"
                    }
                    // Prepare the details map for each location
                    val detailMap = mutableMapOf<String, Any?>()

                    // Assign name with location suffix
                    mcnTitle?.let { detailMap["name"] = "$it".trim() } //"$it - $location"
                    // Assign description (body value)
                    mcnBodyValue?.let { detailMap["description"] = "$it".trim() }
                    // Assign categories
                    mcnCategory?.let { detailMap["categories"] = listOf(it) }

                    // Assign icon (logo)
                    mcnLogo?.let { detailMap["icon"] = "$it".trim() }


                    // Assign header image if available
                    mcnHeaderImage?.firstOrNull()?.let {
                        detailMap["imageURL"] = it.trim()
                    }

                    // Add the detailMap to contentMap if vid is not empty
                    if (vid.isNotEmpty()) {
                        contentMap[vid] = detailMap
                    }
                }
            }
        }
        // Convert the Set of unique categories to a Map
//        val categoriesMap = categoriesSet.associateWith { mutableMapOf("name" to it) }
        val categoriesMap = categoriesSet.associateWith {
            mutableMapOf("name" to it.replace("_", " "))
        }

        // Combine contentMap and categoriesMap into a single parent map
        val resultMap = mutableMapOf<String, Any?>()
        resultMap["places"] = contentMap
        resultMap["categories"] = categoriesMap
        return resultMap
    }


    fun getJsonFromAssets(context: Context, fileName: String): String? {
        return try {
            context.assets.open(fileName).use { inputStream ->
                val size = inputStream.available()
                val buffer = ByteArray(size)
                inputStream.read(buffer)
                String(buffer)
            }
        } catch (ex: IOException) {
            ex.printStackTrace()
            null
        }
    }

    fun getCurrentDate(): String {
        val calendar = Calendar.getInstance() // Get the current date instance
        val dateFormat =
            SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()) // Set the desired date format
        return dateFormat.format(calendar.time) // Format the current date
    }


    inline fun <reified T> parseJson(jsonString: String, type: Type): T {
        return Gson().fromJson(jsonString, type)
    }


    fun getDateAndMonthFromLong(timestamp: String): String {
        val timestamp = timestamp.toLong() * 1000
        val dateFormat = SimpleDateFormat("MMM dd", Locale.getDefault()) // "02 Oct" format
        val date = Date(timestamp) // Convert long timestamp to Date object
        return dateFormat.format(date) // Format the date
    }

    fun getDateFromTimestamp(timestamp: String): String {
        val timestamp = timestamp.toLong() * 1000
        val dateFormat = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault()) // "02 Oct" format
        val date = Date(timestamp) // Convert long timestamp to Date object
        return dateFormat.format(date) // Format the date
    }


    fun getTimeDifferenceInHours(timeInMilliSeconds: Long): Long {
        val currentTime = System.currentTimeMillis() // Get current time in milliseconds
        val difference = currentTime - timeInMilliSeconds // Find difference
        return TimeUnit.MILLISECONDS.toHours(difference) // Convert milliseconds to hours
    }

    fun getTimeDifferenceInMinutes(scheduledTimeMillis: Long, estimatedTimeMillis: Long): Long {
        val diffMillis = Math.abs(estimatedTimeMillis - scheduledTimeMillis)
        return TimeUnit.SECONDS.toMinutes(diffMillis)
    }


    // Helper function to convert a string to camel case
    fun String.toCamelCase(): String {
        return this.split(" ")
            .filter { it.isNotEmpty() }  // Remove empty strings that may come from multiple spaces
            .joinToString("") { word ->
                word.lowercase(Locale.ROOT).replaceFirstChar { it.uppercaseChar() }
            }.replaceFirstChar { it.lowercaseChar() } // Ensure the first character is lowercase
    }

    fun String.toTitleCase(): String {
        return this.split(" ")
            .filter { it.isNotEmpty() }  // Remove empty words caused by multiple spaces
            .joinToString(" ") { word ->
                word.lowercase(Locale.ROOT).replaceFirstChar { it.uppercaseChar() }
            }
    }

    fun String.formatHTMLString(lang: String): String {
        val formattedHtmlString = """
        <html>
        <head>
            <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">

            <style>
                body { font-size: 14px; line-height: 1.6; margin: 0; padding: 5px; 
        font-family:'Effra', 'San Francisco', Helvetica, Arial, san-serif;}   
          a { color: inherit; text-decoration: none; }
        </style>
        </head>
        <body lang="$lang" style="text-align: ${if (lang == "en") "left" else "right"};">
        $this
        </body>
        </html>
        """

        return formattedHtmlString

    }

    fun getBoardingTime(
        estimateTime: String?, scheduledTime: String?, isBoarding: Boolean
    ): Long {
        // Early return if either time is null or empty
        if (scheduledTime.isNullOrEmpty()) return 0L

        // Calculate time difference in minutes
        val diffMinutes = if (!estimateTime.isNullOrEmpty()) {
            val diffMillis = abs(estimateTime.toLong() - scheduledTime.toLong())
            TimeUnit.SECONDS.toMinutes(diffMillis)
        } else 0L

        // Determine which time to use based on conditions
        val baseTime = if (diffMinutes > 15) estimateTime else scheduledTime
        // Qatar is UTC+3
        val qatarOffset = ZoneOffset.ofHours(3)
        // Convert to LocalDateTime
        val dateTime = baseTime?.let {
            LocalDateTime.ofEpochSecond(
                it.toLong(), 0, qatarOffset
            )
        } ?: return 0L

        // Apply boarding time adjustment if needed
        val finalDateTime = if (isBoarding) {
            dateTime.minusHours(1)
        } else {
            dateTime
        }
        // Convert LocalDateTime back to milliseconds
        return finalDateTime.atZone(qatarOffset).toInstant().toEpochMilli() / 1000
    }


    /**
     * Is valid time 4 promotion notifitcation boolean.
     *
     * @param prevDate the prev date
     * @return the boolean
     */
    fun is24hrsPassed(prevDate: String?): Boolean {
        var state = true

        if (prevDate != null && prevDate.length > 0) {

            val cal = Calendar.getInstance()

            try {
                val calendar = Calendar.getInstance()
                calendar.timeInMillis = prevDate.toLong()

                calendar.add(Calendar.HOUR, DWELL_TIME_INHRS)
                val prevDateField = calendar.time
                val currentDateField = cal.time

                state = currentDateField.after(prevDateField)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        return state
    }

    fun is24hrsPassed(prevDate: Long): Boolean {
        var state = true
        if (prevDate > 0) {
            val cal = Calendar.getInstance()

            try {

                val calendar = Calendar.getInstance()
                calendar.timeInMillis = prevDate

                calendar.add(Calendar.HOUR, 24)
                val prevDateField = calendar.time

                val currentDateField = cal.time

                state = currentDateField.after(prevDateField)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        return state
    }


    fun convertMilliSec2TimeStamp(appReleaseTimestamp: String): String {
        //
        val date = Date(appReleaseTimestamp.toLong())
        val formatter: DateFormat = SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.ENGLISH)
        val dateFormatted = formatter.format(date)

        return dateFormatted
    }
}