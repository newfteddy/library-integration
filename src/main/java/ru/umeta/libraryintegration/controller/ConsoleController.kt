package ru.umeta.libraryintegration.controller

import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import ru.umeta.libraryintegration.service.MainService

/**
 * Created by ctash on 29.01.16.
 */
class ConsoleController : ApplicationRunner {

    override fun run(appArgs: ApplicationArguments) {
        val args = appArgs.sourceArgs
        val command = args[0]
        when (command) {
            "-parse" -> MainService.use {
                it.parseDirectory(args[1])
            }
            "-parseInit" -> MainService.use {
                it.parseDirectoryInit(args[1])
            }
            "-find" -> MainService.use {
                it.find()
            }
        }
    }
}