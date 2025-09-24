package com.example.hackatonprjoect.data.repositories

import android.util.Log
import androidx.annotation.WorkerThread
import com.example.hackatonprjoect.common.model.fids.FidsEntity
import com.example.hackatonprjoect.common.model.fids.FidsReqVO
import com.example.hackatonprjoect.common.model.fids.Flights
import com.example.hackatonprjoect.common.utils.Constants.baseLocationURL
import com.example.hackatonprjoect.core.network.ApiService
import com.visioglobe.visiomoveessential.models.VMEPosition
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import javax.inject.Inject

class MainRepository @Inject constructor(
    private val apiService: ApiService
) {

    suspend fun fetchFlightDetails(fidsReqVO: FidsReqVO): FidsEntity? {
        var flightData = apiService.fetchFlights(fidsReqVO)

        if (flightData.flights != null && flightData.flights?.isNotEmpty() == true) {
            return flightData.flights?.first()?.let { parseFlightsData(it) }
        } else {
            return null
        }
    }

    suspend fun createUser(deviceID: String, fcmToken: String): Flow<String> {

        return flow {
            val url = URL(baseLocationURL)
            var urlConnection: HttpURLConnection? = null
            try {
                urlConnection = url.openConnection() as HttpURLConnection
                urlConnection.requestMethod = "POST"
                urlConnection.doOutput = true // Enable sending data
                urlConnection.doInput = true // Enable receiving data
                urlConnection.setRequestProperty("Content-Type", "application/json")
    //            urlConnectio\n.connect() // Establish the connection


                val postData = "{" +
                        "  \"data\": {" +
                        "    \"name\": \"FAMILY\"," +
                        "    \"deviceId\": \"" + deviceID+"\","+
                        "    \"tokenId\": \"" + fcmToken +"\", "+
                        "    \"channelId\": \"4\"," +
                        "    \"lat\": \"" + "0" + "\","+
                        "    \"lon\": \"" + "0" + "\","+
                        "    \"floor\": \"" + "1" + "\""+
                        "  }" +
                        "}"

                Log.i("MainACtivity update", postData.toString())
                val postDataBytes = postData.toByteArray(Charsets.UTF_8)
                urlConnection.setRequestProperty("Content-Length", postDataBytes.size.toString())

                val outputStream = urlConnection.outputStream
                outputStream.write(postDataBytes)
                outputStream.flush()
                outputStream.close()

                val responseCode = urlConnection.responseCode
                if (responseCode == HttpURLConnection.HTTP_OK || responseCode == HttpURLConnection.HTTP_CREATED) {
                    val reader = BufferedReader(InputStreamReader(urlConnection.inputStream))
                    val response = StringBuilder()
                    var line: String?
                    while (reader.readLine().also { line = it } != null) {
                        response.append(line)
                    }
                    reader.close()

                    print("hisham"+response.toString());

                    Log.i("MainACtivity update", response.toString())

                    response.toString()

                    val json = JSONObject( response.toString())

                    val data = json.get("data") as JSONObject

                    val docID = data.getString("documentId")
                    Log.i("MainACtivity update",   "doc--------"+data.getString("documentId"))

    //                json.getString()



                    emit(response.toString())
                } else {
                    Log.i("MainACtivity update", "Error: HTTP Response Code $responseCode")

                    emit("Error: HTTP Response Code $responseCode")

                }
            } catch (e: Exception) {
                emit("Error: ${e.toString()}")
            } finally {
                urlConnection?.disconnect() // Close the connection to release resources
            }

        }
    }


    fun getOtherDeviceLocation(docID: String): Flow<String> {

        return flow {
            val urlStr = baseLocationURL +"/"+docID
            Log.e("--------","urlStr: $urlStr")
            val url = URL(urlStr)
            var urlConnection: HttpURLConnection? = null
            try {
                urlConnection = url.openConnection() as HttpURLConnection
                urlConnection.requestMethod = "GET" // Set the request method to GET

                urlConnection.setRequestProperty("Content-Type","application/json");
                urlConnection.connect() // Establish the connection

                val responseCode = urlConnection.responseCode
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    val reader = BufferedReader(InputStreamReader(urlConnection.inputStream))
                    val response = StringBuilder()
                    var line: String?
                    while (reader.readLine().also { line = it } != null) {
                        response.append(line)
                    }
                    reader.close()

                    print("hisham"+response.toString());

                    Log.e("--------","response: ${response.toString()}")

                    Log.i("MainACtivity", response.toString())

                    emit(response.toString())
                } else {
                    Log.e("--------","Error: HTTP Response Code: ${responseCode}")
                    emit("Error: HTTP Response Code $responseCode")
                }
            } catch (e: Exception) {
                Log.e("--------","Error: ${e.toString()}")
                emit("Error: ${e.toString()}")
            } finally {
                urlConnection?.disconnect() // Close the connection to release resources
            }
        }
    }

    @WorkerThread
    suspend fun updateCurrentLocation(pos: VMEPosition?, docID: String): String {


        val urlStr = baseLocationURL +"/"+docID
        Log.e("--------","urlStr: $urlStr")
        val url = URL(urlStr)
        var urlConnection: HttpURLConnection? = null
        try {
            urlConnection = url.openConnection() as HttpURLConnection
            urlConnection.requestMethod = "PUT"
            urlConnection.doOutput = true // Enable sending data
            urlConnection.doInput = true // Enable receiving data
            urlConnection.setRequestProperty("Content-Type", "application/json")
//            urlConnection.connect() // Establish the connection


            val postData = "{" +
                    "  \"data\": {" +

                    "    \"lat\": \"" + pos?.latitude.toString() + "\","+
                    "    \"lon\": \"" + pos?.longitude.toString() + "\","+
                    "    \"floor\": \"" + pos?.scene!!.floorID + "\""+
                    "  }" +
                    "}"


            Log.e("--------","postData: $postData")
            val postDataBytes = postData.toByteArray(Charsets.UTF_8)
            urlConnection.setRequestProperty("Content-Length", postDataBytes.size.toString())

            val outputStream = urlConnection.outputStream
            outputStream.write(postDataBytes)
            outputStream.flush()
            outputStream.close()

            val responseCode = urlConnection.responseCode
            if (responseCode == HttpURLConnection.HTTP_OK || responseCode == HttpURLConnection.HTTP_CREATED) {
                val reader = BufferedReader(InputStreamReader(urlConnection.inputStream))
                val response = StringBuilder()
                var line: String?
                while (reader.readLine().also { line = it } != null) {
                    response.append(line)
                }
                reader.close()


                print("hisham"+response.toString());

                Log.i("MainACtivity update", response.toString())

                Log.e("--------","response: $response.toString()")
                return response.toString()
            } else {
                Log.e("--------","response: Error: HTTP Response Code $responseCode")
                Log.i("MainACtivity update", "Error: HTTP Response Code $responseCode")

                return "Error: HTTP Response Code $responseCode"

            }
        } catch (e: Exception) {
            Log.e("--------","response: Error: catch ${e.toString()}")
            return "Error: ${e.toString()}"
        } finally {
            urlConnection?.disconnect() // Close the connection to release resources
        }
    }

    private fun parseFlightsData(flightDetails: Flights): FidsEntity {
        return try {
            FidsEntity(
                uid = flightDetails.uid,
                type = flightDetails.type.takeIf { !it.isNullOrEmpty() } ?: "Default Title",
                flightNumber = flightDetails.flightNumber.takeIf { !it.isNullOrEmpty() } ?: "QR001",
                airlineCode = flightDetails.airlineCode.takeIf { !it.isNullOrEmpty() } ?: "",
                aircraft = flightDetails.aircraft.takeIf { !it.isNullOrEmpty() } ?: "",
                destinationCode = flightDetails.destinationCode.takeIf { !it.isNullOrEmpty() }
                    ?: "COK",
                originCode = flightDetails.originCode.takeIf { !it.isNullOrEmpty() } ?: "DOH",
                scheduledTime = flightDetails.scheduledTime.takeIf { !it.isNullOrEmpty() } ?: "",
                estimateTime = flightDetails.estimateTime.takeIf { !it.isNullOrEmpty() } ?: "",
                Codeshare = flightDetails.codeshare.takeIf { !it.isNullOrEmpty() } ?: "EZ",
                SharedFlightNumber = flightDetails.sharedFlightNumber.takeIf { !it.isNullOrEmpty() }
                    ?: "",
                Stand = flightDetails.Stand.takeIf { !it.isNullOrEmpty() } ?: "",
                viaCode = flightDetails.viaCode.takeIf { !it.isNullOrEmpty() } ?: "",
                BaggageBelt = flightDetails.baggageBelt.takeIf { !it.isNullOrEmpty() } ?: "Belt",
                airlineLogo = flightDetails.airlineLogo.takeIf { !it.isNullOrEmpty() } ?: "",
                gateNoGeneral = flightDetails.gateNoGeneral.takeIf { !it.isNullOrEmpty() } ?: "",
                gateNoPremium = flightDetails.gateNoPremium.takeIf { !it.isNullOrEmpty() } ?: "",
                gateOpenGeneral = flightDetails.gateOpenGeneral.takeIf { !it.isNullOrEmpty() }
                    ?: "",
                gateOpenPremium = flightDetails.gateOpenPremium.takeIf { !it.isNullOrEmpty() }
                    ?: "",
                gateCloseGeneral = flightDetails.gateCloseGeneral.takeIf { !it.isNullOrEmpty() }
                    ?: "",
                gateClosePremium = flightDetails.gateClosePremium.takeIf { !it.isNullOrEmpty() }
                    ?: "",
                actualTimeOfDep = flightDetails.actualTimeOfDep.takeIf { !it.isNullOrEmpty() }
                    ?: "",
                checkInCounter = flightDetails.checkInCounter.takeIf { !it.isNullOrEmpty() } ?: "",
                checkInCounterDisplay = flightDetails.checkInCounterDisplay.takeIf { !it.isNullOrEmpty() }
                    ?: "",
                destinationImage = flightDetails.destination_image.takeIf { !it.isNullOrEmpty() }
                    ?: "",
                statusCode = flightDetails.statusCode.takeIf { !it.isNullOrEmpty() } ?: "",

                flightStatusEn = flightDetails.lang?.en?.flightStatus.takeIf { !it.isNullOrEmpty() }
                    ?: "Scheduled",
                flightStatusAr = flightDetails.lang?.ar?.flightStatus.takeIf { !it.isNullOrEmpty() }
                    ?: "Scheduled",

                airlineNameEn = flightDetails.lang?.en?.airlineName.takeIf { !it.isNullOrEmpty() }
                    ?: "",
                airlineNameAr = flightDetails.lang?.ar?.airlineName.takeIf { !it.isNullOrEmpty() }
                    ?: "",

                destinationCountryEn = flightDetails.lang?.en?.destinationCountry.takeIf { !it.isNullOrEmpty() }
                    ?: "",
                destinationCountryAr = flightDetails.lang?.ar?.destinationCountry.takeIf { !it.isNullOrEmpty() }
                    ?: "",

                destinationNameAr = flightDetails.lang?.ar?.destinationName.takeIf { !it.isNullOrEmpty() }
                    ?: "",
                destinationNameEn = flightDetails.lang?.en?.destinationName.takeIf { !it.isNullOrEmpty() }
                    ?: "",

                destinationCityEn = flightDetails.lang?.en?.destinationCity.takeIf { !it.isNullOrEmpty() }
                    ?: "",
                destinationCityAr = flightDetails.lang?.ar?.destinationCity.takeIf { !it.isNullOrEmpty() }
                    ?: "",

                originCountryEn = flightDetails.lang?.en?.originCountry.takeIf { !it.isNullOrEmpty() }
                    ?: "",
                originCountryAr = flightDetails.lang?.ar?.originCountry.takeIf { !it.isNullOrEmpty() }
                    ?: "",

                originNameAr = flightDetails.lang?.ar?.originName.takeIf { !it.isNullOrEmpty() }
                    ?: "",
                originNameEn = flightDetails.lang?.en?.originName.takeIf { !it.isNullOrEmpty() }
                    ?: "",

                originCityEn = flightDetails.lang?.en?.originCity.takeIf { !it.isNullOrEmpty() }
                    ?: "",
                originCityAr = flightDetails.lang?.ar?.originCity.takeIf { !it.isNullOrEmpty() }
                    ?: "",

                viaNameEn = flightDetails.lang?.en?.viaName.takeIf { !it.isNullOrEmpty() },
                viaNameAr = flightDetails.lang?.ar?.viaName.takeIf { !it.isNullOrEmpty() },
                viaCityEn = flightDetails.lang?.en?.viaCity.takeIf { !it.isNullOrEmpty() },
                viaCityAr = flightDetails.lang?.ar?.viaCity.takeIf { !it.isNullOrEmpty() },
                viaCountryEn = flightDetails.lang?.en?.viaCountry.takeIf { !it.isNullOrEmpty() },
                viaCountryAr = flightDetails.lang?.ar?.viaCountry.takeIf { !it.isNullOrEmpty() },


                locationId = flightDetails.LocationID.takeIf { !it.isNullOrEmpty() } ?: listOf(
                    "B01-UL001-IDC0063",
                    "B01-UL001-IDC0139",
                    "B01-UL001-IDC0062",
                    "B01-UL001-IDB0240",
                    "B01-UL001-IDB0239",
                    "B01-UL001-IDB0236",
                    "B01-UL001-IDB0246",
                    "B01-UL001-IDB0255",
                    "B01-UL001-IDB0250",
                    "B01-UL001-IDB0269",
                    "B01-UL001-IDB0272",
                    "B01-UL001-IDB0277",
                    "B01-UL001-IDB0279",
                    "B01-UL001-IDA0167",
                    "B01-UL001-IDA0162",
                    "B01-UL001-IDA0161",
                    "B01-UL001-IDA0172",
                    "B01-UL001-IDA0176",
                    "B01-UL001-IDA0182",
                    "B01-UL001-IDA0192",
                    "B01-UL001-IDA0197",
                    "B01-UL001-IDA0201",
                    "B01-UL001-IDA0221",
                    "B01-UL001-IDC0130",
                    "B01-UL001-IDC1116",
                    "B01-UL001-IDC1113",
                    "B01-UL000-IDC0068",
                    "B01-UL000-IDC0069",
                    "B01-UL000-IDC0070",
                    "B01-UL000-IDC0071",
                    "B01-UL001-IDC0443",
                    "B01-UL001-IDC0448",
                    "B01-UL001-IDC1118",
                    "B01-UL001-IDC1115",
                    "B01-UL001-IDC1117",
                    "B01-UL001-IDC1114",
                    "B01-UL001-IDC1111",
                    "B01-UL001-IDC1119",
                    "B01-UL001-IDC1110",
                    "B01-UL001-IDC1112",
                    "B01-UL001-IDC1108"
                ).random(),
            )

            // Assuming menuDao is your DAO instance
        } catch (exp: Exception) {
            exp.message?.let { /*Timber.tag("error").d(it)*/ }
            FidsEntity(uid = "", type = "Error", flightNumber = "N/A", airlineCode = "Unknown")
        }
    }

}