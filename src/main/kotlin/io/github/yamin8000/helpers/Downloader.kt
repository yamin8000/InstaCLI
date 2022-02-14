package io.github.yamin8000.helpers

import io.github.yamin8000.Dyad
import io.github.yamin8000.utils.Constants.downloadDir
import io.github.yamin8000.utils.FileUtils.createDirIfNotExists
import io.github.yamin8000.utils.FileUtils.createDirInDownloadsIfNotExists
import okhttp3.OkHttpClient
import okhttp3.Request
import okio.buffer
import okio.sink
import java.io.File

class Downloader(private val okHttpClient: OkHttpClient) {

    init {
        createDirIfNotExists(downloadDir)
    }

    fun download(url: String, filePath: String, isReplacingOld: Boolean = false): Pair<File?, Throwable?> {
        return try {
            val requiredDirectory = filePath.substringBeforeLast("/")
            createDirInDownloadsIfNotExists(requiredDirectory)
            if (isReplacingOld) {
                deleteOldFile(filePath)
                download(url, filePath)
            } else {
                if (isFileExists("${downloadDir}/$filePath")) null to Exception("File already exists, Skipping! => $filePath")
                else download(url, filePath)
            }
        } catch (e: Exception) {
            null to e
        }
    }

    private fun download(url: String, filePath: String): Dyad<File?> {
        try {
            val response = okHttpClient.newCall(Request.Builder().url(url).build()).execute()
            val downloadedFile = File("${downloadDir}/$filePath")
            val sink = downloadedFile.sink().buffer()
            val body = response.body
            return if (body != null) {
                sink.writeAll(body.source())
                sink.close()
                downloadedFile to null
            } else {
                sink.close()
                null to Throwable("Response body is null")
            }
        } catch (e: Exception) {
            return null to e
        }
    }

    private fun isFileExists(directory: String) = File(directory).exists()

    private fun deleteOldFile(directory: String) {
        val file = File("${downloadDir}/$directory")
        if (file.exists()) file.delete()
    }
}