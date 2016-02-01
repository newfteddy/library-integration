package ru.umeta.libraryintegration

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.SpringBootApplication

/**
 * Created by ctash on 02.02.16.
 */
@EnableAutoConfiguration
@SpringBootApplication
open class BootApplication {
    companion object {
        @JvmStatic fun main(args: Array<String>) {
            SpringApplication.run(BootApplication::class.java, *args)
        }
    }
}