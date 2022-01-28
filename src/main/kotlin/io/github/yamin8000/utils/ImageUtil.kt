package io.github.yamin8000.utils

import io.github.yamin8000.Dyad
import io.github.yamin8000.console.printC
import java.awt.Color
import java.awt.image.BufferedImage
import java.io.File
import java.io.IOException
import javax.imageio.ImageIO

object ImageUtil {

    fun viewImage(file: File) {
        val (imageBuffer, _) = imageReader(file)
        if (imageBuffer != null) {

            val newImage = Scalr.resize(
                imageBuffer,
                Scalr.Method.BALANCED,
                50
            )

            newImage?.let {
                for (i in 0 until newImage.width) {
                    for (j in 0 until newImage.height) {
                        val pixel = newImage.getRGB(j, i)
                        val color = Color(pixel)
                        drawPixel(getNearestColor(color))
                    }
                    println()
                }
            }
        }
    }

    private fun drawPixel(color: Color) {
        val quarter = "░"
        val half = "▒"
        val quarterHalf = "▓"
        val full = "█"
        when {
            color.red == 255 && color.green == 255 && color.blue == 255 -> printC { full.white.bright }
            color.red == 0 && color.green == 0 && color.blue == 0 -> printC { full.black }
            color.red == 255 && color.green == 0 && color.blue == 0 -> printC { full.red }
            color.red == 127 && color.green == 0 && color.blue == 0 -> printC { half.red }
            color.red == 0 && color.green == 255 && color.blue == 0 -> printC { full.green }
            color.red == 0 && color.green == 0 && color.blue == 255 -> printC { full.blue }
            color.red == 255 && color.green == 255 && color.blue == 0 -> printC { full.yellow }
            color.red == 0 && color.green == 255 && color.blue == 255 -> printC { full.cyan }
            color.red == 255 && color.green == 0 && color.blue == 255 -> printC { full.purple }
            color.red == 127 && color.green == 127 && color.blue == 0 -> printC { half.yellow }
            color.red == 127 && color.green == 127 && color.blue == 127 -> printC { half.black }
            color.red == 255 && color.green == 127 && color.blue == 127 -> printC { quarterHalf.red }
            color.red == 255 && color.green == 255 && color.blue == 127 -> printC { half.yellow }
            color.red == 63 && color.green == 63 && color.blue == 63 -> printC { quarter.black }
            color.red == 127 && color.green == 63 && color.blue == 63 -> printC { quarterHalf.red }
            color.red == 127 && color.green == 127 && color.blue == 63 -> printC { quarter.yellow }
            color.red == 63 && color.green == 0 && color.blue == 0 -> printC { quarter.red }
            color.red == 63 && color.green == 63 && color.blue == 0 -> printC { quarter.yellow }
            color.red == 63 && color.green == 0 && color.blue == 63 -> printC { quarter.purple }
            color.red == 127 && color.green == 63 && color.blue == 0 -> printC { quarterHalf.yellow }
            color.red == 0 && color.green == 63 && color.blue == 0 -> printC { quarter.green }
            color.red == 0 && color.green == 63 && color.blue == 63 -> printC { quarter.cyan }
            color.red == 255 && color.green == 126 && color.blue == 63 -> printC { quarterHalf.red.bright }
            else -> printC { quarter.black }
        }
    }

    private fun getNearestColor(oldColor: Color): Color {
        val newColor: Color
        val rgbList = mutableListOf(oldColor.red, oldColor.green, oldColor.blue)
        rgbList.forEachIndexed { index, color ->
            when (color * 2) {
                in 0..63 -> rgbList[index] = 0
                in 64..127 -> rgbList[index] = 63
                in 128..255 -> rgbList[index] = 127
                in 255..510 -> rgbList[index] = 255
            }
        }
        newColor = Color(rgbList[0], rgbList[1], rgbList[2])
        return newColor
    }

    private fun imageReader(file: File): Dyad<BufferedImage?> {
        return try {
            ImageIO.read(file) to null
        } catch (e: IOException) {
            null to e
        }
    }
}