package com.waigel.healthcard.utils

import javax.smartcardio.CommandAPDU

class APDUHelper {
    companion object {
        fun createReadCommand(pos: Int, length: Int): CommandAPDU {
            return CommandAPDU(
                byteArrayOf(
                    0x00.toByte(), 0xB0.toByte(), (pos / 256).toByte(), (pos % 256).toByte(),
                    length.toByte()
                )
            )
        }
    }
}