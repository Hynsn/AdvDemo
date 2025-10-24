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
import android.nfc.tech.Ndef
import android.os.Build
import android.util.Log
import java.io.IOException
import java.nio.ByteBuffer
import java.util.Arrays
import java.util.concurrent.ConcurrentLinkedQueue
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
    private const val READ = 0
    const val WRITE = 1
    const val WRITE_WITH_PWD = 2
    const val CLEAR_PWD = 3
    private var nfcAction = READ

    private val nedfMessageQueue = ConcurrentLinkedQueue<NdefMessage>()

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

    fun enableForegroundDispatch(
        activity: Activity,
        message: NdefMessage? = null,
        action: Int = READ
    ) {
        if (message!=null){
            nedfMessageQueue.offer(message)
        }
        nfcAction = action
        NFCUtil.enableForegroundDispatch(activity)
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

    fun handleIntent(intent: Intent, messages: ((List<NdefMessage>) -> (Unit))? = null) {
        val uid = getUid(intent)
        if (nfcAction > WRITE && uid?.isNotEmpty() == true) {
            val pwdPair = AESUtil.createPwd(uid)
            var mfc: MifareUltralight? = null
            intent.getParcelableExtra<Tag>(NfcAdapter.EXTRA_TAG)?.let {
                mfc = MifareUltralight.get(it)
            }
            when (nfcAction) {
                WRITE_WITH_PWD -> {
                    // 写入密码保护
                    while (nedfMessageQueue.isNotEmpty()) {
                        val message = nedfMessageQueue.poll()
                        val ret = writeNdefWithPWD(mfc, pwdPair.first, pwdPair.second, message)
                        Log.i(TAG, "writeNdefWithPWD: $ret")
                    }
                }

                CLEAR_PWD -> {
                    // 删除密码保护
                    val ret = clearWithPWD(mfc, pwdPair.first, pwdPair.second)
                    Log.i(TAG, "clearWithPWD: $ret")
                }
            }
        } else {
            if (nfcAction == WRITE) {
                while (nedfMessageQueue.isNotEmpty()) {
                    val message = nedfMessageQueue.poll()
                    val ret = writeNdefMessage(intent, message)
                    Log.i(TAG, "writeNdefMessage: $ret")
                }
            }
            if (messages != null) {
                Log.i(TAG, "handleIntent: 读取数据")
                receiverNdefMessages(intent, messages)
            }
        }
    }

    private fun writeNdefMessage(intent: Intent,message: NdefMessage): Boolean {
        if (NfcAdapter.ACTION_NDEF_DISCOVERED == intent.action) {
            val tag = intent.getParcelableExtra<Tag>(NfcAdapter.EXTRA_TAG)
            tag?.let {
                val ndef = Ndef.get(tag)
                ndef?.let {
                    try {
                        ndef.connect()
                        if (!ndef.isWritable) {
                            Log.e(TAG, "标签不可写")
                            ndef.close()
                            return false
                        }
//                        ndef.makeReadOnly()
                        ndef.writeNdefMessage(message)
                        ndef.close()
                        Log.d(TAG, "写入成功")
                        return true
                    } catch (e: Exception) {
                        Log.e(TAG, "写入NFC标签失败", e)
                    } finally {
                        ndef.close()
                    }
                }
            }
        }
        return false
    }

    private fun receiverNdefMessages(intent: Intent,messages:(List<NdefMessage>)->(Unit)) {
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
            messages(messages)
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

    @OptIn(ExperimentalStdlibApi::class)
    private fun mfuWriteNdefMessage(mfu: MifareUltralight, ndefMessage: NdefMessage){
        val def = ndefMessageToData(ndefMessage)
        Log.i(TAG, "mfuWriteNdefMessage: ${def.toHexString()}")
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
            Log.i(TAG, "command ${command.toHexString()}")
            val cmdAck = mfu.transceive(command)
            Log.i(TAG, "cmdAck ${cmdAck.toHexString()}")
        }
    }

    /**
     * 写入NFC设置密码
     */
    @OptIn(ExperimentalStdlibApi::class)
    private fun writeNdefWithPWD(
        mfu: MifareUltralight?,
        pwd: ByteArray = byteArrayOf(0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte()),
        pack: ByteArray = byteArrayOf(0.toByte(), 0.toByte()),
        ndefMessage: NdefMessage? = null,
    ): Boolean {
        if (mfu == null) {
            Log.e(TAG, "writePassword: mfc = null")
            return false
        }
        if (pwd.size < 4) {
            Log.i(TAG, "pwd: size < 4")
            return false
        }

        try {
            mfu.connect()

            val readAuth0Ack = mfu.readPages(131)
            Log.i(TAG, "readAuth0Ack: ${readAuth0Ack.toHexString()}")
            if (readAuth0Ack[3] == 0xFF.toByte()) {
                Log.i(TAG, "密码保护未启用，直接写数据")
            } else {
                Log.i(TAG, "密码保护已启用，用密码进行询问")
                // 用密码进行询问
                val authAck = mfu.transceive(
                    byteArrayOf(
                        0x1B  //PWD_AUTH
                        , pwd[0], pwd[1], pwd[2], pwd[3]
                    )
                )
                Log.i(TAG, "authAck: ${authAck.toHexString()}")
                // Check if PACK is matching expected PACK
                // This is a (not that) secure method to check if tag is genuine
                if ((authAck != null) && (authAck.size >= 2)) {
                    val packResponse = Arrays.copyOf(authAck, 2)
                    if (!(pack[0] == packResponse[0] && pack[1] == packResponse[1])) {
                        Log.i(
                            TAG,
                            "Tag could not be authenticated:\n${packResponse.toHexString()} ${pack.toHexString()}"
                        )
                        mfu.close()
                        return false
                    } else {
                        Log.i(
                            TAG,
                            "Tag could be authenticated:\n ${packResponse.toHexString()} ${pack.toHexString()}"
                        )
                    }
                } else {
                    Log.i(TAG, "response: 不满足规则 ${authAck.toHexString()}")
                }
            }
            // write ndefMessage
            if (ndefMessage != null) {
                mfuWriteNdefMessage(mfu, ndefMessage)
            }

            // set PACK:
            val packAck = mfu.transceive(
                byteArrayOf(
                    0xA2.toByte(),
                    0x86.toByte(), /*PAGE 44*/
                    pack[0], pack[1], 0, 0  // Write PACK into first 2 Bytes and 0 in RFUI bytes
                )
            )
            Log.i(TAG, "set PACK ${packAck.toHexString()}")

            // set PWD:  设置密码为用户设置的密码
            val pwdAck = mfu.transceive(
                byteArrayOf(
                    0xA2.toByte(),
                    0x85.toByte(),  /*PAGE 43*/
                    pwd[0],
                    pwd[1],
                    pwd[2],
                    pwd[3]  // Write PACK into first 2 Bytes and 0 in RFUI bytes
                )
            )
            Log.i(TAG, "set PWD ${pwdAck.toHexString()}")

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

            // 设置Auth0  auth0实际控制是否启用密码保护
            val responseAuth0 = mfu.readPages(131)

            if (responseAuth0 != null && responseAuth0.size >= 16) {
                val auth0 = 0
                val authAck = mfu.transceive(
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
                Log.i(TAG, "set Auth0 ${authAck.toHexString()}")
            }
            Log.i(TAG, "写密码完成")
            mfu.close()
            return true
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: FormatException) {
            e.printStackTrace()
        } finally {
            mfu.close()
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
    private fun clearWithPWD(
        mfu: MifareUltralight?,
        pwd: ByteArray,
        pack: ByteArray = byteArrayOf(0.toByte(), 0.toByte())
    ): Boolean {
        if (mfu == null) {
            Log.i(TAG, "deletePassword: mfc = null")
            return false
        }
        if (pwd.size < 4) {
            Log.i(TAG, "pwd: size < 4")
            return false
        }

        try {
            mfu.connect()
            val pwd_default = byteArrayOf(0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte())
            val readAuth0Ack = mfu.readPages(131)
            Log.i(TAG, "readAuth0Ack: ${readAuth0Ack.toHexString()}")
            if (readAuth0Ack[3] == 0xFF.toByte()) {
                Log.i(TAG, "密码保护未启用，无需清除")
                mfu.close()
                return false
            } else {
                // 用户设置的密码询问登录
                val response = mfu.transceive(
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
                        mfu.close()
                        return false
                    } else {
                        Log.i(
                            TAG,
                            "Tag could be authenticated: ${packResponse.toHexString()}${pack.toHexString()}"
                        )
                    }
                } else {
                    Log.i(TAG, "response: 不满足规则 ${response.toHexString()}")
                }
            }

            // pack置为默认
            val packAck = mfu.transceive(
                byteArrayOf(
                    0xA2.toByte(),
                    0x86.toByte(), /*PAGE 44*/
                    pack[0], pack[1], 0, 0  // Write PACK into first 2 Bytes and 0 in RFUI bytes
                )
            )
            Log.i(TAG, "set PACK ${packAck.toHexString()}")

            //pwd置为默认
            val pwdAck = mfu.transceive(
                byteArrayOf(
                    0xA2.toByte(),
                    0x85.toByte(),  /*PAGE 43*/
                    pwd_default[0],
                    pwd_default[1],
                    pwd_default[2],
                    pwd_default[3]  // Write PACK into first 2 Bytes and 0 in RFUI bytes
                )
            )
            Log.i(TAG, "set PWD ${pwdAck.toHexString()}")
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
            val responseAuth0 = mfu.readPages(131)

            if (responseAuth0 != null && responseAuth0.size >= 16) {
                val auth0Ack = mfu.transceive(
                    byteArrayOf(
                        0xA2.toByte(),
                        0x83.toByte(),
                        0x04,
                        0x00,
                        0x00,
                        0x0ff.toByte() //将0-2位按原数据写回
                    )
                )
                Log.i(TAG, "set Auth0 ${auth0Ack.toHexString()} ")
            }
            Log.i(TAG, "清除密码成功")
            mfu.close()
            return true
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: FormatException) {
            e.printStackTrace()
        } finally {
            mfu.close()
        }
        return false
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