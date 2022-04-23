package io.github.instakiller.utils

import com.github.ajalt.mordant.rendering.TextColors
import com.github.ajalt.mordant.terminal.Terminal
import io.github.instakiller.Dyad
import java.awt.Color
import java.awt.image.BufferedImage
import java.io.File
import java.io.IOException
import javax.imageio.ImageIO

object ImageUtil {

    fun viewImage(file: File, size: Int = 50) {
        val (imageBuffer, _) = imageReader(file)
        if (imageBuffer != null) {

            val newImage = Scalr.resize(
                imageBuffer,
                Scalr.Method.SPEED,
                size
            )

            newImage?.let {
                for (i in 0 until newImage.height) {
                    for (j in 0 until newImage.width) {
                        val pixel = newImage.getRGB(j, i)
                        val color = Color(pixel)
                        drawPixel(color)
                    }
                    println()
                }
            }
        }
    }

    private fun drawPixel(color: Color) {
        val terminal = Terminal()
        val style = TextColors.rgb(color.red / 255f, color.green / 255f, color.blue / 255f)
        terminal.print(style("█"))
    }

    private fun imageReader(file: File): Dyad<BufferedImage?> {
        return try {
            ImageIO.read(file) to null
        } catch (e: IOException) {
            null to e
        }
    }
}