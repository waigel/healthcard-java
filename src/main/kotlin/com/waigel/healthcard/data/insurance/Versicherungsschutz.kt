package com.waigel.healthcard.data.insurance

import com.fasterxml.jackson.annotation.JsonProperty

data class Versicherungsschutz(
    @JsonProperty("Beginn")
    val beginn: Long,

    @JsonProperty("Kostentraeger")
    val kostentraeger: Kostentraeger
)