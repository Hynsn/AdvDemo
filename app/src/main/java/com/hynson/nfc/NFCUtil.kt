package com.hynson.nfc

import android.app.Activity
import android.app.PendingIntent
import android.content.Intent
import android.nfc.FormatException
import android.nfc.NdefMessage
import android.nfc.NdefRecord
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.nfc.tech.MifareClassic
import android.nfc.tech.MifareUltralight
import android.nfc.tech.MifareUltralight.PAGE_SIZE
import android.nfc.tech.Ndef
import android.os.Build
import android.util.Log
import java.io.IOException
import java.nio.ByteBuffer
import java.util.Arrays
import kotlin.collections.contains
import kotlin.collections.forEach
import kotlin.experimental.and
import kotlin.experimental.or

object NFCUtil {
    //        NfcAdapter.ACTION_TAG_DISCOVERED：通用意图，适用于处理所有类型的 NFC 标签。
//        NfcAdapter.ACTION_TECH_DISCOVERED：特定技术意图，适用于处理特定技术类型的 NFC 标签。
//        NfcAdapter.ACTION_NDEF_DISCOVERED：NDEF 格式意图，适用于处理 NDEF 格式数据的 NFC 标签。
    private const val PAGE_SIZE = 4 // MifareUltralight 页面大小为 4 字节
    private const val START_PAGE = 0x04 // 从页面 4 开始写入数据
    private var nfcAdapter: NfcAdapter? = null
    private var readWrite = false
    private var setPwd = false
    fun init(context: Activity) {
        nfcAdapter = NfcAdapter.getDefaultAdapter(context)
    }

    fun isEnabled(): Boolean {
        return nfcAdapter?.isEnabled == true
    }

    private fun enableForegroundDispatch(
        activity: Activity,
    ) {
        val mutable = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.FLAG_MUTABLE
        } else {
            0
        }
        val intent = Intent(activity, activity.javaClass).apply {
            addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
        }
        val pendingIntent = PendingIntent.getActivity(
            activity,
            0,
            intent,
            mutable
        )
        nfcAdapter?.enableForegroundDispatch(activity, pendingIntent, null, null)
    }

    fun enableReadWriteForegroundDispatch(
        activity: Activity,
        boolean: Boolean = true
    ) {
        readWrite = boolean
        enableForegroundDispatch(activity)
    }

    fun enableLockForegroundDispatch(
        activity: Activity,
        boolean: Boolean = true
    ) {
        setPwd = boolean
        enableForegroundDispatch(activity)
    }

    fun disableForegroundDispatch(context: Activity) {
        nfcAdapter?.disableForegroundDispatch(context)
    }

    fun openNFCSettings(context: Activity) {
        val intent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            Intent(android.provider.Settings.Panel.ACTION_NFC)
        } else {
            Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS)
        }
        context.startActivity(intent)
    }

    private fun createNdefMessage(content: String): NdefMessage {
        return try {
            val payload = content.toByteArray()
            val record = NdefRecord(
                NdefRecord.TNF_MIME_MEDIA,
                "text/plain".toByteArray(),
                ByteArray(0),
                payload
            )

            NdefMessage(arrayOf(record))
        } catch (e: Exception) {
            Log.e(TAG, "创建NDEF消息失败", e)
            NdefMessage(ByteArray(0))
        }
    }

    @OptIn(ExperimentalStdlibApi::class)
    fun handleIntent(intent: Intent) {
//        if (readWrite) {
//            Log.i(TAG, "handleIntent: 读取数据")
//            readNfc(intent)
//        } else {
//            Log.i(TAG, "handleIntent: 写数据")
//            writeNfc(intent)
//        }
        val uid = getUid(intent)
        if (uid?.isNotEmpty() == true) {
            val allPwd = AESUtil.createPwd(uid)
            val size = allPwd.size
            val pwd = allPwd.copyOfRange(0, 4)
            val pack = allPwd.copyOfRange(size - 2, size)
            Log.i(TAG, "pwd: ${pwd.toHexString()}, pack: ${pack.toHexString()}")
            var mfc: MifareUltralight? = null
            intent.getParcelableExtra<Tag>(NfcAdapter.EXTRA_TAG)?.let {
                mfc = MifareUltralight.get(it)
            }
            //        mfc.close()
//        ndef?.connect()
//        ndef?.writeNdefMessage(NdefMessage(NdefRecord.createUri("veo://hynson.com")))
            if (setPwd) {
                writePassword(mfc, pwd, pack)
            } else {
                deletePassword(mfc, pwd, pack)
            }
        }
    }

    private fun writeNfc(intent: Intent) {
        if (NfcAdapter.ACTION_NDEF_DISCOVERED == intent.action) {
            val tag = intent.getParcelableExtra<Tag>(NfcAdapter.EXTRA_TAG)
            tag?.let {
                val ndef = Ndef.get(tag)
                ndef?.let {
                    try {
                        ndef.connect()
                        if (!ndef.isWritable) {
                            Log.e(TAG, "标签不可写")
                            return
                        }
//                        ndef.makeReadOnly()
                        ndef.writeNdefMessage(NdefMessage(NdefRecord.createUri("veo://hynson.com")))
                        Log.d(TAG, "写入成功")
                    } catch (e: Exception) {
                        Log.e(TAG, "写入NFC标签失败", e)
                    } finally {
                        try {
                            ndef.close()
                        } catch (e: Exception) {
                            Log.e(TAG, "关闭NFC连接失败", e)
                        }
                    }
                }
            }
        }
    }

    private fun readNfc(intent: Intent) {
        val validActions = listOf(
            NfcAdapter.ACTION_TAG_DISCOVERED,
            NfcAdapter.ACTION_TECH_DISCOVERED,
            NfcAdapter.ACTION_NDEF_DISCOVERED
        )
        if (intent.action in validActions) {
            val rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES)
            val messages = mutableListOf<NdefMessage>()
            if (rawMsgs != null) {
                rawMsgs.forEach {
                    messages.add(it as NdefMessage)
                }
            } else {
                // Unknown tag type
                val empty = ByteArray(0)
                val id = intent.getByteArrayExtra(NfcAdapter.EXTRA_ID)
                val tag = intent.parcelable<Tag>(NfcAdapter.EXTRA_TAG) ?: return
                val payload = NFCUtil.dumpTagData(tag).toByteArray()
                val record = NdefRecord(NdefRecord.TNF_UNKNOWN, empty, id, payload)
                val msg = NdefMessage(arrayOf(record))
                messages.add(msg)
            }

            for (message in messages) {
                val message = NdefMessageParser.parse(message)
                Log.i(TAG, "message: $message")
                for (record in message) {
                    Log.i(TAG, "record: $record")
                }
            }
        }
    }

    private fun toHex(bytes: ByteArray): String {
        val sb = StringBuilder()
        for (i in bytes.indices.reversed()) {
            val b = bytes[i].toInt() and 0xff
            if (b < 0x10) sb.append('0')
            sb.append(Integer.toHexString(b))
            if (i > 0) {
                sb.append(" ")
            }
        }
        return sb.toString()
    }

    private fun toReversedHex(bytes: ByteArray): String {
        val sb = StringBuilder()
        for (i in bytes.indices) {
            if (i > 0) {
                sb.append(" ")
            }
            val b = bytes[i].toInt() and 0xff
            if (b < 0x10) sb.append('0')
            sb.append(Integer.toHexString(b))
        }
        return sb.toString()
    }

    private fun toDec(bytes: ByteArray): Long {
        var result: Long = 0
        var factor: Long = 1
        for (i in bytes.indices) {
            val value = bytes[i].toLong() and 0xffL
            result += value * factor
            factor *= 256L
        }
        return result
    }

    private fun toReversedDec(bytes: ByteArray): Long {
        var result: Long = 0
        var factor: Long = 1
        for (i in bytes.indices.reversed()) {
            val value = bytes[i].toLong() and 0xffL
            result += value * factor
            factor *= 256L
        }
        return result
    }

    fun dumpTagData(tag: Tag): String {
        val sb = StringBuilder()
        val id = tag.id
        sb.append("ID (hex): ").append(toHex(id)).append('\n')
        sb.append("ID (reversed hex): ").append(toReversedHex(id)).append('\n')
        sb.append("ID (dec): ").append(toDec(id)).append('\n')
        sb.append("ID (reversed dec): ").append(toReversedDec(id)).append('\n')
        val prefix = "android.nfc.tech."
        sb.append("Technologies: ")
        for (tech in tag.techList) {
            sb.append(tech.substring(prefix.length))
            sb.append(", ")
        }
        sb.delete(sb.length - 2, sb.length)
        for (tech in tag.techList) {
            if (tech == MifareClassic::class.java.name) {
                sb.append('\n')
                var type = "Unknown"
                try {
                    val mifareTag = MifareClassic.get(tag)

                    when (mifareTag.type) {
                        MifareClassic.TYPE_CLASSIC -> type = "Classic"
                        MifareClassic.TYPE_PLUS -> type = "Plus"
                        MifareClassic.TYPE_PRO -> type = "Pro"
                    }
                    sb.appendLine("Mifare Classic type: $type")
                    sb.appendLine("Mifare size: ${mifareTag.size} bytes")
                    sb.appendLine("Mifare sectors: ${mifareTag.sectorCount}")
                    sb.appendLine("Mifare blocks: ${mifareTag.blockCount}")
                } catch (e: Exception) {
                    sb.appendLine("Mifare classic error: ${e.message}")
                }
            }
            if (tech == MifareUltralight::class.java.name) {
                sb.append('\n')
                val mifareUlTag = MifareUltralight.get(tag)
                var type = "Unknown"
                when (mifareUlTag.type) {
                    MifareUltralight.TYPE_ULTRALIGHT -> type = "Ultralight"
                    MifareUltralight.TYPE_ULTRALIGHT_C -> type = "Ultralight C"
                }
                sb.append("Mifare Ultralight type: ")
                sb.append(type)
            }
        }
        return sb.toString()
    }

    fun ndefMessageToData(message: NdefMessage): ByteArray {
        val recordsData = mutableListOf<Byte>()
        val records = message.records

        for ((index, record) in records.withIndex()) {
            var header: Byte = 0
            if (index == 0) header = header or 0x80.toByte() // MB
            if (index == records.size - 1) header = header or 0x40.toByte() // ME
            val payloadLength = record.payload.size
            val useShortRecord = payloadLength <= 0xFF
            if (useShortRecord) header = header or 0x10
            val hasId = record.id.isNotEmpty()
            if (hasId) header = header or 0x08
            header = header or (record.tnf and 0x07).toByte()
            recordsData.add(header)

            // Type length
            recordsData.add(record.type.size.toByte())

            // Payload length (1 or 4 bytes)
            if (useShortRecord) {
                recordsData.add(payloadLength.toByte())
            } else {
                val len32 = ByteBuffer.allocate(4).putInt(payloadLength).array()
                recordsData.addAll(len32.asList())
            }

            // Optional ID field
            if (hasId) {
                recordsData.add(record.id.size.toByte())
                recordsData.addAll(record.id.asList())
            }

            recordsData.addAll(record.type.asList())
            recordsData.addAll(record.payload.asList())
        }

        // Add NDEF TLV wrapper [0x03][LEN][RECORDS...][0xFE]
        val ndef = mutableListOf<Byte>()
        ndef.add(0x03.toByte())

        val recordsDataArray = recordsData.toByteArray()
        if (recordsDataArray.size <= 0xFE) {
            ndef.add(recordsDataArray.size.toByte())
        } else {
            ndef.add(0xFF.toByte())
            val len16 = ByteBuffer.allocate(2).putShort(recordsDataArray.size.toShort()).array()
            ndef.addAll(len16.asList())
        }

        ndef.addAll(recordsData)
        ndef.add(0xFE.toByte())

        // Pad to 4-byte boundary
        return ndef.toByteArray().paddedToPageSize(4)
    }

    fun ByteArray.paddedToPageSize(pageSize: Int): ByteArray {
        val paddingSize = (pageSize - size % pageSize) % pageSize
        return this + ByteArray(paddingSize)
    }

    fun List<Byte>.toByteArray(): ByteArray {
        return ByteArray(size) { this[it].toInt().toByte() }
    }

    fun List<Byte>.addAll(other: List<Byte>) {
        this.addAll(other.map { it.toInt().toByte() })
    }

    /**
     * 写入NFC设置密码
     */
    @OptIn(ExperimentalStdlibApi::class)
    private fun writePassword(
        mfc: MifareUltralight?,
        pwd: ByteArray,
        pack: ByteArray = byteArrayOf(0.toByte(), 0.toByte())
    ): Boolean {
        if (mfc == null) {
            Log.i(TAG, "writePassword: mfc = null")
            return false
        }

        val pwd_default = byteArrayOf(0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte())

        try {
            mfc.connect()
            //先用默认密码进行询问
            val response = mfc.transceive(
                byteArrayOf(
                    0x1B  //PWD_AUTH
                    , pwd_default[0], pwd_default[1], pwd_default[2], pwd_default[3]
                )
            )
            Log.i(TAG, "response: ${response.toHexString()}")

            // Check if PACK is matching expected PACK
            // This is a (not that) secure method to check if tag is genuine
            if ((response != null) && (response.size >= 2)) {
                val packResponse = Arrays.copyOf(response, 2)
                if (!(pack[0] == packResponse[0] && pack[1] == packResponse[1])) {
                    Log.i(
                        TAG,
                        "Tag could not be authenticated:\n${packResponse.toHexString()} ${pack.toHexString()}"
                    )
                } else {
                    Log.i(
                        TAG,
                        "Tag could be authenticated:\n ${packResponse.toHexString()} ${pack.toHexString()}"
                    )
                }
            } else {
                Log.i(TAG, "response: 不满足规则 ${response.toHexString()}")
            }

            val def = ndefMessageToData(NdefMessage(NdefRecord.createUri("veo://hynson.com")))
            val totalPages = (def.size + PAGE_SIZE - 1) / PAGE_SIZE // 计算需要写入的总页面数

            for (i in 0 until totalPages) {
                val start = i * PAGE_SIZE
                val end = start + PAGE_SIZE
                val pageData = def.copyOfRange(start, if (end < def.size) end else def.size)
                val pageAddress = (START_PAGE + i).toByte()

                val command = if (pageData.size < 4){
                    ByteArray(2 + pageData.size).paddedToPageSize(6)
                } else {
                    ByteArray(2 + pageData.size)
                }
                command[0] = 0xA2.toByte() // 写入页面命令
                command[1] = pageAddress // 写入页面命令
                System.arraycopy(pageData, 0, command, 2, pageData.size)
                Log.i(TAG, "transceive ${command.toHexString()}")
                val defRet = mfc.transceive(command)
                Log.i(TAG, "defRet ${defRet.toHexString()}")
            }

            // set PACK:
            val packRet = mfc.transceive(
                byteArrayOf(
                    0xA2.toByte(),
                    0x86.toByte(), /*PAGE 44*/
                    pack[0], pack[1], 0, 0  // Write PACK into first 2 Bytes and 0 in RFUI bytes
                )
            )
            Log.i(TAG, "writePassword: set PACK ${packRet.toHexString()}")

            // set PWD:  设置密码为用户设置的密码
            val pwdRet = mfc.transceive(
                byteArrayOf(
                    0xA2.toByte(),
                    0x85.toByte(),  /*PAGE 43*/
                    pwd[0],
                    pwd[1],
                    pwd[2],
                    pwd[3]  // Write PACK into first 2 Bytes and 0 in RFUI bytes
                )
            )
            Log.i(TAG, "writePassword: set PWD ${pwdRet.toHexString()}")

/*            // set AUTHLIM: 设置错误次数限制
            val responseAuthLim = mfc.readPages(132)
            if (responseAuthLim != null && responseAuthLim.size >= 16) {
                val prot =
                    false  // false = PWD_AUTH for write only, true = PWD_AUTH for read and write
                val authLim = 0;  //0-7

                val authlimRet = mfc.transceive(
                    byteArrayOf(
                        0xA2.toByte(),
                        0x84.toByte(),
                        (responseAuthLim[0] and 0x078 or (if (prot) 0x080.toByte() else 0x000) or ((authLim and 0x007).toByte())).toByte(),
                        responseAuthLim[1],
                        responseAuthLim[2],
                        responseAuthLim[3]

                        //将1-3位按原数据写会
                    )
                )
                Log.i(TAG, "writePassword: set AUTHLIM ${authlimRet.toHexString()}")
            }*/

            //设置Auth0  auth0实际控制是否启用密码保护
            val responseAuth0 = mfc.readPages(131)

            if (responseAuth0 != null && responseAuth0.size >= 16) {
                val prot =
                    false;  // false = PWD_AUTH for write only, true = PWD_AUTH for read and write
                val auth0 = 0

                val authRet = mfc.transceive(
                    byteArrayOf(
                        0xA2.toByte(),
                        0x83.toByte(),
                        0x04,
                        0x00,
                        0x00,
                        //将0-2位按原数据写会
                        (auth0 and 0x0ff).toByte()
                    )
                )
                Log.i(TAG, "设置Auth0 ${authRet.toHexString()}")
            }
            Log.i("写密码完成", "写密码完成")
            mfc.close()
            return true
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: FormatException) {
            e.printStackTrace()
        } finally {
            mfc.close()
        }
        return false
    }

    private fun getUid(intent: Intent): ByteArray? {
        intent.getParcelableExtra<Tag>(NfcAdapter.EXTRA_TAG)?.let {
            return it.id
        }
        return null
    }

    /**
     * 删除NFC设置的密码保护
     */
    @OptIn(ExperimentalStdlibApi::class)
    private fun deletePassword(
        mfc: MifareUltralight?,
        pwd: ByteArray,
        pack: ByteArray = byteArrayOf(0.toByte(), 0.toByte())
    ) {
        if (mfc == null) {
            Log.i(TAG, "deletePassword: mfc = null")
            return
        }

        //得出的PWD即用户设置的密码
        mfc.connect()

        val pwd_default = byteArrayOf(0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte())

        try {
            //用户设置的密码询问登录
            val response = mfc.transceive(
                byteArrayOf(
                    0x1B, pwd[0], pwd[1], pwd[2], pwd[3]
                )
            )

            // Check if PACK is matching expected PACK
            // This is a (not that) secure method to check if tag is genuine
            if ((response != null) && (response.size >= 2)) {
                val packResponse = Arrays.copyOf(response, 2);
                if (!(pack[0] == packResponse[0] && pack[1] == packResponse[1])) {
                    Log.i(
                        TAG,
                        "Tag could not be authenticated: ${packResponse.toHexString()}${pack.toHexString()}"
                    )
                } else {
                    Log.i(
                        TAG,
                        "Tag could be authenticated: ${packResponse.toHexString()}${pack.toHexString()}"
                    )
                }
            } else {
                Log.i(TAG, "response: 不满足规则 ${response.toHexString()}")
            }

            //pack置为默认
            val packRet = mfc.transceive(
                byteArrayOf(
                    0xA2.toByte(),
                    0x86.toByte(), /*PAGE 44*/
                    pack[0], pack[1], 0, 0  // Write PACK into first 2 Bytes and 0 in RFUI bytes
                )
            )
            Log.i(TAG, "set PACK ${packRet.toHexString()}")

            //pwd置为默认
            val pwdRet = mfc.transceive(
                byteArrayOf(
                    0xA2.toByte(),
                    0x85.toByte(),  /*PAGE 43*/
                    pwd_default[0],
                    pwd_default[1],
                    pwd_default[2],
                    pwd_default[3]  // Write PACK into first 2 Bytes and 0 in RFUI bytes
                )
            )
            Log.i(TAG, "set PWD ${pwdRet.toHexString()}")
/*            // set AUTHLIM:
            //将AUTHLIM（第42页，字节0，位2-0）设置为失败的最大密码验证尝试次数
            val responseAuthLim = mfc.readPages(132)
            if (responseAuthLim != null && responseAuthLim.size >= 16) {
                val prot =
                    false  // false = PWD_AUTH for write only, true = PWD_AUTH for read and write
                val authLim = 0;  //0-7

                val authlimRet = mfc.transceive(
                    byteArrayOf(
                        0xA2.toByte(),
                        0x84.toByte(),
                        (responseAuthLim[0] and 0x078 or (if (prot) 0x080.toByte() else 0x000) or ((authLim and 0x007).toByte())).toByte(),
                        responseAuthLim[1],
                        responseAuthLim[2],
                        responseAuthLim[3]
                        //将1-3位按原数据写会
                    )
                )
                Log.i(TAG, "set AUTHLIM ${authlimRet.toHexString()}")
            }*/

            //设置Auth0 如果auth0设置为FF则为禁用密码保护
            val responseAuth0 = mfc.readPages(131)

            if (responseAuth0 != null && responseAuth0.size >= 16) {
                val auth0Ret = mfc.transceive(
                    byteArrayOf(
                        0xA2.toByte(),
                        0x83.toByte(),
                        0x04,
                        0x00,
                        0x00,
                        //将0-2位按原数据写会
                        0x0ff.toByte()
                    )
                )
                Log.i(TAG, "set Auth0 ${auth0Ret.toHexString()} ")
            }
            Log.i(TAG, "清除密码成功")

        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: FormatException) {
            e.printStackTrace()
        } finally {
            mfc.close()
        }
    }

    private const val TAG = "NFCUtil"
}

inline fun <reified T> Intent.parcelable(key: String): T? {
    setExtrasClassLoader(T::class.java.classLoader)
    return when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> getParcelableExtra(
            key,
            T::class.java
        )

        else -> @Suppress("DEPRECATION") getParcelableExtra(key) as? T
    }
}