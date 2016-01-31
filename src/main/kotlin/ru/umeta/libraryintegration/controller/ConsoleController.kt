package ru.umeta.libraryintegration.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component
import ru.umeta.libraryintegration.service.MainService

/**
 * Created by ctash on 29.01.16.
 */
@Component
class ConsoleController @Autowired constructor(val mainService: MainService): CommandLineRunner {
    override fun run(vararg args: String?) {
        val command = args[0]
        when (command) {
            "-parse" -> mainService.parseDirectory(args[1]?:"")
            "-parseInit" -> mainService.parseDirectoryInit(args[1]?:"")
            "-find" ->  mainService.find()
        }
    }
}