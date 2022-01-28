package yamin.utils

import yamin.utils.Constants.downloadDir
import java.io.File

object FileUtils {

    fun createDirIfNotExists(pathname: String): Boolean {
        val dir = File("$downloadDir/$pathname")
        if (!dir.exists()) dir.mkdirs()
        return dir.exists()
    }
}