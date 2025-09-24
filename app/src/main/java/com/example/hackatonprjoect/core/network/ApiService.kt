package com.example.hackatonprjoect.core.network



import com.example.example.DeviceResponse
import com.example.hackatonprjoect.common.model.fids.FidsReqVO
import com.example.hackatonprjoect.common.model.fids.FlightData
import com.example.hackatonprjoect.common.model.notifications.PushMonitorReqVO
import com.example.hackatonprjoect.common.model.notifications.RegistartionResponse
import com.hia.common.model.notifications.RegistrationReqVO
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST


interface ApiService {

    @POST("/webservices/v2/fids") // Adjust the endpoint according to your API
    suspend fun fetchFlights(@Body fidsReqVO: FidsReqVO): FlightData // Change Response type based on your API

    @POST("/webservices/push/registration") // Adjust the endpoint according to your API
    suspend fun registerDeviceForNotification(@Body regReqVU: RegistrationReqVO): RegistartionResponse

    @POST("/webservices/push/monitor") // Adjust the endpoint according to your API
    suspend fun flightMonitoring(@Body regReqVU: PushMonitorReqVO): RegistartionResponse // Change Response type based on your API


    @GET("devices/ohlnggdtuj3tral255pwnd31") // Adjust the endpoint according to your API
    suspend fun getDeviceResponse(): DeviceResponse // Change Response type based on your API




}