package org.example

import java.io.File

fun cutFile(fileIn: File, fileOut: File, skipSteps: Int){
    val reader = fileIn.bufferedReader()
    val writer = fileOut.bufferedWriter()
    var i = 0
    do {
        i++
        val line = reader.readLine()
        if (i%skipSteps == 0) writer.appendLine(line)
    } while (!line.isNullOrEmpty())
    reader.close()
    writer.close()
}