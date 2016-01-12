package ru.umeta.libraryintegration.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.support.FileSystemXmlApplicationContext
import org.springframework.stereotype.Service
import ru.umeta.libraryintegration.json.UploadResult
import ru.umeta.libraryintegration.parser.IXMLParser
import ru.umeta.libraryintegration.parser.ModsXMLParser
import java.io.File
import java.util.*

/**
 * The main service to handle the integration logic
 * Created by k.kosolapov on 14/04/2015.
 */
@Service
class MainService
@Autowired constructor(
        private val parser: IXMLParser = ModsXMLParser(),
        private val documentService: DocumentService = DocumentService()) {


    @Throws(InterruptedException::class)
    fun parseDirectory(path: String): UploadResult {

        val fileList = getFilesToParse(path)
        var total = 0
        val result = UploadResult(0, 0)
        for (file in fileList) {
            val startTime = System.nanoTime()
            val resultList = parser.parse(file)
            val size = resultList.size
            total += size
            println("resultList size is " + size)
            val uploadResult = documentService.processDocumentList(resultList, null)
            val endTime = System.nanoTime()
            println("The documents bulk is added in " + (endTime - startTime).toDouble() / 1000000000.0 + ". Total: " + total)
            result.parsedDocs = result.parsedDocs + uploadResult.parsedDocs
            result.newEnriched = result.newEnriched + uploadResult.newEnriched
        }

        return result
    }


    fun parseDirectoryBalance(path: String, saltLevel: Int): UploadResult {
        val fileList = getFilesToParse(path)
        val result = UploadResult(0, 0)
        for (file in fileList) {
            val startTime = System.nanoTime()
            val resultList = parser.parse(file)
            val parseTime = System.nanoTime()
            println("The documents bulk parsed in " + (parseTime - startTime).toDouble() / 1000000000.0)
            println("resultList size is " + resultList.size)
            for (parseResult in resultList) {
                val saltedResult = documentService.addNoise(parseResult, saltLevel)
                if (saltedResult == null) {
                    println("The parsed result either had no authors or the title was blank.")
                    continue
                }
                val uploadResult = documentService.processDocumentList(saltedResult, null)
                println("The result with salt of level " + saltLevel + " is " + uploadResult.newEnriched)

            }

        }
        return result
    }
}

fun getFilesToParse(path: String): List<File> {
    val file = File(path)
    if (file.exists()) {
        if (file.isDirectory) {
            if (file.listFiles() != null) {
                val result = ArrayList<File>()
                file.listFiles().forEach { subFile ->
                    result.addAll(getFilesToParse(subFile.path))
                }
                return result
            }
        } else {
            return listOf(file)
        }
    }
    return emptyList()
}

fun parseDirectoryStatic(path: String): UploadResult {
    val fileList = getFilesToParse(path)
    val result = UploadResult(0, 0)
    for (file in fileList) {
        val startTime = System.nanoTime()
        val resultList = ModsXMLParser().parse(file)
        val parseTime = System.nanoTime()
        println("The documents bulk parsed in " + (parseTime - startTime).toDouble() / 1000000000.0)
        println("resultList size is " + resultList.size)
    }
    return result
}

fun main(args: Array<String>) {
    val context = FileSystemXmlApplicationContext("/META-INF/standalone-context" + ".xml")

    val bean = context.getBean(MainService::class.java)
    try {
        bean.parseDirectory("C:\\work\\repos\\zharvester\\ZHarvester\\out\\results")
    } catch (e: InterruptedException) {
        e.printStackTrace()
    }

}
