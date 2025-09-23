package com.example.hackatonprjoect.common.model.fids

import com.google.gson.annotations.SerializedName

data class FlightData(
    @SerializedName("error") var error: Boolean? = null,
    @SerializedName("status") var status: Boolean? = null,
    @SerializedName("baseCity") var baseCity: String? = null,
    @SerializedName("flights") var flights: ArrayList<Flights>? = arrayListOf()
)

data class En(

    @SerializedName("originName") var originName: String? = null,
    @SerializedName("originCity") var originCity: String? = null,
    @SerializedName("originCountry") var originCountry: String? = null,
    @SerializedName("destinationName") var destinationName: String? = null,
    @SerializedName("destinationCity") var destinationCity: String? = null,
    @SerializedName("destinationCountry") var destinationCountry: String? = null,
    @SerializedName("viaName") var viaName: String? = null,
    @SerializedName("viaCity") var viaCity: String? = null,
    @SerializedName("viaCountry") var viaCountry: String? = null,
    @SerializedName("airlineName") var airlineName: String? = null,
    @SerializedName("flightStatus") var flightStatus: String? = null

)

data class Ar(

    @SerializedName("originName") var originName: String? = null,
    @SerializedName("originCity") var originCity: String? = null,
    @SerializedName("originCountry") var originCountry: String? = null,
    @SerializedName("destinationName") var destinationName: String? = null,
    @SerializedName("destinationCity") var destinationCity: String? = null,
    @SerializedName("destinationCountry") var destinationCountry: String? = null,
    @SerializedName("viaName") var viaName: String? = null,
    @SerializedName("viaCity") var viaCity: String? = null,
    @SerializedName("viaCountry") var viaCountry: String? = null,
    @SerializedName("airlineName") var airlineName: String? = null,
    @SerializedName("flightStatus") var flightStatus: String? = null

)

data class Lang(
    @SerializedName("en") var en: En? = En(), @SerializedName("ar") var ar: Ar? = Ar()
)


data class Flights(
    @SerializedName("uid") var uid: String,
    @SerializedName("type") var type: String? = null,
    @SerializedName("flightNumber") var flightNumber: String? = null,
    @SerializedName("airlineCode") var airlineCode: String? = null,
    @SerializedName("aircraft") var aircraft: String? = null,
    @SerializedName("destinationCode") var destinationCode: String? = null,
    @SerializedName("originCode") var originCode: String? = null,
    @SerializedName("scheduledTime") var scheduledTime: String? = null,
    @SerializedName("estimateTime") var estimateTime: String? = null,
    @SerializedName("Codeshare") var codeshare: String? = null,
    @SerializedName("SharedFlightNumber") var sharedFlightNumber: String? = null,
    @SerializedName("Stand") var Stand: String? = null,
    @SerializedName("viaCode") var viaCode: String? = null,
    @SerializedName("touchdownTime") var touchdownTime: String? = null,
    @SerializedName("actualTimeOfArr") var actualTimeOfArr: String? = null,
    @SerializedName("BaggageBelt") var baggageBelt: String? = null,
    @SerializedName("lang") var lang: Lang? = Lang(),
    @SerializedName("airlineLogo") var airlineLogo: String? = null,
    @SerializedName("statusCode") var statusCode: String? = null,
    @SerializedName("LocationID") var LocationID: String? = null,
    @SerializedName("gateNoGeneral") var gateNoGeneral: String? = null,
    @SerializedName("gateNoPremium") var gateNoPremium: String? = null,
    @SerializedName("gateOpenGeneral") var gateOpenGeneral: String? = null,
    @SerializedName("gateOpenPremium") var gateOpenPremium: String? = null,
    @SerializedName("gateCloseGeneral") var gateCloseGeneral: String? = null,
    @SerializedName("gateClosePremium") var gateClosePremium: String? = null,
    @SerializedName("actualTimeOfDep") var actualTimeOfDep: String? = null,
    @SerializedName("destination_image") var destination_image: String? = null,
    @SerializedName("checkInCounter") var checkInCounter: String? = null,
    @SerializedName("checkInCounterDisplay") var checkInCounterDisplay: String? = null,
)

