package com.waigel.healthcard

import com.waigel.healthcard.exceptions.HealthCardException
import com.waigel.healthcard.exceptions.HealthCardReadException
import com.waigel.healthcard.smartcard.APDUCommands
import com.waigel.healthcard.utils.BcdHelper
import org.slf4j.LoggerFactory
import java.nio.ByteBuffer
import javax.smartcardio.Card
import javax.smartcardio.CardChannel
import javax.smartcardio.CommandAPDU
import javax.smartcardio.ResponseAPDU

class HealthCardChannel() : CardChannel() {

    private lateinit var superChannel: CardChannel
    private val logger = LoggerFactory.getLogger(HealthCardChannel::class.java)

    constructor(channel: CardChannel) : this() {
        this.superChannel = channel
    }


    override fun getCard(): Card {
        return superChannel.card
    }

    override fun getChannelNumber(): Int {
        return superChannel.channelNumber
    }

    fun getVersion(command: CommandAPDU): String {
        val response = this.transmit(command)
        val unpackBcd = BcdHelper.unpackBcd(response.data)
        if (unpackBcd.size != 10) {
            throw HealthCardException("Invalid version response")
        }
        val part1 = BcdHelper.bcdToDecimal(unpackBcd.copyOfRange(0, 3))
        val part2 = BcdHelper.bcdToDecimal(unpackBcd.copyOfRange(3, 6))
        val part3 = BcdHelper.bcdToDecimal(unpackBcd.copyOfRange(6, 10))
        return String.format("%d.%d.%d", part1, part2, part3)
    }

    fun getCardGeneration(): HealthCardGeneration {
        val v1 = getVersion(APDUCommands.VERSION_1)
        val v2 = getVersion(APDUCommands.VERSION_2)
        val v3 = getVersion(APDUCommands.VERSION_3)

        return if (v1 == "3.0.0" && v2 == "3.0.0" && v3 == "3.0.2") {
            logger.debug("Card generation G1")
            HealthCardGeneration.GENERATION_1
        } else if (v1 == "3.0.0" && v2 == "3.0.1" && v3 == "3.0.3") {
            logger.debug("Card generation G1 plus")
            HealthCardGeneration.GENERATION_1_PLUS
        } else if (v1 == "4.0.0" && v2 == "4.0.0" && v3 == "4.0.0") {
            logger.debug("Card generation G2")
            HealthCardGeneration.GENERATION_2
        } else {
            logger.debug("Card generation unknown")
            HealthCardGeneration.UNKNOWN
        }
    }


    override fun transmit(command: CommandAPDU?): ResponseAPDU {
        val res = superChannel.transmit(command)
        val sw1 = res.sW1
        val sw2 = res.sW2

        logger.debug("sw1: $sw1, sw2: $sw2, sw = ${res.sw}")
        if (sw1 == 0x62 && sw2 == 0x83) {
            throw HealthCardException("File deactivated.")
        } else if (sw1 == 0x6A && sw2 == 0x82) {
            throw HealthCardException("File not found.")
        } else if (sw1 == 0x69 && sw2 == 0x00) {
            throw HealthCardException("Command is not allowed.")
        } else if (sw1 == 0x62 && sw2 == 0x82) {
            throw HealthCardReadException("End Of File Reached Warning.")
        } else if (sw1 == 0x62 && sw2 == 0x81) {
            throw HealthCardReadException("Corrupted Data Warning.")
        } else if (sw1 == 0x69 && sw2 == 0x86) {
            throw HealthCardReadException("No current EF.")
        } else if (sw1 == 0x69 && sw2 == 0x82) {
            throw HealthCardReadException("Security Status not satisfied.")
        } else if (sw1 == 0x69 && sw2 == 0x81) {
            throw HealthCardReadException("Wrong File Type.")
        } else if (sw1 == 0x6B && sw2 == 0x00) {
            throw HealthCardReadException("Offset too big.")
        }
        logger.debug("sw1=$sw1 and sw2=$sw2 are ok.")
        return res
    }

    override fun transmit(command: ByteBuffer?, response: ByteBuffer?): Int {
        return superChannel.transmit(command, response)
    }

    override fun close() {
        superChannel.close()
    }
}