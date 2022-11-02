package com.waigel.healthcard.utils

class BcdHelper {
    companion object {

        /**
         * Decodes an unsigned packed BCD to its integer number.
         *
         * @param bcd the BCD to decode.
         * @return the decoded integer number.
         */
        fun bcdToDecimal(bcd: ByteArray): Long {
            return this.bcdToString(bcd).toLong()
        }

        /**
         * Decodes an unsigned packed BCD to its integer number wrapped in a `String`.
         *
         * @param bcd the BCD to decode.
         * @return the decoded integer number wrapped inside a `String`.
         */
        private fun bcdToString(bcd: ByteArray): String {
            val sb = StringBuilder()
            for (i in bcd.indices) {
                sb.append(bcdToString(bcd[i]))
            }
            return sb.toString()
        }

        /**
         * Decodes an unsigned packed BCD byte to its integer number wrapped in a `String`.
         *
         * @param bcd the BCD byte to decode.
         * @return the decoded integer number wrapped inside a `String`.
         */
        private fun bcdToString(bcd: Byte): String {
            val sb = StringBuilder()
            var high = (bcd.toInt() and 0xf0).toByte()
            high = (high.toInt() ushr 4.toByte().toInt()).toByte()
            high = (high.toInt() and 0x0f).toByte()
            val low = (bcd.toInt() and 0x0f).toByte()
            sb.append(high.toInt())
            sb.append(low.toInt())
            return sb.toString()
        }

        /**
         * Unpack a packed BCD byte array to a byte array.
         * @param bcd the packed BCD byte array.
         * @return the unpacked byte array.
         */
        fun unpackBcd(bcd: ByteArray): ByteArray {
            val sb = StringBuilder()
            for (i in bcd.indices) {
                sb.append(String.format("%8s", Integer.toBinaryString(bcd[i].toInt() and 0xFF)).replace(' ', '0'))
            }
            val halfByteArray = ByteArray(bcd.size * 2)
            for (i in bcd.indices) {
                halfByteArray[i * 2] = (bcd[i].toInt() ushr 4).toByte()
                halfByteArray[i * 2 + 1] = (bcd[i].toInt() and 0x0F).toByte()
            }
            return halfByteArray
        }

    }
}