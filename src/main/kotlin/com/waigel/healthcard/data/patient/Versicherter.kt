package com.waigel.healthcard.data.patient

import com.fasterxml.jackson.annotation.JsonProperty

data class Versicherter(
    @JsonProperty("Versicherten_ID")
    val versichertenID: String,

    @JsonProperty("Person")
    val person: Person
)