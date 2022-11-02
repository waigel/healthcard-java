package com.waigel.healthcard.data.insurance

import com.fasterxml.jackson.annotation.JsonProperty

data class Versicherter(
    @JsonProperty(value = "Versicherungsschutz")
    val versicherungsschutz: Versicherungsschutz,

    @JsonProperty(value = "Zusatzinfos")
    val zusatzinfos: Zusatzinfos
)