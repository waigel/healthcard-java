package com.waigel.healthcard.data.patient

import com.fasterxml.jackson.annotation.JsonProperty

data class Land(
    @JsonProperty("Wohnsitzlaendercode")
    val wohnsitzlaendercode: String
)