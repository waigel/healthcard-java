package com.waigel.healthcard.data.insurance

import com.fasterxml.jackson.annotation.JsonProperty

data class ZusatzinfosGKV(
    @JsonProperty("Zusatzinfos_Abrechnung_GKV")
    val zusatzinfosAbrechnungGKV: ZusatzinfosAbrechnungGKV,

    @JsonProperty("Versichertenart")
    val versichertenart: Long
)