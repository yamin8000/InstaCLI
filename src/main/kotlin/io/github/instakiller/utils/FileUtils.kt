package io.github.instakiller.utils

import io.github.instakiller.utils.Constants.downloadDir
import java.io.File

object FileUtils {

    fun createDirIfNotExists(pathname: String): Boolean {
        val dir = File(pathname.trim())
        if (!dir.exists()) dir.mkdirs()
        return dir.exists()
    }

    fun createDirInDownloadsIfNotExists(pathname: String) = createDirIfNotExists("$downloadDir/$pathname")
}