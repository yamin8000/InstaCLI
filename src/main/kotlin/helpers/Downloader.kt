package yamin.helpers

import okhttp3.OkHttpClient
import okhttp3.Request
import okio.buffer
import okio.sink
import yamin.utils.Constants.downloadDir
import java.io.File


class Downloader(private val okHttpClient: OkHttpClient) {

    init {
        val file = File(downloadDir)
        if (!file.exists()) file.mkdir()
    }

    fun download(url: String, directory: String, isReplacingOld: Boolean = false): Pair<File?, Throwable?> {
        return try {
            if (isReplacingOld) {
                deleteOldFile(directory)
                download(url, directory)
            } else {
                if (isFileExists(directory)) null to Exception("File already exists, Skipping! => $directory")
                else download(url, directory)
            }
        } catch (e: Exception) {
            null to e
        }
    }

    private fun download(url: String, directory: String): Pair<File?, Throwable?> {
        try {
            val response = okHttpClient.newCall(Request.Builder().url(url).build()).execute()
            val downloadedFile = File("${downloadDir}/$directory")
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

    private fun isFileExists(directory: String) = File("${downloadDir}/$directory").exists()

    private fun deleteOldFile(directory: String) {
        val file = File("${downloadDir}/$directory")
        if (file.exists()) file.delete()
    }
}