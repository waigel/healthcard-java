package com.waigel.healthcard.data.patient

import com.fasterxml.jackson.annotation.JsonProperty
import com.waigel.healthcard.data.patient.Land

data class StrassenAdresse(
    @JsonProperty("Ort")
    val ort: String,

    @JsonProperty("Postleitzahl")
    val postleitzahl: Long,

    @JsonProperty("Strasse")
    val strasse: String,

    @JsonProperty("Hausnummer")
    val hausnummer: Long,

    @JsonProperty("Land")
    val land: Land
)