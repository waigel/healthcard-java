package com.waigel.healthcard.data.patient

import com.fasterxml.jackson.annotation.JsonProperty

data class Person(
    @JsonProperty("StrassenAdresse")
    val strassenAdresse: StrassenAdresse,

    @JsonProperty("Geburtsdatum")
    val geburtsdatum: Long,

    @JsonProperty("Nachname")
    val nachname: String,

    @JsonProperty("Geschlecht")
    val geschlecht: String,

    @JsonProperty("Vorname")
    val vorname: String
)