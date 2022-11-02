package com.waigel.healthcard.data.patient

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.ObjectMapper
import org.json.JSONObject
import org.json.XML

data class PatientData(
    @JsonProperty(value = "UC_PersoenlicheVersichertendatenXML")
    val ucPersoenlicheVersichertendatenXML: UCPersoenlicheVersichertendatenXML
) {
    companion object {
        fun fromXml(decompressedPatientXMLData: String): PatientData {
            val objectMapper = ObjectMapper()
            val json: JSONObject = XML.toJSONObject(decompressedPatientXMLData)
            return objectMapper.readValue(json.toString(), PatientData::class.java)
        }
    }
}

