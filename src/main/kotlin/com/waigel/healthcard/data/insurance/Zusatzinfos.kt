package com.waigel.healthcard.data.insurance

import com.fasterxml.jackson.annotation.JsonProperty

data class Zusatzinfos(
    @JsonProperty("ZusatzinfosGKV")
    val zusatzinfosGKV: ZusatzinfosGKV
)