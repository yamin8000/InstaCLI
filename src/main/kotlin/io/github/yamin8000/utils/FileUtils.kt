package io.github.yamin8000.utils

import io.github.yamin8000.utils.Constants.downloadDir
import java.io.File

object FileUtils {

    fun createDirIfNotExists(pathname: String): Boolean {
        val dir = File("$downloadDir/$pathname")
        if (!dir.exists()) dir.mkdirs()
        return dir.exists()
    }
}