package com.waigel.healthcard

import com.waigel.healthcard.data.insurance.InsuranceData
import com.waigel.healthcard.data.patient.PatientData
import com.waigel.healthcard.smartcard.APDUCommands
import com.waigel.healthcard.utils.APDUHelper
import org.slf4j.LoggerFactory
import java.nio.charset.Charset
import java.util.concurrent.TimeUnit
import java.util.zip.GZIPInputStream
import javax.smartcardio.CardTerminal


/**
 * This library represents a Health Card Reader for the German Health Insurance Card (eGK).
 * The specification can be found here: https://fachportal.gematik.de/fileadmin/user_upload/fachportal/files/Spezifikationen/Basis-Rollout/Elektronische_Gesundheitskarte/gemLF_Impl_eGK_V160.pdf
 * @author Johannes Waigel
 */
class HealthCardReader
/**
 * Specify the card terminal to use.
 * @param cardTerminal The card terminal to use.
 * @return The HealthCardReader instance.
 */(
    private var cardTerminal: CardTerminal, private val disablePatientData: Boolean = false,
    private val disableInsuranceData: Boolean = false, private val disableGeneration: Boolean = false
) {

    private val logger = LoggerFactory.getLogger(HealthCardReader::class.java)
    private val eventListeners = mutableListOf<HealthCardReaderEvents>()
    private var thread: Thread? = null

    init {
        try {
            this.startEventListenerForCardInsertion()
        } catch (e: Exception) {
            logger.error("Error while starting the event listener for card insertion.", e)
            logger.info("Trying to start the event listener again in 5 seconds.")
            Thread.sleep(5000)
            this.startEventListenerForCardInsertion()
        }
    }

    /**
     * Add a listener to the HealthCardReader.
     * @param listener The listener to add.
     */
    fun addEventListener(listener: HealthCardReaderEvents) {
        this.eventListeners.add(listener)
    }


    private fun readFile(cardChannel: HealthCardChannel, offset: Int, length: Int): ByteArray {
        val data = mutableListOf<Byte>()
        val maxRead = 0xFC
        var pointer = offset
        while (data.size < length) {
            val bytesLeft = length - data.size
            val readLen = if (bytesLeft < maxRead) bytesLeft else maxRead
            val dataChunk = cardChannel.transmit(
                APDUHelper.createReadCommand(pointer, readLen)
            )
            pointer += readLen
            data.addAll(dataChunk.data.toList())
        }
        return data.toByteArray()

    }

    private fun getPatientData(channel: HealthCardChannel): PatientData {
        val startTimestamp = System.currentTimeMillis()
        channel.transmit(APDUCommands.SELECT_MF)
        channel.transmit(APDUCommands.SELECT_HCA)
        channel.transmit(APDUCommands.SELECT_FILE_PD)

        //create a read command for the first two bytes of the file which contains the length of the patient file
        val patientDataSize = channel.transmit(APDUHelper.createReadCommand(0, 2))
        val pdLength = ((patientDataSize.data[0].toInt() shl 8) + patientDataSize.data[1].toInt()) - 2

        channel.transmit(APDUCommands.SELECT_MF)
        channel.transmit(APDUCommands.SELECT_HCA)
        channel.transmit(APDUCommands.SELECT_FILE_PD)

        //create a read command for the whole patient file
        val patientDataCompressed = readFile(channel, 2, pdLength) + ByteArray(16)
        val decompressedPatientXml =
            GZIPInputStream(patientDataCompressed.inputStream()).bufferedReader(Charset.forName("ISO-8859-15")).readText()
        logger.info("Patient Data: $decompressedPatientXml")
        val endTimestamp = System.currentTimeMillis()
        val timeInSec = (endTimestamp - startTimestamp) / 1000.0
        logger.info("Time to read patient data: ${timeInSec}} seconds")

        return PatientData.fromXml(decompressedPatientXml)
    }

    private fun getInsuranceData(channel: HealthCardChannel): InsuranceData {
        channel.transmit(APDUCommands.SELECT_MF)
        channel.transmit(APDUCommands.SELECT_HCA)
        channel.transmit(APDUCommands.SELECT_FILE_VD)

        //create a read command for the first two bytes of the file which contains the length of the patient file
        val insuranceDataSize = channel.transmit(APDUHelper.createReadCommand(0, 8))
        val vdStart = ((insuranceDataSize.data[0].toInt() shl 8) + insuranceDataSize.data[1].toInt())
        val vdEnd = ((insuranceDataSize.data[2].toInt() shl 8) + insuranceDataSize.data[3].toInt())
        val vdLength = vdEnd - (vdStart - 1)

        channel.transmit(APDUCommands.SELECT_MF)
        channel.transmit(APDUCommands.SELECT_HCA)
        channel.transmit(APDUCommands.SELECT_FILE_VD)

        val insuranceDataCompressed = readFile(channel, vdStart, vdLength) + ByteArray(16)
        val decompressedInsuranceData =
            GZIPInputStream(insuranceDataCompressed.inputStream()).bufferedReader(Charset.forName("ISO-8859-15")).readText()
        logger.info("Insurance Data: $decompressedInsuranceData")

        return InsuranceData.fromXml(decompressedInsuranceData)
    }

    /**
     * Close the HealthCardReader thread
     */
    fun close() {
        if (this.thread !== null && this.thread!!.isAlive) {
            this.thread!!.interrupt()
        }
    }

    private fun ejectCard(channel: HealthCardChannel) {
        logger.info("[${cardTerminal.name}] Ejecting card")
        channel.transmit(APDUCommands.EJECT_CARD)
    }

    /**
     * Start the event listener for card insertion
     */
    private fun startEventListenerForCardInsertion() {
        val thread = Thread {
            while (cardTerminal.waitForCardPresent(0)) {
                try {

                    //check if the card is not present skip card read activity and
                    // fire cardRemoved event
                    if (!cardTerminal.isCardPresent) {
                        logger.info("[${cardTerminal.name}] Card removed")
                        this.eventListeners.forEach { it.onCardRemoved() }
                        continue
                    }
                    logger.info("[${cardTerminal.name}] Card inserted")
                    this.eventListeners.forEach { it.onCardInserted() }

                    val card = cardTerminal.connect("T=1")
                    val channel = HealthCardChannel(card.basicChannel)

                    /**
                     * Fetch information from smartcard if not disabled
                     */
                    val generation: HealthCardGeneration? = disableGeneration.let { disabled ->
                        if (disabled) null else channel.getCardGeneration()
                    }
                    val patientData: PatientData? = disablePatientData.let { disabled ->
                        if (disabled) null else getPatientData(channel)
                    }
                    val insuranceData: InsuranceData? = disableInsuranceData.let { disabled ->
                        if (disabled) null else getInsuranceData(channel)
                    }
                    //fire the onCardReadSuccess event
                    this.eventListeners.forEach {
                        it.onCardReadDataSuccessfully(
                            patientData,
                            insuranceData,
                            generation
                        )
                    }
                    //eject card
                    this.ejectCard(channel)
                    cardTerminal.waitForCardAbsent(0)
                    logger.info("[${cardTerminal.name}] Card removed")
                    this.eventListeners.forEach { it.onCardRemoved() }
                } catch (e: Exception) {
                    logger.error("Error while reading the card: ", e)
                    this.eventListeners.forEach { it.onCardReadError(e) }
                    cardTerminal.waitForCardAbsent(0)
                }
                TimeUnit.MILLISECONDS.sleep(100)
            }
        }
        this.thread = thread
        thread.start()
    }

    fun getCardTerminal(): CardTerminal {
        return this.cardTerminal
    }


}