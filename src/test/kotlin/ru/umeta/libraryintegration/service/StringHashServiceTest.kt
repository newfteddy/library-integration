package ru.umeta.libraryintegration.service

import org.junit.Test

import org.junit.Assert.*

/**
 * Created by ctash on 04.04.2016.
 */
class StringHashServiceTest {

    @Test
    fun distanceWithTheorems() {
        println(distanceWithTheorems("hello1", "hello2"))
        println(distanceWithTheorems("helloworld1", "helloworld2"))
        println(distanceWithTheorems("foo bar", "bar foo"))
    }

}