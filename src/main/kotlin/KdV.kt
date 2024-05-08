package org.example

import java.io.BufferedWriter
import java.io.File
import java.nio.Buffer
import kotlin.math.E
import kotlin.math.pow

fun KdV(file: File): BufferedWriter{
    val t_init = 0
    val x_init = 0
    val delta_x = 0.01739
    val delta_t = 0.02
    val delta = 0.022
    val leftWall = -20.0
    val rightWall = 20.0
    val finalTime = 5000.0
    val timeSteps = (finalTime/delta_t).toInt() + 1
    val saveTime = 50000
    val size = ((rightWall - leftWall)/delta_x).toInt() + 1

    val sech = {x: Double -> E.pow(x) / (1 + E.pow(2*x))  }
    var u0 = Array(size){ sech(it.toDouble() * delta_x - 20.0) }

    var u: Array<Array<Double>> = arrayOf(u0)
    val ring: (Int) -> Int = { x -> (x + size) % size }
    val ringTime: (Int) -> Int = { x -> (x + saveTime) % saveTime }
    val stepBody: (Array<Array<Double>>, Int, Int) -> Double  = {x: Array<Array<Double>>, m: Int, n: Int
        -> - 1/3*delta_t/delta_x * (x[m][ring(n+1)] + x[m][n]+ x[m][ring(n-1)])*(x[m][ring(n+1)] - x[m][ring(n-1)])
        - delta.pow(2)*delta_t/ Math.pow(delta_x, 3.0) * (x[m][ring(n+2)] - 2*x[m][ring(n+1)] + 2*x[m][ring(n-1)] - x[m][ring(n-2)])}
    val kdvStep: (Array<Array<Double>>, Int, Int) -> Double  = {x: Array<Array<Double>>, m: Int, n: Int
        -> x[m-1][n] + stepBody(x, m, n)}
    val firstKdvStep: (Array<Array<Double>>, Int, Int) -> Double  = {x: Array<Array<Double>>, m: Int, n: Int
        -> x[m][n] + stepBody(x, m, n)}

    var u1 = List(size){it}.map { x -> firstKdvStep(u, 0, x) }.toTypedArray()

    file.delete()
    val writer = file.bufferedWriter()
    for(time in 0 .. timeSteps) {
        val uNext = List(size){it}.map { x -> kdvStep(arrayOf(u0,u1), ringTime(1), x) }.toTypedArray()
        writer.appendLine(uNext.map { y -> y.toBigDecimal().toPlainString() }
            .reduce { acc, y -> "$acc,$y" })
        if(time%10000==0) println("$time of $timeSteps")
        u0 = u1
        u1 = uNext
    }
    return writer
}