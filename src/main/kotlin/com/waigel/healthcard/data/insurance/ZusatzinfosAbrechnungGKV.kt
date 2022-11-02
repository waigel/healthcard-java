package com.waigel.healthcard.data.insurance

import com.fasterxml.jackson.annotation.JsonProperty

data class ZusatzinfosAbrechnungGKV(
    @JsonProperty("WOP")
    val wop: Long
)