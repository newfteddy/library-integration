package ru.umeta.libraryintegration.fs

import org.apache.commons.codec.DecoderException
import org.apache.commons.io.FileUtils
import org.apache.commons.io.LineIterator
import org.apache.commons.io.output.FileWriterWithEncoding
import org.apache.commons.codec.binary.Hex
import org.springframework.stereotype.Component
import org.springframework.util.StringUtils
import ru.umeta.libraryintegration.model.StringHash

import java.io.File
import java.io.IOException
import java.nio.charset.Charset
import java.util.concurrent.Executors
import java.util.concurrent.LinkedBlockingQueue

/**
 * Created by k.kosolapov on 12/2/2015.
 */
@Component
class StringHashFsPersister {

    private val executorService = Executors.newSingleThreadExecutor()

    private val storageFile = File("stringHash.blob")


    init {
        if (!storageFile.exists()) {
            try {
                storageFile.createNewFile()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    fun save(stringHash: StringHash) {
        executorService.execute {
            try {
                FileWriterWithEncoding(storageFile, Charset.forName(UTF_8), true).use { writerWithEncoding ->
                    writerWithEncoding.write(Hex.encodeHexString(byteArrayOf(stringHash.hashPart1, stringHash.hashPart2, stringHash.hashPart3, stringHash.hashPart4)))
                    writerWithEncoding.write(SEPARATOR)
                    writerWithEncoding.write(stringHash.id.toString())
                    writerWithEncoding.write(SEPARATOR)
                    writerWithEncoding.write(stringHash.value)
                    writerWithEncoding.write(SEPARATOR + "\n")
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    fun fillMaps(map: MutableMap<String, StringHash>, idMap: MutableMap<Long, StringHash>): Long {
        var lastId: Long = 0
        try {
            val it = FileUtils.lineIterator(storageFile, UTF_8)
            try {
                while (it.hasNext()) {
                    try {

                        val line = it.nextLine()
                        var splitStrings = StringUtils.tokenizeToStringArray(line, SEPARATOR)

                        if (splitStrings.size != 3) {
                            if (splitStrings.size == 2 && "00000000" == splitStrings[0]) {
                                val newSplitStrings = arrayOfNulls<String>(3)
                                newSplitStrings[0] = splitStrings[0]
                                newSplitStrings[1] = splitStrings[1]
                                newSplitStrings[2] = ""
                                splitStrings = newSplitStrings
                            } else {
                                continue
                            }
                        }

                        val bytes = Hex.decodeHex(splitStrings[0].toCharArray())
                        val hashPart1: Byte
                        val hashPart2: Byte
                        val hashPart3: Byte
                        val hashPart4: Byte

                        if (bytes.size < 4) {
                            continue
                        } else {
                            hashPart1 = bytes[0]
                            hashPart2 = bytes[1]
                            hashPart3 = bytes[2]
                            hashPart4 = bytes[3]
                        }
                        val id = splitStrings[1].toLong()
                        lastId = Math.max(id, lastId)
                        val value = splitStrings[2]

                        val stringHash = StringHash(id, value, hashPart1, hashPart2, hashPart3, hashPart4)
                        map.put(value, stringHash)
                        idMap.put(id, stringHash)

                    } catch (e: DecoderException) {
                        e.printStackTrace()
                    }

                }

            } finally {
                LineIterator.closeQuietly(it)
            }
        } catch (e: IOException) {
            println("Cannot open the storage file")
        }

        return lastId
    }

    companion object {
        const val SEPARATOR = "|"
        const val UTF_8 = "UTF-8"
    }
}
