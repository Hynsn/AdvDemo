package com.hynson.nfc

import android.net.Uri
import android.nfc.NdefRecord
import java.io.ByteArrayOutputStream
import java.nio.charset.StandardCharsets

data class UriRecord(val uri: Uri): ParsedNdefRecord() {
    override val name: String
        get() = "URI"

    companion object {

        private val URI_PREFIX_MAP = mapOf(
            0x00.toByte() to "",
            0x01.toByte() to "http://www.",
            0x02.toByte() to "https://www.",
            0x03.toByte() to "http://",
            0x04.toByte() to "https://",
            0x05.toByte() to "tel:",
            0x06.toByte() to "mailto:",
            0x07.toByte() to "ftp://anonymous:anonymous@",
            0x08.toByte() to "ftp://ftp.",
            0x09.toByte() to "ftps://",
            0x0A.toByte() to "sftp://",
            0x0B.toByte() to "smb://",
            0x0C.toByte() to "nfs://",
            0x0D.toByte() to "ftp://",
            0x0E.toByte() to "dav://",
            0x0F.toByte() to "news:",
            0x10.toByte() to "telnet://",
            0x11.toByte() to "imap:",
            0x12.toByte() to "rtsp://",
            0x13.toByte() to "urn:",
            0x14.toByte() to "pop:",
            0x15.toByte() to "sip:",
            0x16.toByte() to "sips:",
            0x17.toByte() to "tftp:",
            0x18.toByte() to "btspp://",
            0x19.toByte() to "btl2cap://",
            0x1A.toByte() to "btgoep://",
            0x1B.toByte() to "tcpobex://",
            0x1C.toByte() to "irdaobex://",
            0x1D.toByte() to "file://",
            0x1E.toByte() to "urn:epc:id:",
            0x1F.toByte() to "urn:epc:tag:",
            0x20.toByte() to "urn:epc:pat:",
            0x21.toByte() to "urn:epc:raw:",
            0x22.toByte() to "urn:epc:",
            0x23.toByte() to "urn:nfc:"
        )

        fun isUri(record: NdefRecord): Boolean {
            return (record.tnf == NdefRecord.TNF_WELL_KNOWN && record.type.contentEquals(NdefRecord.RTD_URI)) || (record.tnf == NdefRecord.TNF_ABSOLUTE_URI)
        }

        fun parse(record: NdefRecord): UriRecord? {
            val tnf = record.tnf
            if (tnf == NdefRecord.TNF_WELL_KNOWN) {
                return parseWellKnown(record)
            } else if (tnf == NdefRecord.TNF_ABSOLUTE_URI) {
                return parseAbsolute(record)
            }
            return null
        }

        private fun parseWellKnown(record: NdefRecord): UriRecord {
            val payload: ByteArray = record.payload
            /*
             * payload[0] contains the URI Identifier Code, per the
             * NFC Forum "URI Record Type Definition" section 3.2.2.
             *
             * payload[1]...payload[payload.length - 1] contains the rest of
             * the URI.
             */
            val prefix: String = URI_PREFIX_MAP[payload[0]]!! // TODO

            val outputStream = ByteArrayOutputStream()
            outputStream.write(prefix.toByteArray(StandardCharsets.UTF_8))
            outputStream.write(payload.copyOfRange(1, payload.size))
            val fullUri: ByteArray = outputStream.toByteArray()

            val uri = Uri.parse(String(fullUri, StandardCharsets.UTF_8))
            return UriRecord(uri)
        }

        private fun parseAbsolute(record: NdefRecord): UriRecord {
            val payload: ByteArray = record.payload
            val uri = Uri.parse(String(payload, StandardCharsets.UTF_8))
            return UriRecord(uri)
        }
    }
}
