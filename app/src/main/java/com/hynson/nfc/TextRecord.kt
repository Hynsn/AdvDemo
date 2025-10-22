package com.hynson.nfc

import android.nfc.NdefRecord
import java.io.UnsupportedEncodingException
import java.nio.charset.StandardCharsets

data class TextRecord(val text: String) : ParsedNdefRecord() {
    override val name: String
        get() = "Text"

    companion object {

        fun isText(record: NdefRecord): Boolean {
            return record.tnf == NdefRecord.TNF_WELL_KNOWN && record.type.contentEquals(NdefRecord.RTD_TEXT)
        }

        fun parse(record: NdefRecord): TextRecord? {
            return try {
                val payload: ByteArray = record.payload
                /*
                 * payload[0] contains the "Status Byte Encodings" field, per the
                 * NFC Forum "Text Record Type Definition" section 3.2.1.
                 *
                 * bit7 is the Text Encoding Field.
                 *
                 * if (Bit_7 == 0): The text is encoded in UTF-8 if (Bit_7 == 1):
                 * The text is encoded in UTF16
                 *
                 * Bit_6 is reserved for future use and must be set to zero.
                 *
                 * Bits 5 to 0 are the length of the IANA language code.
                 */
                val textEncoding =
                    if (payload[0].toInt() and 128 == 0) StandardCharsets.UTF_8 else StandardCharsets.UTF_16
                val languageCodeLength = payload[0].toInt() and 63
                val text = String(
                    payload,
                    languageCodeLength + 1,
                    payload.size - languageCodeLength - 1,
                    textEncoding
                )
                return TextRecord(text)
            } catch (e: UnsupportedEncodingException) {
                // should never happen unless we get a malformed tag.
                e.printStackTrace()
                null
            }
        }
    }
}
