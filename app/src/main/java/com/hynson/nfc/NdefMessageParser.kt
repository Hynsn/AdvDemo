package com.hynson.nfc

import android.nfc.NdefMessage
import android.nfc.NdefRecord

object NdefMessageParser {
    fun parse(message: NdefMessage): List<ParsedNdefRecord> {
        return getRecords(message.records)
    }

    fun getRecords(records: Array<NdefRecord>): List<ParsedNdefRecord> {
        val elements = mutableListOf<ParsedNdefRecord>()
        for (record in records) {
            if (UriRecord.isUri(record)) {
                UriRecord.parse(record)?.let {
                    elements.add(it)
                }
            } else if (TextRecord.isText(record)) {
                TextRecord.parse(record)?.let {
                    elements.add(it)
                }
            } else if (SmartPoster.isSmartPoster(record)) {
                SmartPoster.parse(record)?.let {
                    elements.add(it)
                }
            } else {
                elements.add(object : ParsedNdefRecord() {
                    override val name: String
                        get() = "Unknown"
                })
            }
        }
        return elements
    }
}