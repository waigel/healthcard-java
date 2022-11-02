package com.waigel.healthcard.data.insurance

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.ObjectMapper
import org.json.JSONObject
import org.json.XML


data class InsuranceData(
    @JsonProperty(value = "UC_AllgemeineVersicherungsdatenXML")
    val ucAllgemeineVersicherungsdatenXML: UCAllgemeineVersicherungsdatenXML
) {
    companion object {
        fun fromXml(decompressedInsuranceXMLData: String): InsuranceData {
            val objectMapper = ObjectMapper()
            val json: JSONObject = XML.toJSONObject(decompressedInsuranceXMLData)
            return objectMapper.readValue(json.toString(), InsuranceData::class.java)
        }
    }
}


