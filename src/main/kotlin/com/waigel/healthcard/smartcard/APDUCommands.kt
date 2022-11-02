package com.waigel.healthcard.smartcard

import javax.smartcardio.CommandAPDU

class APDUCommands {

    companion object {
        val VERSION_1 = CommandAPDU(
            byteArrayOf(0x00, 0xB2.toByte(), 0x01, 0x84.toByte(), 0x00.toByte())
        )
        val VERSION_2 = CommandAPDU(
            byteArrayOf(0x00, 0xB2.toByte(), 0x02, 0x84.toByte(), 0x00.toByte())
        )
        val VERSION_3 = CommandAPDU(
            byteArrayOf(0x00, 0xB2.toByte(), 0x03, 0x84.toByte(), 0x00.toByte())
        )
        val SELECT_MF: CommandAPDU = CommandAPDU(
            byteArrayOf(
                0x00.toByte(),
                0xA4.toByte(),
                0x04.toByte(),
                0x0C.toByte(),
                0x07.toByte(),
                0xD2.toByte(),
                0x76.toByte(),
                0x00.toByte(),
                0x01.toByte(),
                0x44.toByte(),
                0x80.toByte(),
                0x00.toByte()
            )
        )
        val SELECT_HCA: CommandAPDU = CommandAPDU(
            byteArrayOf(
                0x00.toByte(),
                0xA4.toByte(),
                0x04.toByte(),
                0x0C.toByte(),
                0x06.toByte(),
                0xD2.toByte(),
                0x76.toByte(),
                0x00.toByte(),
                0x00.toByte(),
                0x01.toByte(),
                0x02.toByte()
            )
        )

        val SELECT_FILE_PD: CommandAPDU = CommandAPDU(
            byteArrayOf(
                0x00.toByte(),
                0xB0.toByte(),
                0x81.toByte(),
                0x00.toByte(),
                0x02.toByte()
            )
        )

        val SELECT_FILE_VD: CommandAPDU = CommandAPDU(
            byteArrayOf(
                0x00.toByte(),
                0xB0.toByte(),
                0x82.toByte(),
                0x00.toByte(),
                0x08.toByte()
            )
        )

        val RESET_CARD_TERMINAL: CommandAPDU = CommandAPDU(
            byteArrayOf(
                0x20.toByte(),
                0x11.toByte(),
                0x00.toByte(),
                0x00.toByte(),
                0x00.toByte()
            )
        )

        val EJECT_CARD: CommandAPDU = CommandAPDU(
            byteArrayOf(
                0x20.toByte(),
                0x15.toByte(),
                0x01.toByte(),
                0x00.toByte(),
                0x01.toByte(),
                0x01.toByte()
            )
        )

    }

}