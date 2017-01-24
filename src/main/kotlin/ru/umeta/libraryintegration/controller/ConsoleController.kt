package ru.umeta.libraryintegration.controller

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component
import ru.umeta.libraryintegration.service.MainService

/**
 * Created by ctash on 29.01.16.
 */
@Component
open class ConsoleController : CommandLineRunner {

    companion object {
        private val logger = LoggerFactory.getLogger(ConsoleController::class.java)
    }

    @Autowired
    var mainService: MainService? = null

    override fun run(vararg args: String?) {

        logger.info("Entered ConsoleController")
        if (args.isEmpty()) {
            logger.error("No command was passed.")
            return
        }
        val command = args[0]
        when (command) {
            "parse" -> {
                mainService?.parseDirectory(getDirArg(args))
            }

            "parseInit" -> {
                mainService?.parseDirectoryInit(getDirArg(args))
            }
            "parseLarge" -> {
                mainService?.parseDirectoryLarge(getDirArg(args))
            }
            "find" -> mainService?.find()
            "collect" -> mainService?.collect()
            "collectd" -> mainService?.collectDebug()
            "collectl" -> mainService?.collectLegacy()
            "strings" -> mainService?.strings()
            "getstat" -> mainService?.getStat()
        }
    }

    private fun getDirArg(args: Array<out String?>): String {
        val dir: String
        if (args.size > 1) {
            dir = args[1] ?: ""
        } else {
            dir = ""
        }
        return dir
    }

}