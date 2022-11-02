package com.waigel.healthcard

import com.waigel.healthcard.data.insurance.InsuranceData
import com.waigel.healthcard.data.patient.PatientData

interface HealthCardReaderEvents {
    fun onCardInserted()
    fun onCardRemoved()
    fun onCardReadError(e: Exception)
    fun onCardReadDataSuccessfully(patient: PatientData, insurance: InsuranceData, generation: HealthCardGeneration)

}