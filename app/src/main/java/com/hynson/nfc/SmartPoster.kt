package com.hynson.nfc

import android.nfc.NdefMessage
import android.nfc.NdefRecord

data class SmartPoster(val uri: UriRecord, val text: TextRecord?) : ParsedNdefRecord() {
    override val name: String
        get() = "Smart Poster"

    companion object {
        fun isSmartPoster(record: NdefRecord): Boolean {
            return record.tnf == NdefRecord.TNF_WELL_KNOWN && record.type.contentEquals(NdefRecord.RTD_SMART_POSTER)
        }

        fun parse(record: NdefRecord): ParsedNdefRecord? {
            val subRecords = NdefMessage(record.payload)
            val records: Iterable<ParsedNdefRecord> =
                NdefMessageParser.getRecords(subRecords.records)
            val uri = records.filterIsInstance<UriRecord>().firstOrNull() as UriRecord
            val title = records.filterIsInstance<TextRecord>().firstOrNull()
            return SmartPoster(uri, title)
        }
    }

}
