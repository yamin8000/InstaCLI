package yamin.modules

import com.github.instagram4j.instagram4j.IGClient
import com.github.instagram4j.instagram4j.models.media.timeline.ImageCarouselItem
import com.github.instagram4j.instagram4j.models.media.timeline.TimelineCarouselMedia
import com.github.instagram4j.instagram4j.models.media.timeline.TimelineImageMedia
import com.github.instagram4j.instagram4j.models.media.timeline.TimelineMedia
import yamin.console.ConsoleHelper.getBooleanInput
import yamin.console.ConsoleHelper.getIntegerInput
import yamin.console.ConsoleHelper.getMultipleStrings
import yamin.console.printlnC
import yamin.helpers.Downloader
import yamin.helpers.LoggerHelper.printBlackBar
import yamin.helpers.LoggerHelper.progress
import yamin.helpers.PostsHelper
import yamin.modules.Menus.postInfo
import yamin.utils.Constants.downloadDir
import yamin.utils.FileUtils.createDirIfNotExists
import yamin.utils.Utility
import yamin.utils.Utility.isoTimeOfEpoch
import java.util.*

class PostModule(scanner: Scanner, private val igClient: IGClient) : Module(scanner, postInfo) {

    private val downloader: Downloader by lazy(LazyThreadSafetyMode.NONE) { Downloader(igClient.httpClient) }

    override fun run(): Int {
        when (super.run()) {
            0 -> return 0
            1 -> showMenu()
            2 -> showUserPosts()
            3 -> saveUserPostsImages()
        }

        run()
        return 0
    }

    private fun showUserPosts(isSavingOutsideCaller: Boolean = false) {
        val usernames = scanner.getMultipleStrings("username")
        val limit = scanner.getIntegerInput("Enter number of posts to fetch:", 0 until Integer.MAX_VALUE)
        usernames.forEach { username ->
            progress {
                val (posts, error) = PostsHelper(igClient).getUserFeed(username, limit)
                it()
                if (posts != null && error == null) {
                    if (posts.isNotEmpty()) {
                        printlnC { "\n" + "${posts.size}".green.bright.bold.reverse + " posts have been fetched, enter number of posts you want to see: ".green }
                        val count = scanner.getIntegerInput(range = 1..posts.size)
                        printPosts(posts.take(count))
                        if (!isSavingOutsideCaller) {
                            val isSaving = scanner.getBooleanInput("Do you want to save posts' images as files? (y/n)")
                            if (isSaving) saveImages(posts, username)
                        } else saveImages(posts, username)
                    } else printlnC { "($username) has no posts!".bold.red }
                } else printlnC { "Failed to get posts! Error: ${error?.message}".red.bold }
            }
        }
    }

    private fun saveImages(posts: List<TimelineMedia>, username: String, indicator: Pair<Int, Int>? = null) {
        posts.forEach { timelineMedia ->
            createDirIfNotExists("images/$username/posts")
            when (timelineMedia) {
                is TimelineCarouselMedia -> {
                    timelineMedia.carousel_media.forEach { item ->
                        if (item is ImageCarouselItem) {
                            saveSingleImage(username, item)
                        }
                    }
                }
                is TimelineImageMedia -> saveSingleImage(username, timelineMedia)
                else -> printlnC { "${Utility.now()} ===> Unsupported media type".red.bold }
            }
        }
        val text = if (indicator == null) {
            "All images of ($username) have been saved!"
        } else {
            "part ${indicator.first} of ${indicator.second} ($username) images have been saved!"
        }
        printlnC { text.green }
    }

    private fun saveSingleImage(username: String, media: Any) {
        val imageUrl = getImageUrl(media)
        if (imageUrl != null) {
            val imageName = imageUrl.substringAfterLast("/").substringBefore("?")
            val directory = "images/$username/posts/$imageName"
            progress {
                val (imageFile, downloadError) = downloader.download(imageUrl, directory)
                it()
                if (imageFile != null && downloadError == null) {
                    printlnC { "($username) -> Image saved successfully to $$downloadDir/images/$username/posts/$imageName".green.bright }
                } else printlnC { "Skipping, Failed to download image! Error: ${downloadError?.message}".red.bold }
            }
        } else printlnC { "Skipping, ($username) -> Image url is null!".yellow.bright }
    }

    private fun getImageUrl(media: Any): String? {
        return when (media) {
            is ImageCarouselItem -> media.image_versions2.candidates.first().url
            is TimelineImageMedia -> media.image_versions2.candidates.first().url
            else -> null
        }
    }

    private fun saveUserPostsImages() {
        showUserPosts(true)
    }

    companion object {
        private fun printPosts(posts: List<TimelineMedia>) {
            posts.forEach {
                printSinglePost(it)
                printBlackBar(100)
            }
        }

        private fun printSinglePost(post: TimelineMedia) {
            printlnC { "Caption: ".green + "${post.caption?.text}".green.bold }
            printlnC { "Date: ".green + isoTimeOfEpoch(post.caption.created_at_utc).green.bold }
            printlnC { "Likes: ".green + "${post.like_count}".green.bold }
            printlnC { "Comments: ".green + "${post.comment_count}".green.bold }
            printlnC { "Location: ".green + "${post.location?.name}".green.bold }
            val previewComments = post.preview_comments
            if (previewComments != null) {
                printlnC { "Preview comments: ".green }
                previewComments.forEach {
                    printlnC { "(${it.user.username})".blue + ": ${it.text}".green.bold }
                }
            }
        }
    }
}