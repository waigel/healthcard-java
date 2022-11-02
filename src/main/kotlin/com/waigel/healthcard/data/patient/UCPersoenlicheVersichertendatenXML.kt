package com.waigel.healthcard.data.patient

import com.fasterxml.jackson.annotation.JsonProperty

data class UCPersoenlicheVersichertendatenXML(
    @JsonProperty("xmlns")
    val xmlns: String,

    @JsonProperty("CDM_VERSION")
    val cdmVersion: String,

    @JsonProperty("Versicherter")
    val versicherter: Versicherter
)