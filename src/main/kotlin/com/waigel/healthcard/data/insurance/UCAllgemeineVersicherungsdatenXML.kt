package com.waigel.healthcard.data.insurance

import com.fasterxml.jackson.annotation.JsonProperty

data class UCAllgemeineVersicherungsdatenXML(
    @JsonProperty("xmlns")
    val xmlns: String,

    @JsonProperty("CDM_VERSION")
    val cdmVersion: String,

    @JsonProperty("Versicherter")
    val versicherter: Versicherter
)