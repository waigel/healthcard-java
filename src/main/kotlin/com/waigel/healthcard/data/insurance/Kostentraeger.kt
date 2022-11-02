package com.waigel.healthcard.data.insurance

import com.fasterxml.jackson.annotation.JsonProperty

data class Kostentraeger(
    @JsonProperty("Kostentraegerlaendercode")
    val kostentraegerlaendercode: String,

    @JsonProperty("Kostentraegerkennung")
    val kostentraegerkennung: Long,

    @JsonProperty("AbrechnenderKostentraeger")
    val abrechnenderKostentraeger: Kostentraeger? = null,

    @JsonProperty("Name")
    val name: String
)