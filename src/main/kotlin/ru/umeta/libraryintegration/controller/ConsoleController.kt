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
public class ConsoleController : CommandLineRunner {

    @Autowired
    public var mainService: MainService? = null;

    override fun run(vararg args: String?) {

        logger.info("Entered ConsoleController")
        val command = args[0]
        when (command) {
            "-parse" -> mainService?.parseDirectory(args[1] ?: "")
            "-parseInit" -> mainService?.parseDirectoryInit(args[1] ?: "")
            "-find" -> mainService?.find()
            "-collect" -> mainService?.collect();
            "-collectd" -> mainService?.collectDebug();
            "-collectl" -> mainService?.collectLegacy();
            "-strings" -> mainService?.strings();
            "-getstat" -> mainService?.getStat();
        }
    }

    companion object {
        val logger = LoggerFactory.getLogger(ConsoleController::class.java)
    }
}