package ru.umeta.libraryintegration.fs

import org.apache.commons.io.FileUtils
import org.apache.commons.io.LineIterator
import org.apache.commons.io.output.FileWriterWithEncoding
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.util.StringUtils
import ru.umeta.libraryintegration.inmemory.StringHashRepository
import ru.umeta.libraryintegration.model.EnrichedDocument

import java.io.File
import java.io.IOException
import java.nio.charset.Charset
import java.util.*
import java.util.concurrent.BlockingQueue
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.LinkedBlockingQueue
import java.util.function.Consumer


/**
 * File system persister for [EnrichedDocument].
 * @author Kirill Kosolapov
 */
@Component
class EnrichedDocumentFsPersister
@Autowired
constructor(private val stringHashRepository: StringHashRepository) {

    private val executorService = Executors.newSingleThreadExecutor();
    private val documentStorageFile = File("enrichedDocument.blob")
    private val mutex = Object()

    init {
        if (!documentStorageFile.exists()) {
            try {
                documentStorageFile.createNewFile()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    fun save(document: EnrichedDocument) {
        executorService.execute {
            synchronized (mutex) {
                try {
                    FileWriterWithEncoding(documentStorageFile, Charset.forName(UTF_8), true).use { writerWithEncoding ->
                        writerWithEncoding.write(document.id.toString())
                        writerWithEncoding.write(SEPARATOR)
                        writerWithEncoding.write(document.author.toString())
                        writerWithEncoding.write(SEPARATOR)
                        writerWithEncoding.write(document.title.toString())
                        writerWithEncoding.write(SEPARATOR)
                        writerWithEncoding.write(document.isbn.toString())
                        writerWithEncoding.write(SEPARATOR)
                        writerWithEncoding.write(document.publishYear.toString())
                        writerWithEncoding.write(SEPARATOR + "\n")
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }
        }
    }

    fun applyToPersisted(consumer: (EnrichedDocument) -> Unit): Long {
        var lastId: Long = 0
        try {
            val it = FileUtils.lineIterator(documentStorageFile, UTF_8)
            try {
                while (it.hasNext()) {
                    try {
                        val line = it.nextLine()
                        val splitStrings = StringUtils.tokenizeToStringArray(line, SEPARATOR)

                        if (splitStrings.size != 5) {
                            continue
                        }

                        val id = splitStrings[0].toLong()
                        val author = splitStrings[1].toLong()
                        val title = splitStrings[2].toLong()
                        val isbn = splitStrings[3]
                        val publishYear = if ("null" == splitStrings[4]) null else splitStrings[4].toInt()
                        val document = EnrichedDocument(id, title, author, isbn, null, Date(), publishYear)
                        lastId = Math.max(id, lastId)
                        consumer(document)
                    } catch (e: NumberFormatException) {
                        println("One of the number ids is not a number.")
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
