package ru.umeta.libraryintegration.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import ru.umeta.libraryintegration.json.UploadResult
import ru.umeta.libraryintegration.service.MainService

/**
 * The Main Rest Controller
 * Created by Kirill Kosolapov (https://github.com/c-tash) on 06.04.2015.
 */
@RestController
@RequestMapping("/rest")

class MainRestController @Autowired constructor(mainService: MainService) {

    private val mainService: MainService = mainService;

    @RequestMapping("/upload")
    fun upload(@RequestParam(value = "path", defaultValue = "default path") path: String): UploadResult {
        return mainService.parseDirectory(path)
    }

    @RequestMapping("/balance")
    fun balance(@RequestParam(value = "path", defaultValue = "default path") path: String,
                @RequestParam(value = "level", defaultValue = "1") level: Int): UploadResult {
        return mainService.parseDirectoryBalance(path, level)
    }

}
