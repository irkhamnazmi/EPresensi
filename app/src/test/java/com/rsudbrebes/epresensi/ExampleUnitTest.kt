package com.rsudbrebes.epresensi

import android.content.Intent
import android.os.Handler
import com.rsudbrebes.epresensi.ui.auth.AuthActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.Test

import org.junit.Assert.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.concurrent.schedule
import kotlin.system.measureTimeMillis

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }
    @Test
    fun timeTest() {
//        val current = LocalDateTime.now()
//        val formatter = DateTimeFormatter.ofPattern("HH:mm:ss")
//        val formatted = current.format(formatter)
//        print(current.second.toString())
//        if(current.hour == 5 || current.hour == 14 || current.hour == 21){
//            when(current.minute){
//                41 -> {
//                   print("Berhasijhkjhl")
//                }
//            }
//        }

    }
    @Test
    fun delayTest(){
//        println("Jadi....sdsd")
//        Thread.sleep(10000)
//        println("Jadi....")
        for (i in 1..300) {
            println("WKWK")
            Thread.sleep(3000)
        }
    }





}