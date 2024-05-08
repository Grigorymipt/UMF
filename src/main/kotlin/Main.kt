package org.example

import java.io.File
import java.lang.Math.cos
import java.lang.Math.pow
import kotlin.math.E
import kotlin.math.PI
import kotlin.math.pow


fun main() {
    println("Hello World!")
    val file = File("in.csv")
    KdV(file).close()

    cutFile(file, File("out.csv"),100)
}