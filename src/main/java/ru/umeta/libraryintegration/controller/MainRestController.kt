package ru.umeta.libraryintegration.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import ru.umeta.libraryintegration.json.UploadResult
import ru.umeta.libraryintegration.model.StringHash
import ru.umeta.libraryintegration.service.MainService
import ru.umeta.libraryintegration.service.StringHashService
import ru.umeta.libraryintegration.service.getTokens
import ru.umeta.libraryintegration.service.parseDirectoryStatic
import java.util.*

/**
 * The Main Rest Controller
 * Created by Kirill Kosolapov (https://github.com/c-tash) on 06.04.2015.
 */
@RestController
@RequestMapping("/rest")

class MainRestController @Autowired constructor(
        val mainService: MainService,
        val stringHashService: StringHashService) {

    @RequestMapping("/upload")
    fun upload(@RequestParam(value = "path", defaultValue = "default path") path: String): UploadResult {
        return mainService.parseDirectory(path)
    }

    @RequestMapping("/balance")
    fun balance(@RequestParam(value = "path", defaultValue = "default path") path: String,
                @RequestParam(value = "level", defaultValue = "1") level: Int): UploadResult {
        return mainService.parseDirectoryBalance(path, level)
    }

    @RequestMapping("/bench")
    fun benchmark() {
        val str = "Россолимо Т. Е. Рыбалов Л. Б. Москвина-Тарханова И. А. Акад. пед. и социал. наук Моск. психол.-социа"
        var rightBorder = 9;
        while (rightBorder < 100) {
            println("__________________________________")
            println(rightBorder + 1)
            val subString = str.subSequence(0..rightBorder) as String
            val stringHash = stringHashService.getStringHash(subString)
            val stringHashTokens = getTokens(subString)
            var subStringMut:StringBuilder = StringBuilder(subString)
            val shuffleOrder = ArrayList<Int>()
            for (j in 0..rightBorder/2) {
                shuffleOrder.add(j)
            }
            for (j in 0..rightBorder/2) {
                var sameHashCount1 = 0;
                var sameHashCount2 = 0;
                var sameHashCount3 = 0;
                var dist = 0.0;
                for (i in 0..999) {
                    subStringMut = StringBuilder(subString)
                    Collections.shuffle(shuffleOrder)
                    for (k in 0..j) {
                        subStringMut.setCharAt(shuffleOrder[k], 'ð' + k)
                    }
                    val subStringMutated = subStringMut.toString()
                    val tokens = stringHashService.getSimHashTokens(subStringMutated)
                    val mutStringHash = stringHashService.getStringHash(subStringMutated)
                    dist += stringHashService.distance(stringHashTokens, tokens)

                    var partsSame = 0;
                    if (stringHash.hashPart1 == mutStringHash.hashPart1) {
                        partsSame++;
                    }
                    if (stringHash.hashPart2 == mutStringHash.hashPart2) {
                        partsSame++;
                    }
                    if (stringHash.hashPart3 == mutStringHash.hashPart3) {
                        partsSame++;
                    }
                    if (stringHash.hashPart4 == mutStringHash.hashPart4) {
                        partsSame++;
                    }
                    if (partsSame >= 1) {
                        sameHashCount1++
                    }
                    if (partsSame >= 2) {
                        sameHashCount2++
                    }
                    if (partsSame >= 3) {
                        sameHashCount3++
                    }
                }
                println(j.toString() + "\t" +  sameHashCount1 + "\t" + sameHashCount2 + "\t" + sameHashCount3 + "\t"
                        + dist/1000)
            }
            rightBorder += 10
        }
    }

}
